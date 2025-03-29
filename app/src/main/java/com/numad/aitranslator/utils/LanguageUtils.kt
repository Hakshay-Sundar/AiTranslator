package com.numad.aitranslator.utils

import android.util.Log

object LanguageUtils {
    private val detectionLanguageDictionary = HashMap<String, String>()
    private val translationLanguageDictionary = HashMap<String, String>()
    const val DETECTION_DICTIONARY = 1;
    const val TRANSLATION_DICTIONARY = 2;

    init {
        setupDetectionLanguageDictionary()
        setupTranslationLanguageDictionary()
    }

    private fun setupDetectionLanguageDictionary() {
        detectionLanguageDictionary["af"] = "Afrikaans"
        detectionLanguageDictionary["am"] = "Amharic"
        detectionLanguageDictionary["ar"] = "Arabic"
        detectionLanguageDictionary["az"] = "Azerbaijani"
        detectionLanguageDictionary["be"] = "Belarusian"
        detectionLanguageDictionary["bg"] = "Bulgarian"
        detectionLanguageDictionary["bn"] = "Bengali"
        detectionLanguageDictionary["bs"] = "Bosnian"
        detectionLanguageDictionary["ca"] = "Catalan"
        detectionLanguageDictionary["ceb"] = "Cebuano"
        detectionLanguageDictionary["co"] = "Corsican"
        detectionLanguageDictionary["cs"] = "Czech"
        detectionLanguageDictionary["cy"] = "Welsh"
        detectionLanguageDictionary["da"] = "Danish"
        detectionLanguageDictionary["de"] = "German"
        detectionLanguageDictionary["el"] = "Greek"
        detectionLanguageDictionary["en"] = "English"
        detectionLanguageDictionary["eo"] = "Esperanto"
        detectionLanguageDictionary["es"] = "Spanish"
        detectionLanguageDictionary["et"] = "Estonian"
        detectionLanguageDictionary["eu"] = "Basque"
        detectionLanguageDictionary["fa"] = "Persian"
        detectionLanguageDictionary["fi"] = "Finnish"
        detectionLanguageDictionary["fil"] = "Filipino"
        detectionLanguageDictionary["fr"] = "French"
        detectionLanguageDictionary["fy"] = "Western Frisian"
        detectionLanguageDictionary["ga"] = "Irish"
        detectionLanguageDictionary["gd"] = "Scots Gaelic"
        detectionLanguageDictionary["gl"] = "Galician"
        detectionLanguageDictionary["gu"] = "Gujarati"
        detectionLanguageDictionary["ha"] = "Hausa"
        detectionLanguageDictionary["haw"] = "Hawaiian"
        detectionLanguageDictionary["he"] = "Hebrew"
        detectionLanguageDictionary["hi"] = "Hindi"
        detectionLanguageDictionary["hmn"] = "Hmong"
        detectionLanguageDictionary["hr"] = "Croatian"
        detectionLanguageDictionary["ht"] = "Haitian"
        detectionLanguageDictionary["hu"] = "Hungarian"
        detectionLanguageDictionary["hy"] = "Armenian"
        detectionLanguageDictionary["id"] = "Indonesian"
        detectionLanguageDictionary["ig"] = "Igbo"
        detectionLanguageDictionary["is"] = "Icelandic"
        detectionLanguageDictionary["it"] = "Italian"
        detectionLanguageDictionary["ja"] = "Japanese"
        detectionLanguageDictionary["jv"] = "Javanese"
        detectionLanguageDictionary["ka"] = "Georgian"
        detectionLanguageDictionary["kk"] = "Kazakh"
        detectionLanguageDictionary["km"] = "Khmer"
        detectionLanguageDictionary["kn"] = "Kannada"
        detectionLanguageDictionary["ko"] = "Korean"
        detectionLanguageDictionary["ku"] = "Kurdish"
        detectionLanguageDictionary["ky"] = "Kyrgyz"
        detectionLanguageDictionary["la"] = "Latin"
        detectionLanguageDictionary["lb"] = "Luxembourgish"
        detectionLanguageDictionary["lo"] = "Lao"
        detectionLanguageDictionary["lt"] = "Lithuanian"
        detectionLanguageDictionary["lv"] = "Latvian"
        detectionLanguageDictionary["mg"] = "Malagasy"
        detectionLanguageDictionary["mi"] = "Maori"
        detectionLanguageDictionary["mk"] = "Macedonian"
        detectionLanguageDictionary["ml"] = "Malayalam"
        detectionLanguageDictionary["mn"] = "Mongolian"
        detectionLanguageDictionary["mr"] = "Marathi"
        detectionLanguageDictionary["ms"] = "Malay"
        detectionLanguageDictionary["mt"] = "Maltese"
        detectionLanguageDictionary["my"] = "Burmese"
        detectionLanguageDictionary["ne"] = "Nepali"
        detectionLanguageDictionary["nl"] = "Dutch"
        detectionLanguageDictionary["no"] = "Norwegian"
        detectionLanguageDictionary["ny"] = "Nyanja"
        detectionLanguageDictionary["pa"] = "Punjabi"
        detectionLanguageDictionary["pl"] = "Polish"
        detectionLanguageDictionary["ps"] = "Pashto"
        detectionLanguageDictionary["pt"] = "Portuguese"
        detectionLanguageDictionary["ro"] = "Romanian"
        detectionLanguageDictionary["ru"] = "Russian"
        detectionLanguageDictionary["sd"] = "Sindhi"
        detectionLanguageDictionary["si"] = "Sinhala"
        detectionLanguageDictionary["sk"] = "Slovak"
        detectionLanguageDictionary["sl"] = "Slovenian"
        detectionLanguageDictionary["sm"] = "Samoan"
        detectionLanguageDictionary["sn"] = "Shona"
        detectionLanguageDictionary["so"] = "Somali"
        detectionLanguageDictionary["sq"] = "Albanian"
        detectionLanguageDictionary["sr"] = "Serbian"
        detectionLanguageDictionary["st"] = "Sesotho"
        detectionLanguageDictionary["su"] = "Sundanese"
        detectionLanguageDictionary["sv"] = "Swedish"
        detectionLanguageDictionary["sw"] = "Swahili"
        detectionLanguageDictionary["ta"] = "Tamil"
        detectionLanguageDictionary["te"] = "Telugu"
        detectionLanguageDictionary["tg"] = "Tajik"
        detectionLanguageDictionary["th"] = "Thai"
        detectionLanguageDictionary["tr"] = "Turkish"
        detectionLanguageDictionary["uk"] = "Ukrainian"
        detectionLanguageDictionary["ur"] = "Urdu"
        detectionLanguageDictionary["uz"] = "Uzbek"
        detectionLanguageDictionary["vi"] = "Vietnamese"
        detectionLanguageDictionary["xh"] = "Xhosa"
        detectionLanguageDictionary["yi"] = "Yiddish"
        detectionLanguageDictionary["yo"] = "Yoruba"
        detectionLanguageDictionary["zh"] = "Chinese"
        detectionLanguageDictionary["zu"] = "Zulu"
    }

