package com.numad.aitranslator.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numad.aitranslator.repositories.TranslatorRepository
import com.numad.aitranslator.utils.LanguageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslatorViewModel @Inject constructor(
    private val translatorRepository: TranslatorRepository
) : ViewModel() {

    private val _languageFrom = MutableStateFlow<String?>(null)
    val languageFrom = _languageFrom.asStateFlow()

    private val _languageTo = MutableStateFlow<String?>(null)
    val languageTo = _languageTo.asStateFlow()

    private val _translatedText = MutableStateFlow("")
    val translatedText = _translatedText.asStateFlow()

    fun translateText(text: String) {
        viewModelScope.launch {
            _translatedText.value = "Translating..."
            val translatedText = translatorRepository.translateText(text)
            _translatedText.value = translatedText
        }
    }

    fun detectLanguage(text: String) {
        viewModelScope.launch {
            val detectedLanguage = translatorRepository.detectLanguage(text)
            if(detectedLanguage.isNotEmpty()) {
                _languageFrom.value = LanguageUtils.getLanguageName(detectedLanguage)
            }
        }
    }
}