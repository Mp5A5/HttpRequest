package com.mp5a5.www.library.utils;

import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


/**
 * describe：文件下载工具类
 * author ：mp5a5 on 2019/4/4 16：13
 * email：wwb199055@126.com
 */
public class DownloadFileUtils {


    public static File getFileFromUrl(String url) throws IOException {
        String saveDirPath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/temp";
        //SDCard的状态
        String state = Environment.getExternalStorageState();
        //判断SDCard是否挂载上
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(AppContextUtils.getContext(), "SD卡不存在", Toast.LENGTH_SHORT).show();
            return null;
        }
        String savePath = isExistDir(saveDirPath);
        return new File(savePath, getNameFromUrl(url));

    }

    public static String getDownloadPath(String url) throws IOException {
        File file = getFileFromUrl(url);
        return Objects.requireNonNull(file).getAbsolutePath();
    }

    private static String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    private static String isExistDir(String saveDir) throws IOException {
        File downloadFile = new File(saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        return downloadFile.getAbsolutePath();
    }

}
