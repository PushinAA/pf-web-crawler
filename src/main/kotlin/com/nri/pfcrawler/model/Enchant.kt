package com.nri.pfcrawler.model

data class Enchant(
    var name: String? = null,
    var marketCost: Int = 0,
    var craftCost: Int = 0,
    var enchantmentsPrice: Int = 0,
    var casterLevel: Int = 0,
    var aura: String? = null,
    var description: String? = null,
    var type: String? = null,
    var magicProps: String = "",
    var craftReqs: String="",
    var requirements: Requirement = Requirement()
)
