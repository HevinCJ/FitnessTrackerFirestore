package com.example.fitnesstrackerfirestore.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstrackerfirestore.models.Timer
import com.example.fitnesstrackerfirestore.result.FirestoreResult
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HomeViewModel:ViewModel() {

    private var _fetcheddata = MutableStateFlow<FirestoreResult<List<Timer>>>(FirestoreResult.Loading())
    val fetcheddata: Flow<FirestoreResult<List<Timer>>> get() = _fetcheddata

    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    fun fetchDataFromFireStore(){
      viewModelScope.launch {
        try {
            val querySnapshot = db.collection("timer").get().await()

            if (querySnapshot.isEmpty) {
                _fetcheddata.emit(FirestoreResult.Error("Data Found Empty"))
                return@launch
            }

                Log.d("timerlistviewmodel",querySnapshot.documents.toString())

            val timerlist = mutableListOf<Timer>()
            for (document in querySnapshot.documents){
               document.toObject(Timer::class.java)?.let {data->
                   timerlist.add(data)
               }
            }
            Log.d("timerlist",timerlist.toString())

            _fetcheddata.emit(FirestoreResult.Success(timerlist))

        }catch (e:Exception){
            _fetcheddata.emit(FirestoreResult.Error(e.message))
        }

      }
    }

}