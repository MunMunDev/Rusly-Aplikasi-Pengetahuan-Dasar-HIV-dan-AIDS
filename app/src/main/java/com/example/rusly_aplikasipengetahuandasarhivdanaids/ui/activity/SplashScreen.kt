package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase.FirebaseService
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.admin.AdminMainActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.admin.AdminSemuaUserActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.user.MainActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.SharedPreferencesLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging

class SplashScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash_screen)

        val sharedPref = SharedPreferencesLogin(this@SplashScreen)
        var database: DatabaseReference
        database = FirebaseService().firebase().child("users")

        var cekToken = ""
        var cekLogin = false

        FirebaseMessaging.getInstance().token.apply {
            addOnSuccessListener { p0 ->
                Log.d("messagingToken", "onSuccess: $p0")

                var valueId = sharedPref.getId()

                database.child(valueId).addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.child("id").value != null){
                            val id = snapshot.child("id").value.toString()
                            val nama = snapshot.child("nama").value.toString()
                            val umur = snapshot.child("umur").value.toString()
                            val username = snapshot.child("username").value.toString()
                            val password = snapshot.child("password").value.toString()
                            val sebagai = snapshot.child("sebagai").value.toString()
                            val token = snapshot.child("token").value.toString()

                            database.child(valueId).child("id").setValue(id)
                            database.child(valueId).child("nama").setValue(nama)
                            database.child(valueId).child("umur").setValue(umur)
                            database.child(valueId).child("username").setValue(username)
                            database.child(valueId).child("password").setValue(password)
                            database.child(valueId).child("sebagai").setValue(sebagai)
                            database.child(valueId).child("token").setValue(p0)

                            sharedPref.setLogin(
                                id, nama, umur.trim().toInt(),
                                username, password, sebagai, token
                            )

                            cekLogin = true
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        cekLogin = false
                    }
                })
            }
            addOnCanceledListener {
//                Toast.makeText(this@SplashScreen, "Gagal Mendapatkan Token", Toast.LENGTH_SHORT).show()
                cekLogin = false
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if(cekLogin){
                if(sharedPref.getSebagai().isNotEmpty()){
                    if(sharedPref.getSebagai() == "user"){
                        startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                        finish()
                    }
                    else if(sharedPref.getSebagai() == "dokter"){
                        startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                        finish()
                    }
                    else if(sharedPref.getSebagai() == "admin"){
                        startActivity(Intent(this@SplashScreen, AdminMainActivity::class.java))
                        finish()
                    }

//                    if(cekToken != sharedPref.getToken()){
//                        database = FirebaseService().firebase().child("users")
//
//                        database.child(sharedPref.getId()).child("id").setValue(sharedPref.getId())
//                        database.child(sharedPref.getId()).child("nama").setValue(sharedPref.getNama())
//                        database.child(sharedPref.getId()).child("umur").setValue(sharedPref.getUmur())
//                        database.child(sharedPref.getId()).child("username").setValue(sharedPref.getUsername())
//                        database.child(sharedPref.getId()).child("password").setValue(sharedPref.getPassword())
//                        database.child(sharedPref.getId()).child("sebagai").setValue(sharedPref.getSebagai())
//                        database.child(sharedPref.getId()).child("token").setValue(cekToken)
//
//                        sharedPref.setLogin(sharedPref.getId(), sharedPref.getNama(), sharedPref.getUmur(),
//                            sharedPref.getUsername(), sharedPref.getPassword(), sharedPref.getSebagai(), cekToken)
//                    }
                }
                else{
                    sharedPref.setLogin("", "", 0, "", "", "", "")
                    startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
                    finish()
                }
            }
            else{
                sharedPref.setLogin("", "", 0, "", "", "", "")
                startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
                finish()
            }

        }, 6000) // 6 detik
    }
}