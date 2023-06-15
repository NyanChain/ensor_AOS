package com.nyanchain.ensor.data.model.response

import com.kakao.sdk.user.model.User

data class LoginResponse(
    val message: String,
    val success: Boolean,
    val token: String,
    val user: User
) {
    data class User(
        val email: String,
        val kakao_id: String,
        val nickname: String
    )
}