import UIKit
import SwiftUI
import ComposeApp

// MARK: - Compose View Wrappers

struct SearchComposeView: UIViewControllerRepresentable {
    let onRepositoryClick: (Repository_) -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        return MainViewControllerKt.SearchViewController(onRepositoryClick: onRepositoryClick)
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct RepositoryDetailsComposeView: UIViewControllerRepresentable {
    let repository: Repository_

    func makeUIViewController(context: Context) -> UIViewController {
        return MainViewControllerKt.RepoDetailsViewController(repository: repository)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct FavoritesComposeView: UIViewControllerRepresentable {
    let onRepositoryClick: (Repository_) -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        return MainViewControllerKt.FavoritesViewController(onRepositoryClick: onRepositoryClick)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

// MARK: - Main Content View

struct ContentView: View {
    @State private var searchSelectedRepository: Repository_?
    @State private var favoritesSelectedRepository: Repository_?

    var body: some View {
        TabView {
            NavigationStack {
                SearchComposeView(onRepositoryClick: { repository in
                    searchSelectedRepository = repository
                })
                .navigationTitle("Search")
                .navigationBarTitleDisplayMode(.large)
                .ignoresSafeArea(edges: .bottom)
                .navigationDestination(item: $searchSelectedRepository) { repository in
                    RepositoryDetailsComposeView(repository: repository)
                        .navigationTitle(repository.name)
                        .navigationBarTitleDisplayMode(.inline)
                        .ignoresSafeArea(edges: .bottom)
                }
            }
            .tabItem {
                Label("Search", systemImage: "magnifyingglass")
            }

            NavigationStack {
                FavoritesComposeView(onRepositoryClick: { repository in
                    favoritesSelectedRepository = repository
                })
                .navigationTitle("Favorites")
                .navigationBarTitleDisplayMode(.large)
                .ignoresSafeArea(edges: .bottom)
                .navigationDestination(item: $favoritesSelectedRepository) { repository in
                    RepositoryDetailsComposeView(repository: repository)
                        .navigationTitle(repository.name)
                        .navigationBarTitleDisplayMode(.inline)
                        .ignoresSafeArea(edges: .bottom)
                }
            }
            .tabItem {
                Label("Favorites", systemImage: "star.fill")
            }
        }
    }
}
