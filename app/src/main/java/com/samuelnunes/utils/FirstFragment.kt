package com.samuelnunes.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.samuelnunes.utility_tool_kit.binding.fadeAnimationText
import com.samuelnunes.utility_tool_kit.binding.setDebounceListener
import com.samuelnunes.utility_tool_kit.binding.text
import com.samuelnunes.utility_tool_kit.debounce.DebounceTextWatcher
import com.samuelnunes.utility_tool_kit.extensions.*
import com.samuelnunes.utility_tool_kit.utils.UiText
import com.samuelnunes.utils.databinding.FragmentFirstBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        binding.teste.addTextChangedListener(object: DebounceTextWatcher(){
            override fun runAfterDelayed(query: String) {
                binding.textviewFirst.fadeAnimationText(UiText.StringResource(R.string.teste, query))
            }
        })
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setDebounceListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}