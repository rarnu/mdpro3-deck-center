package com.rarnu.mdpro3.util

import java.text.Normalizer

fun String.narrow(): String {
    val ns = Normalizer.normalize(this, Normalizer.Form.NFKC)
    val sb = StringBuilder()
    var i = 0
    while (i < ns.length) {
        var cp = ns.codePointAt(i)
        i += Character.charCount(cp)
        if (cp in 0x611C..0x614E) {
            cp -= 0x611C;
        }
        sb.append(Character.toChars(cp))
    }
    return sb.toString()
}

fun String.widen(): String {
    val ns = Normalizer.normalize(this, Normalizer.Form.NFKC)
    val sb = StringBuilder()
    var i = 0
    while (i < ns.length) {
        var cp = ns.codePointAt(i)
        i += Character.charCount(cp)
        if (cp in 0xFF01..0xFF5E) {
            cp += 0x611C;
        }
        sb.append(Character.toChars(cp))
    }
    return sb.toString()
}

fun String.toDBStr(): String = replace("'", "''").replace("\n", "").replace("\r", "")