package com.example.wallpaperapp.presentation.adapter

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.contentValuesOf
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wallpaperapp.R
import com.example.wallpaperapp.Utils.Constants.WRITE_STORAGE_PERMISSION_REQUEST_CODE
import com.example.wallpaperapp.domain.entity.WallpaperLink
import com.example.wallpaperapp.presentation.viewmodel.WallpaperViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ImagesRecyclerViewAdapter(
    private var dataSet: List<WallpaperLink>,
    private val activity: Activity,
    private val viewModel: WallpaperViewModel ) :
    RecyclerView.Adapter<ImagesRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View, dataSet: List<WallpaperLink>, private val viewModel: WallpaperViewModel) : RecyclerView.ViewHolder(view) {
        val imageView: AppCompatImageView

        init {
            imageView = view.findViewById(R.id.imageView)

            imageView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val wallpaperLink = dataSet[position].wallpaperLink
                    if(!viewModel.isSettingWallpaper){
                        viewModel.viewModelScope.launch {
                            viewModel.setWallpaper(wallpaperLink)
                        }
                    }else{
                        Toast.makeText(
                            view.context,
                            "Wallpaper setting is in progress. Please wait.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view, dataSet, viewModel)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Glide.with(viewHolder.imageView.context)
            .load(dataSet[position].wallpaperLink)
            .into(viewHolder.imageView)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


}
