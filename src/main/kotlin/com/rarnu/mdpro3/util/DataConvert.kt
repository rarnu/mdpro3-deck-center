package com.rarnu.mdpro3.util

object DataConvert {

    fun strToCardType(str: String): Long = when (str) {
        "pendulum" -> 0x1000000L
        "trap" -> 0x04L
        "spell" -> 0x02L
        "monster" -> 0x01L
        else -> 0L
    }

    fun strToAttribute(str: String): Int = when (str) {
        "divine" -> 0x40
        "dark" -> 0x20
        "light" -> 0x10
        "wind" -> 0x08
        "fire" -> 0x04
        "water" -> 0x02
        "earth" -> 0x01
        else -> 0
    }

    fun strToIcon(str: String): Long = when (str) {
        "counter" -> 0x100000L
        "field" -> 0x80000L
        "equip" -> 0x40000L
        "continuous" -> 0x20000L
        "quick-play" -> 0x10000L
        "ritual" -> 0x80L
        else -> 0L
    }

    fun strToSubType(str: String): Long = when (str) {
        "spsummon" -> 0x2000000L
        "link" -> 0x4000000L
        "pendulum" -> 0x1000000L
        "xyz" -> 0x800000L
        "synchro" -> 0x2000L
        "ritual" -> 0x80L
        "fusion" -> 0x40L
        "toon" -> 0x400000L
        "flip" -> 0x200000L
        "tuner" -> 0x1000L
        "gemini" -> 0x800L
        "union" -> 0x400L
        "spirit" -> 0x200L
        "effect" -> 0x20L
        "normal" -> 0x10L
        else -> 0L
    }

    fun strToRace(str: String): Long = when (str) {
        "galaxy" -> 0x80000000L
        "celestialKnight" -> 0x40000000L
        "omegaPsycho" -> 0x20000000L
        "hydragon" -> 0x10000000L
        "magicalKnight" -> 0x8000000L
        "cyborg" -> 0x4000000L
        "illusion" -> 0x2000000L
        "cyberse" -> 0x1000000L
        "wyrm" -> 0x800000L
        "creatorGod" -> 0x400000L
        "divineBeast" -> 0x200000L
        "psychic" -> 0x100000L
        "reptile" -> 0x80000L
        "seaSerpent" -> 0x40000L
        "fish" -> 0x20000L
        "dinosaur" -> 0x10000L
        "beastWarrior" -> 0x8000L
        "beast" -> 0x4000L
        "dragon" -> 0x2000L
        "thunder" -> 0x1000L
        "insect" -> 0x800L
        "plant" -> 0x400L
        "wingedBeast" -> 0x200L
        "rock" -> 0x100L
        "pyro" -> 0x80L
        "aqua" -> 0x40L
        "machine" -> 0x20L
        "zombie" -> 0x10L
        "fiend" -> 0x8L
        "fairy" -> 0x4L
        "spellcaster" -> 0x2L
        "warrior" -> 0x1L
        else -> 0L
    }

    fun strToMonsterType(str: String): Long = when (str) {
        "link" -> 0x4000000L
        "xyz" -> 0x800000L
        "token" -> 0x4000L
        "synchro" -> 0x2000L
        "ritual" -> 0x80L
        "fusion" -> 0x40L
        "effect" -> 0x20L
        "normal" -> 0x10L
        else -> 0L
    }
}