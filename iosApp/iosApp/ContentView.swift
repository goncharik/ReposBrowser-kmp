import UIKit
import SwiftUI
import ComposeApp

// MARK: - Compose View Wrappers

struct SearchComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.SearchViewController()
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
    var body: some View {
        TabView {
            NavigationStack {
                SearchComposeView()
                    .navigationTitle("Search")
                    .navigationBarTitleDisplayMode(.large)
                    .ignoresSafeArea(edges: .bottom)
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