    private fun setupTranslationLanguageDictionary() {
        translationLanguageDictionary["af"] = "Afrikaans"
        translationLanguageDictionary["ar"] = "Arabic"
        translationLanguageDictionary["be"] = "Belarusian"
        translationLanguageDictionary["bg"] = "Bulgarian"
        translationLanguageDictionary["bn"] = "Bengali"
        translationLanguageDictionary["ca"] = "Catalan"
        translationLanguageDictionary["cs"] = "Czech"
        translationLanguageDictionary["cy"] = "Welsh"
        translationLanguageDictionary["da"] = "Danish"
        translationLanguageDictionary["de"] = "German"
        translationLanguageDictionary["el"] = "Greek"
        translationLanguageDictionary["en"] = "English"
        translationLanguageDictionary["eo"] = "Esperanto"
        translationLanguageDictionary["es"] = "Spanish"
        translationLanguageDictionary["et"] = "Estonian"
        translationLanguageDictionary["fa"] = "Persian"
        translationLanguageDictionary["fi"] = "Finnish"
        translationLanguageDictionary["fr"] = "French"
        translationLanguageDictionary["ga"] = "Irish"
        translationLanguageDictionary["gl"] = "Galician"
        translationLanguageDictionary["gu"] = "Gujarati"
        translationLanguageDictionary["he"] = "Hebrew"
        translationLanguageDictionary["hi"] = "Hindi"
        translationLanguageDictionary["hr"] = "Croatian"
        translationLanguageDictionary["ht"] = "Haitian"
        translationLanguageDictionary["hu"] = "Hungarian"
        translationLanguageDictionary["id"] = "Indonesian"
        translationLanguageDictionary["is"] = "Icelandic"
        translationLanguageDictionary["it"] = "Italian"
        translationLanguageDictionary["ja"] = "Japanese"
        translationLanguageDictionary["ka"] = "Georgian"
        translationLanguageDictionary["kn"] = "Kannada"
        translationLanguageDictionary["ko"] = "Korean"
        translationLanguageDictionary["lt"] = "Lithuanian"
        translationLanguageDictionary["lv"] = "Latvian"
        translationLanguageDictionary["mk"] = "Macedonian"
        translationLanguageDictionary["mr"] = "Marathi"
        translationLanguageDictionary["ms"] = "Malay"
        translationLanguageDictionary["mt"] = "Maltese"
        translationLanguageDictionary["nl"] = "Dutch"
        translationLanguageDictionary["no"] = "Norwegian"
        translationLanguageDictionary["pl"] = "Polish"
        translationLanguageDictionary["pt"] = "Portuguese"
        translationLanguageDictionary["ro"] = "Romanian"
        translationLanguageDictionary["ru"] = "Russian"
        translationLanguageDictionary["sk"] = "Slovak"
        translationLanguageDictionary["sl"] = "Slovenian"
        translationLanguageDictionary["sq"] = "Albanian"
        translationLanguageDictionary["sv"] = "Swedish"
        translationLanguageDictionary["sw"] = "Swahili"
        translationLanguageDictionary["ta"] = "Tamil"
        translationLanguageDictionary["te"] = "Telugu"
        translationLanguageDictionary["th"] = "Thai"
        translationLanguageDictionary["tl"] = "Tagalog"
        translationLanguageDictionary["tr"] = "Turkish"
        translationLanguageDictionary["uk"] = "Ukrainian"
        translationLanguageDictionary["ur"] = "Urdu"
        translationLanguageDictionary["vi"] = "Vietnamese"
        translationLanguageDictionary["zh"] = "Chinese"
    }

