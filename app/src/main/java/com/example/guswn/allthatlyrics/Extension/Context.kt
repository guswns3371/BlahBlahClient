@file:JvmName("Extension")
package com.example.guswn.allthatlyrics.Extension

import android.content.Context
import android.util.Log

fun Context.log(msg : Any){
    Log.e(this::class.java.simpleName,"$msg")
}

