package com.mp5a5.www.library.use;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.mp5a5.www.library.utils.AppContextUtils;

import org.reactivestreams.Publisher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author ：mp5a5 on 2019/1/2 14：31
 * @describe 文件上传
 * @email：wwb199055@126.com
 */
public class UploadManager {


    private UploadManager() {
    }

    private static volatile UploadManager instance;

    public static UploadManager getInstance() {
        if (instance == null) {
            synchronized (UploadManager.class) {
                if (instance == null) {
                    instance = new UploadManager();
                }
            }
        }
        return instance;
    }

    public Single<List<MultipartBody.Part>> uploadMultiPicList(@NonNull List<File> pathList) {

        return Flowable.fromIterable(pathList).concatMap((Function<File, Flowable<MultipartBody.Part>>) f -> {
            Bitmap bitmap = BitmapFactory.decodeFile(f.toString());
            File file = compressBitmapToFile(bitmap, AppContextUtils.getContext());
            Log.e("-->文件大小：", bytesTrans(file.length()) + ",fileSize=" + file.length() / 1024 + "kb");
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            return Flowable.just(imageBodyPart);
        }).collect((Callable<List<MultipartBody.Part>>) ArrayList::new, List::add)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Single<List<MultipartBody.Part>> uploadPicList(@NonNull List<File> pathList) {


        return Flowable.fromIterable(pathList).concatMap(new Function<File, Publisher<MultipartBody.Part>>() {
            @Override
            public Publisher<MultipartBody.Part> apply(File f) throws Exception {
                Log.e("-->", "===Subscriber: " + Thread.currentThread().getName());
                Bitmap bitmap = BitmapFactory.decodeFile(f.toString());
                File file = compressBitmapToFile(bitmap, AppContextUtils.getContext());
                Log.e("-->文件大小：", bytesTrans(file.length()) + ",fileSize=" + file.length() / 1024 + "kb");
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                return Flowable.just(imageBodyPart);
            }
        }).collect(new Callable<List<MultipartBody.Part>>() {
            @Override
            public List<MultipartBody.Part> call() throws Exception {
                Log.e("-->", "===Subscriber: " + Thread.currentThread().getName());
                return new LinkedList<>();
            }
        }, new BiConsumer<List<MultipartBody.Part>, MultipartBody.Part>() {
            @Override
            public void accept(List<MultipartBody.Part> partList, MultipartBody.Part part) throws Exception {
                Log.e("-->", "===Subscriber: " + Thread.currentThread().getName());
                partList.add(part);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());








                /*concatMap((Function<File, Flowable<MultipartBody.Part>>) f -> {
            Log.e("-->", "===Subscriber: " + Thread.currentThread().getName());
            Bitmap bitmap = BitmapFactory.decodeFile(f.toString());
            File file = compressBitmapToFile(bitmap, AppContextUtils.getContext());
            Log.e("-->文件大小：", bytesTrans(file.length()) + ",fileSize=" + file.length() / 1024 + "kb");
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            return Flowable.just(imageBodyPart);
        }).collect((Callable<List<MultipartBody.Part>>) ArrayList::new, List::add)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());*/
    }

    private File compressBitmapToFile(@NonNull Bitmap bitmap, Context context) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到outputStream中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        int options = 100;
        Log.e("-->图片未压缩前的大小:", bytesTrans((outputStream.toByteArray().length)));
        //循环判断如果压缩后图片是否大于500kb,大于继续压缩
        while (outputStream.toByteArray().length / 1024 > 1024) {
            //重置outputStream即清空outputStream
            outputStream.reset();
            //每次都减少10
            options -= 10;
            //这里压缩options%，把压缩后的数据存放到outputStream中
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
            long length = outputStream.toByteArray().length;
            Log.e("-->图片压缩后的大小:", bytesTrans(length));
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);
        String bitmapUrl = context.getFilesDir().getAbsoluteFile() + File.separator + filename + ".jpg";
        File file = new File(bitmapUrl);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(outputStream.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        recycleBitmap(bitmap);
        return file;
    }


    private String bytesTrans(long bytes) {
        BigDecimal fileSize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = fileSize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        if (returnValue > 1) return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = fileSize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
        return (returnValue + "KB");
    }

    private void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps == null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }

}
