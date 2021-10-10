package com.example.roomorm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.roomorm.databinding.ActivityMainBinding
import com.example.roomorm.repository.VehicleRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buildList()
        addListeners()
    }
    private fun buildList() {
// Get Repository
        val repository = VehicleRepository.getRepository(this)
// Build Layout manager
        val layoutManager = GridLayoutManager(this, 1)
// Catch other thread
        lifecycleScope.launch {
            repository.allVehicles.collect { vehicles ->
                binding.rvVehicles.apply {
                    adapter = VehiclesAdapter(vehicles, this@MainActivity)
                    setLayoutManager(layoutManager)
                }
            }
        }
    }
    private fun addListeners() {
        binding.fbAdd.setOnClickListener {
            startActivity(Intent(this, AddVehicleActivity::class.java))
        }


    }
}


