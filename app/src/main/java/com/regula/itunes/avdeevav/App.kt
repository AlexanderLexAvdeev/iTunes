package com.regula.itunes.avdeevav

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

import io.realm.Realm

class App : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        fun getContext() = context
    }

    override fun onCreate() {

        super.onCreate()

        context = applicationContext
        Realm.init(context)
    }
}
