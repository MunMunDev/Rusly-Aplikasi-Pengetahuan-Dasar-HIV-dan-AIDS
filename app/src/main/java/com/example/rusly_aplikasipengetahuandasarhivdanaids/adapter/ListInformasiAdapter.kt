package com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.InformationDataModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ListInformasiBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.OnClickItem

class ListInformasiAdapter(
    private var listInformation: ArrayList<InformationDataModel>
): RecyclerView.Adapter<ListInformasiAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListInformasiBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListInformasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listInformation.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val information = listInformation[position]
        holder.binding.apply {
            tvHeader.text = information.judul
            tvBody.text = information.isi

            clHeader.setOnClickListener {

                if(tvBody.visibility == View.GONE){
                    TransitionManager.beginDelayedTransition(clHeader, AutoTransition())
                    tvBody.visibility = View.VISIBLE
                    arrowHeader.setBackgroundResource(R.drawable.baseline_arrow_drop_up_24)
                }
                else if(tvBody.visibility == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(clHeader, AutoTransition())
                    tvBody.visibility = View.GONE
                    arrowHeader.setBackgroundResource(R.drawable.baseline_arrow_drop_down_24)
                }
            }
        }
    }
}