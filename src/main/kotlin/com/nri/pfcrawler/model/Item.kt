package com.nri.pfcrawler.model

data class Item(
    var name: String = "",
    var description: String = "",
    var marketCost: Int = 0,
    var craftCost: Int = 0,
    var casterLevel: Int = 0,
    var aura: String = "",
    var weight: Float = 0f,
    var slot: String = "",
    var itemType: String = "",
    var magicProps: String = "",
    var craftReqs: String="",
    var requirements: Requirement = Requirement()
)
