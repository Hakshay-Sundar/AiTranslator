package com.numad.aitranslator.dao

data class TextSelectionResponse(
    var success: Boolean,
    var texts: List<String>,
    var error: String?
) {
    constructor() : this(false, emptyList(), "")

    fun setSuccess(success: Boolean): TextSelectionResponse {
        this.success = success
        return this
    }

    fun setTexts(texts: List<String>): TextSelectionResponse {
        this.texts = texts
        return this
    }

    fun setError(error: String?): TextSelectionResponse {
        this.error = error
        return this
    }
}
