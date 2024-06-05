package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter.ListInformasiAdapter
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase.FirebaseConfig
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.InformationDataModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityInformasiHivAidsBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.user.MainActivity
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.KontrolNavigationDrawer
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.LoadingAlertDialog
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.SharedPreferencesLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class InformasiHivAidsActivity : Activity() {
    lateinit var binding: ActivityInformasiHivAidsBinding
    lateinit var sharedPref: SharedPreferencesLogin
    lateinit var loading: LoadingAlertDialog
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private var firebaseConfig = FirebaseConfig()
    private lateinit var adapter: ListInformasiAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformasiHivAidsBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(binding.root)

        sharedPref = SharedPreferencesLogin(this@InformasiHivAidsActivity)
        loading = LoadingAlertDialog(this@InformasiHivAidsActivity)
        setKontrolNavigationDrawer()
//        viewSelengkapnya()
        fetchData()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@InformasiHivAidsActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@InformasiHivAidsActivity)

        }
    }

    private fun fetchData(){
        var listInformation : ArrayList<InformationDataModel>
        firebaseConfig.fetchInformation().addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listInformation = arrayListOf<InformationDataModel>()
                for(value in snapshot.children){
                    var id: String? = ""
                    var urutan: String? = ""
                    var judul: String? = ""
                    var isi: String? = ""

                    id = value.child("id").value.toString()
                    urutan = value.child("urutan").value.toString()
                    judul = value.child("judul").value.toString()
                    isi = value.child("isi").value.toString()

                    listInformation.add(
                        InformationDataModel(
                            id, urutan, judul, isi
                        )
                    )
                }

                val sortedFromUrutan = listInformation.sortedWith(compareBy { it.urutan })
                val list = arrayListOf<InformationDataModel>()
                list.addAll(sortedFromUrutan)
                setAdapter(list)

//                Log.d("AdminInformasiHivAidsActivityTAG", "size: ${list.size}")
//                Log.d("AdminInformasiHivAidsActivityTAG", "size: ${list.size}")

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@InformasiHivAidsActivity, "Maaf, data gagal diambil", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setAdapter(list: ArrayList<InformationDataModel>) {
        adapter = ListInformasiAdapter(list)
        binding.apply {
            rvInformation.layoutManager = LinearLayoutManager(
                this@InformasiHivAidsActivity, LinearLayoutManager.VERTICAL, false
            )
            rvInformation.adapter = adapter
        }
    }

//    private fun viewSelengkapnya() {
//        binding.apply{
//            // Definisi HIV AIDS
//            clDefinisiHivAids.setOnClickListener {
//                if(isiDefinisiHivAids.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiDefinisiHivAids.visibility = View.VISIBLE
//                    arrowDefinisiHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiDefinisiHivAids.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiDefinisiHivAids.visibility = View.GONE
//                    arrowDefinisiHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // Sejarah HIV AIDS
//            clSejarahHivAids.setOnClickListener {
//                if(isiSejarahHivAids.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiSejarahHivAids.visibility = View.VISIBLE
//                    arrowSejarahHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiSejarahHivAids.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiSejarahHivAids.visibility = View.GONE
//                    arrowSejarahHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // Gejala HIV AIDS
//            clGejalaHivAids.setOnClickListener {
//                if(isiGejalaHivAids.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiGejalaHivAids.visibility = View.VISIBLE
//                    arrowGejalaHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiGejalaHivAids.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiGejalaHivAids.visibility = View.GONE
//                    arrowGejalaHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // Penularan HIV AIDS
//            clPenularanHivAids.setOnClickListener {
//                if(isiPenularanHivAids.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiPenularanHivAids.visibility = View.VISIBLE
//                    arrowPenularanHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiPenularanHivAids.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiPenularanHivAids.visibility = View.GONE
//                    arrowPenularanHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // Metode Tes HIV AIDS
//            clMetodeTesHivAids.setOnClickListener {
//                if(isiMetodeTesHivAids.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiMetodeTesHivAids.visibility = View.VISIBLE
//                    arrowMetodeTesHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiMetodeTesHivAids.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiMetodeTesHivAids.visibility = View.GONE
//                    arrowMetodeTesHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // Pencegahan HIV AIDS
//            clPencegahanHivAids.setOnClickListener {
//                if(isiPencegahanHivAids.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiPencegahanHivAids.visibility = View.VISIBLE
//                    arrowPencegahanHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiPencegahanHivAids.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiPencegahanHivAids.visibility = View.GONE
//                    arrowPencegahanHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // Pengobatan HIV AIDS
//            clPengobatanHivAids.setOnClickListener {
//                if(isiPengobatanHivAids.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiPengobatanHivAids.visibility = View.VISIBLE
//                    arrowPengobatanHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiPengobatanHivAids.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiPengobatanHivAids.visibility = View.GONE
//                    arrowPengobatanHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // Mencegah HIV AIKomplikasiDS
//            clMencegahHivAidsKomplikasi.setOnClickListener {
//                if(isiMencegahHivAidsKomplikasi.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiMencegahHivAidsKomplikasi.visibility = View.VISIBLE
//                    arrowMencegahHivAidsKomplikasi.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiMencegahHivAidsKomplikasi.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiMencegahHivAidsKomplikasi.visibility = View.GONE
//                    arrowMencegahHivAidsKomplikasi.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // UndangUndangKerahasiaanDataPasien HIV AIDS
//            clUndangUndangKerahasiaanDataPasienHivAids.setOnClickListener {
//                if(isiUndangUndangKerahasiaanDataPasienHivAids.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiUndangUndangKerahasiaanDataPasienHivAids.visibility = View.VISIBLE
//                    arrowUndangUndangKerahasiaanDataPasienHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiUndangUndangKerahasiaanDataPasienHivAids.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiUndangUndangKerahasiaanDataPasienHivAids.visibility = View.GONE
//                    arrowUndangUndangKerahasiaanDataPasienHivAids.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // Data Hiv Aids Di Indonesia HIV AIDS
//            clDataHivAidsDiParepare.setOnClickListener {
//                if(isiDataHivAidsDiParepare.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiDataHivAidsDiParepare.visibility = View.VISIBLE
//                    arrowDataHivAidsDiParepare.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(isiDataHivAidsDiParepare.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    isiDataHivAidsDiParepare.visibility = View.GONE
//                    arrowDataHivAidsDiParepare.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//            // Data Hiv Aids Di Asia Pasific HIV AIDS
//            clDataHivAidsDiIndonesia.setOnClickListener {
//                if(llDataHivAidsDiIndonesia.visibility == View.GONE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    llDataHivAidsDiIndonesia.visibility = View.VISIBLE
//                    arrowDataHivAidsDiIndonesia.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
//                }
//                else if(llDataHivAidsDiIndonesia.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(lyHivAids, AutoTransition())
//                    llDataHivAidsDiIndonesia.visibility = View.GONE
//                    arrowDataHivAidsDiIndonesia.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
//                }
//            }
//
//        }
//    }


    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this@InformasiHivAidsActivity, MainActivity::class.java))
        finish()
    }
}