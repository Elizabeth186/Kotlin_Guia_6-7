package com.example.roomorm

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roomorm.databinding.ItemVehicleBinding
import com.example.roomorm.entities.Vehicle
import com.example.roomorm.repository.VehicleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("MemberVisibilityCanBePrivate")
class VehiclesAdapter(private val list: List<Vehicle>, private val  context: Context) :
    RecyclerView.Adapter<VehiclesAdapter.VehiclesViewHolder>() {

    class VehiclesViewHolder(val binding: ItemVehicleBinding) :
        RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehiclesViewHolder {
        val binding = ItemVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehiclesViewHolder(binding)
    }
    override fun onBindViewHolder(holder: VehiclesViewHolder, position: Int) {
        with(holder.binding) {
            tvTitle.text =  "${list[position].brand?.name} ${list[position].brand?.country} ${list[position].name}"
            tvYear.text = list[position].year.toString()
            tvColor.text = list[position].color
            btnClean.setOnClickListener {
                val repository = VehicleRepository.getRepository(context)
                CoroutineScope(Dispatchers.IO).launch{
                    repository.deleteid(list[position].id)

                }}
        }
    }
    override fun getItemCount(): Int = list.size
}