package com.example.wallpaperapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallpaperapp.Utils.Resource
import com.example.wallpaperapp.domain.entity.WallpaperLink
import com.example.wallpaperapp.domain.repository.WallpaperRepository
import com.example.wallpaperapp.presentation.WallpaperUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallpaperViewModel @Inject constructor(private val repository: WallpaperRepository): ViewModel() {

    private val _wallpaperList : MutableStateFlow<WallpaperUiState> =
        MutableStateFlow(WallpaperUiState.Loading)

    val wallpaperList get() = _wallpaperList.asStateFlow()

    fun fetchWallpapes() {

        viewModelScope.launch(Dispatchers.IO) {
           repository.getImages().collect() { resource->
               Log.d("Res", "fetchWallpapes: " + resource.data.toString())
               when (resource){
                   is Resource.Success -> {
                       if(resource.data.isNullOrEmpty()){
                           _wallpaperList.update { WallpaperUiState.Loading }
                       } else{
                           _wallpaperList.update { WallpaperUiState.Success(resource.data) }
                       }
                   }
                   is Resource.Error -> {
                       _wallpaperList.update { WallpaperUiState.EmptyList }
                   }
               }
           }
        }
    }

}