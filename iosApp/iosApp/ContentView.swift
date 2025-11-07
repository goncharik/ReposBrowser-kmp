import UIKit
import SwiftUI
import ComposeApp

// MARK: - Compose View Wrappers

struct SearchComposeView: UIViewControllerRepresentable {
    let onRepositoryClick: (Repository) -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        // Set the callback before creating the view controller
        MainViewControllerKt.iosRepositoryClickCallback = onRepositoryClick
        return MainViewControllerKt.SearchViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct RepositoryDetailsComposeView: UIViewControllerRepresentable {
    let repository: Repository

    func makeUIViewController(context: Context) -> UIViewController {
        return MainViewControllerKt.RepoDetailsViewController(repository: repository)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct FavoritesComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.FavoritesViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

// MARK: - Main Content View

struct ContentView: View {
    @State private var selectedRepository: Repository?

    var body: some View {
        TabView {
            NavigationStack {
                SearchComposeView(onRepositoryClick: { repository in
                    selectedRepository = repository
                })
                .navigationTitle("Search")
                .navigationBarTitleDisplayMode(.large)
                .ignoresSafeArea(edges: .bottom)
                .navigationDestination(item: $selectedRepository) { repository in
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
                FavoritesComposeView()
                    .navigationTitle("Favorites")
                    .navigationBarTitleDisplayMode(.large)
                    .ignoresSafeArea(edges: .bottom)
            }
            .tabItem {
                Label("Favorites", systemImage: "star.fill")
            }
        }
    }
}



