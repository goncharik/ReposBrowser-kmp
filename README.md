## 🚀 Getting Started

### Prerequisites

- Android Studio (latest stable version)
- Xcode 14+ (for iOS development)
- GitHub Personal Access Token

### Setup Instructions

1. **Clone the repository**
```bash
   git clone https://github.com/yourusername/your-repo.git
   cd your-repo
```

2. **Configure GitHub Token**
   
   This app uses the GitHub GraphQL API and requires a personal access token.
   
   **Get a token:**
   - Go to [GitHub Settings > Tokens](https://github.com/settings/tokens)
   - Click **"Generate new token"** → **"Generate new token (classic)"**
   - Give it a name: `Apollo KMP Demo App`
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

3. **Build the project**
```bash
   ./gradlew build
```

4. **Run Android**
   - Open project in Android Studio
   - Run on emulator or device

5. **Run iOS**
   - Open `iosApp/iosApp.xcodeproj` in Xcode
   - Run on simulator or device

### ⚠️ Important

- Never commit `Secrets.kt` to git (it's already in `.gitignore`)
- The token has read-only access to public repositories
- If you accidentally commit your token, revoke it immediately on GitHub

### Troubleshooting

**Error: "Please replace YOUR_GITHUB_TOKEN with your actual GitHub token"**
- You forgot to create `Secrets.kt` or didn't replace the placeholder

**Error: "Invalid GitHub token format"**
- Make sure your token starts with `ghp_` or `github_pat_`
- Verify you copied the entire token

**Error: "Bad credentials" from GitHub API**
- Your token may be expired or invalid
- Generate a new token and update `Secrets.kt`