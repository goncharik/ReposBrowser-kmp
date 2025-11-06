package com.honcharenko.reposbrowser.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.honcharenko.reposbrowser.data.local.ReposBrowserDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Android-specific Koin module.
 * Provides Android-specific dependencies like SQLDelight driver.
 */
actual fun platformModule(): Module = module {
    // SqlDriver for Android
    // Context is provided by Koin's androidContext() in App initialization
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = ReposBrowserDatabase.Schema,
            context = get(),
            name = "reposbrowser.db"
        )
    }
}
