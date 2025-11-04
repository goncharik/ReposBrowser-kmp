package com.honcharenko.reposbrowser.config

/**
 * Example secrets file.
 * 
 * To use this project:
 * 1. Copy this file and rename it to "Secrets.kt" in the same directory
 * 2. Replace the placeholder values with your actual credentials
 * 3. Never commit Secrets.kt to git (it's in .gitignore)
 * 
 * How to get a GitHub Personal Access Token:
 * 1. Go to https://github.com/settings/tokens
 * 2. Click "Generate new token" -> "Generate new token (classic)"
 * 3. Give it a name (e.g., "ReposBrowser")
 * 4. Select scopes: "public_repo" (read access to public repositories)
 * 5. Click "Generate token"
 * 6. Copy the token and paste it below
 * 
 * Note: The token should start with "ghp_" for personal access tokens
 */
object Secrets {
    /**
     * GitHub Personal Access Token
     * Required scopes: public_repo (or just leave with default public access)
     * 
     * Example format: "ghp_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
     */
    const val GITHUB_TOKEN = "YOUR_GITHUB_TOKEN_HERE"
}