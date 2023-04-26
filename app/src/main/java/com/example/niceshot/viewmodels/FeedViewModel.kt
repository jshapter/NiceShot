package com.example.niceshot.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.niceshot.data.DataRepository
import com.example.niceshot.data.entities.Photo
import com.example.niceshot.data.entities.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FeedViewModel(dataRepository: DataRepository) : ViewModel() {

    private val rep = dataRepository

    fun returnUser (
        id: Int,
        photoRepository: DataRepository = rep
    ) : User {
        return photoRepository.getUserStream(id)
    }

    val feedUiState: StateFlow<FeedUiState> =
        dataRepository.getAllPhotosStream().map { FeedUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = FeedUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class FeedUiState(val photoList: List<Photo> = listOf())