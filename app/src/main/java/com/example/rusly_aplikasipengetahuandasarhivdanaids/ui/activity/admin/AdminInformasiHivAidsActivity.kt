package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.admin

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter.AdminListInformasiHivAidsAdapter
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase.FirebaseConfig
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.InformationDataModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityAdminInformasiHivAidsBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.AlertDialogAdminInformasiBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.AlertDialogKeteranganBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.AlertDialogKonfirmasiBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.KataAcak
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.KontrolNavigationDrawer
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.OnClickItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList

class AdminInformasiHivAidsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminInformasiHivAidsBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private var firebaseConfig = FirebaseConfig()
    private lateinit var adapter: AdminListInformasiHivAidsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminInformasiHivAidsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        fetchData()
    }

    private fun setKontrolNavigationDrawer() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminInformasiHivAidsActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminInformasiHivAidsActivity)
        }
    }

    private fun setButton() {
        binding.apply {
            btnTambahData.setOnClickListener{
                showDialogTambahInformasi()
            }
        }
    }

    private fun showDialogTambahInformasi() {
        val view = AlertDialogAdminInformasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminInformasiHivAidsActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            btnSimpan.setOnClickListener{
                var check = true
                if(etUrutan.text.toString().isEmpty()){
                    etUrutan.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etJudulInformasi.text.toString().isEmpty()){
                    etJudulInformasi.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etIsiInformasi.text.toString().isEmpty()){
                    etIsiInformasi.error = "Tidak Boleh Kosong"
                    check = false
                }

                if(check){
                    Toast.makeText(this@AdminInformasiHivAidsActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                    val urutan = etUrutan.text.toString()
                    val judul = etJudulInformasi.text.toString()
                    val isi = etIsiInformasi.text.toString()

                    postTambahInformasi(urutan, judul, isi)
                    dialogInputan.dismiss()

//                    Log.d("AdminInformasiHivAidsActivityTAG", "urutan: $urutan")
//                    Log.d("AdminInformasiHivAidsActivityTAG", "judul: $judul")
//                    Log.d("AdminInformasiHivAidsActivityTAG", "isi: $isi")
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahInformasi(urutan: String, judul: String, isi: String) {
        val id = KataAcak().getHurufDanAngka(10)
        val kataAcak = "information-$id"
        firebaseConfig.fetchInformation().child(kataAcak).child("id").setValue(id)
        firebaseConfig.fetchInformation().child(kataAcak).child("urutan").setValue(urutan)
        firebaseConfig.fetchInformation().child(kataAcak).child("judul").setValue(judul)
        firebaseConfig.fetchInformation().child(kataAcak).child("isi").setValue(isi)
    }

    private fun fetchData() {
//        val arrayInformation = firebaseConfig.getInformation()
//        val sortedFromUrutan = arrayInformation.sortedWith(compareBy { it.urutan })
        var listInformation : ArrayList<InformationDataModel>
//        listInformation.addAll(sortedFromUrutan)
//        setAdapter(listInformation)

        firebaseConfig.fetchInformation().addValueEventListener(object: ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
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
                Toast.makeText(this@AdminInformasiHivAidsActivity, "Maaf, data gagal diambil", Toast.LENGTH_SHORT).show()
            }

        })

//        Log.d("AdminInformasiHivAidsActivityTAG", "size: ${listInformation.size}")
//        Log.d("AdminInformasiHivAidsActivityTAG", "size: ${listInformation.size}")
    }

    private fun setAdapter(list: ArrayList<InformationDataModel>) {
        adapter = AdminListInformasiHivAidsAdapter(list, object: OnClickItem.AdminInformation{
            override fun clickItemSetting(information: InformationDataModel, it: View) {
                val popupMenu = PopupMenu(this@AdminInformasiHivAidsActivity, it)
                popupMenu.inflate(R.menu.popup_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                showDialogEditInformasi(information)
                                return true
                            }
                            R.id.hapus -> {
                                showDialogHapusInformasi(information)
                                return true
                            }
                        }
                        return true
                    }

                })
                popupMenu.show()
            }

            override fun clickItemJudul(judul: String, it: View) {
                setShowJudul(judul)
            }

            override fun clickItemIsi(isi: String, it: View) {
                setShowIsi(isi)
            }

        })
        binding.apply {
            rvInformasi.layoutManager = LinearLayoutManager(
                this@AdminInformasiHivAidsActivity, LinearLayoutManager.VERTICAL, false
            )
            rvInformasi.adapter = adapter
        }
    }

    private fun showDialogEditInformasi(information: InformationDataModel) {
        val view = AlertDialogAdminInformasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminInformasiHivAidsActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            etUrutan.setText(information.urutan)
            etJudulInformasi.setText(information.judul)
            etIsiInformasi.setText(information.isi)

            btnSimpan.setOnClickListener{
                var check = true
                if(etUrutan.text.isEmpty()){
                    etUrutan.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etJudulInformasi.text.isEmpty()){
                    etJudulInformasi.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etIsiInformasi.text.isEmpty()){
                    etIsiInformasi.error = "Tidak Boleh Kosong"
                    check = false
                }

                if(check){
                    val id = information.id!!
                    val valeuUrutanLama = information.urutan!!
                    val valeuUrutan = etUrutan.text.toString()
                    val valeuJudul = etJudulInformasi.text.toString()
                    val valeuIsi = etIsiInformasi.text.toString()

                    postEditInformasi(id, valeuUrutanLama, valeuUrutan, valeuJudul, valeuIsi)

                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }

    }

    private fun postEditInformasi(id: String, urutanLama: String, urutan: String, judul: String, isi: String) {
        firebaseConfig.fetchInformation().child("information-$id").child("id").setValue(id)
        firebaseConfig.fetchInformation().child("information-$id").child("urutan").setValue(urutan)
        firebaseConfig.fetchInformation().child("information-$id").child("judul").setValue(judul)
        firebaseConfig.fetchInformation().child("information-$id").child("isi").setValue(isi)
    }

    private fun showDialogHapusInformasi(information: InformationDataModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminInformasiHivAidsActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Hapus Data Ini?"
            tvBodyKonfirmasi.text = "Judul: ${information.judul} \nSetelah menghapus Informasi ini, data tidak dapat dikembalikan. "

            btnKonfirmasi.setOnClickListener{
                binding.apply {
                    val id = information.id!!
                    postHapusInformasi(id)
                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postHapusInformasi(id: String){
        firebaseConfig.fetchInformation().child("information-$id").removeValue().addOnSuccessListener {
            Toast.makeText(this@AdminInformasiHivAidsActivity, "Berhasil", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setShowJudul(judul: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminInformasiHivAidsActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = "Judul Informasi HIV AIDS"
            tvBodyKeterangan.text = judul
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun setShowIsi(isi: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminInformasiHivAidsActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = "Keterangan Informasi HIV AIDS"
            val isiPakaiNewLine = isi.replace("\r\n", System.getProperty("line.separator"));
            tvBodyKeterangan.text = isiPakaiNewLine
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }


}