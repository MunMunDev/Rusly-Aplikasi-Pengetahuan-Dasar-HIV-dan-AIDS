package com.example.rusly_aplikasipengetahuandasarhivdanaids.ui.activity.admin

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter.AdminListInformasiPertanyaanOtomatisAdapter
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase.FirebaseConfig
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.PertanyaanOtomatisModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ActivityAdminPertanyaanOtomatisBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.AlertDialogAdminInformasiBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.AlertDialogAdminPertanyaanOtomatisBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.AlertDialogKeteranganBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.AlertDialogKonfirmasiBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.KataAcak
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.KontrolNavigationDrawer
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.OnClickItem
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.TanggalDanWaktu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AdminPertanyaanOtomatisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminPertanyaanOtomatisBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private var firebaseConfig = FirebaseConfig()
    private lateinit var adapter: AdminListInformasiPertanyaanOtomatisAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPertanyaanOtomatisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        fetchData()
    }

    private fun setKontrolNavigationDrawer() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminPertanyaanOtomatisActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminPertanyaanOtomatisActivity)
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
        val view = AlertDialogAdminPertanyaanOtomatisBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPertanyaanOtomatisActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            btnSimpan.setOnClickListener{
                var check = true
                if(etPertanyaan.text.toString().isEmpty()){
                    etPertanyaan.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etJawaban.text.toString().isEmpty()){
                    etJawaban.error = "Tidak Boleh Kosong"
                    check = false
                }

                if(check){
                    Toast.makeText(this@AdminPertanyaanOtomatisActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                    val judul = etPertanyaan.text.toString()
                    val isi = etJawaban.text.toString()

                    postTambahInformasi(judul, isi)
                    dialogInputan.dismiss()

//                    Lothis@AdminPertanyaanOtomatisActivityTAG", "urutan: $urutan")
//                    Lothis@AdminPertanyaanOtomatisActivityTAG", "judul: $judul")
//                    Lothis@AdminPertanyaanOtomatisActivityTAG", "isi: $isi")
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahInformasi(pertanyaan: String, jawaban: String) {
        val id = KataAcak().getHurufDanAngka(10)
        val idPertanyaan = TanggalDanWaktu().hurufAcak(id)

        firebaseConfig.fetchPertanyaanOtomatis().child(idPertanyaan).child("idPertanyaan").setValue(idPertanyaan)
        firebaseConfig.fetchPertanyaanOtomatis().child(idPertanyaan).child("pertanyaan").setValue(pertanyaan)
        firebaseConfig.fetchPertanyaanOtomatis().child(idPertanyaan).child("jawaban").setValue(jawaban)
    }

    private fun fetchData() {
//        val arrayInformation = firebaseConfig.getInformation()
//        val sortedFromUrutan = arrayInformation.sortedWith(compareBy { it.urutan })
        var listInformation : ArrayList<PertanyaanOtomatisModel>
//        listInformation.addAll(sortedFromUrutan)
//        setAdapter(listInformation)

        firebaseConfig.fetchPertanyaanOtomatis().addValueEventListener(object: ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                listInformation = arrayListOf<PertanyaanOtomatisModel>()
                for(value in snapshot.children){
                    var idPertanyaan: String? = ""
                    var pertanyaan: String? = ""
                    var jawaban: String? = ""

                    idPertanyaan = value.child("idPertanyaan").value.toString()
                    pertanyaan = value.child("pertanyaan").value.toString()
                    jawaban = value.child("jawaban").value.toString()

                    listInformation.add(
                        PertanyaanOtomatisModel(
                            idPertanyaan, pertanyaan, jawaban
                        )
                    )
                }

                setAdapter(listInformation)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminPertanyaanOtomatisActivity, "Maaf, data gagal diambil", Toast.LENGTH_SHORT).show()
            }

        })

//        Lothis@AdminPertanyaanOtomatisActivityTAG", "size: ${listInformation.size}")
//        Lothis@AdminPertanyaanOtomatisActivityTAG", "size: ${listInformation.size}")
    }

    private fun setAdapter(list: ArrayList<PertanyaanOtomatisModel>) {
        adapter = AdminListInformasiPertanyaanOtomatisAdapter(list, object: OnClickItem.AdminPertanyaanOtomatis{
            override fun clickItemSetting(list: PertanyaanOtomatisModel, it: View) {
                val popupMenu = PopupMenu(this@AdminPertanyaanOtomatisActivity, it)
                popupMenu.inflate(R.menu.popup_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                showDialogEditInformasi(list)
                                return true
                            }
                            R.id.hapus -> {
                                showDialogHapusInformasi(list)
                                return true
                            }
                        }
                        return true
                    }

                })
                popupMenu.show()
            }

            override fun clickItemPertanyaan(pertanyaan: String, it: View) {
                setShowJudul(pertanyaan)
            }

            override fun clickItemJawaban(jawaban: String, it: View) {
                setShowIsi(jawaban)
            }

        })
        binding.apply {
            rvInformasi.layoutManager = LinearLayoutManager(
                this@AdminPertanyaanOtomatisActivity, LinearLayoutManager.VERTICAL, false
            )
            rvInformasi.adapter = adapter
        }
    }

    private fun showDialogEditInformasi(information: PertanyaanOtomatisModel) {
        val view = AlertDialogAdminPertanyaanOtomatisBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPertanyaanOtomatisActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            etPertanyaan.setText(information.pertanyaan)
            etJawaban.setText(information.jawaban)

            btnSimpan.setOnClickListener{
                var check = true
                if(etPertanyaan.text.isEmpty()){
                    etPertanyaan.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etJawaban.text.isEmpty()){
                    etJawaban.error = "Tidak Boleh Kosong"
                    check = false
                }

                if(check){
                    val id = information.idPertanyaan!!
                    val valuePertanyaan = etPertanyaan.text.toString()
                    val valueJawaban = etJawaban.text.toString()

                    postEditInformasi(id, valuePertanyaan, valueJawaban)

                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }

    }

    private fun postEditInformasi(idPertanyaan: String, pertanyaan: String, jawawban: String) {
        firebaseConfig.fetchPertanyaanOtomatis().child("$idPertanyaan").child("idPertanyaan").setValue(idPertanyaan)
        firebaseConfig.fetchPertanyaanOtomatis().child("$idPertanyaan").child("pertanyaan").setValue(pertanyaan)
        firebaseConfig.fetchPertanyaanOtomatis().child("$idPertanyaan").child("jawaban").setValue(jawawban)
    }

    private fun showDialogHapusInformasi(information: PertanyaanOtomatisModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPertanyaanOtomatisActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Hapus Data Ini?"
            tvBodyKonfirmasi.text = "Setelah menghapus Informasi ini, data tidak dapat dikembalikan. "

            btnKonfirmasi.setOnClickListener{
                binding.apply {
                    val id = information.idPertanyaan!!
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
        firebaseConfig.fetchPertanyaanOtomatis().child("$id").removeValue().addOnSuccessListener {
            Toast.makeText(this@AdminPertanyaanOtomatisActivity, "Berhasil", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setShowJudul(judul: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPertanyaanOtomatisActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = "Keterangan Pertanyaan"
            tvBodyKeterangan.text = judul
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun setShowIsi(isi: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPertanyaanOtomatisActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = "Keterangan Jawaban"
            val isiPakaiNewLine = isi.replace("\r\n", System.getProperty("line.separator"));
            tvBodyKeterangan.text = isiPakaiNewLine
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }
}