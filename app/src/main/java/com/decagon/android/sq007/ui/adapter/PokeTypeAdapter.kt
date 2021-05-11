package com.decagon.android.sq007.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.data.remote.response.Type
import com.decagon.android.sq007.util.PokemonTypeColor.getTypeColor

class PokeTypeAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Type>() {

        override fun areItemsTheSame(oldItem: Type, newItem: Type): Boolean {
            return oldItem.type.toString() == newItem.type.toString()
        }

        override fun areContentsTheSame(oldItem: Type, newItem: Type): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.poke_type_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Type>) {
        differ.submitList(list)
    }

    class ViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Type) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            val pokeType = itemView.findViewById<TextView>(R.id.tvPokeType)
            val pokeTypeCard = itemView.findViewById<CardView>(R.id.pokeType)

            val type = item.type.name
            pokeType.text = type

            /*Set Card Background Color*/
            val color = getTypeColor(type)
            pokeTypeCard.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Type)
    }
}
