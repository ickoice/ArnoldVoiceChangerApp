package com.radanlievristo.arnoldvoicechangerapp;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SpeakApi {

    @POST("speak")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<ResponseBody> speakQuote(@Body SpeakRequestModel speakRequest);
}
