package com.manuelblanco.spacedemo.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.manuelblanco.spacedemo.network.ConnectionLiveData

/**
 * Created by Manuel Blanco Murillo on 3/20/21.
 */
open class BaseActivity : AppCompatActivity() {

    protected lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionLiveData = ConnectionLiveData(this)
    }
}