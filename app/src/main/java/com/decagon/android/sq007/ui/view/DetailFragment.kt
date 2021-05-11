package com.decagon.android.sq007.ui.view

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import coil.bitmap.BitmapPool
import coil.load
import coil.size.Size
import coil.transform.Transformation
import com.decagon.android.sq007.R
import com.decagon.android.sq007.data.model.PokeWilListModel
import com.decagon.android.sq007.data.remote.response.Pokemon
import com.decagon.android.sq007.data.remote.response.Pokemon.Companion.maxAttack
import com.decagon.android.sq007.data.remote.response.Pokemon.Companion.maxDefense
import com.decagon.android.sq007.data.remote.response.Pokemon.Companion.maxExp
import com.decagon.android.sq007.data.remote.response.Pokemon.Companion.maxHp
import com.decagon.android.sq007.data.remote.response.Pokemon.Companion.maxSpeed
import com.decagon.android.sq007.data.remote.response.Type
import com.decagon.android.sq007.databinding.FragmentDetailBinding
import com.decagon.android.sq007.ui.adapter.PokeTypeAdapter
import com.decagon.android.sq007.viewModel.PokemonDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(), PokeTypeAdapter.Interaction {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PokemonDetailViewModel
    lateinit var retrievedPokemon: PokeWilListModel
    lateinit var pokemonAttributes: Pokemon
    lateinit var pokeTypeRvAdapter: PokeTypeAdapter
    private val args by navArgs<DetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(PokemonDetailViewModel::class.java)

        retrievedPokemon = args.pokemon
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var errorMessage = "Init"

        /*Set Image and Layout Background Color*/
        binding.detailImage.load(retrievedPokemon.imageUrl) {
            transformations(object : Transformation {
                override fun key() = "paletteTransformer"
                override suspend fun transform(
                    pool: BitmapPool,
                    input: Bitmap,
                    size: Size
                ): Bitmap {
                    val palette = Palette.from(input).generate { palette: Palette? ->
                        // access palette instance here
                        binding.detailPage.setBackgroundColor(
                            palette?.lightVibrantSwatch?.rgb ?: ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                        /*Set Status bar Color to image vibrant swatch*/
                        val window = activity?.window
                        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                        window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                        window?.statusBarColor =
                            palette?.lightVibrantSwatch?.rgb ?: ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    }
                    return input
                }
            })
        }

        viewModel.getPokemonInfo(retrievedPokemon.number.toString())
        viewModel.myPokemonInfo.observe(
            viewLifecycleOwner,
            Observer {
                pokemonAttributes = it
                binding.detailPokeName.text = it.name
                binding.detailPokeHeight.text = "${it.height} M"
                binding.detailPokeWeight.text = "${it.weight} KG"
                /*Set-up Recycler View*/
                binding.pokeTypeRecyclerView.apply {
                    layoutManager =
                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                    pokeTypeRvAdapter = PokeTypeAdapter(this@DetailFragment)
                    pokeTypeRvAdapter.submitList(it.types)
                    adapter = pokeTypeRvAdapter
                }
                /*Set-up Progress View*/
                binding.progressHp.apply {
                    val hp: Int = (30..300).random()
                    progress = hp.toFloat()
                    max = maxHp.toFloat()
                    labelText = "$hp/$maxHp"
                }
                binding.progressAtk.apply {
                    val attack: Int = (30..300).random()
                    progress = attack.toFloat()
                    max = maxAttack.toFloat()
                    labelText = "$attack/$maxAttack"
                }
                binding.progressDef.apply {
                    val defense: Int = (30..300).random()
                    progress = defense.toFloat()
                    max = maxDefense.toFloat()
                    labelText = "$defense/$maxDefense"
                }

                binding.progressSpd.apply {
                    val speed: Int = (30..300).random()
                    progress = speed.toFloat()
                    max = maxSpeed.toFloat()
                    labelText = "$speed/$maxSpeed"
                }
                binding.progressExp.apply {
                    val exp: Int = (30..300).random()
                    progress = exp.toFloat()
                    max = maxExp.toFloat()
                    labelText = "$exp/$maxExp"
                }

                Log.d("POKI", "$pokemonAttributes")
            }
        )

        viewModel.loadError.observe(
            viewLifecycleOwner,
            Observer {
                errorMessage = it
                Log.d("ERROR", errorMessage)
            }
        )
    }

    override fun onItemSelected(position: Int, item: Type) {
    }
}
