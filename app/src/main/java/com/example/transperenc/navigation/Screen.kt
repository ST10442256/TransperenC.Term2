package com.example.transperenc.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Login : Screen()
    @Serializable
    data object Dashboard : Screen()
    @Serializable
    data object Reports : Screen()
    @Serializable
    data object Documents : Screen()
    @Serializable
    data object DonorPortal : Screen()
    @Serializable
    data object Settings : Screen()
    @Serializable
    data object Register : Screen()
    @Serializable
    data object Profile : Screen()
    @Serializable
    data object Analytics : Screen()
}
