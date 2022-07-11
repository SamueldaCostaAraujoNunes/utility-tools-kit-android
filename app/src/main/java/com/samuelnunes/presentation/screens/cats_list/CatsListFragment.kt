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
import com.samuelnunes.domain.utils.Resource
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
        populateGifs()
    }

    private val concatAdapter = ConcatAdapter(catGifAdapter, breedListAdapter)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        binding.root.adapter = concatAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateGifs()
        populateBreedList()
    }

    private fun populateBreedList() {
        viewModel.getAllBreeds().observe(viewLifecycleOwner) { res ->
            when(res) {
                is Result.Loading -> showLoading()
                is Result.Empty -> hideLoading()
                is Result.Success -> {
                    hideLoading()
                    breedListAdapter.submitList(res.data)
                }
                is Result.Error -> {
                    hideLoading()
                    res.data.let { breedListAdapter.submitList(it) }
                    Snackbar.make(binding.root, res.exception.toUiText().value, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun populateGifs() {
        viewModel.getCatsGifs().observe(viewLifecycleOwner) { res ->
            when(res) {
                is Result.Loading -> showLoading()
                is Result.Empty -> hideLoading()
                is Result.Success -> {
                    hideLoading()
                    catGifAdapter.submitList(res.data)
                }
                is Result.Error -> {
                    hideLoading()
                    res.data.let { catGifAdapter.submitList(it) }
                    Snackbar.make(binding.root, res.exception.toUiText().value, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading() {
        Timber.d("Show Loading")
    }

    private fun hideLoading() {
        Timber.d("Hide Loading")
    }

}