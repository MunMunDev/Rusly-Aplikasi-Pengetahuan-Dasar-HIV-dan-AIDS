package com.example.rusly_aplikasipengetahuandasarhivdanaids.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.*
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.admin.AdminInformasiHivAidsActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.admin.AdminMainActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.admin.AdminSemuaDokterActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.admin.AdminSemuaUserActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.user.MainActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.user.UpdateAkunActivity

class KontrolNavigationDrawer(var context: Context) {
    var sharedPreferences = SharedPreferencesLogin(context)
    fun cekSebagai(navigation: com.google.android.material.navigation.NavigationView){
        if(sharedPreferences.getSebagai() == "user"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.nav_menu_user)
        }
        else if(sharedPreferences.getSebagai() == "dokter"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.nav_menu_dokter)
        }
        else if(sharedPreferences.getSebagai() == "admin"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.nav_menu_admin)
        }
    }
    fun onClickItemNavigationDrawer(navigation: com.google.android.material.navigation.NavigationView, navigationLayout: DrawerLayout, igNavigation:ImageView, activity: Activity){
        navigation.setNavigationItemSelectedListener {
            if(sharedPreferences.getSebagai() == "user"){
                when(it.itemId){
                    R.id.userNavDrawerHome ->{
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerInformasi ->{
                        val intent = Intent(context, InformasiHivAidsActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerKonsultasi ->{
                        val intent = Intent(context, KonsultasiActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerTentangAplikasi ->{
                        val intent = Intent(context, TentangAplikasiActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerAkun ->{
                        val intent = Intent(context, UpdateAkunActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userBtnKeluar ->{
                        logout(activity)
                    }
                }
            }
            else if(sharedPreferences.getSebagai() == "dokter"){
                when(it.itemId){
                    R.id.dokterNavDrawerHome ->{
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.dokterNavDrawerInformasi ->{
                        val intent = Intent(context, InformasiHivAidsActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.dokterNavDrawerKonsultasiUser ->{
                        val intent = Intent(context, KonsultasiActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.dokterNavDrawerTentangAplikasi ->{
                        val intent = Intent(context, TentangAplikasiActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.dokterNavDrawerAkun ->{
                        val intent = Intent(context, UpdateAkunActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.btnDokterKeluar ->{
                        logout(activity)
                    }
                }
            }
            else if(sharedPreferences.getSebagai() == "admin"){
                when(it.itemId){
                    R.id.adminNavDrawerHome ->{
                        val intent = Intent(context, AdminMainActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerInformasi ->{
                        val intent = Intent(Intent(context, AdminInformasiHivAidsActivity::class.java))
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerDokter ->{
                        val intent = Intent(context, AdminSemuaDokterActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerUser ->{
                        val intent = Intent(Intent(context, AdminSemuaUserActivity::class.java))
                        context.startActivity(intent)
                        activity.finish()
                    }
//                    R.id.adminNavDrawerSettings ->{
//                        val intent = Intent(context, MainActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
                    R.id.btnAdminKeluar ->{
                        logout(activity)
                    }
                }
            }
            navigationLayout.closeDrawer(GravityCompat.START)
            true
        }
        // garis 3 navigasi
        igNavigation.setOnClickListener {
            navigationLayout.openDrawer(GravityCompat.START)
        }
    }

    fun logout(activity: Activity){
        val viewAlertDialog = View.inflate(context, R.layout.alert_dialog_logout, null)
        val btnLogout = viewAlertDialog.findViewById<Button>(R.id.btnLogout)
        val btnBatalLogout = viewAlertDialog.findViewById<Button>(R.id.btnBatalLogout)

        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setView(viewAlertDialog)
        val dialog = alertDialog.create()
        dialog.show()
        btnLogout.setOnClickListener {
//            sharedPreferences.setLogin(0, 0, "", "", "")
            sharedPreferences.setLogin( "","", 0, "", "", "", "")
            context.startActivity(Intent(context, LoginActivity::class.java))
            activity.finish()
        }
        btnBatalLogout.setOnClickListener {
            dialog.dismiss()
        }
    }
}