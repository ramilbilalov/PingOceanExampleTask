package com.test.testapplicationpingocean.settingsStorage

interface ISettingCheckerStorage {

    fun setChecked(newValue:Boolean)

    fun getChecked(): Boolean?

    fun saveChanges()


}