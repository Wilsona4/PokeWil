package com.decagon.android.sq007.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.decagon.android.sq007.R
import com.decagon.android.sq007.data.model.PokeWilListModel
import com.decagon.android.sq007.databinding.FragmentListBinding
import com.decagon.android.sq007.ui.adapter.PokeWilListAdapter
import com.decagon.android.sq007.ui.adapter.PokeWilListAdapter.*
import com.decagon.android.sq007.util.PokemonListUtil.getPokemonList
import com.decagon.android.sq007.util.PokemonListUtil.localPokemonList
import com.decagon.android.sq007.viewModel.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment(), Interaction {

    private lateinit var pokemonListAdapter: PokeWilListAdapter
    private lateinit var viewModel: PokemonListViewModel

    private var pokemonList = getPokemonList()
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(PokemonListViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*Set Status bar Color*/
        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window?.statusBarColor = activity?.resources?.getColor(R.color.backgroundSecond)!!

        var endReached = viewModel.endReached
        var loadError = viewModel.loadError
        var isLoading = viewModel.isLoading

        binding.progressBar.visibility = View.VISIBLE

//        pokemonList = viewModel.pokemonList.value
        viewModel.pokemonList.observe(
            viewLifecycleOwner,
            Observer {
                pokemonListAdapter.submitList(it)
                pokemonList = it.toMutableList()
                localPokemonList = pokemonList as MutableList<PokeWilListModel>
            }
        )
        Log.d("LIST", "${getPokemonList()}")
        binding.progressBar.visibility = View.INVISIBLE

        /*Initialise RecyclerView*/
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(activity, 2)
            pokemonListAdapter = PokeWilListAdapter(this@ListFragment)
            adapter = pokemonListAdapter
        }
    }

    override fun onItemSelected(position: Int, item: PokeWilListModel) {
        Toast.makeText(requireActivity(), pokemonList[position].pokemonName, Toast.LENGTH_SHORT)
            .show()

        val currentPokemon = pokemonList[position]

        val action = ListFragmentDirections.actionListFragmentToDetailFragment(currentPokemon)
        findNavController().navigate(action)
    }

    companion object {
        val POKE = "poke"
    }
}
