package com.example.test.Services;

import com.example.test.Class.Imagen;
import com.example.test.Class.ImgBase64;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IImage {
    @Headers("Authorization: Client-ID 8bcc638875f89d9")
    @POST("3/image")
    Call<Imagen> create(@Body ImgBase64 image);
}
