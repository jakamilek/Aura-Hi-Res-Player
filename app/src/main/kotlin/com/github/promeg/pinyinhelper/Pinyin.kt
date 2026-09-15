package com.github.promeg.pinyinhelper

import android.icu.text.Transliterator
import java.text.Normalizer

/**
 * Local compatibility implementation for the tiny subset of TinyPinyin used by
 * Aura. It uses Android's bundled ICU transliterator instead of an external
 * TinyPinyin/JitPack dependency.
 */
object Pinyin {
    private val transliterator by lazy {
        Transliterator.getInstance("Han-Latin")
    }

    fun toPinyin(char: Char): String {
        val transliterated = transliterator.transliterate(char.toString())
        return Normalizer.normalize(transliterated, Normalizer.Form.NFD)
            .replace(Regex("\\p{M}+"), "")
            .replace(Regex("[^A-Za-z]"), "")
            .lowercase()
    }
}
