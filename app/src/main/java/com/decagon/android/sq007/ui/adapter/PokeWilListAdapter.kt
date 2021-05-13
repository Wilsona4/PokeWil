package com.decagon.android.sq007.ui.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.bitmap.BitmapPool
import coil.load
import coil.request.ImageRequest
import coil.size.Size
import coil.transform.Transformation
import com.decagon.android.sq007.R
import com.decagon.android.sq007.data.model.PokeWilListModel

class PokeWilListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val diffCallBack = object : DiffUtil.ItemCallback<PokeWilListModel>() {

        override fun areItemsTheSame(
            oldItem: PokeWilListModel,
            newItem: PokeWilListModel
        ): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(
            oldItem: PokeWilListModel,
            newItem: PokeWilListModel
        ): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return RvViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.pokemon_list_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RvViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<PokeWilListModel>) {
        differ.submitList(list)
    }

    class RvViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: PokeWilListModel) = with(itemView) {
            val pokeWilName = itemView.findViewById<TextView>(R.id.tvPokeListName)
            val pokeWilNumber = itemView.findViewById<TextView>(R.id.tvPokeListNumber)
            val pokeWilImage = itemView.findViewById<ImageView>(R.id.ivPokeListImage)
            val pokeWilCard = itemView.findViewById<CardView>(R.id.pokeCardView)

            val num = "#" + item.number.toString()

            pokeWilName.text = item.pokemonName
            pokeWilNumber.text = num

            ImageRequest.Builder(pokeWilName.context).transformations(object : Transformation {
                override fun key(): String = "palleteTransformer"

                override suspend fun transform(
                    pool: BitmapPool,
                    input: Bitmap,
                    size: Size
                ): Bitmap {
                    Palette.from(input).generate { palette: Palette? ->
                        // access palette instance here
                        pokeWilCard.setCardBackgroundColor(
                            palette?.lightVibrantSwatch?.rgb ?: ContextCompat.getColor(
                                context,
                                R.color.white
                            )
                        )
                    }
                    return input
                }
            })

            pokeWilImage.load(item.imageUrl) {
                transformations(object : Transformation {
                    override fun key() = "paletteTransformer"
                    override suspend fun transform(
                        pool: BitmapPool,
                        input: Bitmap,
                        size: Size
                    ): Bitmap {

                        Palette.from(input).generate { palette: Palette? ->
                            // access palette instance here
                            pokeWilCard.setCardBackgroundColor(
                                palette?.lightVibrantSwatch?.rgb ?: ContextCompat.getColor(
                                    context,
                                    R.color.white
                                )
                            )
                        }
                        return input
                    }
                })
            }

            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: PokeWilListModel)
    }
}
