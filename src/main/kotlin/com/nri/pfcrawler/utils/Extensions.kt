package com.nri.pfcrawler.utils

import it.skrape.selects.CssSelectable
import it.skrape.selects.ElementNotFoundException
import it.skrape.selects.html5.p
import it.skrape.selects.text

fun CssSelectable.magicProps(): String {
    var magicPropsStr: String
    try {
        magicPropsStr = selection("div:has(p.divider) > p:not(:empty)") { findFirst { text } }
        if (magicPropsStr.isBlank() || magicPropsStr.equals("DESCRIPTION", true)) {
            magicPropsStr = selection("div:has(p.divider) > p:not(:empty)") { findSecond { text } }
        }
    } catch (e: ElementNotFoundException) {
        magicPropsStr = try {
            selection("div:has(h4) > p:not(:empty)") { findFirst { text } }
        } catch (e: ElementNotFoundException) {
            selection("div:has(p:contains(DESCRIPTION)) > p:not(:empty)") { findFirst { text } }
        }
    }
    return magicPropsStr
}

fun String.extractAura(): String {
    return try {
        split(";").map { it.trim() }
            .first { it.startsWith("Aura") }
            .removePrefix("Aura")
            .trim()
    } catch (e: NoSuchElementException) {
        ""
    }
}

fun String.extractCasterLevel(): Int {
    return try {
        split(";").map { it.trim() }
            .first { it.startsWith("CL") }
            .removePrefix("CL")
            .trim()
            .removeSuffix("st")
            .removeSuffix("nd")
            .removeSuffix("rd")
            .removeSuffix("th")
            .toInt()
    } catch (e: NumberFormatException) {
        0
    } catch (e: NoSuchElementException) {
        0
    }
}

fun String.extractSlot(): String {
    return try {
        split(";").map { it.trim() }
            .first { it.startsWith("Slot") }
            .removePrefix("Slot")
            .trim()
    } catch (e: NoSuchElementException) {
        ""
    }
}

fun String.extractWeight(): Float {
    return try {
        split(";").map { it.trim() }
            .first { it.startsWith("Weight") }
            .removePrefix("Weight")
            .removeSuffix("lb.")
            .removeSuffix("lbs.")
            .trim()
            .toFloat()
    } catch (e: NumberFormatException) {
        0f
    } catch (e: NoSuchElementException) {
        0f
    }
}

fun String.extractPrice(): Int {
    return try {
        split(";").map { it.trim() }
            .first { it.startsWith("Price") || it.startsWith("Cost") || it.startsWith("Gold") }
            .removePrefix("Price")
            .removePrefix("Cost")
            .removePrefix("Gold")
            .trim()
            .removePrefix("+")
            .removeSuffix(".")
            .removeSuffix("gp")
            .trim()
            .replace(",", "")
            .toInt()
            .times(100)
    } catch (e: NumberFormatException) {
        0
    } catch (e: NoSuchElementException) {
        0
    }
}

fun String.extractEnchantmentsPrice(): Int {
    return try {
        split(";").map { it.trim() }
            .first { it.startsWith("Price") || it.startsWith("Cost") || it.startsWith("Gold") }
            .removePrefix("Price")
            .removePrefix("Cost")
            .removePrefix("Gold")
            .trim()
            .removePrefix("+")
            .trim()
            .takeIf { it.endsWith("bonus", true) }
            ?.removeSuffix("bonus")
            ?.trim()
            ?.toInt() ?: 0
    } catch (e: NumberFormatException) {
        0
    } catch (e: NoSuchElementException) {
        0
    }
}

fun CssSelectable.description(): String {
    var description = ""
    try {
        selection(".divider ~ p:not(.divider, p:last-of-type),.divider ~ ul") {
            findAll {
                description = this.text
            }
        }
    } catch (e: ElementNotFoundException) {
        try {
            selection("div.breadcrumbs ~ p:not(.divider, p:last-of-type, p:first-of-type),.divider ~ ul") {
                findAll {
                    description = this.text
                }
            }
        } catch (e: ElementNotFoundException) {
            try {
                selection("h4 ~ p:not(h4, p:last-of-type),.divider ~ ul") {
                    findAll {
                        description = this.text
                    }
                }
            } catch (e: ElementNotFoundException) {
                try {
                    selection("p:contains(DESCRIPTION) ~ p:not(p:contains(CONSTRUCTION REQUIREMENTS), p:last-of-type),.divider ~ ul") {
                        findAll {
                            description = this.text
                        }
                    }
                } catch (e: ElementNotFoundException) {
                    return ""
                }
            }
        }
    }
    return description
}

fun CssSelectable.craftReqs(): String {
    var craftReqs = ""
    try {
        craftReqs = p(".divider:contains(CONSTRUCTION REQUIREMENTS) + p") {
            findFirst { text }
        }
    } catch (e: ElementNotFoundException) {
        try {
            craftReqs = p("h4:contains(CONSTRUCTION REQUIREMENTS) + p") {
                findFirst { text }
            }
        } catch (e: ElementNotFoundException) {
            try {
                craftReqs = p("p:contains(CONSTRUCTION REQUIREMENTS) + p") {
                    findFirst { text }
                }
            } catch (e: ElementNotFoundException) {
                return craftReqs
            }
        }
    }
    return craftReqs
}

fun String.extractFeats(): List<String> {
    return try {
        split(";").map { it.trim() }
            .first { it.startsWith("Feats") }
            .removePrefix("Feats")
            .trim()
            .split(",")
            .map { it.trim() }
    } catch (e: NoSuchElementException) {
        split(";").map { it.trim() }[0]
            .split(",")
            .map { it.trim() }
    }
}

fun String.extractSpells(): List<String> {
    return try {
        split(";").map { it.trim() }
            .first { it.startsWith("Spells") }
            .removePrefix("Spells")
            .trim()
            .split(",")
            .map { it.trim() }
    } catch (e: NoSuchElementException) {
        listOf()
    }
}

fun String.extractSpecial(): String {
    return try {
        split(";").map { it.trim() }
            .first { it.startsWith("Special") }
            .removePrefix("Special")
            .trim()
    } catch (e: NoSuchElementException) {
        ""
    }
}
