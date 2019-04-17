package com.mp5a5.www.httprequest.net.api.upload;

import com.mp5a5.www.httprequest.net.entity.UploadEntity;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import java.util.List;

/**
 * @author ：mp5a5 on 2019/1/2 15：33
 * @describe
 * @email：wwb199055@126.com
 */
public interface UploadApi {

    @Multipart
    @POST("ues/app/upload/pictures")
    Observable<UploadEntity> postUpload(@Part List<MultipartBody.Part> partList);
}
