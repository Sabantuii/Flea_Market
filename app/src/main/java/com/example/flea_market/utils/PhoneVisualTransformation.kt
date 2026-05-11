package com.example.flea_market.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // text — это только цифры, которые ввел пользователь (макс 10 штук)
        val trimmed = if (text.text.length >= 10) text.text.substring(0..9) else text.text
        var out = "+7 ("

        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 2) out += ") "
            if (i == 5) out += "-"
            if (i == 7) out += "-"
        }

        val phoneNumberOffsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 2) return offset + 4 // До "+7 ("
                if (offset <= 5) return offset + 6 // До ") "
                if (offset <= 7) return offset + 7 // До первого "-"
                if (offset <= 10) return offset + 8 // До второго "-"
                return 18 // Максимальная длина маски
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Это то, что уронило приложение. Добавляем жесткие границы:
                if (offset <= 4) return 0
                if (offset <= 7) return (offset - 4).coerceIn(0, text.length)
                if (offset <= 11) return (offset - 6).coerceIn(0, text.length)
                if (offset <= 13) return (offset - 7).coerceIn(0, text.length)
                if (offset <= 18) return (offset - 8).coerceIn(0, text.length)
                return text.length
            }
        }

        return TransformedText(AnnotatedString(out), phoneNumberOffsetMapping)
    }
}