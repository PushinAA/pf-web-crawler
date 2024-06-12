package com.nri.pfcrawler.crawler

import com.nri.pfcrawler.model.Item
import com.nri.pfcrawler.utils.craftReqs
import com.nri.pfcrawler.utils.description
import com.nri.pfcrawler.utils.extractAura
import com.nri.pfcrawler.utils.extractCasterLevel
import com.nri.pfcrawler.utils.extractFeats
import com.nri.pfcrawler.utils.extractPrice
import com.nri.pfcrawler.utils.extractSlot
import com.nri.pfcrawler.utils.extractSpecial
import com.nri.pfcrawler.utils.extractSpells
import com.nri.pfcrawler.utils.extractWeight
import com.nri.pfcrawler.utils.magicProps
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.extractIt
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.h1

class ItemCrawler: Crawler<Item>() {
    override fun startingUrl(): String {
        return "https://www.d20pfsrd.com/magic-items/wondrous-items/"
    }

    override fun shouldVisit(link: String): Boolean {
        return link.startsWith("https://www.d20pfsrd.com/magic-items/wondrous-items/a-b") &&
            !link.startsWith("https://www.d20pfsrd.com/magic-items/wondrous-items/#") &&
            link.endsWith("/")
    }

    override fun visit(link: String): Item? {
        val extracted = skrape(HttpFetcher) {
            request { url = link }
            extractIt<Item> { item ->
                htmlDocument {
                    item.itemType = "WONDROUS_ITEM"
                    val magicProps = magicProps()
                    item.magicProps = magicProps
                    val aura = magicProps.extractAura()
                    if (aura.startsWith("no aura")) {
                        return@htmlDocument
                    }
                    item.aura = aura
                    val casterLevel = magicProps.extractCasterLevel()
                    item.casterLevel = casterLevel
                    val slot = magicProps.extractSlot()
                    item.slot = slot
                    val weight = magicProps.extractWeight()
                    item.weight = weight
                    val price = magicProps.extractPrice()
                    item.marketCost = price
                    val name = h1 { findFirst { text } }
                    item.name = name
                    val craftReqs = craftReqs()
                    val description = description()
                    item.description = description
                    item.craftReqs = craftReqs
                    val craftCost = craftReqs.extractPrice()
                    item.craftCost = craftCost
                    val feats = craftReqs.extractFeats()
                    item.requirements.featNames = feats
                    val spells = craftReqs.extractSpells()
                    item.requirements.spells = spells
                    val special = craftReqs.extractSpecial()
                    item.requirements.addPrep = special
                }
            }
        }
        if (extracted.name.isNotBlank()) {
            return extracted
        }
        return null
    }
}
