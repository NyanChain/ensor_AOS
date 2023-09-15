package com.nyanchain.ensor.data.model.response

import com.kakao.sdk.user.model.User

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val user: User,
    val token: String
) {
    data class User(
        val kakao_id: String,
        val nickname: String,
        val email: String
    )
}