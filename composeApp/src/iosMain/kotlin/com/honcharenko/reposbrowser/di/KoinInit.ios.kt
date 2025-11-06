package com.honcharenko.reposbrowser.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.honcharenko.reposbrowser.data.local.ReposBrowserDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * iOS-specific Koin module.
 * Provides iOS-specific dependencies like SQLDelight driver.
 */
actual fun platformModule(): Module = module {
    // SqlDriver for iOS
    single<SqlDriver> {
        NativeSqliteDriver(
            schema = ReposBrowserDatabase.Schema,
            name = "reposbrowser.db"
        )
    }
}
