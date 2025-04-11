package com.numad.aitranslator

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * TEST OUTPUT: Pulled from device
 * ================================================================================================
 * Input | Target Lang | Firebase Output | Google Output | Similarity |
 * ================================================================================================
 * Hello | es | Hola | Hola | 100.00% |
 * Goodbye | fr | Au revoir | Au revoir | 100.00% |
 * I love you | de | Ich liebe dich | Ich liebe dich | 100.00% |
 * Where is the nearest train station? | fr | Où se trouve la gare la plus proche? | Où est la gare la plus proche ? | 77.78% |
 * I would like to order a coffee with milk and sugar. | es | Me gustaría pedir un café con leche y azúcar. | Me gustaría pedir un café con leche y azúcar. | 100.00% |
 * The weather today is quite nice, isn't it? | de | Das Wetter heute ist ziemlich nett, nicht wahr? | Das Wetter ist heute ziemlich schön, oder? | 55.32% |
 * Please make sure to lock the door when you leave. | it | Assicurati di bloccare la porta quando parti. | Assicurati di chiudere a chiave la porta quando esci. | 66.04% |
 * Learning a new language takes time and practice. | fr | Apprendre une nouvelle langue prend du temps et de la pratique. | Apprendre une nouvelle langue prend du temps et de la pratique. | 100.00% |
 * Can you recommend a good restaurant nearby? | ja | Can you recommend a good restaurant nearby? | 近くの良いレストランをおすすめしてもらえますか？ | 0.00% |
 * This book was far more interesting than I expected. | es | Este libro fue mucho más interesante de lo que esperaba. | Este libro fue mucho más interesante de lo que esperaba. | 100.00% |
 * Do not forget to water the plants while I'm away. | de | Vergessen Sie nicht, die Pflanzen zu gewässern, während ich weg bin. | Vergiss nicht, die Pflanzen zu gießen, während ich weg bin. | 80.88% |
 * Even small acts of kindness can have a big impact. | it | Anche piccoli atti di gentilezza possono avere un grande impatto. | Anche piccoli atti di gentilezza possono avere un grande impatto. | 100.00% |
 * */
@RunWith(AndroidJUnit4::class)
class FirebaseTranslationTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun runTranslationComparisons() {
        val testCases = listOf(
            Triple("Hello", "es", "Hola"),
            Triple("Goodbye", "fr", "Au revoir"),
            Triple("I love you", "de", "Ich liebe dich"),
            Triple("Where is the nearest train station?", "fr", "Où est la gare la plus proche ?"),
            Triple(
                "I would like to order a coffee with milk and sugar.",
                "es",
                "Me gustaría pedir un café con leche y azúcar."
            ),
            Triple(
                "The weather today is quite nice, isn't it?",
                "de",
                "Das Wetter ist heute ziemlich schön, oder?"
            ),
            Triple(
                "Please make sure to lock the door when you leave.",
                "it",
                "Assicurati di chiudere a chiave la porta quando esci."
            ),
            Triple(
                "Learning a new language takes time and practice.",
                "fr",
                "Apprendre une nouvelle langue prend du temps et de la pratique."
            ),
            Triple(
                "Can you recommend a good restaurant nearby?",
                "ja",
                "近くの良いレストランをおすすめしてもらえますか？"
            ),
            Triple(
                "This book was far more interesting than I expected.",
                "es",
                "Este libro fue mucho más interesante de lo que esperaba."
            ),
            Triple(
                "Do not forget to water the plants while I'm away.",
                "de",
                "Vergiss nicht, die Pflanzen zu gießen, während ich weg bin."
            ),
            Triple(
                "Even small acts of kindness can have a big impact.",
                "it",
                "Anche piccoli atti di gentilezza possono avere un grande impatto."
            )
        )

        val reportBuilder = StringBuilder()
        reportBuilder.appendLine("| Input | Target Lang | Firebase Output | Google Output | Similarity |")
        reportBuilder.appendLine("|-------|-------------|------------------|----------------|------------|")

        for ((inputText, targetLang, expectedOutput) in testCases) {
            val translated = runBlocking {
                translateTextFirebase(inputText, targetLang)
            }

            val similarity = computeLevenshteinSimilarity(translated, expectedOutput)

            reportBuilder.appendLine(
                "| $inputText | $targetLang | $translated | $expectedOutput | ${
                    "%.2f".format(
                        similarity * 100
                    )
                }% |"
            )
        }

        // Print the report
        println()
        println(reportBuilder.toString())
        println()
    }

    private suspend fun translateTextFirebase(text: String, targetLang: String): String =
        suspendCoroutine { cont ->
            val translationOptions = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(targetLang)
                .build()

            val translator = Translation.getClient(translationOptions)

            translator.downloadModelIfNeeded()
                .addOnSuccessListener {
                    translator.translate(text)
                        .addOnSuccessListener { translatedText ->
                            cont.resume(translatedText)
                        }
                        .addOnFailureListener { cont.resume("ERROR: ${it.localizedMessage}") }
                }
                .addOnFailureListener { cont.resume("ERROR: ${it.localizedMessage}") }
        }

    private fun computeLevenshteinSimilarity(a: String, b: String): Float {
        val maxLen = maxOf(a.length, b.length)
        if (maxLen == 0) return 1f
        val dist = levenshtein(a, b)
        return 1f - dist.toFloat() / maxLen
    }

    private fun levenshtein(lhs: String, rhs: String): Int {
        val lhsLen = lhs.length
        val rhsLen = rhs.length
        val dp = Array(lhsLen + 1) { IntArray(rhsLen + 1) }

        for (i in 0..lhsLen) dp[i][0] = i
        for (j in 0..rhsLen) dp[0][j] = j

        for (i in 1..lhsLen) {
            for (j in 1..rhsLen) {
                val cost = if (lhs[i - 1] == rhs[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,     // deletion
                    dp[i][j - 1] + 1,     // insertion
                    dp[i - 1][j - 1] + cost // substitution
                )
            }
        }
        return dp[lhsLen][rhsLen]
    }
}
