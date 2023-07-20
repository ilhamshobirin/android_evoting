package com.example.evoting.retrofit

import com.example.evoting.model.LoginRequest
import com.example.evoting.model.LoginResponse
import com.example.evoting.model.QuickCountResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiEndpoint {

    //======= AUTH ========
    @Headers("Accept:application/json", "Content-Type:application/json")
    @POST("login")
    fun postLogin(@Body info: LoginRequest): Call<LoginResponse>

    @Headers("Accept:application/json", "Content-Type:application/json")
    @POST("logout")
    fun postLogout(@Header("Authorization") token: String): Call<ResponseBody>

    //======= QUICK COUNT ========
    @Headers("Accept:application/json")
    @GET("quick_count")
    fun getQuickCount(@Header("Authorization") token: String): Call<QuickCountResponse>

//
//    @Headers("Accept:application/json", "Content-Type:application/json")
//    @POST("register")
//    fun postRegister(@Body info: RegisterRequest): Call<RegisterResponse>
//
//    @Headers("Accept:application/json", "Content-Type:application/json")
//    @POST("logout")
//    fun postLogout(@Header("Authorization") token: String): Call<ResponseBody>
//
//    //======= REPORT/TICKET ========
//    @Headers("Accept:application/json")
//    @GET("category_report")
//    fun getCategoryReport(@Header("Authorization") token: String): Call<CategoryReportResponse>
//
//    @Multipart
//    @Headers("Accept:application/json")
//    @POST("report-trouble")
//    fun postReport(
//        @Header("Authorization") token: String,
//        @Part report_pict: MultipartBody.Part?,
//        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
//    ): Call<ResponseBody>
//
//    @Headers("Content-Type:application/json")
//    @GET("report-trouble/report-by/{id}")
//    fun getReportByUserId(@Path("id") id: Int?,
//                    @Header("Authorization") token: String): Call<ReportResponse>
//
//    //======= ASSET ========
//    @Headers("Accept:application/json")
//    @GET("assets")
//    fun getAssets(@Header("Authorization") token: String): Call<AssetResponse>
//
//    @Headers("Accept:application/json")
//    @GET("used-assets/used-by/{id}")
//    fun getMyAssets(@Path("id") id: Int?,
//                    @Header("Authorization") token: String): Call<MyAssetResponse>
//
//    @Headers("Accept:application/json")
//    @POST("used_assets")
//    fun postRentAsset(@Header("Authorization") token: String,
//                      @Body info: RentAssetRequest
//    ): Call<ResponseBody>
}