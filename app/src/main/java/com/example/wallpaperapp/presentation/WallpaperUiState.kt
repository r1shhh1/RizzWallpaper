package com.example.wallpaperapp.presentation

import com.example.wallpaperapp.data.api.model.PicSumItem
import com.example.wallpaperapp.domain.entity.WallpaperLink

sealed class WallpaperUiState {

    object Loading: WallpaperUiState()

    object EmptyList: WallpaperUiState()

    //update data when there is success
    data class Success(val data: List<WallpaperLink>) : WallpaperUiState()

    //dont update data when there is error
    data class Error(val message:String): WallpaperUiState()
}