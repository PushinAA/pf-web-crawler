package com.nri.pfcrawler.crawler

import com.nri.pfcrawler.model.Enchant
import com.nri.pfcrawler.utils.craftReqs
import com.nri.pfcrawler.utils.description
import com.nri.pfcrawler.utils.extractAura
import com.nri.pfcrawler.utils.extractCasterLevel
import com.nri.pfcrawler.utils.extractEnchantmentsPrice
import com.nri.pfcrawler.utils.extractFeats
import com.nri.pfcrawler.utils.extractPrice
import com.nri.pfcrawler.utils.extractSpecial
import com.nri.pfcrawler.utils.extractSpells
import com.nri.pfcrawler.utils.magicProps
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.extractIt
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.h1

class EnchantCrawler: Crawler<Enchant>() {
    override fun startingUrl(): String {
        return "https://www.d20pfsrd.com/magic-items/magic-armor/magic-armor-and-shield-special-abilities/"
    }

    override fun shouldVisit(link: String): Boolean {
        return link.startsWith("https://www.d20pfsrd.com/magic-items/magic-armor/magic-armor-and-shield-special-abilities")
    }

    override fun visit(link: String): Enchant? {
        val extracted = skrape(HttpFetcher) {
            request { url = link }
            extractIt<Enchant> { item ->
                htmlDocument {
                    item.type = "ARMOR"
                    val name = h1 { findFirst { text } }
                    item.name = name
                    val magicProps = magicProps()
                    val aura = magicProps.extractAura()
                    item.aura = aura
                    val marketCost = magicProps.extractPrice()
                    item.marketCost = marketCost
                    val enchantmentsPrice = magicProps.extractEnchantmentsPrice()
                    item.enchantmentsPrice = enchantmentsPrice
                    val casterLevel = magicProps.extractCasterLevel()
                    item.casterLevel = casterLevel
                    item.magicProps = magicProps
                    val description = description()
                    item.description = description
                    val craftReqs = craftReqs()
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
        return extracted
    }
}
