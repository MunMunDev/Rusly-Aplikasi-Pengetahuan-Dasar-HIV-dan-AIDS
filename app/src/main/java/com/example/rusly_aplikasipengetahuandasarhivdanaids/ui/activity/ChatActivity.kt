package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter.ListChatOtomatisAdapter
import com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter.ListPertanyaanOtomatisAdapter
import com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter.MessageKonsultasiAdapter
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase.FirebaseService
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.retrofit.ApiService
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.ChatOtomatisModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.MessageModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.NotificationModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.PertanyaanOtomatisModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.PushNotificationModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityChatBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.AlertDialogKeteranganBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.SharedPreferencesLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class ChatActivity : Activity() {
    lateinit var binding : ActivityChatBinding
    lateinit var sharedPref: SharedPreferencesLogin
    lateinit var database: DatabaseReference
    lateinit var databaseChatOtomatis: DatabaseReference
    lateinit var databasePertanyaanOtomatis: DatabaseReference
    lateinit var messageAdapter: MessageKonsultasiAdapter
    lateinit var messageArrayList : ArrayList<MessageModel>
    var idSent: String? = null
    var idReceived: String? = null
    var senderRoom: String? = null
    var receivedRoom: String? = null
    var nama: String? = null
    var token: String? = null
    val TAG = "ChatActivity";

    val STORAGE_PERMISSION_CODE = 10
    val IMAGE_CODE = 11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(view)

        fetchChatKonsultasi()
        fetchChatPertanyaanOtomatis()
        fetchtPertanyaanOtomatis()
        fetchDataSebelumnya()
        setCheckJamOperasional()
        setButton()
        hurufAcak()

        fetchChatPertanyaanOtomatis()
    }

    private fun fetchDataSebelumnya() {
        binding.apply {
            val bundle = intent.extras
            sharedPref = SharedPreferencesLogin(this@ChatActivity)
            idSent = sharedPref.getId()
            idReceived = bundle!!.getString("id").toString()
            nama = bundle!!.getString("nama").toString()
            token = bundle!!.getString("token").toString()

            tvNamaDokter.text = nama
        }
    }
    private fun setButton() {
        binding.apply {
            ivBack.setOnClickListener{
                finish()
            }

            btnInfoDontSendMessage.setOnClickListener{
                llMessage.visibility = View.VISIBLE
                llDontSendMessage.visibility = View.GONE

                etMessage.requestFocus()
            }

            btnInfoChatOtomatis.setOnClickListener{
                llMessage.visibility = View.GONE
                llDontSendMessage.visibility = View.VISIBLE
                llChatOtomatis.visibility = View.GONE
                svPertanyaanOtomatis.visibility = View.GONE
                rvListKonsultasiChatDokter.visibility = View.VISIBLE

                etMessage.requestFocus()
            }


            btnSendMessage.setOnClickListener {
                if(etMessage.text.trim().isNotEmpty()){
                    // Kirim data
                    database.addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            database.child("$senderRoom").child("idMessage").setValue(senderRoom)
                            database.child("$senderRoom").child("message").setValue(etMessage.text.toString().trim())
                            database.child("$senderRoom").child("idSent").setValue(idSent)
                            database.child("$senderRoom").child("idReceived").setValue(idReceived)
                            database.child("$senderRoom").child("tanggal").setValue(tanggalSekarangZonaMakassar())
                            database.child("$senderRoom").child("waktu").setValue(waktuSekarangZonaMakassar())
                            database.child("$senderRoom").child("ket").setValue("belum dibaca")

                            postMessage(sharedPref.getNama(), etMessage.text.toString(), token.toString())

                            etMessage.text = null
                            hurufAcak()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@ChatActivity, "Gagal", Toast.LENGTH_SHORT).show()
                        }

                    })
                }
            }
        }
    }

    private fun setCheckJamOperasional() {
        val waktuZonaMakassar = waktuSekarangZonaMakassar().split(":")
        val jam = waktuZonaMakassar[0].trim().toInt()
        if(jam.toLong() >= 8.toLong() && jam.toLong() <= 16.toLong()){
            binding.apply {
                llMessage.visibility = View.VISIBLE
                llDontSendMessage.visibility = View.GONE

                etMessage.requestFocus()
            }
        } else{
            binding.apply {
                llMessage.visibility = View.GONE
                llDontSendMessage.visibility = View.VISIBLE
            }
        }
    }

    private fun fetchChatKonsultasi(){
        database = FirebaseService().firebase().child("chats").child("message")
        database.addValueEventListener(object: ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                messageArrayList = ArrayList()
                for(value in snapshot.children){
                    var valueIdMessage: String? = ""
                    var valueMessage: String? = ""
                    var valueIdSent: String? = ""
                    var valueIdReceived: String? = ""
                    var valueTanggal: String? = ""
                    var valueWaktu: String? = ""
                    var valueKet: String? = ""

                    for(valueKedua in value.children){
                        val childIdMessage = value.child("idMessage").value.toString()
                        val childMessage = value.child("message").value.toString()
                        val childIdSent = value.child("idSent").value.toString()
                        val childIdReceived = value.child("idReceived").value.toString()
                        val childTanggal = value.child("tanggal").value.toString()
                        val childWaktu = value.child("waktu").value.toString()

                        if(childIdSent == idSent && childIdReceived == idReceived){
                            valueIdMessage = childIdMessage
                            valueMessage = childMessage
                            valueIdSent = childIdSent
                            valueIdReceived = childIdReceived
                            valueTanggal = childTanggal
                            valueWaktu = childWaktu
                            valueKet = childWaktu
                        }
                        else if(childIdSent == idReceived && childIdReceived == idSent){
                            valueIdMessage = childIdMessage
                            valueMessage = childMessage
                            valueIdSent = childIdSent
                            valueIdReceived = childIdReceived
                            valueTanggal = childTanggal
                            valueWaktu = childWaktu
                            valueKet = childWaktu
                        }

                    }
                    if(valueMessage!!.isNotEmpty() && valueIdSent!!.isNotEmpty() && valueIdReceived!!.isNotEmpty()){
                        messageArrayList.add(
                            MessageModel(
                                valueIdMessage.toString(),
                                valueMessage.toString(),
                                valueIdSent.toString(),
                                valueIdReceived.toString(),
                                valueTanggal.toString(),
                                valueWaktu.toString(),
                                valueKet.toString()
                            )
                        )
                    }
                }
                val sorted = messageArrayList.sortedWith(compareBy { it.idMessage })

                setAdapterChatKonsultasi(sorted)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, "Gagal", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setAdapterChatKonsultasi(list: List<MessageModel>) {
        val messageAdapter = MessageKonsultasiAdapter(this@ChatActivity, list, sharedPref.getId())

        binding.apply {
            rvListKonsultasiChatDokter.layoutManager = LinearLayoutManager(this@ChatActivity)
            rvListKonsultasiChatDokter.adapter = messageAdapter
            rvListKonsultasiChatDokter.scrollToPosition(messageArrayList.size-1)
            messageAdapter.notifyDataSetChanged()
        }
    }

    private fun fetchChatPertanyaanOtomatis(){
        databaseChatOtomatis = FirebaseService().firebase().child("chats").child("pertanyaanOtomatis")
        databaseChatOtomatis.addValueEventListener(object: ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                var chatOtomatisArrayList : ArrayList<ChatOtomatisModel> = ArrayList()
                for(value in snapshot.children){
                    var valueIdPertanyaanOtomatis: String? = ""
                    var valueIdSent: String? = ""
                    var valuePertanyaan: String? = ""
                    var valueJawaban: String? = ""

                    for(valueKedua in value.children){
                        val childIdPertanyaanOtomatis = value.child("idPertanyaanOtomatis").value.toString()
                        val childIdSent = value.child("idSent").value.toString()
                        val childPertanyaan = value.child("pertanyaan").value.toString()
                        val childJawaban = value.child("jawaban").value.toString()

                        if(childIdSent == idSent){
                            valueIdPertanyaanOtomatis = childIdPertanyaanOtomatis
                            valueIdSent = childIdSent
                            valuePertanyaan = childPertanyaan
                            valueJawaban = childJawaban
                        }
                    }

                    if(valueIdSent!!.isNotEmpty()){
                        chatOtomatisArrayList.add(
                            ChatOtomatisModel(
                                valueIdPertanyaanOtomatis.toString(),
                                valueIdSent.toString(),
                                "1",
                                valuePertanyaan.toString(),
                            )
                        )
                        chatOtomatisArrayList.add(
                            ChatOtomatisModel(
                                valueIdPertanyaanOtomatis.toString(),
                                valueIdSent.toString(),
                                "2",
                                valueJawaban.toString(),
                            )
                        )
                    }
//                    messageArrayList.add(MessageModel(valueMessage!!, valueIdSent!!, valueIdReceived!!))
//                        messageArrayList[0] = MessageModel(valueMessage!!, valueIdSent!!, valueIdReceived!!)
                    Log.d(TAG, "onDataChange 1: ${chatOtomatisArrayList.size}")
                    Log.d(TAG, "onDataChange 2: ${chatOtomatisArrayList.size}")

                }
                Log.d(TAG, "onDataChange: size: ${chatOtomatisArrayList.size}")

                val sorted = chatOtomatisArrayList.sortedWith(compareBy { it.idPertanyaanOtomatis })
                Log.d(TAG, "sorted: ")
                for(i in sorted){
                    Log.d(TAG, "sorted data 23: ${i.idSent}, ${i.message}, ${i.idJenis}, ${i.idPertanyaanOtomatis} ")
                }

                setAdapterChatPertanyaanOtomatis(sorted)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, "Gagal", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setAdapterChatPertanyaanOtomatis(list: List<ChatOtomatisModel>){
        val chatOtomatisAdapter = ListChatOtomatisAdapter(this@ChatActivity, list)

        binding.apply {
            rvListChatPertanyaan.layoutManager = LinearLayoutManager(this@ChatActivity)
            rvListChatPertanyaan.adapter = chatOtomatisAdapter
            rvListChatPertanyaan.scrollToPosition(list.size-1)
            chatOtomatisAdapter.notifyDataSetChanged()
        }
    }

    private fun fetchtPertanyaanOtomatis(){
        databasePertanyaanOtomatis = FirebaseService().firebase().child("pertanyaan")
        databasePertanyaanOtomatis.addValueEventListener(object: ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                var pertanyaanOtomatisArrayList : ArrayList<PertanyaanOtomatisModel> = ArrayList()
                for(value in snapshot.children){
                    var valueIdPertanyaan: String? = ""
                    var valuePertanyaan: String? = ""
                    var valueJawaban: String? = ""

                    for(valueKedua in value.children){
                        val childIdPertanyaan = value.child("idPertanyaan").value.toString()
                        val childPertanyaan = value.child("pertanyaan").value.toString()
                        val childJawaban = value.child("jawaban").value.toString()

                        valueIdPertanyaan = childIdPertanyaan
                        valuePertanyaan = childPertanyaan
                        valueJawaban = childJawaban
                    }
                    if(valuePertanyaan!!.isNotEmpty() && valueJawaban!!.isNotEmpty()){
                        pertanyaanOtomatisArrayList.add(
                            PertanyaanOtomatisModel(
                                valueIdPertanyaan.toString(),
                                valuePertanyaan.toString(),
                                valueJawaban.toString()
                            )
                        )
                    }
                    Log.d(TAG, "onDataChange 1: ${pertanyaanOtomatisArrayList.size}")
                    Log.d(TAG, "onDataChange 2: ${pertanyaanOtomatisArrayList.size}")

                }
                Log.d(TAG, "onDataChange: size: ${pertanyaanOtomatisArrayList.size}")

                val sorted = pertanyaanOtomatisArrayList.sortedWith(compareBy { it.idPertanyaan })
                Log.d(TAG, "sorted: ")
                for(i in sorted){
                    Log.d(TAG, "sorted data: ${i.idPertanyaan}, ${i.pertanyaan}, ${i.jawaban} ")
                }

                setAdapterPertanyaanOtomatis(sorted)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, "Gagal", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setAdapterPertanyaanOtomatis(sorted: List<PertanyaanOtomatisModel>) {
        val pertanyaanOtomatisAdapter = ListPertanyaanOtomatisAdapter(this@ChatActivity, sorted, idSent!!)
        binding.apply {
            rvListPertanyaanOtomatis.layoutManager = LinearLayoutManager(this@ChatActivity)
            rvListPertanyaanOtomatis.adapter = pertanyaanOtomatisAdapter
            rvListPertanyaanOtomatis.scrollToPosition(sorted.size-1)
            svPertanyaanOtomatis.fullScroll(sorted.size)
        }
        pertanyaanOtomatisAdapter.notifyDataSetChanged()
    }

    private fun setShowJudul(jam: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@ChatActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = "Jam Operasional"
            tvBodyKeterangan.text = "Saran chat Dokter pada jam $jam"
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    fun hurufAcak(){

        val dateTime = "${tanggalSekarangZonaMakassar()}-${waktuSekarangZonaMakassar()}"

        this.senderRoom = "${dateTime}--$idSent--$idReceived"
        this.receivedRoom = "${dateTime}--$idReceived--$idSent"
    }

    fun tanggalSekarang():String{
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        val dateTime = simpleDateFormat.format(calendar.time)

        return dateTime
    }

    fun waktuSekarang():String{
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss")
        simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        val time = simpleDateFormat.format(calendar.time)

        return time
    }

    @SuppressLint("SimpleDateFormat")
    fun tanggalSekarangZonaMakassar():String{
        var date = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val makassarZone  = ZoneId.of("Asia/Makassar")
            val makassarTanggal = LocalDate.now(makassarZone)
            val tanggal = makassarTanggal
            date = "$tanggal"
        } else {
            val makassarTimeZone = TimeZone.getTimeZone("Asia/Makassar")
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            dateFormat.timeZone = makassarTimeZone
            val currentDate = Date()
            val makassarDate = dateFormat.format(currentDate)
            date = makassarDate
        }

        return date
    }

    @SuppressLint("SimpleDateFormat")
    fun waktuSekarangZonaMakassar():String{
        var time = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val makassarZone  = ZoneId.of("Asia/Makassar")
            val makassarTime = LocalTime.now(makassarZone)
            val waktu = makassarTime.toString().split(".")
            time = waktu[0]

        } else {
            val makassarTimeZone = TimeZone.getTimeZone("Asia/Makassar")
            val timeFormat = SimpleDateFormat("HH:mm:ss")
            timeFormat.timeZone = makassarTimeZone
            val currentTime = Date()
            val makassarTime = timeFormat.format(currentTime)
            time = makassarTime
        }
        return time
    }

    fun idMessage(): String{
        var str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        var hurufAcak = ""
        for(i in 1..20){
            hurufAcak+=str.random()
        }

        return hurufAcak
    }

    fun postMessage(valueNama:String, valueMessage:String, token: String){
        ApiService.getRetrofit().postChat(PushNotificationModel(NotificationModel(valueNama,valueMessage), token))
            .enqueue(object: Callback<PushNotificationModel> {
                override fun onResponse(
                    call: Call<PushNotificationModel>,
                    response: Response<PushNotificationModel>
                ) {
//                    Toast.makeText(this@ChatActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(
                    call: Call<PushNotificationModel>,
                    t: Throwable
                ) {
                    Toast.makeText(this@ChatActivity, "Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
    }


}