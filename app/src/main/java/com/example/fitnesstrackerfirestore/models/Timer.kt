package com.example.fitnesstrackerfirestore.models

import com.google.firebase.firestore.PropertyName

data class Timer(
   val currentcycles: Int = 0,
   val iscompleted: Boolean = false,
   val timestamp: String = "",
   val totalcycles: Int = 0,
   val totaltime: Int =0
)
