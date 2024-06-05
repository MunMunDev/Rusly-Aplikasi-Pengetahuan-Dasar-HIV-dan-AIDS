package com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model

import com.google.gson.annotations.SerializedName

class MessageModel {
    @SerializedName("idMessage")
    var idMessage: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("gambar")
    var gambar: String? = null

    @SerializedName("idSent")
    var idSent: String? = null

    @SerializedName("idReceived")
    var idReceived: String? = null

    @SerializedName("tanggal")
    var tanggal: String? = null

    @SerializedName("waktu")
    var waktu: String? = null

    @SerializedName("ket")
    var ket: String? = null

//    @SerializedName("ket")
//    var ket: String? = null

    constructor(){}

//    constructor(message:String, idSent:String, idReceived:String, ket:String){
//        this.message = message
//        this.idSent = idSent
//        this.idReceived = idReceived
//        this.ket = ket
//    }

    constructor(idMessage:String, message:String, gambar:String, idSent:String, idReceived:String, tanggal:String, waktu:String, ket:String){
        this.idMessage = idMessage
        this.message = message
        this.gambar = gambar
        this.idSent = idSent
        this.idReceived = idReceived
        this.tanggal = tanggal
        this.waktu = waktu
        this.ket = ket
    }
}