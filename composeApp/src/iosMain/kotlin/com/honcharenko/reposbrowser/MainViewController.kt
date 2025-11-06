package com.honcharenko.reposbrowser

import androidx.compose.ui.window.ComposeUIViewController
import com.honcharenko.reposbrowser.di.initKoin

/**
 * Initializes Koin for iOS.
 * Should be called once from Swift/UIKit before using the MainViewController.
 */
fun initKoinIos() {
    initKoin()
}

/**
 * Main view controller for iOS.
 * Creates a Compose UIViewController with the App composable.
 */
fun MainViewController() = ComposeUIViewController { App() }