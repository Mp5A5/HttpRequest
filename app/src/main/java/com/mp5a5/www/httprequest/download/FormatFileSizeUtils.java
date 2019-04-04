package com.mp5a5.www.httprequest.download;

import java.text.DecimalFormat;

/**
 * 作者：王文彬 on 2018/4/9 15：34
 * 邮箱：wwb199055@126.com
 */

public class FormatFileSizeUtils {

  //末尾保留两位的文件大小
  public static String FormatFileSize(long fileS) {
    DecimalFormat df = new DecimalFormat("#.00");
    String fileSizeString = "";
    if (fileS < 1024) {
      fileSizeString = df.format((double) fileS) + "B";
    } else if (fileS < 1048576) {
      fileSizeString = df.format((double) fileS / 1024) + "K";
    } else if (fileS < 1073741824) {
      fileSizeString = df.format((double) fileS / 1048576) + "M";
    } else {
      fileSizeString = df.format((double) fileS / 1073741824) + "G";
    }
    return fileSizeString;
  }


  /** 格式化文件大小，不保留末尾的0 */
  public static String formatFileSize(long len) {
    return formatFileSize(len, false);
  }

  /** 格式化文件大小，保留末尾的0，达到长度一致 */
  public static String formatFileSize(long len, boolean keepZero) {
    String size;
    DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
    DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
    if (len < 1024) {
      size = String.valueOf(len + "B");
    } else if (len < 10 * 1024) {
      // [0, 10KB)，保留两位小数
      size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
    } else if (len < 100 * 1024) {
      // [10KB, 100KB)，保留一位小数
      size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
    } else if (len < 1024 * 1024) {
      // [100KB, 1MB)，个位四舍五入
      size = String.valueOf(len / 1024) + "KB";
    } else if (len < 10 * 1024 * 1024) {
      // [1MB, 10MB)，保留两位小数
      if (keepZero) {
        size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024 / 1024 / (float) 100)) + "MB";
      } else {
        size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100) + "MB";
      }
    } else if (len < 100 * 1024 * 1024) {
      // [10MB, 100MB)，保留一位小数
      if (keepZero) {
        size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024 / 1024 / (float) 10)) + "MB";
      } else {
        size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10) + "MB";
      }
    } else if (len < 1024 * 1024 * 1024) {
      // [100MB, 1GB)，个位四舍五入
      size = String.valueOf(len / 1024 / 1024) + "MB";
    } else {
      // [1GB, ...)，保留两位小数
      size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100) + "GB";
    }
    return size;
  }

}
