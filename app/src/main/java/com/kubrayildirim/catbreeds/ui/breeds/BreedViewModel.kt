package com.kubrayildirim.catbreeds.ui.breeds

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kubrayildirim.catbreeds.R
import com.kubrayildirim.catbreeds.data.model.Breed
import com.kubrayildirim.catbreeds.repository.BreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedViewModel @Inject constructor(
    private val breedRepository: BreedRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val breedEventChannel = Channel<BreedEvent>()
    val breedEvent = breedEventChannel.receiveAsFlow()

    fun saveBreed(breed: Breed) {
        viewModelScope.launch {
            breedRepository.insertBreed(breed)
            breedEventChannel.send(BreedEvent.ShowBreedSavedMessage(context.getString(R.string.added_to_favorites)))
        }
    }

    sealed class BreedEvent {
        data class ShowBreedSavedMessage(val message: String) : BreedEvent()
    }
}