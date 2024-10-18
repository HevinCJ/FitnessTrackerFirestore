package com.example.fitnesstrackerfirestore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TimerParameter(
    val time:Int,
    val noOfExercise:Int
):Parcelable