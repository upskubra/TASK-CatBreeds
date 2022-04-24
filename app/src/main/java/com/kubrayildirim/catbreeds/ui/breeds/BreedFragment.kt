package com.kubrayildirim.catbreeds.ui.breeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.kubrayildirim.catbreeds.R
import com.kubrayildirim.catbreeds.databinding.FragmentBreedBinding
import com.kubrayildirim.catbreeds.util.loadImage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_breed.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BreedFragment : Fragment() {

    private var _binding: FragmentBreedBinding? = null
    private val binding get() = _binding!!
    private lateinit var breedViewModel: BreedViewModel
    private val args by navArgs<BreedFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        breedViewModel = ViewModelProvider(this)[BreedViewModel::class.java]

        binding.apply {
            val breed = args.breed

            cat_detail_back.setOnClickListener {
                val action = BreedFragmentDirections.actionBreedFragmentToBreedListFragment()
                Navigation.findNavController(it).navigate(action)
            }

            cat_detail_name.text = breed.name
            cat_detail_image.loadImage(breed.image?.url)
            cat_detail_description.text = breed.description
            cat_detail_origin.text = "Origin: ${breed.origin}"

            cat_detail_card.setOnClickListener {
                webView.visibility = View.VISIBLE
                webView.apply {
                    webViewClient = WebViewClient()
                    breed.wikipedia_url?.let {
                        loadUrl(breed.wikipedia_url)
                    }
                }
            }

            favButton.setOnClickListener {
                if (breed.image != null) {
                    breedViewModel.saveBreed(breed)
                } else {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.problem_in_add_favorites),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        observeBreedData()
    }

    private fun observeBreedData() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            breedViewModel.breedEvent.collect { event ->
                when (event) {
                    is BreedViewModel.BreedEvent.ShowBreedSavedMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}