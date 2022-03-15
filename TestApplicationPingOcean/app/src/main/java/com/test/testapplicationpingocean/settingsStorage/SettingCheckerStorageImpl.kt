package com.test.testapplicationpingocean.settingsStorage

import android.content.Context
import android.content.SharedPreferences
import android.util.Log


object SettingCheckerStorageImpl : ISettingCheckerStorage {
    private const val prefName = "SettingCheckerStoragePrefs"
    private const val prefFirstCheck = "${prefName}.firstCheck"

    private var sharedPreferences: SharedPreferences? = null

    private var secondActivity: Boolean = false


    fun init(context: Context) {

        if (sharedPreferences != null) {
            return
        }
        sharedPreferences =
            context.applicationContext.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        secondActivity = sharedPreferences?.getBoolean(prefFirstCheck, false) ?: true
    }

    override fun setChecked(newValue: Boolean) {
        secondActivity = newValue
    }

    override fun getChecked(): Boolean {
        return secondActivity
    }

    override fun saveChanges() {
        sharedPreferences?.edit()?.putBoolean(prefFirstCheck, secondActivity)?.apply()

    }


}