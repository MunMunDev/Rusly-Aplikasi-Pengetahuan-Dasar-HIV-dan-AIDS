package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase.FirebaseConfig
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityAdminKonselorBinding

class AdminKonselorActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAdminKonselorBinding
    private var firebaseConfig = FirebaseConfig()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminKonselorBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}