package com.nri.pfcrawler.model

data class Requirement(
    var casterLevel: Int = 0,
    var addPrep: String = "",
    var skills: String = "",
    var craftSkill: String = "",
    var difficultClass: Int = 0,
    var featNames: List<String> = listOf(),
    var spells: List<String> = listOf()
)
