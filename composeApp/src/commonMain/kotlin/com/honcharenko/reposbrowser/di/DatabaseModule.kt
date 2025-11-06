package com.honcharenko.reposbrowser.di

import app.cash.sqldelight.db.SqlDriver
import com.honcharenko.reposbrowser.data.local.FavoritesDatabase
import org.koin.dsl.module

/**
 * Koin module for database-related dependencies.
 * Provides SQLDelight driver and database wrapper.
 *
 * Note: SqlDriver is platform-specific and must be
 * provided by the platform-specific Koin configuration.
 */
val databaseModule = module {
    // SqlDriver - Platform-specific (provided by platform modules)
    // Expected to be added in platform-specific initialization

    // FavoritesDatabase - Singleton
    single { FavoritesDatabase(driver = get()) }
}
