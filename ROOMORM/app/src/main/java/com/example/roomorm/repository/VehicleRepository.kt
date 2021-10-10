package com.example.roomorm.repository

import android.content.Context
import com.example.roomorm.dao.BrandDao
import com.example.roomorm.dao.VehicleDao
import com.example.roomorm.database.VehicleRoomDatabase
import com.example.roomorm.entities.Brand
import com.example.roomorm.entities.Vehicle
import kotlinx.coroutines.flow.Flow

class VehicleRepository(private val vehicleDao: VehicleDao, private val brandDao: BrandDao) {
    companion object {
        private var INSTANCE : VehicleRepository? = null
        fun getRepository(context: Context) : VehicleRepository {
            return INSTANCE ?: synchronized(this) {
                val database = VehicleRoomDatabase.getDatabase(context)
                val instance = VehicleRepository(database.vehicleDao(), database.brandDao())
                INSTANCE = instance
                instance
            }
        }
    }


    val allVehicles: Flow<List<Vehicle>> = vehicleDao.getAlphabetizedVehicles()

    suspend fun insert(vehicle: Vehicle) {
        vehicleDao.insert(vehicle)
    }
    suspend fun deleteid(Id:Int) {
         vehicleDao.deleteid(Id)
    }

    val brands: Flow<List<Brand>> = brandDao.getBrands()
    suspend fun insertBrand(brand: Brand) {
        brandDao.insert(brand)
    }
    suspend fun deleteBrand(id: Int) {
        brandDao.deleteBrand(id)
    }

}