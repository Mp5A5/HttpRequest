package com.mp5a5.www.httprequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import com.mp5a5.www.httprequest.net.api.java_nba.NBANoSingletonService
import com.mp5a5.www.httprequest.net.api.kt_nba.NBAKTService
import com.mp5a5.www.httprequest.net.api.upload.UploadService
import com.mp5a5.www.httprequest.net.entity.NBAJEntity
import com.mp5a5.www.httprequest.net.entity.NBAKTEntity
import com.mp5a5.www.httprequest.net.entity.UploadEntity
import com.mp5a5.www.library.use.BaseObserver
import com.mp5a5.www.library.use.UploadManager
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_kt_test.*
import org.jetbrains.anko.toast
import java.io.File
import java.net.URISyntaxException

class KotlinTestActivity : RxAppCompatActivity() {


    private val list = mutableListOf<File>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kt_test)

        btnNBA.setOnClickListener {
            NBAKTService
                .getKtNBAInfo("6949e822e6844ae6453fca0cf83379d3")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe(object : BaseObserver<NBAKTEntity>(this, true) {
                    override fun onSuccess(response: NBAKTEntity) {
                        toast(response.result?.title!!)
                    }
                })
        }

        tvTest.setOnClickListener {
            NBANoSingletonService()
                .getNBAInfo("6949e822e6844ae6453fca0cf83379d3")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe(object : BaseObserver<NBAJEntity>() {
                    override fun onSuccess(response: NBAJEntity?) {
                        toast(response?.result?.title!!)
                    }
                })
        }

        btnChoose.setOnClickListener { v ->
            RxPermissions(this).requestEach(android.Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe {
                if (it.granted) {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
                    intent.type = "image/*"
                    startActivityForResult(intent, 1)
                }
                if (it.shouldShowRequestPermissionRationale) {
                    toast("请打开权限")
                }
            }


        }

        btnUpload.setOnClickListener { v ->
            UploadManager.getInstance()
                .uploadMultiPicList(list)
                .subscribe { t ->
                    UploadService.getInstance()
                        .uploadPic(t)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(this@KotlinTestActivity.bindToLifecycle())
                        .subscribe(object : BaseObserver<UploadEntity>(this, true) {
                            override fun onSuccess(response: UploadEntity?) {
                                toast(response?.msg!!)
                            }

                        })
                }

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (1 == requestCode) {
                val uri = data?.data
                val arrayOf = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = this.contentResolver.query(uri, arrayOf, null, null, null);
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    val path = cursor.getString(columnIndex);
                    var file: File? = null
                    try {
                        file = File(path)
                        list.add(file)
                    } catch (e: URISyntaxException) {
                        e.printStackTrace()
                    }
                }
                cursor.close();
            }
            toast("选取了：${list.size} 张照片")

        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}

