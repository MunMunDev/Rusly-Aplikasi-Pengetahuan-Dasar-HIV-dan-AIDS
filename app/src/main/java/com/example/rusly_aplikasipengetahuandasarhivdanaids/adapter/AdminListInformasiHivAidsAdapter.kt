package com.example.rusly_aplikasipengetahuandasarhivdanaids.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rusly_aplikasipengetahuandasarhivdanaids.R
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.InformationDataModel
import com.example.rusly_aplikasipengetahuandasarhivdanaids.databinding.ListAdminInformasiBinding
import com.example.rusly_aplikasipengetahuandasarhivdanaids.utils.OnClickItem

class AdminListInformasiHivAidsAdapter(
    private var listInformation: ArrayList<InformationDataModel>,
    private var onClick: OnClickItem.AdminInformation
): RecyclerView.Adapter<AdminListInformasiHivAidsAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListAdminInformasiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListAdminInformasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
                tvUrutan.text = "Urutan"
                tvJudul.text = "Judul Informasi "
                tvIsi.text = "Isi Informasi"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvUrutan.setBackgroundResource(R.drawable.bg_table_title)
                tvJudul.setBackgroundResource(R.drawable.bg_table_title)
                tvIsi.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvUrutan.setTextColor(Color.parseColor("#ffffff"))
                tvJudul.setTextColor(Color.parseColor("#ffffff"))
                tvIsi.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvUrutan.setTypeface(null, Typeface.BOLD)
                tvJudul.setTypeface(null, Typeface.BOLD)
                tvIsi.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val information = listInformation[(position-1)]
                val isiPakaiNewLine = information.isi!!.replace("\n", System.getProperty("line.separator")!!);

//                tvNo.text = "$position"
//                tvUrutan.text = information.urutan
//                tvJudul.text = information.judul
//                tvIsi.text = information.isi
//                tvSetting.text = ":::"

                tvNo.text = "$position"
                tvUrutan.text = information.urutan
                tvJudul.text = information.judul
                tvIsi.text = isiPakaiNewLine
                tvSetting.text = ":::"

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvUrutan.setBackgroundResource(R.drawable.bg_table)
                tvJudul.setBackgroundResource(R.drawable.bg_table)
                tvIsi.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvUrutan.setTextColor(Color.parseColor("#000000"))
                tvJudul.setTextColor(Color.parseColor("#000000"))
                tvIsi.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvUrutan.setTypeface(null, Typeface.NORMAL)
                tvJudul.setTypeface(null, Typeface.NORMAL)
                tvIsi.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvJudul.gravity = Gravity.CENTER_VERTICAL
                tvIsi.gravity = Gravity.CENTER_VERTICAL

                tvJudul.setOnClickListener{
                    onClick.clickItemJudul(information.judul!!, it)
                }
                tvIsi.setOnClickListener{
                    onClick.clickItemIsi(information.isi!!, it)
                }
                tvSetting.setOnClickListener {
                    onClick.clickItemSetting(information, it)
                }
            }
        }
    }
}