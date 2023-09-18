package com.nyanchain.ensor.data.network


import com.google.gson.JsonElement
import com.nyanchain.ensor.data.model.response.*
import retrofit2.Response
import retrofit2.http.*

interface APIs {

    data class RequestSignup(val accessToken: String)
    @POST("/login")
    suspend fun signUp(
        @Body body: RequestSignup
    ) : Response<LoginResponse>

    data class QrRequest(val qrCodeData: String)
    @POST("/censor")
    suspend fun postQr(
        @Body body: QrRequest
    ) : Response<JsonElement> //Todo: 백엔드 수정되면 QrResponse로 수정

    @GET("/review") //임의로 설정. 백보고 api 다시확인필요
    suspend fun getReview(
    ) : Response<List<ReviewResponse>>

    @GET("/mypage")
    suspend fun myPage(
    ) : Response<MypageResponse>

    @GET("/list/environment")
    suspend fun getEnvironment(
    ): Response<List<EnvironmentResponse>>

    @GET("/list/vegan")
    suspend fun getVegan(
    ): Response<List<VeganResponse>>

    data class SaveRequest(val qrCodeData: String, var productName: String)
    @POST("/save")
    suspend fun postSave(
        @Body body: SaveRequest
    ) : Response<SaveResponse>

    @GET("/savelist")
    suspend fun getSave(
    ): Response<List<MysaveResponse>>



}