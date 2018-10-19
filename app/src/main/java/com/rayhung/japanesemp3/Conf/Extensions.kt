package com.rayhung.japanesemp3.Conf

import android.app.Activity
import android.support.v4.app.Fragment
import android.util.Log
import com.rayhung.japanesemp3.BuildConfig

fun Activity.LogD(text: String){
    if (BuildConfig.DEBUG) {
        Log.d(this::class.java.simpleName, text)
    }
}

fun Fragment.LogD(text: String){
    if (BuildConfig.DEBUG) {
        Log.d(this::class.java.simpleName, text)
    }
}