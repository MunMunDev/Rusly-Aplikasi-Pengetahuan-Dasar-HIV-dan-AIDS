package com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.PertanyaanOtomatisModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ListAdminInformasiBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ListAdminPertanyaanOtomatisBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.OnClickItem

class AdminListInformasiPertanyaanOtomatisAdapter(
    private var listInformation: ArrayList<PertanyaanOtomatisModel>,
    private var onClick: OnClickItem.AdminPertanyaanOtomatis
): RecyclerView.Adapter<AdminListInformasiPertanyaanOtomatisAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListAdminPertanyaanOtomatisBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListAdminPertanyaanOtomatisBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(listInformation.size>0){
            listInformation.size+1
        } else{
            0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if(position==0){
                tvNo.text = "NO"
                tvPertanyaan.text = "Pertanyaan"
                tvJawaban.text = "Jawaban"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvPertanyaan.setBackgroundResource(R.drawable.bg_table_title)
                tvJawaban.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvPertanyaan.setTextColor(Color.parseColor("#ffffff"))
                tvJawaban.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvPertanyaan.setTypeface(null, Typeface.BOLD)
                tvJawaban.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val list = listInformation[(position-1)]
                val isiPakaiNewLine = list.jawaban!!.replace("\n", System.getProperty("line.separator")!!);

//                tvNo.text = "$position"
//                tvPertanyaan.text = list.judul
//                tvJawaban.text = list.isi
//                tvSetting.text = ":::"

                tvNo.text = "$position"
                tvPertanyaan.text = list.pertanyaan
                tvJawaban.text = isiPakaiNewLine
                tvSetting.text = ":::"

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvPertanyaan.setBackgroundResource(R.drawable.bg_table)
                tvJawaban.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvPertanyaan.setTextColor(Color.parseColor("#000000"))
                tvJawaban.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvPertanyaan.setTypeface(null, Typeface.NORMAL)
                tvJawaban.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvPertanyaan.gravity = Gravity.CENTER_VERTICAL
                tvJawaban.gravity = Gravity.CENTER_VERTICAL

                tvPertanyaan.setOnClickListener{
                    onClick.clickItemPertanyaan(list.pertanyaan!!, it)
                }
                tvJawaban.setOnClickListener{
                    onClick.clickItemJawaban(list.jawaban!!, it)
                }
                tvSetting.setOnClickListener {
                    onClick.clickItemSetting(list, it)
                }
            }
        }
    }
}