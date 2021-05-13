package com.decagon.android.sq007.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AbsListView
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.data.model.PokeWilListModel
import com.decagon.android.sq007.databinding.FragmentListBinding
import com.decagon.android.sq007.ui.adapter.PokeWilListAdapter
import com.decagon.android.sq007.ui.adapter.PokeWilListAdapter.*
import com.decagon.android.sq007.util.Constant.PAGE_SIZE
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

        viewModel.pokemonList.observe(
            viewLifecycleOwner,
            Observer {
                pokemonList = it.toMutableList()
                pokemonListAdapter.submitList(pokemonList)
                if (pokemonList.isNotEmpty()) {
                    hideProgressBar()
                }
                localPokemonList = pokemonList as MutableList<PokeWilListModel>
            }
        )

        Log.d("LIST", "${getPokemonList()}")

        /*Initialise RecyclerView*/
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(activity, 2)
            pokemonListAdapter = PokeWilListAdapter(this@ListFragment)
            adapter = pokemonListAdapter
            addOnScrollListener(this@ListFragment.scrollListener)
        }

        /*Set-up Search functionality*/
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchPokemonList(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                newText?.let { viewModel.searchPokemonList(it) }
                return false
            }
        })
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    //    Set-up onClick Listener for pokemon Items
    override fun onItemSelected(position: Int, item: PokeWilListModel) {
        Toast.makeText(requireActivity(), pokemonList[position].pokemonName, Toast.LENGTH_SHORT)
            .show()

        val currentPokemon = pokemonList[position]

        val action = ListFragmentDirections.actionListFragmentToDetailFragment(currentPokemon)
        findNavController().navigate(action)
    }

    //    Set-up Scroll Listener for List Pagination
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage =
                !viewModel.isLoading.value && !viewModel.endReached.value
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.loadPaginatedPokemonList()
                isScrolling = false
            } else {
                binding.recyclerView.setPadding(16, 0, 16, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }
}
