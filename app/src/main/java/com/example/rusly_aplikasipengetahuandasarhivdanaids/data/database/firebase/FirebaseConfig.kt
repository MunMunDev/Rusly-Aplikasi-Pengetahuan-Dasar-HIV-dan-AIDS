package com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.firebase

import android.annotation.SuppressLint
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.InformationDataModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.KonselorModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class FirebaseConfig {
    private lateinit var database: DatabaseReference

    private fun setDatabase(): DatabaseReference{
        return FirebaseService().firebase()
    }

    fun fetchInformation(): DatabaseReference{
        database = setDatabase().child("information")
        return database
    }
    fun getInformation(): ArrayList<InformationDataModel>{
        val databaseInformation = fetchInformation()
        val informationArrayList = arrayListOf<InformationDataModel>()
        databaseInformation.addValueEventListener(object: ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                for(value in snapshot.children){
                    var id: String? = ""
                    var urutan: String? = ""
                    var judul: String? = ""
                    var isi: String? = ""

                    id = value.child("id").value.toString()
                    urutan = value.child("urutan").value.toString()
                    judul = value.child("judul").value.toString()
                    isi = value.child("isi").value.toString()

                    informationArrayList.add(
                        InformationDataModel(
                            id, urutan, judul, isi
                        )
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        return informationArrayList
    }

    private fun fetchKonselor(): DatabaseReference{
        database = setDatabase().child("konselor")
        return database
    }

    fun getKonselor(): ArrayList<KonselorModel>{
        val databaseKonselor = fetchKonselor()
        val arrayKonselor = arrayListOf<KonselorModel>()
        databaseKonselor.addValueEventListener(object: ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                for(value in snapshot.children){
                    val nama = value.child("nama").value.toString()
                    val alamat = value.child("alamat").value.toString()
                    val pekerjaan = value.child("pekerjaan").value.toString()
                    val ttl = value.child("ttl").value.toString()

                    arrayKonselor.add(
                        KonselorModel(
                            nama, alamat, pekerjaan, ttl
                        )
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        return arrayKonselor
    }
}