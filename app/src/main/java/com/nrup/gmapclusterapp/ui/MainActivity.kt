package com.nrup.gmapclusterapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nrup.gmapclusterapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}