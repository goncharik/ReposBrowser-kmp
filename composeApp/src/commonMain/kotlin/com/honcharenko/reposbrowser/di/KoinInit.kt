package com.honcharenko.reposbrowser.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

/**
 * Initializes Koin dependency injection.
 * Combines common modules with platform-specific modules.
 */
fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(
        networkModule,
        databaseModule,
        repositoryModule,
        viewModelModule,
        platformModule() // Platform-specific module
    )
}

/**
 * Expect function to provide platform-specific Koin module.
 * Each platform (Android, iOS) must provide its own implementation.
 *
 * Platform modules should include:
 * - DatabaseDriverFactory (platform-specific)
 * - Any other platform-specific dependencies
 */
expect fun platformModule(): Module
