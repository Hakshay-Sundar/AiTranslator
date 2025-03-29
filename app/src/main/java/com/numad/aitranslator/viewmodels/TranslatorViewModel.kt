package com.numad.aitranslator.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numad.aitranslator.R
import com.numad.aitranslator.dao.TranslationResults
import com.numad.aitranslator.repositories.TranslatorRepository
import com.numad.aitranslator.utils.LanguageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslatorViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val translatorRepository: TranslatorRepository,
    private val languageUtils: LanguageUtils
) : ViewModel() {

    private val _languageFrom = MutableStateFlow<String?>(null)
    val languageFrom = _languageFrom.asStateFlow()

    private val _languageTo = MutableStateFlow<String?>(null)
    val languageTo = _languageTo.asStateFlow()

    private val _translatedText = MutableStateFlow("")
    val translatedText = _translatedText.asStateFlow()

    private val _languagesFromDetectionDictionary = MutableStateFlow(listOf<String>())
    val languagesFromDetectionDictionary = _languagesFromDetectionDictionary.asStateFlow()

    private val _triggerErrorAlert = MutableStateFlow(false)
    val triggerErrorAlert = _triggerErrorAlert.asStateFlow()

    fun translateText(text: String) {
        viewModelScope.launch {
            _translatedText.value = context.getString(R.string.translating)
            val sourceLanguage = languageUtils.getLanguageCode(
                _languageFrom.value,
                LanguageUtils.DETECTION_DICTIONARY
            )
            val targetLanguage = languageUtils.getLanguageCode(
                _languageTo.value,
                LanguageUtils.TRANSLATION_DICTIONARY
            )
            val translatedText =
                translatorRepository.translateText(text, sourceLanguage, targetLanguage)

            when (translatedText) {
                is TranslationResults.Success -> {
                    _translatedText.value = translatedText.translatedText
                }

                is TranslationResults.Error -> {
                    _translatedText.value = ""
                    _languageTo.value = null
                    _triggerErrorAlert.value = true
                }
            }
        }
    }

    fun resetErrorAlert() {
        _triggerErrorAlert.value = false
    }

    fun detectLanguage(text: String) {
        viewModelScope.launch {
            val detectedLanguage = translatorRepository.detectLanguage(text)
            if (detectedLanguage.isNotEmpty()) {
                _languageFrom.value = LanguageUtils.getLanguageName(detectedLanguage)
            }
        }
    }

    fun getLanguagesBasedOnDictionary(detectionType: Int) {
        viewModelScope.launch {
            val languages = languageUtils.getLanguageList(detectionType)
            _languagesFromDetectionDictionary.value = languages
        }
    }

    fun setLanguageSelected(language: String, detectionType: Int) {
        viewModelScope.launch {
            if (detectionType == languageUtils.DETECTION_DICTIONARY) {
                if (language.isEmpty()) {
                    _languageFrom.value = null
                    return@launch
                }
                _languageFrom.value = language
            } else if (detectionType == languageUtils.TRANSLATION_DICTIONARY) {
                if (language.isEmpty()) {
                    _languageTo.value = null
                    return@launch
                }
                _languageTo.value = language
            }
        }
    }

    fun reset() {
        _languageFrom.value = null
        _languageTo.value = null
        _translatedText.value = ""
        _triggerErrorAlert.value = false
    }
}