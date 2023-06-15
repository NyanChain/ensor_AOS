package com.nyanchain.ensor.data.model.response

data class EnvironmentResponse(
    val category: Int,
    val censorCom: String,
    val censorID: String,
    val censorText: String,
    val imgUrl: String
)