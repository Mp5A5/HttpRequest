package com.mp5a5.www.httprequest

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mp5a5.www.httprequest.download.FormatFileSizeUtils
import com.mp5a5.www.library.use.DownloaderManager
import com.mp5a5.www.library.utils.DownloadFileUtils
import kotlinx.android.synthetic.main.activity_download.*
import org.jetbrains.anko.toast

/**
 * @describe
 * @author ：mp5a5 on 2019/4/4 17：28
 * @email：wwb199055@126.com
 */

class DownloadActivity : AppCompatActivity(), DownloaderManager.ProgressListener {

    override fun onPreExecute(contentLength: Long) {
        if (this.contentLength == 0L) {
            this.contentLength = contentLength
            tv_download_progress.max = (contentLength / 1024).toInt()

        }
    }

    @SuppressLint("SetTextI18n")
    override fun update(totalBytes: Long, done: Boolean) {
        // 注意加上断点的长度
        this.totalBytes = totalBytes + breakPoints
        tv_download_progress.progress = ((totalBytes + breakPoints) / 1024).toInt()
        tv_pdf_progress.text = FormatFileSizeUtils.FormatFileSize(totalBytes + breakPoints) + "/" + FormatFileSizeUtils
            .FormatFileSize(contentLength)

        if (done) {
            //下载文件的路径
            val path = DownloadFileUtils.getDownloadPath(urlPath)
        }
    }

    override fun onFail() {
        breakPoints = 0L
        totalBytes = 0L
        setFailView()
    }

    private val urlPath = "http://gdown.baidu.com/data/wisegame/55dc62995fe9ba82/jinritoutiao_448.apk"
    private val permissions_storage = arrayOf(
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE"
    )
    private val request_external_storage = 1

    private lateinit var downloaderManager: DownloaderManager

    private var breakPoints = 0L
    private var totalBytes = 0L
    private var contentLength: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
        initView()

        iv_stop_download.setOnClickListener {
            downloaderManager.pause()
            breakPoints = totalBytes
            setStopView()
        }

        bt_re_download.setOnClickListener {
            downloaderManager.downloadStartPoint(breakPoints)
            setDownloadView()
        }

        bt_download.setOnClickListener {
            downloaderManager = DownloaderManager(urlPath, this)
            downloaderManager.downloadStartPoint(0L)
            setDownloadView()
        }

    }

    private fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断是否有这个权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager
                    .PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, permissions_storage, request_external_storage
                )
            } else {
                downloaderManager = DownloaderManager(urlPath, this)
                downloaderManager.downloadStartPoint((0L))
                setDownloadView()
            }
        } else {
            downloaderManager = DownloaderManager(urlPath, this)
            downloaderManager.downloadStartPoint(0L)
            setDownloadView()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == request_external_storage && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            downloaderManager = DownloaderManager(urlPath, this)
            downloaderManager.downloadStartPoint(0L)
            setDownloadView()
        } else {
            toast("没有权限")
        }
    }

    private fun setDownloadView() {
        ll_download_set.visibility = View.VISIBLE
        bt_re_download.visibility = View.GONE
        bt_download.visibility = View.GONE
    }

    private fun setStopView() {
        ll_download_set.visibility = View.VISIBLE
        bt_re_download.visibility = View.VISIBLE
        bt_download.visibility = View.GONE
    }

    private fun setFailView() {
        ll_download_set.visibility = View.GONE
        bt_re_download.visibility = View.GONE
        bt_download.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        downloaderManager.cancelDownload()
    }
}