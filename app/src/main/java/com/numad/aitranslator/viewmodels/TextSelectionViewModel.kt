package com.numad.aitranslator.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numad.aitranslator.dao.GenericResponse
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

    fun fetchTextFromImage(image: Bitmap) {
        viewModelScope.launch {
            val result = translatorRepository.fetchTextFromImage(image = image)

            if (result.success) {
                _imageTexts.value = result.texts
            } else {
                // TODO: Need to handle this error somehow.
            }
        }
    }
}