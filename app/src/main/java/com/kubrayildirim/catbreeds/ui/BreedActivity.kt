package com.kubrayildirim.catbreeds.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kubrayildirim.catbreeds.databinding.ActivityBreedsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_breeds.*

@AndroidEntryPoint
class BreedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBreedsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreedsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.toolbar.title = "Cat Breeds"
        setSupportActionBar(toolbar)
        supportActionBar?.show()
    }
}