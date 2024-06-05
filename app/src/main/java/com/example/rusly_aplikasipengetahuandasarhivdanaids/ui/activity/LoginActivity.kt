package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase.FirebaseService
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.UsersModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityLoginBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.admin.AdminMainActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.admin.AdminSemuaUserActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.user.MainActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.LoadingAlertDialog
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.SharedPreferencesLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : Activity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var loading: LoadingAlertDialog
    lateinit var sharedPref : SharedPreferencesLogin
    lateinit var database : DatabaseReference
    lateinit var usersArrayList: ArrayList<UsersModel>
    val TAG = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(view)

        sharedPref = SharedPreferencesLogin(this@LoginActivity)
        loading = LoadingAlertDialog(this@LoginActivity)

        binding.apply {
            btnLogin.setOnClickListener {
                if(etUsername.text.isNotEmpty() && etPassword.text.isNotEmpty()){
                    loading.alertDialogLoading()
                    login(etUsername.text.toString().trim(), etPassword.text.toString().trim())
                }
                else if(etUsername.text.isEmpty()){
                    etUsername.error = "Tidak Boleh Kosong !"
                }
                else if(etPassword.text.isEmpty()){
                    etPassword.error = "Tidak Boleh Kosong !"
                }
            }

            tvDaftar.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegistrasiActivity::class.java))
//                finish()
            }
        }
    }

    fun login(valueUsername: String, valuePassword: String){
        database = FirebaseService().firebase().child("users")

        var id: String?
        var nama: String?
        var umur: String?
        var username: String?
        var password: String?
        var sebagai: String?
        var token: String?
        var cekSebagai: String? = null

        database.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usersArrayList = ArrayList()
                var cekLogin = false
                for(value in snapshot.children){
                    id = value.child("id").value.toString()
                    nama = value.child("nama").value.toString()
                    umur = value.child("umur").value.toString()
                    username = value.child("username").value.toString()
                    password = value.child("password").value.toString()
                    sebagai = value.child("sebagai").value.toString()
                    token = value.child("token").value.toString()

                    Log.d(TAG, "onDataChangeCoba: $id")
                    Log.d(TAG, "onDataChangeCoba: $nama")
                    Log.d(TAG, "onDataChangeCoba: $umur")
                    Log.d(TAG, "onDataChangeCoba: $username")
                    Log.d(TAG, "onDataChangeCoba: $password")
                    Log.d(TAG, "onDataChangeCoba: $sebagai")
                    Log.d(TAG, "onDataChangeCoba: $token")

//                    val currentUsers = value.getValue(UsersModel::class.java)
                    if(username!!.trim() == valueUsername.trim() && password!!.trim() == valuePassword.trim()){
                        cekSebagai = sebagai!!
                        sharedPref.setLogin(
                            id!!,
                            nama!!,
                            umur!!.toInt(),
                            username!!,
                            password!!,
                            sebagai!!,
                            token!!
                        )
                        cekLogin = true

                        FirebaseMessaging.getInstance().token.apply {
                            addOnSuccessListener { p0 ->
                                Log.d("messagingToken", "onSuccess: $p0")
                                var valueToken = p0
                                if(token!=p0){
                                    database.child(id!!).child("id").setValue(id)
                                    database.child(id!!).child("nama").setValue(nama)
                                    database.child(id!!).child("umur").setValue(umur)
                                    database.child(id!!).child("username").setValue(username)
                                    database.child(id!!).child("password").setValue(password)
                                    database.child(id!!).child("sebagai").setValue(sebagai)
                                    database.child(id!!).child("token").setValue(valueToken)
                                }
                            }
                            addOnCanceledListener {
                                Toast.makeText(this@LoginActivity, "Gagal Mendapatkan Token", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                }
                if(cekLogin){
                    Toast.makeText(this@LoginActivity, "$cekSebagai", Toast.LENGTH_SHORT).show()
                    if(cekSebagai == "admin"){
                        Toast.makeText(this@LoginActivity, "Berhasil Login", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@LoginActivity, AdminMainActivity::class.java))
                        loading.alertDialogCancel()
                    } else{
                        Toast.makeText(this@LoginActivity, "Berhasil Login", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        loading.alertDialogCancel()
                    }
                }
                else{
                    Toast.makeText(this@LoginActivity, "Username dan Password tidak ditemukan", Toast.LENGTH_LONG).show()
                    loading.alertDialogCancel()
                }

//                if(snapshot.child(username).exists()){
//                    if(snapshot.child(username).child("password").getValue(String::class.java).equals(password)){
//                        val id = snapshot.child(username).child("id").getValue(String::class.java)!!
//                        val nama = snapshot.child(username).child("nama").getValue(String::class.java)!!
//                        val umur = (snapshot.child(username).child("umur").getValue(Int::class.java))!!.toInt()
//                        val sebagai = snapshot.child(username).child("sebagai").getValue(String::class.java)!!
//                        sharedPref.setLogin(id, nama, umur, username, password, sebagai)
//                        loading.alertDialogCancel()
//                        Toast.makeText(this@LoginActivity, "Berhasil Login", Toast.LENGTH_SHORT).show()
//                        if(sebagai == "user"){
//                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                            finish()
//                        }
//                        else if(sebagai == "dokter"){
//                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                            finish()
//                        }
//                        else if(sebagai == "admin"){
//                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                            finish()
//                        }
//                    }
//                    else{
//                        Toast.makeText(this@LoginActivity, "Gagal Login", Toast.LENGTH_SHORT).show()
//                        loading.alertDialogCancel()
//                    }
//                }
//                else{
//                    Toast.makeText(this@LoginActivity, "Gagal Login", Toast.LENGTH_SHORT).show()
//                    loading.alertDialogCancel()
//                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Database Error "+error.message, Toast.LENGTH_SHORT).show()
                loading.alertDialogCancel()
            }

        })
    }


    var tapDuaKali = false
    override fun onBackPressed() {
        if (tapDuaKali){
            super.onBackPressed()
        }
        tapDuaKali = true
        Toast.makeText(this@LoginActivity, "Tekan Sekali Lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            tapDuaKali = false
        }, 2000)

    }
}