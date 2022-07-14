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
import com.samuelnunes.utility_tool_kit.binding.visibleIf
import com.samuelnunes.utility_tool_kit.domain.Result
import com.samuelnunes.utility_tool_kit.utils.toUiText
import com.samuelnunes.utils.databinding.FragmentFirstBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CatsListFragment : Fragment() {

    private val viewModel: CatsListViewModel by viewModels()

    private lateinit var binding: FragmentFirstBinding

    private val breedListAdapter = BreedListAdapter { breed ->
        val direction = CatsListFragmentDirections
            .actionBreedFragmentToWikipediaPage(breed.wikipediaName!!)
        findNavController().navigate(direction)
    }
    private val catGifAdapter = CatGifListAdapter {
        viewModel.fetchNewGifs()
    }

    private val concatAdapter = ConcatAdapter(catGifAdapter, breedListAdapter)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        binding.recyclerCats.adapter = concatAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorNotify()
        loadingState()
        populateGifs()
        populateBreedList()
    }

    private fun errorNotify() {
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Snackbar.make(binding.root, error.toString(requireContext()), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun loadingState() {
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.loading.visibleIf(loading)
        }
    }

    private fun populateBreedList() {
        viewModel.breeds.observe(viewLifecycleOwner) { breeds ->
            breedListAdapter.submitList(breeds)
        }
    }

    private fun populateGifs() {
        viewModel.gifs.observe(viewLifecycleOwner) { gifs ->
            catGifAdapter.submitList(gifs)
        }
    }
}