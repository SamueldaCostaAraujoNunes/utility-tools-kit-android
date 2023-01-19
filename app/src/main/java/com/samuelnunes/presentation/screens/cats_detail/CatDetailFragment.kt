package com.samuelnunes.presentation.screens.cats_detail

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING
import com.samuelnunes.presentation.screens.MainActivity
import com.samuelnunes.utility_tool_kit.extensions.autoScroll
import com.samuelnunes.utils.databinding.FragmentCatDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CatDetailFragment : Fragment() {

    private val arguments by navArgs<CatDetailFragmentArgs>()
    private val viewModel: CatDetailViewModel by viewModels()
    private lateinit var binding: FragmentCatDetailBinding
    private val imageListAdapter: ImageListAdapter = ImageListAdapter()

    companion object {
        private const val timerDelay: Long = 3000
    }

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
        binding.apply {
            vpCats.adapter = imageListAdapter
            vpCats.autoScroll()
            dotsIndicator.attachTo(vpCats)
            scrollView.fullScroll(ScrollView.FOCUS_UP)
        }

        setupObservers()
        populateBreed()
    }

    private fun setupObservers() {
        viewModel.getCat(arguments.id)
    }

    private fun populateBreed() = viewModel.breed.observe(viewLifecycleOwner) {
        (activity as MainActivity).binding.toolbar.title = it.name
        binding.breed = it
        imageListAdapter.submitList(it.images)
    }
}