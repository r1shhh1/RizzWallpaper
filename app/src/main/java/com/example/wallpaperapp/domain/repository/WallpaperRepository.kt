package com.example.wallpaperapp.domain.repository

import com.example.wallpaperapp.Utils.Resource
import com.example.wallpaperapp.data.api.PicSumApi
import com.example.wallpaperapp.domain.entity.WallpaperLink
import kotlinx.coroutines.flow.Flow

interface WallpaperRepository {

   suspend fun getImages(): Flow<Resource<List<WallpaperLink>>>
}