package com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.MessageModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageKonsultasiAdapter(val context: Context, val messageList: List<MessageModel>, var id:String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = "MessageKonsultasiAdapterTag"
    val ITEM_SENT = 1
    val ITEM_RECEIVED = 2

    var checkBoxMessage = false
    var nomorCheck = 0
    var valueDataIdMessage = arrayListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1){
            val view = LayoutInflater.from(context).inflate(R.layout.list_sent, parent, false)
            return SentViewHolder(view)
        }
        else{
            val view = LayoutInflater.from(context).inflate(R.layout.list_received, parent, false)
            return ReceivedViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if(holder.javaClass == SentViewHolder::class.java){
            //Tampilkan Chat Pengirim
            val viewHolder = holder as SentViewHolder
            viewHolder.sentMessage.text = messageList[position].message
            Log.d("MessageKonsultasi", "MessageKonsultasiAdapter 1: idSent: ${messageList[position].idSent} dan idReceived: ${messageList[position].idReceived}")
            var idMessage = messageList[position].idMessage


            viewHolder.sentMessage.setOnLongClickListener(object: OnLongClickListener{
                override fun onLongClick(v: View?): Boolean {

//                    Log.d(TAG, "onLongClick: Pembatas")
//
//                    var cek = false
//                    var no = 0
//                    for (value in valueDataIdMessage){
//                        if(value==idMessage){
//                            valueDataIdMessage.remove(value)
//                            valueDataIdMessage.removeAt(no)
//                            cek = true
//                        }
//                         no++
//                    }
//                    Log.d(TAG, "onLongClick: oho")
//                    if(!cek){
//                        valueDataIdMessage.add(idMessage.toString())
//                    }
//
//                    for (value in valueDataIdMessage){
//                        Log.d(TAG, "onLongClick: data: ${value}")
//                    }
//                    return true

                    var popupMenu = PopupMenu(context, v)
                    popupMenu.inflate(R.menu.popup_menu_hapus)
                    popupMenu.setOnMenuItemClickListener { v->
                        when(v.itemId){
                            R.id.hapus->
                                hapusPesan(messageList[position])
                        }

                        return@setOnMenuItemClickListener true
                    }
                    popupMenu.show()

                    return true
                }

            })

            if(checkBoxMessage){
                viewHolder.sentMessage.setOnClickListener {

                }
            }
        }
        else{
            //Tampilkan Chat Diterima
            val viewHolder = holder as ReceivedViewHolder
            viewHolder.receivedMessage.text = messageList[position].message
            Log.d("MessageKonsultasi2", "MessageKonsultasiAdapter 2: idSent: ${messageList[position].idSent} dan idReceived: ${messageList[position].idReceived}")

        }

//        Log.d("MessageKonsultasiAdapter", "onBindViewHolder: idSent: ${messageList[position].idSent} dan idReceived: ${messageList[position].idReceived}")
    }

    fun hapusPesan(messageList: MessageModel){
        var view = View.inflate(context, R.layout.alert_dialog_hapus, null)
        var btnHapus = view.findViewById<Button>(R.id.btnHapus)
        var btnBatalHapus = view.findViewById<Button>(R.id.btnBatalHapus)

        var alertDialog = AlertDialog.Builder(context)
        alertDialog.setView(view)
        var dialog = alertDialog.create()
        dialog.show()

        btnHapus.setOnClickListener {
            var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("chats").child("message")

            database.addListenerForSingleValueEvent(object:ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    database.child("${messageList.idMessage}").removeValue()
                    dialog.dismiss()
                    Toast.makeText(context, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Gagal Hapus", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }

            })
        }

        btnBatalHapus.setOnClickListener {
            dialog.dismiss()
        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        if(currentMessage.idSent == id){
            return ITEM_SENT
        }
        else{
            return ITEM_RECEIVED
        }
    }

    class SentViewHolder(v: View): RecyclerView.ViewHolder(v){
        val sentMessage = v.findViewById<TextView>(R.id.tvMessageSent)
    }
    class ReceivedViewHolder(v: View): RecyclerView.ViewHolder(v){
        val receivedMessage = v.findViewById<TextView>(R.id.tvMessageReceived)
    }

}