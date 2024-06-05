package com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase.FirebaseService
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.PertanyaanOtomatisModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ListPertanyaanOtomatisBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.LoadingAlertDialog
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.TanggalDanWaktu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ListPertanyaanOtomatisAdapter(
    val context: Context,
    val listPertanyaan : List<PertanyaanOtomatisModel>,
    val idSent: String
): RecyclerView.Adapter<ListPertanyaanOtomatisAdapter.ViewHolder>() {
    val loading = LoadingAlertDialog(context)
    class ViewHolder(val binding: ListPertanyaanOtomatisBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListPertanyaanOtomatisBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(listPertanyaan.isNotEmpty()){
            listPertanyaan.size
        } else{
            0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pertanyaan = listPertanyaan[position]
        holder.binding.apply {
            tvPertanyaan.text = pertanyaan.pertanyaan

            tvPertanyaan.setOnClickListener {
                loading.alertDialogLoading()
                postPertanyaan(idSent, pertanyaan.pertanyaan!!, pertanyaan.jawaban!!)
            }
        }
    }

    private fun postPertanyaan(idSent:String, pertanyaan: String, jawaban: String) {
        var tanggalDanWaktu = TanggalDanWaktu()
        var id = tanggalDanWaktu.hurufAcak(idSent)

        val database: DatabaseReference = FirebaseService().firebase().child("chats").child("pertanyaanOtomatis")
        database.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                database.child(id).child("idPertanyaanOtomatis").setValue(id)
                database.child(id).child("idSent").setValue(idSent)
                database.child(id).child("pertanyaan").setValue(pertanyaan)
                database.child(id).child("jawaban").setValue(jawaban)

                loading.alertDialogCancel()
            }

            override fun onCancelled(error: DatabaseError) {
                loading.alertDialogCancel()
                Toast.makeText(context, "Gagal Pertanyaan", Toast.LENGTH_SHORT).show()
            }

        })
    }
}