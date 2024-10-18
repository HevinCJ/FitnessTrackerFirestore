package com.example.fitnesstrackerfirestore.result

sealed class FirestoreResult<T>(val data:T?=null,val message:String?=null) {
    class Loading<T>(): FirestoreResult<T>()
    class Success<T>(data: T): FirestoreResult<T>(data)
    class Error<T>(message: String?): FirestoreResult<T>(data = null,message)
}