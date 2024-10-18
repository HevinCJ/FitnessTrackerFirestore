package com.example.fitnesstrackerfirestore.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstrackerfirestore.result.FirestoreResult
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StartTimerViewModel:ViewModel() {

    private val db:FirebaseFirestore by lazy {
        Firebase.firestore
    }

    private var _savedData = MutableStateFlow<FirestoreResult<Boolean>>(FirestoreResult.Loading())
    val savedData:Flow<FirestoreResult<Boolean>> get() = _savedData

     fun saveDataToFireStore(timer: Map<String, Any>) {
       viewModelScope.launch {
          try {
                  db.collection("timer").add(timer)
                      .addOnCompleteListener {
                          if (it.isSuccessful){
                              viewModelScope.launch {
                                  _savedData.emit(FirestoreResult.Success(true))
                                  Log.d("addedtimer",timer.toString())
                              }
                          }else{
                              viewModelScope.launch {
                                  _savedData.emit(FirestoreResult.Error(it.exception?.message))
                              }
                          }
                      }

          }catch (e:Exception){
              _savedData.emit(FirestoreResult.Error(e.message))
          }
       }
    }

}