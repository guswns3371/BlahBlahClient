@file:JvmName("MyContext")
@file:JvmMultifileClass
package com.example.guswn.allthatlyrics.extension

import android.content.Context
import android.util.Log

fun Context.log(tag : String,msg :Any){
    Log.e(this::class.java.simpleName,"#$tag#\n$msg")
}