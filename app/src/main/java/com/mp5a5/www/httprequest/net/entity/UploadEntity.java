package com.mp5a5.www.httprequest.net.entity;

import com.mp5a5.www.library.net.revert.BaseResponseEntity;

import java.util.List;


/**
 * describe：
 * author ：mp5a5 on 2019/1/02 17：34
 * email：wwb199055@126.com
 */

public class UploadEntity extends BaseResponseEntity {

    public DataBean data;

    public class DataBean {

        public List<FileBean> files;

        public class FileBean {
            public String fileName;
            public String url;
        }
    }



}
