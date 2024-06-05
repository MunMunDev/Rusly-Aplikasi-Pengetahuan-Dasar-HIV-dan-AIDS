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
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.ChatOtomatisModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.MessageModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ListChatOtomatisAdapter(
    val context: Context,
    val messageList: List<ChatOtomatisModel>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = "MessageKonsultasiAdapterTag"
    val ITEM_SENT = 1
    val ITEM_RECEIVED = 2

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
            var idMessage = messageList[position].idPertanyaanOtomatis
            Log.d("MessageKonsultasi", "MessageKonsultasiAdapter 1: idSent: ${messageList[position].idSent} dan idReceived: ${messageList[position].message}")
        }
        else{
            //Tampilkan Chat Diterima
            val viewHolder = holder as ReceivedViewHolder
            viewHolder.receivedMessage.text = messageList[position].message
            Log.d("MessageKonsultasi2", "MessageKonsultasiAdapter 2: idSent: ${messageList[position].idSent} dan idReceived: ${messageList[position].message}")
        }

//        Log.d("MessageKonsultasiAdapter", "onBindViewHolder: idSent: ${messageList[position].idSent} dan idReceived: ${messageList[position].idReceived}")
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        if(currentMessage.idJenis == "1"){
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