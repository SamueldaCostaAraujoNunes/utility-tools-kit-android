package com.samuelnunes.presentation.screens.cats_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.google.android.material.snackbar.Snackbar
import com.samuelnunes.domain.entity.Breed
import com.samuelnunes.utility_tool_kit.binding.goneIf
import com.samuelnunes.utility_tool_kit.binding.visibleIf
import com.samuelnunes.utils.databinding.FragmentFirstBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatsListFragment : Fragment() {

    private val viewModel: CatsListViewModel by viewModels()
    private lateinit var binding: FragmentFirstBinding

    private val breedListAdapter = BreedListAdapter(::clickBreedItem)
    private val catGifAdapter = CatGifListAdapter(::fetchNewGifs)
    private val concatAdapter = ConcatAdapter(catGifAdapter, breedListAdapter)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentFirstBinding.inflate(inflater, container, false).apply {
            binding = this
            recyclerCats.adapter = concatAdapter
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorNotify()
        loadingState()
        populateGifs()
        populateBreedList()
        networkState()
    }

    private fun errorNotify() {
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Snackbar.make(binding.root, error.toString(requireContext()), Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun loadingState() =
        viewModel.loading.observe(viewLifecycleOwner, binding.loading::visibleIf)

    private fun populateBreedList() =
        viewModel.breeds.observe(viewLifecycleOwner, breedListAdapter::submitList)

    private fun populateGifs() =
        viewModel.gifs.observe(viewLifecycleOwner, catGifAdapter::submitList)

    private fun networkState() =
        viewModel.networkConnectivity.observe(viewLifecycleOwner, binding.cardNetworkState::goneIf)

    private fun clickBreedItem(breed: Breed) {
        val direction = CatsListFragmentDirections
            .actionBreedFragmentToWikipediaPage(breed.wikipediaName!!)
        findNavController().navigate(direction)
    }

    private fun fetchNewGifs(image: Breed.Image) = viewModel.fetchNewGifs()

}