package com.bilalov.aleftask

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class GetData : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun saveData() {
        
        Log.e("TTT_SAVE_DATA","saveData")
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun sendData(){
        //saveData(savedInstanceState)
        Log.e("TTT_SEND_DATA","sendData")
    }
}