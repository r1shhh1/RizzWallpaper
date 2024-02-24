package com.example.wallpaperapp.data.api

import android.content.Context
import android.preference.PreferenceManager
import com.example.wallpaperapp.Utils.Constants.CACHE_KEY
import com.example.wallpaperapp.domain.entity.WallpaperLink
import com.google.gson.Gson
import javax.inject.Inject

class SharedPreferenceWallpaperCache @Inject constructor(private val context: Context) : WallpaperCache {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    override fun getWallpaperLinks(): List<WallpaperLink>? {
        val cachedLinksJson = sharedPreferences.getString(CACHE_KEY, null)
        return cachedLinksJson?.let {
            Gson().fromJson(it, Array<WallpaperLink>::class.java).toList()
        }
    }
    override fun saveWallpaperLinks(links: List<WallpaperLink>) {
        val json = Gson().toJson(links)
        sharedPreferences.edit().putString(CACHE_KEY, json).apply()
    }

}