    fun getLanguageName(languageCode: String): String {
        return detectionLanguageDictionary[languageCode] ?: "Unknown"
    }

    /**
     * A function to get the language code from the language name.
     * @param languageName The name of the language.
     * @param dictionaryType This utility file handles a detection dictionary and a translation dictionary.
     * You must provide details on which dictionary to use.
     *
     * @return The language code of the language. "Unknown" if the language is not found.
     * In case something were to go wrong, and no language Name were to be provided, the default language is English.
     * */
    fun getLanguageCode(languageName: String?, dictionaryType: Int): String {
        if (languageName == null) {
            return "en"
        }

        val dictionary = when (dictionaryType) {
            DETECTION_DICTIONARY -> {
                detectionLanguageDictionary
            }

            TRANSLATION_DICTIONARY -> {
                translationLanguageDictionary
            }

            else -> {
                Log.e("LanguageUtils", "Invalid dictionary type: $dictionaryType")
                return "Unknown"
            }
        }

        for ((code, name) in dictionary) {
            if (name == languageName) {
                return code
            }
        }
        return "Unknown"
    }

    /**
     * A function to get the list of languages available in the dictionary based on the scenario.
     * @param dictionaryType This utility file handles a detection dictionary and a translation dictionary.
     * Based on the scenario, the list of languages will be returned.
     *
     * @return The list of languages available in the dictionary. An empty list if the dictionary type is invalid.
     * */
    fun getLanguageList(dictionaryType: Int): List<String> {
        when (dictionaryType) {
            DETECTION_DICTIONARY -> {
                return detectionLanguageDictionary.values.toList().sorted()
            }

            TRANSLATION_DICTIONARY -> {
                return translationLanguageDictionary.values.toList().sorted()
            }

            else -> {
                Log.e("LanguageUtils", "Invalid dictionary type: $dictionaryType")
                return emptyList()
            }
        }
    }

    /**
     * A function to get the list of language codes available in the dictionary based on the scenario.
     * @param dictionaryType This utility file handles a detection dictionary and a translation dictionary.
     * Based on the scenario, the list of language codes for the available languages will be returned.
     *
     * @return The list of language codes available in the dictionary. An empty list if the dictionary type is invalid.
     * */
    fun getLanguageCodeList(dictionaryType: Int): List<String> {
        when (dictionaryType) {
            DETECTION_DICTIONARY -> {
                return detectionLanguageDictionary.keys.toList()
            }

            TRANSLATION_DICTIONARY -> {
                return translationLanguageDictionary.keys.toList()
            }

            else -> {
                Log.e("LanguageUtils", "Invalid dictionary type: $dictionaryType")
                return emptyList()
            }
        }
    }
}
