package com.example.wallpaperapp.data.api

import android.content.Context
import android.preference.PreferenceManager
import com.example.wallpaperapp.domain.entity.WallpaperLink
import com.google.gson.Gson

interface WallpaperCache {
    fun getWallpaperLinks(): List<WallpaperLink>?
    fun saveWallpaperLinks(links: List<WallpaperLink>)
}

