package com.msomu.androidkt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.msomu.androidkt.presentation.App
import com.msomu.androidkt.presentation.ui.theme.BaseMVVMAndroidKtTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaseMVVMAndroidKtTheme {
                App()
            }
        }
    }
}