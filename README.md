# ReposBrowser 🚀

A **Kotlin Multiplatform** demo application showcasing modern mobile development with shared business logic and part of the UI between iOS and Android. Search GitHub repositories, view details, and save your favorites.

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.9.0-blue)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![Apollo Kotlin](https://img.shields.io/badge/Apollo%20Kotlin-4.3.3-purple)](https://github.com/apollographql/apollo-kotlin)
[![SQLDelight](https://img.shields.io/badge/SQLDelight-2.1.0-green)](https://github.com/cashapp/sqldelight)
[![Koin](https://img.shields.io/badge/Koin-4.1.0-yellow)](https://insert-koin.io/)
[![Coil](https://img.shields.io/badge/Coil-3.3.0-red)](https://coil-kt.github.io/coil/)

## ✨ Features

- 🔍 **Search GitHub Repositories** - Real-time search using GitHub's GraphQL API
- 📊 **Repository Details** - View comprehensive information about any repository
- ⭐ **Favorites** - Save and manage your favorite repositories locally
- 🎨 **Native Navigation** - SwiftUI TabView on iOS, Compose Navigation on Android
- 🔄 **Shared Business Logic** - Common code for networking, data storage, and ViewModels
- 📦 **Modern Architecture** - Clean Architecture with Repository pattern and DI

## 🎯 Purpose

This project demonstrates:

- **Apollo Kotlin v4** integration in a KMP project
- **Koin** dependency injection across platforms
- **SQLDelight** for local data persistence
- **Compose Multiplatform** UI shared between platforms
- **Native navigation** integration (SwiftUI for iOS, Compose for Android)
- Production-quality architecture in a demo context

Perfect for:
- Learning KMP development
- Understanding Apollo GraphQL in mobile apps
- Reference architecture for new KMP projects
- Portfolio/interview showcase

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                         Presentation                        │
│  ┌──────────────────────┐      ┌──────────────────────┐     │
│  │   Android (Compose)  │      │    iOS (SwiftUI)     │     │
│  │  - Compose Navigation│      │  - TabView           │     │
│  │  - Material3         │      │  - NavigationStack   │     │
│  └──────────────────────┘      └──────────────────────┘     │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                    Shared Module (KMP)                      │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  UI Layer (Compose Multiplatform)                   │    │
│  │  - SearchScreen.kt                                  │    │
│  │  - RepoDetailsScreen.kt                             │    │
│  │  - FavoritesScreen.kt                               │    │
│  └─────────────────────────────────────────────────────┘    │
│                              │                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  ViewModel Layer                                    │    │
│  │  - SearchViewModel (StateFlow)                      │    │
│  │  - RepoDetailsViewModel                             │    │
│  │  - FavoritesViewModel                               │    │
│  └─────────────────────────────────────────────────────┘    │
│                              │                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  Domain Layer                                       │    │
│  │  - Repository, RepositoryDetails (models)           │    │
│  │  - GitHubRepository (interface)                     │    │
│  │  - FavoritesRepository (interface)                  │    │
│  └─────────────────────────────────────────────────────┘    │
│                              │                              │
│  ┌────────────────────┬─────────────────────────────────┐   │
│  │  Data Layer (API)  │   Data Layer (Local)            │   │
│  │  - Apollo Client   │   - SQLDelight                  │   │
│  │  - GraphQL Queries │   - Database Driver             │   │
│  │  - GitHubApiClient │   - FavoritesDatabase           │   │
│  └────────────────────┴─────────────────────────────────┘   │
│                              │                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  Dependency Injection (Koin)                        │    │
│  │  - networkModule, databaseModule, viewModelModule   │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

### Tech Stack

| Layer | Technology |
|-------|-----------|
| **UI** | Compose Multiplatform |
| **Navigation** | Compose Navigation (Android), SwiftUI Navigation (iOS) |
| **State Management** | ViewModel, StateFlow, Coroutines |
| **Networking** | Apollo Kotlin 4 (GraphQL) |
| **Local Storage** | SQLDelight |
| **Dependency Injection** | Koin |
| **Build System** | Gradle with Kotlin DSL |

## 📁 Project Structure

```
apollo-kmp-github/
├── composeApp/                 # Shared KMP module
│   ├── src/
│   │   ├── commonMain/
│   │   │   ├── kotlin/
│   │   │   │   ├── config/
│   │   │   │   │   ├── Secrets.kt           # Gitignored secrets
│   │   │   │   │   └── Secrets.example.kt   # Template
│   │   │   │   ├── data/
│   │   │   │   │   ├── api/
│   │   │   │   │   │   ├── ApolloClientFactory.kt
│   │   │   │   │   │   └── GitHubApiClient.kt
│   │   │   │   │   ├── local/
│   │   │   │   │   │   ├── DatabaseDriverFactory.kt
│   │   │   │   │   │   └── FavoritesDatabase.kt
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── Repository.kt
│   │   │   │   │   │   └── RepositoryDetails.kt
│   │   │   │   │   └── repository/
│   │   │   │   │       ├── GitHubRepository.kt
│   │   │   │   │       └── FavoritesRepository.kt
│   │   │   │   ├── di/
│   │   │   │   │   ├── KoinInit.kt
│   │   │   │   │   ├── NetworkModule.kt
│   │   │   │   │   ├── DatabaseModule.kt
│   │   │   │   │   └── ViewModelModule.kt
│   │   │   │   ├── ui/
│   │   │   │   │   ├── screens/
│   │   │   │   │   │   ├── SearchScreen.kt
│   │   │   │   │   │   ├── RepoDetailsScreen.kt
│   │   │   │   │   │   └── FavoritesScreen.kt
│   │   │   │   │   ├── components/
│   │   │   │   │   │   └── RepositoryCard.kt
│   │   │   │   │   └── theme/
│   │   │   │   │       └── Theme.kt
│   │   │   │   └── viewmodel/
│   │   │   │       ├── SearchViewModel.kt
│   │   │   │       ├── RepoDetailsViewModel.kt
│   │   │   │       └── FavoritesViewModel.kt
│   │   │   └── graphql/
│   │   │       ├── schema.graphqls          # GitHub GraphQL schema
│   │   │       ├── SearchRepositories.graphql
│   │   │       └── GetRepositoryDetails.graphql
│   │   ├── androidMain/
│   │   │   └── kotlin/
│   │   │       └── data/local/
│   │   │           └── DatabaseDriverFactory.android.kt
│   │   └── iosMain/
│   │       └── kotlin/
│   │           └── data/local/
│   │               └── DatabaseDriverFactory.ios.kt
│   └── build.gradle.kts
│
├── androidApp/                 # Android-specific code
│   ├── src/main/
│   │   ├── kotlin/
│   │   │   ├── MainActivity.kt
│   │   │   └── Navigation.kt
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
│
├── iosApp/                     # iOS-specific code
│   ├── iosApp/
│   │   ├── ContentView.swift           # SwiftUI TabView
│   │   ├── ComposeViews.swift          # UIViewControllerRepresentable
│   │   └── iosAppApp.swift
│   └── iosApp.xcodeproj
│
├── gradle/
│   └── libs.versions.toml      # Version catalog
├── .gitignore
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## 🚀 Getting Started

### Prerequisites

- Android Studio (latest stable version)
- Xcode 16+ (for iOS development)
- GitHub Personal Access Token

### Setup Instructions

#### 1. **Clone the repository**
```bash
   git clone https://github.com/goncharik/reposbrowser-kmp.git
   cd reposbrowser-kmp
```

#### 2. **Configure GitHub Token**
   
   This app uses the GitHub GraphQL API and requires a personal access token.
   
   **Get a token:**
   - Go to [GitHub Settings > Tokens](https://github.com/settings/tokens)
   - Click **"Generate new token"** → **"Generate new token (classic)"**
   - Give it a name: `ReposBrowser`
   - Select scope: **`public_repo`** (read access to public repositories)
   - Click **"Generate token"** and copy it
   
   **Add token to project:**
```bash
   # Navigate to the config directory
   cd composeApp/src/commonMain/kotlin/com/honcharenko/reposbrowser/config/
   
   # Copy the example file
   cp Secrets.example.kt Secrets.kt
   
   # Edit Secrets.kt and replace YOUR_GITHUB_TOKEN_HERE with your token
```
   
   Your `Secrets.kt` should look like:
```kotlin
   object Secrets {
       const val GITHUB_TOKEN = "ghp_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
   }
```

#### 3. Build the Project

```bash
./gradlew build
```

#### 4. Run on Android

**Option A: Android Studio**
1. Open the project in Android Studio
2. Select `androidApp` configuration
3. Choose an emulator or connected device
4. Click Run ▶️

**Option B: Command Line**
```bash
./gradlew :androidApp:installDebug
```

#### 5. Run on iOS

**Requirements:** macOS with Xcode installed

**Option A: Xcode**
1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Select a simulator or device
3. Click Run ▶️

**Option B: Command Line**
```bash
# Build the KMP framework first
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode

# Open in Xcode
open iosApp/iosApp.xcodeproj
```

## 📚 Learning Resources

### Kotlin Multiplatform
- [Official KMP Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [KMP Get Started Guide](https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-getting-started.html)

### Apollo Kotlin
- [Apollo Kotlin Docs](https://www.apollographql.com/docs/kotlin/)
- [Apollo Kotlin v4 Migration Guide](https://www.apollographql.com/docs/kotlin/v4/migration/4.0/)

### Compose Multiplatform
- [Compose Multiplatform Docs](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Compose for iOS](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-getting-started.html)

### SQLDelight
- [SQLDelight Documentation](https://cashapp.github.io/sqldelight/)
- [SQLDelight Multiplatform Setup](https://cashapp.github.io/sqldelight/2.0.2/multiplatform_sqlite/)

### Koin
- [Koin Documentation](https://insert-koin.io/)
- [Koin Multiplatform](https://insert-koin.io/docs/reference/koin-mp/kmp)

## 📧 Contact

**Eugene Honcharenko** - [@honcharenko_eu](https://x.com/honcharenko_eu)

**Project Link:** [https://github.com/goncharik/reposbrowser-kmp](https://github.com/goncharik/reposbrowser-kmp)

---

**Built with ❤️ using Kotlin Multiplatform**


