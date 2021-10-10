package com.example.roomorm

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.roomorm.databinding.ActiviyAddBrandBinding
import com.example.roomorm.entities.Brand
import com.example.roomorm.repository.VehicleRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddBrandActivity : AppCompatActivity() {
    private lateinit var binding: ActiviyAddBrandBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActiviyAddBrandBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addListeners()
    }
    private fun addListeners() {
        val repository = VehicleRepository.getRepository(this)
        binding.btnAdd.setOnClickListener {
            hideKeyboard()
            with(binding) {
                if (etName.text.isBlank() || etCountry.text.isBlank()) {
                    Snackbar.make(this.root, "Some fields are empty", Snackbar.LENGTH_SHORT).show()
                } else {
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            repository.insertBrand(
                                Brand(
                                    name = etName.text.toString(),
                                    country = etCountry.text.toString(),
                                )
                            )
                        }
                        onBackPressed()
                    }
                }
            }
        }
    }
    private fun hideKeyboard() {
        val manager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}