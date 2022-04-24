package com.kubrayildirim.catbreeds.ui.breeds

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kubrayildirim.catbreeds.R
import com.kubrayildirim.catbreeds.adapter.BreedListAdapter
import com.kubrayildirim.catbreeds.data.model.Breed
import com.kubrayildirim.catbreeds.databinding.FragmentBreedListBinding
import com.kubrayildirim.catbreeds.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreedListFragment : Fragment(), BreedListAdapter.OnItemClickListener {

    private var _binding: FragmentBreedListBinding? = null
    private val binding get() = _binding!!
    private lateinit var breedListViewModel: BreedListViewModel
    private lateinit var breedListAdapter: BreedListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreedListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.show()
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        breedListViewModel = ViewModelProvider(this)[BreedListViewModel::class.java]
        breedListAdapter = BreedListAdapter(this)

        binding.apply {
            rvCatBreeds.adapter = breedListAdapter
        }

        observeCatBreeds()
    }

    private fun observeCatBreeds() {
        breedListViewModel.catBreedResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    it.data?.let { newsResponse ->
                        breedListAdapter.submitList(newsResponse.toList())
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    it.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        Log.e("BreedListFragment", message)
                    }
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onItemClick(breed: Breed) {
        val action =
            BreedListFragmentDirections.actionCatBreedListFragmentToBreedFragment(breed)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_fav -> {
                val action =
                    BreedListFragmentDirections.actionBreedListFragmentToSavedBreedFragment()
                findNavController().navigate(action)
                return true
            }
            R.id.menu_item_search -> {
                val action =
                    BreedListFragmentDirections.actionBreedListFragmentToSearchBreedsFragment()
                findNavController().navigate(action)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


}