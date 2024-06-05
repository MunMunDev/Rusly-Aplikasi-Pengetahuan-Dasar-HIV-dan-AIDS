package com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model

import com.google.gson.annotations.SerializedName

class ChatOtomatisModel (
    @SerializedName("idPertanyaanOtomatis")
    var idPertanyaanOtomatis: String? = null,

    @SerializedName("idSent")
    var idSent: String? = null,

    @SerializedName("idJenis")
    var idJenis: String? = null,    // Jenis. 1 maka pertanyaan, 2 maka jawaban

    @SerializedName("message")
    var message: String? = null

//    @SerializedName("pertanyaan")
//    var pertanyaan: String? = null,
//
//    @SerializedName("jawaban")
//    var jawaban: String? = null
)