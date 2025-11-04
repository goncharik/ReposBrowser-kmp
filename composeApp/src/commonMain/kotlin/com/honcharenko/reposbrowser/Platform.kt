package com.honcharenko.reposbrowser

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform