package com.numad.aitranslator.utils

object LanguageUtils {
    private val detectionLanguageDictionary = HashMap<String, String>()

    init {
        setupDetectionLanguageDictionary()
    }

    private fun setupDetectionLanguageDictionary() {
        detectionLanguageDictionary["af"] = "Afrikaans"
        detectionLanguageDictionary["am"] = "Amharic"
        detectionLanguageDictionary["ar"] = "Arabic"
        detectionLanguageDictionary["ar-Latn"] = "Arabic"
        detectionLanguageDictionary["az"] = "Azerbaijani"
        detectionLanguageDictionary["be"] = "Belarusian"
        detectionLanguageDictionary["bg"] = "Bulgarian"
        detectionLanguageDictionary["bg-Latn"] = "Bulgarian"
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
        detectionLanguageDictionary["el-Latn"] = "Greek"
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
        detectionLanguageDictionary["hi-Latn"] = "Hindi"
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
        detectionLanguageDictionary["ja-Latn"] = "Japanese"
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
        detectionLanguageDictionary["ru-Latn"] = "Russian"
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
        detectionLanguageDictionary["zh-Latn"] = "Chinese"
        detectionLanguageDictionary["zu"] = "Zulu"
    }

    fun getLanguageName(languageCode: String): String {
        return detectionLanguageDictionary[languageCode] ?: "Unknown"
    }

    fun getLanguageCode(languageName: String): String {
        for ((code, name) in detectionLanguageDictionary) {
            if (name == languageName) {
                return code
            }
        }
        return "Unknown"
    }

    fun getLanguageList(): List<String> {
        return detectionLanguageDictionary.values.toList()
    }

    fun getLanguageCodeList(): List<String> {
        return detectionLanguageDictionary.keys.toList()
    }
}
