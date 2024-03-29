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

    data class WriteRequest(val product: String, val star: Int, val review: String)
    @POST("/write")
    suspend fun postWrite(
        @Body body: WriteRequest
    ) : Response<JsonElement>

    @GET("/review")
    suspend fun getReview(
        @Query("product") product: String
    ) : Response<List<ReviewResponse>>

    @GET("/rate")
    suspend fun getRate(
        @Query("product") product: String
    ) : Response<RateResponse>

    @GET("/mypage")
    suspend fun myPage(
    ) : Response<MypageResponse>

    @GET("/list/environment")
    suspend fun getEnvironment(
    ): Response<List<EnvironmentResponse>>

    @GET("/list/vegan")
    suspend fun getVegan(
    ): Response<List<VeganResponse>>

    data class SaveRequest(val qrCodeData: String)
    @POST("/save")
    suspend fun postSave(
        @Body body: SaveRequest
    ) : Response<SaveResponse>

    @GET("/savelist")
    suspend fun getSave(
    ): Response<List<MysaveResponse>>



}