package com.mp5a5.www.httprequest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.mp5a5.www.httprequest.net.api.NBAServiceT;
import com.mp5a5.www.httprequest.net.api.NbaService;
import com.mp5a5.www.httprequest.net.api.UploadService;
import com.mp5a5.www.httprequest.net.entity.NBAEntity;
import com.mp5a5.www.library.net.revert.BaseResponseEntity;
import com.mp5a5.www.library.use.BaseDisposableSubscriber;
import com.mp5a5.www.library.use.BaseObserver;
import com.mp5a5.www.library.use.UploadManager;
import com.mp5a5.www.library.utils.RxTransformerUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ：mp5a5 on 2019/1/7 14：03
 * @describe
 * @email：wwb199055@126.com
 */
public class TestAllActivity extends RxAppCompatActivity {

    private List<File> list = new ArrayList<File>();
    private CompositeDisposable mCompositeDisposable = null;

    @SuppressLint("IntentReset")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCompositeDisposable= new CompositeDisposable();

        findViewById(R.id.btnNBA).setOnClickListener(v -> {
            NbaService.getInstance()
                    .getNBAInfo("6949e822e6844ae6453fca0cf83379d3")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(this.bindToLifecycle())
                    .subscribe(new BaseObserver<NBAEntity>(this, true) {

                        @Override
                        public void onSuccess(NBAEntity response) {
                            Toast.makeText(TestAllActivity.this, response.result.title, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(TestAllActivity.this, String.valueOf(response.code), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(TestAllActivity.this, response.reason, Toast.LENGTH_SHORT).show();
                        }

                    });


        });


        findViewById(R.id.btnOwner).setOnClickListener(v -> {
            /*BaseDisposableObserver<NBAEntity> disposable = NbaService.getInstance()
                    .getNBAInfo("6949e822e6844ae6453fca0cf83379d3")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableObserver<NBAEntity>(this, true) {
                        @Override
                        public void onSuccess(NBAEntity response) {
                            Toast.makeText(TestAllActivity.this, response.result.title, Toast.LENGTH_SHORT).show();
                        }
                    });*/

            BaseDisposableSubscriber<NBAEntity> disposable = NbaService.getInstance()
                    .getNBAInfo1("6949e822e6844ae6453fca0cf83379d3")
                    .compose(RxTransformerUtils.flowableTransformer())
                    .subscribeWith(new BaseDisposableSubscriber<NBAEntity>(this, true) {
                        @Override
                        public void onSuccess(NBAEntity response) {
                            Toast.makeText(TestAllActivity.this, response.result.title, Toast.LENGTH_SHORT).show();
                        }
                    });

            mCompositeDisposable.add(disposable);

        });

        findViewById(R.id.tvTest).setOnClickListener(v -> {
            NBAServiceT.INSTANCE
                    .getNBAInfo("6949e822e6844ae6453fca0cf83379d3")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(this.bindToLifecycle())
                    .subscribe(new BaseObserver<NBAEntity>() {

                        @Override
                        public void onSuccess(NBAEntity response) {
                            Toast.makeText(TestAllActivity.this, response.result.title, Toast.LENGTH_SHORT).show();
                        }

                    });
        });

        findViewById(R.id.btnChoose).setOnClickListener(v -> {

            new RxPermissions(this)
                    .requestEach(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(it -> {
                        if (it.granted) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent, 1);
                        }
                        if (it.shouldShowRequestPermissionRationale) {
                            Toast.makeText(this, "请打开权限", Toast.LENGTH_SHORT).show();
                        }
                    });


        });

        findViewById(R.id.btnUpload).setOnClickListener(v -> {
            UploadManager.getInstance()
                    .uploadMultiPicList(list)
                    .subscribe(parts -> {
                        UploadService.getInstance()
                                .uploadPic(parts)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .compose(this.bindToLifecycle())
                                .subscribe(new BaseObserver<BaseResponseEntity>(this, true) {
                                    @Override
                                    public void onSuccess(BaseResponseEntity response) {
                                        Toast.makeText(TestAllActivity.this, response.msg, Toast.LENGTH_SHORT).show();
                                    }

                                });


                    });

        });

        findViewById(R.id.btnDownload).setOnClickListener(v -> {
            startActivity(new Intent(this, DownloadActivity.class));

        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (1 == requestCode) {
                Uri uri = Objects.requireNonNull(data).getData();
                String[] arrayOf = {MediaStore.Images.Media.DATA};
                Cursor cursor = this.getContentResolver().query(Objects.requireNonNull(uri), arrayOf, null, null, null);
                if (Objects.requireNonNull(cursor).moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    String path = cursor.getString(columnIndex);
                    File file = null;
                    try {
                        file = new File(path);
                        list.add(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                cursor.close();
            }
            Toast.makeText(this, "选取了：" + list.size() + " 张照片", Toast.LENGTH_SHORT).show();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null && mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }
}





