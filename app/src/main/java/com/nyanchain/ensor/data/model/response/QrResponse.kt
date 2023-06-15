package com.nyanchain.ensor.data.model.response


    data class QrResponse(
        val tfresult: Boolean,
        val category: Int,
        val censorCom: String,
        val censorID: String,
        val censorText: String,
        val imgUrl: String
    )