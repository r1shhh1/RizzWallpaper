package com.example.wallpaperapp.data.api

import android.annotation.SuppressLint
import com.example.wallpaperapp.Utils.Resource
import com.example.wallpaperapp.domain.entity.WallpaperLink
import com.example.wallpaperapp.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*dependency injection
 */
class WallpaperRepositoryImpl @Inject constructor(
    private val picSumApi: PicSumApi,
    private val cache: WallpaperCache): WallpaperRepository {

    @SuppressLint("SuspiciousIndentation")
    override suspend fun getImages(): Flow<Resource<List<WallpaperLink>>> = flow {
           val cachedLinks = cache.getWallpaperLinks()
           if (cachedLinks != null) {
                emit(Resource.Success(cachedLinks))
           } else {
              try {
                val response = picSumApi.getWallpaperImages()
                //converting the big response into smaller object
                response?.let {
                    val wallpaperLinks: List<WallpaperLink> = response.map {
                        WallpaperLink(it.downloadUrl)
                    }
                    cache.saveWallpaperLinks(wallpaperLinks)
                    emit(Resource.Success(wallpaperLinks))
                }
              } catch (e: Exception) {
                emit(Resource.Error(null, e.message ?: "Error fetching response!"))
              }
           }
        }
}