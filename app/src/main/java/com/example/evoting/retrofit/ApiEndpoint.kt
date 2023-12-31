package com.example.evoting.retrofit

import com.example.evoting.model.voting.AddVotingRequest
import com.example.evoting.model.login.LoginRequest
import com.example.evoting.model.login.LoginResponse
import com.example.evoting.model.QuickCountResponse
import com.example.evoting.model.candidate.AddCandidateRequest
import com.example.evoting.model.candidate.AllCandidateResponse
import com.example.evoting.model.committee.AddCommitteeRequest
import com.example.evoting.model.committee.AllCommitteeResponse
import com.example.evoting.model.voting.AddVoterRequest
import com.example.evoting.model.voting.AllVoterResponse
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
    @GET("rekap")
    fun getRekapData(@Header("Authorization") token: String): Call<QuickCountResponse>

    //======= CANDIDATE ========
    @Headers("Accept:application/json")
    @GET("candidates")
    fun getAllCandidate(@Header("Authorization") token: String): Call<AllCandidateResponse>

    @Multipart
    @Headers("Accept:application/json")
    @POST("candidates")
    fun postCandidate(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part?,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Call<ResponseBody>

    @Headers("Accept:application/json")
    @PUT("candidates/{id}")
    fun putCandidate(
        @Header("Authorization") token: String,
        @Path("id") id:Int,
        @Body info: AddCandidateRequest,
    ): Call<ResponseBody>

    @Headers("Accept:application/json")
    @DELETE("candidates/{id}")
    fun deleteCandidate(
        @Header("Authorization") token: String,
        @Path("id") id:Int,
    ): Call<ResponseBody>


    //======= COMMITTEE ========
    @Headers("Accept:application/json")
    @GET("committees")
    fun getAllCommittee(@Header("Authorization") token: String): Call<AllCommitteeResponse>

    @Headers("Accept:application/json")
    @POST("committees")
    fun addCommittee(@Header("Authorization") token: String, @Body info: AddCommitteeRequest): Call<ResponseBody>


    //======= VOTING ========
    @Headers("Accept:application/json")
    @POST("votings")
    fun addVoting(@Header("Authorization") token: String, @Body info: AddVotingRequest): Call<ResponseBody>

    @Headers("Accept:application/json")
    @DELETE("votings/delete/all")
    fun deleteVoting(@Header("Authorization") token: String): Call<ResponseBody>


    //======= VOTER ========
    @Headers("Accept:application/json")
    @GET("voters")
    fun getAllVoter(@Header("Authorization") token: String): Call<AllVoterResponse>

    @Headers("Accept:application/json")
    @POST("voters")
    fun addVoter(@Header("Authorization") token: String, @Body info: AddVoterRequest): Call<ResponseBody>
}