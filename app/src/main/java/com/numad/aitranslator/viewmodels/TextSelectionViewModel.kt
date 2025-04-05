package com.numad.aitranslator.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numad.aitranslator.repositories.TranslatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextSelectionViewModel @Inject constructor(
    private val translatorRepository: TranslatorRepository
) : ViewModel() {
    private val _imageTexts = MutableStateFlow<List<String>>(emptyList())
    val imageTexts: StateFlow<List<String>> = _imageTexts

    private val _showError = MutableStateFlow(false)
    val showError: StateFlow<Boolean> = _showError

    fun resetError() {
        _showError.value = false
    }

    fun fetchTextFromImage(image: Bitmap) {
        viewModelScope.launch {
            val result = translatorRepository.fetchTextFromImage(image = image)

            if (result.success) {
                _imageTexts.value = result.texts
            } else {
                Log.e("TextSelectionViewModel", "Error fetching text from image: ${result.error}")
                _showError.value = true
            }
        }
    }
}