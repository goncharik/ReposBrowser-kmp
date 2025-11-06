package com.honcharenko.reposbrowser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.honcharenko.reposbrowser.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Initialize Koin for Android
        initKoin {
            androidLogger(Level.ERROR) // Only log errors in production
            androidContext(this@MainActivity)
        }

        setContent {
            AppNavigation()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    AppNavigation()
}