package com.kimoterru.jinttest

import android.app.Application
import com.kimoterru.jinttest.data.local.BinDatabase

class BinApplication: Application() {

    companion object {
        lateinit var instance: BinApplication
    }

    val db by lazy {
        BinDatabase.DataBaseHelper.getDataBase(this)
    }

    init {
        instance = this
    }
}