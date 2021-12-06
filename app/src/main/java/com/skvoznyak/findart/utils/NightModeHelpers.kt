package com.skvoznyak.findart.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

object SharedPref {
    private var mySharePref: SharedPreferences? = null

    fun setContext (context: Context) {
        mySharePref = context.getSharedPreferences("filename", Context.MODE_PRIVATE)
    }

    fun setNightModeState(state: Boolean) {
        if (mySharePref == null) return
        val editor : SharedPreferences.Editor = mySharePref!!.edit()
        editor.putBoolean("NightMode", state)
        editor.commit()
        if (state) {
            Log.d("ivan", "night SET !!!")
            setNightMode()
        } else {
            Log.d("ivan", "light SET !!!")
            setLightMode()
        }
    }

    fun loadNightModeState() : Boolean {
        if (mySharePref == null) return false
        val state = mySharePref!!.getBoolean("NightMode",false)
        return state
    }

    private fun setNightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun setLightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}

fun isNightMode(uiMode : Int) : Boolean {
    when (uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO -> {
            return false
        }
        Configuration.UI_MODE_NIGHT_YES -> {
            return true
        }
    }
    return false
}