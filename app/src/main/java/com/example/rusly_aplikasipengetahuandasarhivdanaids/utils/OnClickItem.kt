package com.example.rusly_aplikasipengetahuandasarhivdanaids.utils

import android.view.View
import com.example.rusly_aplikasipengetahuandasarhivdanaids.data.model.InformationDataModel

interface OnClickItem {
    interface AdminInformation{
        fun clickItemSetting(information: InformationDataModel, it: View)
        fun clickItemJudul(judul: String, it: View)
        fun clickItemIsi(isi: String, it: View)
    }

//    interface Information{
//        fun clickItem(information: InformationDataModel, it: View)
//    }

}