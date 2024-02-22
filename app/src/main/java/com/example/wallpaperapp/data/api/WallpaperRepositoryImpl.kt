package com.example.wallpaperapp.data.api

import com.example.wallpaperapp.Utils.Resource
import com.example.wallpaperapp.domain.entity.WallpaperLink
import com.example.wallpaperapp.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*dependency injection
 */
class WallpaperRepositoryImpl @Inject constructor(val picSumApi: PicSumApi): WallpaperRepository {

    override suspend fun getImages(): Flow<Resource<List<WallpaperLink>>> = flow {

       try{
           val response = picSumApi.getWallpaperImages()

           //converting the big response into smaller object
           response?.let{
               val wallpaperLinks: List<WallpaperLink> = response.map {
                   WallpaperLink(it.downloadUrl)
               }
               emit(Resource.Success(wallpaperLinks))
           }
       }catch (e: Exception){
           emit(Resource.Error(null, e.message?:"Error fetching response!"))
       }

    }

}