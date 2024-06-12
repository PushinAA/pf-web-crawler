package com.nri.pfcrawler.crawler

import com.fasterxml.jackson.databind.ObjectMapper
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.eachLink
import it.skrape.selects.html5.a
import java.io.File
import java.io.FileWriter

abstract class Crawler<T> {
    fun start() {
        val links = skrape(HttpFetcher) {
            request { url = startingUrl() }
            response {
                htmlDocument {
                    a {
                        findAll {
                            eachLink
                        }
                    }
                }
            }
        }
        val items = mutableListOf<T>()
        var counter = 0f
        val shouldVisitLinks: MutableMap<String, String> = mutableMapOf()
        val checked: MutableMap<String, Boolean> = mutableMapOf()
        links.forEach { (text, link) ->
            if (shouldVisit(link) && !checked.getOrDefault(link, false)) {
                shouldVisitLinks[text] = link
                checked[link] = true
            }
        }
        shouldVisitLinks.forEach { (text, link) ->
            val item = visit(link)
            counter++
            if (item != null) {
                items.add(item)
            }
            val percent: Float = (counter / shouldVisitLinks.size) * 100
            println("$percent% --> $text")
        }
        val objectMapper = ObjectMapper()
        val json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(items)
        val file = File("result.json")
        val writer = FileWriter(file)
        writer.write(json)
        writer.close()
    }

    abstract fun shouldVisit(link: String): Boolean

    abstract fun visit(link: String): T?

    abstract fun startingUrl(): String
}
