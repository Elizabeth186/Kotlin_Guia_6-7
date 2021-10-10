package com.example.roomorm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.example.roomorm.databinding.ActivityAddVehicleBinding
import com.example.roomorm.entities.Brand
import com.example.roomorm.entities.Vehicle
import com.example.roomorm.repository.VehicleRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import kotlinx.coroutines.flow.collect


class AddVehicleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddVehicleBinding
    private var brandSelected: Brand? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addListener()
        buildSpinner()
    }



    private fun addListener() {
        val repository = VehicleRepository.getRepository(this)
        binding.btnAddBrand.setOnClickListener {
            startActivity(Intent(this, AddBrandActivity::class.java))
        }
        binding.btnAdd.setOnClickListener {
            hideKeyboard()
            with(binding) {
                if (etName.text.isBlank() || etYear.text.isBlank() || etColor.text.isBlank()) {
                    Snackbar.make(this.root, "Some fields are empty", Snackbar.LENGTH_SHORT).show()
                } else {
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            repository.insert(
                                Vehicle(
                                    name = etName.text.toString(),
                                    year = etYear.text.toString().toInt(),
                                    color = etColor.text.toString(),
                                    brand = brandSelected
                                )
                            )
                        }
                        onBackPressed()
                    }
                }
            }

        }
    }
    private fun buildSpinner() {
        val repository = VehicleRepository.getRepository(this)
        lifecycleScope.launch {
            repository.brands.collect { brands ->
                var names = ArrayList<String>()
                brands.forEach { names.add(it.name) }
                binding.spBrand.apply {
                    adapter = ArrayAdapter(
                        this@AddVehicleActivity,
                        R.layout.support_simple_spinner_dropdown_item,
                        names
                    )
                    onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            brandSelected = brands[position]
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
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
