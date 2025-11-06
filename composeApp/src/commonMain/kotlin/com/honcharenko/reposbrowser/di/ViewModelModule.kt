package com.honcharenko.reposbrowser.di

import com.honcharenko.reposbrowser.viewmodel.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for ViewModel dependencies.
 */
val viewModelModule = module {
    // SearchViewModel
    viewModel {
        SearchViewModel(
            gitHubRepository = get(),
            favoritesRepository = get()
        )
    }

    // Future ViewModels:
    // viewModel { RepoDetailsViewModel(...) }
    // viewModel { FavoritesViewModel(...) }
}
