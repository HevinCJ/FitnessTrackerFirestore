package com.example.fitnesstrackerfirestore.adapter

import android.graphics.Color
import android.widget.TextView
import androidx.databinding.BindingAdapter

class BindingAdapter {


    companion object{



        @BindingAdapter("android:setColortoText")
        @JvmStatic
        fun setColortoText(view:TextView,iscompleted:Boolean){
            when(iscompleted){
                true->{
                    view.setTextColor(Color.GREEN)
                    view.setText("complete")
                }

                false -> {
                    view.setTextColor(Color.RED)
                    view.setText("incomplete")
                }
            }
        }

    }
}