package com.example.randompet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.randompet.R

data class Pet(
    val name: String,
    val breed: String,
    val imageUrl: String
)

class PetAdapter(private var pets: List<Pet>) : RecyclerView.Adapter<PetAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.petImage)
        val nameView: TextView = view.findViewById(R.id.petName)
        val breedView: TextView = view.findViewById(R.id.petBreed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pet_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = pets.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pet = pets[position]
        Glide.with(holder.itemView.context).load(pet.imageUrl).into(holder.imageView)
        holder.nameView.text = pet.name
        holder.breedView.text = pet.breed
    }

    fun updateData(newPets: List<Pet>) {
        this.pets = newPets
        notifyDataSetChanged()
    }
}







