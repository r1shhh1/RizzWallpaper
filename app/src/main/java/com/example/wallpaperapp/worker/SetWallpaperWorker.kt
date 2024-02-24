package com.example.wallpaperapp.worker

import android.app.WallpaperManager
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.wallpaperapp.Utils.Constants.WALLPAPER_URL
import java.net.URL

class SetWallpaperWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val wallpaperUrl = inputData.getString(WALLPAPER_URL)
        val wallpaperManager = WallpaperManager.getInstance(applicationContext)
        try {
            val url = URL(wallpaperUrl)
            val inputStream = url.openStream()
            wallpaperManager.setStream(inputStream)
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}