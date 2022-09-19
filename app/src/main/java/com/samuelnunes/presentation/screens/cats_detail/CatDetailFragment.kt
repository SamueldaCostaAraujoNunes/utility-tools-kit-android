package com.samuelnunes.presentation.screens.cats_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.samuelnunes.utils.databinding.FragmentCatDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CatDetailFragment : Fragment() {

    private val arguments by navArgs<CatDetailFragmentArgs>()
    private val viewModel: CatDetailViewModel by viewModels()
    private lateinit var binding: FragmentCatDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCatDetailBinding.inflate(inflater, container, false).apply {
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        populateBreed()
    }

    private fun setupObservers() {
        viewModel.getCat(arguments.id)
    }

    private fun populateBreed() = viewModel.breed.observe(viewLifecycleOwner) {
        binding.textoDoMeio.text = it.name
    }
}