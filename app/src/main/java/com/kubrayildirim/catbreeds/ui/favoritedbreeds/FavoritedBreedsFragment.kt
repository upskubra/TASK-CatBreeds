package com.kubrayildirim.catbreeds.ui.favoritedbreeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kubrayildirim.catbreeds.R
import com.kubrayildirim.catbreeds.adapter.BreedListAdapter
import com.kubrayildirim.catbreeds.data.model.Breed
import com.kubrayildirim.catbreeds.databinding.FragmentFavoritedBreedsBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FavoritedBreedsFragment : Fragment(),
    BreedListAdapter.OnItemClickListener {

    private var _binding: FragmentFavoritedBreedsBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoritedBreedViewModel: FavoritedBreedViewModel
    private lateinit var breedListAdapter: BreedListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritedBreedsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritedBreedViewModel = ViewModelProvider(this)[FavoritedBreedViewModel::class.java]
        breedListAdapter = BreedListAdapter(this)

        binding.apply {
            rvSavedBreeds.apply {
                adapter = breedListAdapter
                setHasFixedSize(true)
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val breed = breedListAdapter.currentList[viewHolder.adapterPosition]
                    favoritedBreedViewModel.onBreedSwiped(breed)
                }
            }).attachToRecyclerView(rvSavedBreeds)
        }

        getAllBreeds()
        observeSavedBreeds()
    }

    private fun getAllBreeds() {
        favoritedBreedViewModel.getAllBreeds().observe(viewLifecycleOwner) {
            breedListAdapter.submitList(it)
        }
    }

    private fun observeSavedBreeds() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            favoritedBreedViewModel.savedBreedEvent.collect { event ->
                when (event) {
                    is FavoritedBreedViewModel.SavedBreedEvent.ShowUndoDeleteBreedMessage -> {
                        Snackbar.make(
                            requireView(),
                            requireContext().getString(R.string.removed_favorites),
                            Snackbar.LENGTH_LONG
                        ).setAction("Geri al") {
                            favoritedBreedViewModel.onUndoClick(event.breed)
                        }.show()
                    }
                }
            }
        }
    }

    override fun onItemClick(breed: Breed) {
        val action = FavoritedBreedsFragmentDirections.actionSavedBreedsFragmentToBreedFragment(breed)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}