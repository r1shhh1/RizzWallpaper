package com.example.wallpaperapp.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.viewModels
import com.example.wallpaperapp.databinding.ActivityMainBinding
import com.example.wallpaperapp.domain.entity.WallpaperLink
import com.example.wallpaperapp.presentation.WallpaperUiState
import com.example.wallpaperapp.presentation.adapter.ImagesRecyclerViewAdapter
import com.example.wallpaperapp.presentation.viewmodel.WallpaperViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

//state.collect
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val wallpaperViewModel: WallpaperViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //1.setUpViews
        //2.collect the state -> no wallpaper -> loading
        //3.update our wallpaper
        //4.update our UI -> handled automatically

        setupViews()
        collectUiStates()
        wallpaperViewModel.fetchWallpapes()

    }

    fun setupViews (){
        binding.imagesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    fun collectUiStates(){
        lifecycleScope.launch(Dispatchers.Main) {
            wallpaperViewModel.wallpaperList.collect() { wallpaperUiState ->
                when(wallpaperUiState){
                    is WallpaperUiState.Loading -> {
                        //progress load
                        binding.progressBar.visibility = View.VISIBLE
                        Toast.makeText(this@MainActivity,
                            "Wallpapers are loading",
                            Toast.LENGTH_SHORT)
                    }
                    is WallpaperUiState.EmptyList -> {
                        //empty
                        binding.progressBar.visibility = View.VISIBLE
                        Toast.makeText(this@MainActivity,
                            "Wallpapers are currently empty",
                            Toast.LENGTH_SHORT)
                    }
                    is WallpaperUiState.Success -> {
                        //update recycler with wallpapers
                        binding.progressBar.visibility = View.GONE
                        populateDataInRecyclerView(wallpaperUiState.data)

                    }
                    is WallpaperUiState.Error -> {
                        //toast messager -> error
                        Toast.makeText(this@MainActivity,
                            "Error Occured!",
                            Toast.LENGTH_SHORT)
                    }
                }
            }
        }
    }

    private fun populateDataInRecyclerView(list: List<WallpaperLink>) {
        val wallpaperAdapter = ImagesRecyclerViewAdapter(list,this@MainActivity, wallpaperViewModel)
        binding.imagesRecyclerView.adapter = wallpaperAdapter
    }




}
