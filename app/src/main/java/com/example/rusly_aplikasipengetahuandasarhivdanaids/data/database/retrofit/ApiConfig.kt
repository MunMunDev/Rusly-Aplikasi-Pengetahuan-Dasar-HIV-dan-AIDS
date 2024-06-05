package com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.retrofit

import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.NotificationModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.PushNotificationModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.ResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiConfig {

    // POST
    //Daftar user

    @Headers(
        "Content-Type:application/json",
        "Authorization:key=AAAAOVHRxV4:APA91bGqvH35P8L77ZH-YG2N2eU1OqzvL5qfZ4KSW1CYixNCYJeYTHvLGNL-PDcxoEz55rWrQagxEaR4pn5fMkgOc7zhBp6Bw5njHDgblAxDOeaGK_SnW_BwT7DWemmcd8ISNg_BdPrd"
    )
    @POST("fcm/send")
    fun postChat(@Body send: PushNotificationModel):Call<PushNotificationModel>

    @Multipart
    @POST("rusly/upload_gambar.php")
    fun postGambarChat(
        @Part("post_gambar_chat") post_gambar_chat: RequestBody,
        @Part gambar: MultipartBody.Part,
        @Part("nama") nama: RequestBody,
    ): Call<ArrayList<ResponseModel>>

}
