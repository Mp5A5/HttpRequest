package com.mp5a5.www.library.use;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import com.mp5a5.www.library.utils.DownloadFileUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.*;
import okio.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

/**
 * describe：
 * author ：mp5a5 on 2019/4/4 16：13
 * email：wwb199055@126.com
 */
public class DownloaderManager {

    private ProgressListener progressListener;
    private String url;
    private OkHttpClient client;
    private Call call;
    private Response originalResponse;
    private static final int DOWNLOAD_FAIL = 0;
    private static final int DOWNLOAD_UPDATE = 1;
    private static final int DOWNLOAD_EXECUTE = 2;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_FAIL:
                    if (progressListener != null) {
                        progressListener.onFail();
                    }
                    break;
                case DOWNLOAD_UPDATE:
                    UpdateMsg updateMsg = (UpdateMsg) msg.obj;
                    if (progressListener != null) {
                        progressListener.update(updateMsg.totalBytes, updateMsg.done);
                    }
                    break;
                case DOWNLOAD_EXECUTE:
                    if (progressListener != null) {
                        progressListener.onPreExecute(getProgressResponseBody().contentLength());
                    }
                default:
                    break;
            }
        }
    };

    @SuppressLint("CheckResult")
    public DownloaderManager(String url, ProgressListener progressListener) {
        this.url = url;
        this.progressListener = progressListener;

        //判断URL
        //如果项目中不想使用RxJava则自己开个线程将判断加到里面就可以了
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                if (checkURL(url)) {
                    e.onNext(true);
                } else {
                    e.onNext(false);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            mHandler.sendEmptyMessage(DOWNLOAD_FAIL);
                        }
                    }
                });
        //在下载、暂停后的继续下载中可复用同一个client对象
        client = getProgressClient();
    }

    /**
     * 每次下载需要新建新的Call对象
     *
     * @param startPoints 开始下载节点
     */
    private Call newCall(long startPoints) {
        Request request = new Request.Builder()
                .url(url)
                //断点续传要用到的，指示下载的区间
                .header("RANGE", "bytes=" + startPoints + "-")
                .build();
        return client.newCall(request);
    }

    public OkHttpClient getProgressClient() {
        // 拦截器，用上ProgressResponseBody
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(getProgressResponseBody())
                        .build();
            }
        };
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .build();
    }

    private ProgressResponseBody getProgressResponseBody() {
        return new ProgressResponseBody(originalResponse.body(), progressListener);
    }

    /**
     * startsPoint 指定开始下载的点
     *
     * @param startPoint
     */
    public void downloadStartPoint(final long startPoint) {
        call = newCall(startPoint);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(DOWNLOAD_FAIL);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                downloadSave(response, startPoint);
            }
        });
    }

    public void pause() {
        if (call != null) {
            call.cancel();
        }
    }

    public void cancelDownload() {
        if (call != null || !call.isCanceled()) {
            call.cancel();
        }
        if (mHandler != null) {
            mHandler.removeMessages(DOWNLOAD_EXECUTE);
            mHandler.removeMessages(DOWNLOAD_FAIL);
            mHandler.removeMessages(DOWNLOAD_UPDATE);
        }
    }

    private void downloadSave(Response response, long startsPoint) {
        ResponseBody body = response.body();
        InputStream in = Objects.requireNonNull(body).byteStream();
        FileChannel channelOut = null;
        // 随机访问文件，可以指定断点续传的起始位置
        RandomAccessFile randomAccessFile = null;

        try {
            File file = DownloadFileUtils.getFileFromUrl(url);
            randomAccessFile = new RandomAccessFile(file, "rwd");
            //Chanel NIO中的用法，由于RandomAccessFile没有使用缓存策略，直接使用会使得下载速度变慢，亲测缓存下载3.3秒的文件，用普通的RandomAccessFile需要20多秒。
            channelOut = randomAccessFile.getChannel();
            // 内存映射，直接使用RandomAccessFile，是用其seek方法指定下载的起始位置，使用缓存下载，在这里指定下载位置。
            MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, startsPoint, body.contentLength());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                mappedBuffer.put(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                if (channelOut != null) {
                    channelOut.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean checkURL(String url) {
        boolean value = false;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            int code = conn.getResponseCode();
            if (code != 200) {
                value = false;
            } else {
                value = true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }


    private class ProgressResponseBody extends ResponseBody {
        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        private ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
            mHandler.sendEmptyMessage(DOWNLOAD_EXECUTE);
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {

            return new ForwardingSource(source) {

                long totalBytes = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    totalBytes += bytesRead != -1 ? bytesRead : 0;
                    Message message = new Message();
                    UpdateMsg updateMsg = new UpdateMsg(totalBytes, bytesRead == -1);
                    message.what = DOWNLOAD_UPDATE;
                    message.obj = updateMsg;
                    mHandler.sendMessage(message);
                    return bytesRead;
                }
            };
        }

    }

    /**
     * 下载回调
     */
    public interface ProgressListener {

        /**
         * 下载之前操作
         *
         * @param contentLength 下载的总长度
         */
        void onPreExecute(long contentLength);

        /**
         * 下载更新
         *
         * @param totalBytes 下载累计长度
         * @param done       是否下载完成
         */
        void update(long totalBytes, boolean done);

        /**
         * 下载失败
         */
        void onFail();
    }

    /**
     * 下载进度
     */
    private class UpdateMsg {
        UpdateMsg(long totalBytes, boolean done) {
            this.totalBytes = totalBytes;
            this.done = done;
        }

        public long totalBytes;
        public boolean done;
    }

}
