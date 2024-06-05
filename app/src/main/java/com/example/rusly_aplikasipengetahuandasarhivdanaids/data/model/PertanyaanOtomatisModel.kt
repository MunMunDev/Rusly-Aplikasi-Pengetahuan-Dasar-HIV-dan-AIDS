package com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model

import com.google.gson.annotations.SerializedName

class PertanyaanOtomatisModel (
    @SerializedName("idPertanyaan")
    var idPertanyaan: String? = null,

    @SerializedName("pertanyaan")
    var pertanyaan: String? = null,

    @SerializedName("jawaban")
    var jawaban: String? = null
)