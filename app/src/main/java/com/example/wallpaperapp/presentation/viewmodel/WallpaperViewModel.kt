package com.example.wallpaperapp.presentation.viewmodel

import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.example.wallpaperapp.R
import com.example.wallpaperapp.Utils.Constants.WALLPAPER_URL
import com.example.wallpaperapp.Utils.Constants.WRITE_STORAGE_PERMISSION_REQUEST_CODE
import com.example.wallpaperapp.Utils.Resource
import com.example.wallpaperapp.domain.entity.WallpaperLink
import com.example.wallpaperapp.domain.repository.WallpaperRepository
import com.example.wallpaperapp.presentation.WallpaperUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import com.example.wallpaperapp.worker.SetWallpaperWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.jar.Manifest
import javax.inject.Inject

@HiltViewModel
class WallpaperViewModel @Inject constructor(
    private val repository: WallpaperRepository,
    private val context: Context
): ViewModel() {

    private val _wallpaperList : MutableStateFlow<WallpaperUiState> =
        MutableStateFlow(WallpaperUiState.Loading)
    val wallpaperList get() = _wallpaperList.asStateFlow()

    var isSettingWallpaper: Boolean = false

    fun fetchWallpapes() {
        viewModelScope.launch(Dispatchers.IO) {
           repository.getImages().collect() { resource->
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


    suspend fun setWallpaper(wallpaperLink: String){
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val wallpaperManager = WallpaperManager.getInstance(context)
                    val bitmap = getBitmap(wallpaperLink)
                    try {
                        isSettingWallpaper = true
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            wallpaperManager.setBitmap(bitmap, null, true)
                        } else {
                            wallpaperManager.setBitmap(bitmap)
                        }
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "Wallpaper set successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.IO) {
                            Toast.makeText(
                                context,
                                "Error setting wallpaper",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } finally {
                        isSettingWallpaper = false
                    }
                }
            }
    }

    private suspend fun getBitmap(url: String): Bitmap = withContext(Dispatchers.IO){
        Glide.with(context)
            .asBitmap()
            .load(url)
            .submit()
            .get()
    }


    init {
        fetchWallpapes()
    }

}



//fun requestWriteStoragePermission() {
//        val activity = context as? Activity // Assuming the context is an Activity
//        if (activity != null) {
//            ActivityCompat.requestPermissions(
//                activity,
//                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                WRITE_STORAGE_PERMISSION_REQUEST_CODE
//            )
//        } else {
//            Log.d("WallpaperViewModel", "Context is not an Activity, cannot request permission")
//        }
//    }

//fun checkWriteStoragePermission(): Boolean {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//        } else {
//            true // Permission checks not required for older versions
//        }
//    }