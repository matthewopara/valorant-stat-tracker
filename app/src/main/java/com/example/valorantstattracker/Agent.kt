package com.example.valorantstattracker

object Agent {
    const val BRIMSTONE = "Brimstone"
    const val VIPER = "Viper"
    const val OMEN = "Omen"
    const val KILLJOY = "Killjoy"
    const val CYPHER = "Cypher"
    const val SOVA = "Sova"
    const val SAGE = "Sage"
    const val PHOENIX = "Phoenix"
    const val JETT = "Jett"
    const val REYNA = "Reyna"
    const val RAZE = "Raze"
    const val BREACH = "Breach"
    const val SKYE = "Skye"

    private val avatars = mapOf(BRIMSTONE to R.drawable.brimstone_avatar, VIPER to R.drawable.viper_avatar,
            OMEN to R.drawable.omen_avatar, KILLJOY to R.drawable.killjoy_avatar,
            CYPHER to R.drawable.cypher_avatar, SOVA to R.drawable.sova_avatar,
            SAGE to R.drawable.sage_avatar, PHOENIX to R.drawable.phoenix_avatar,
            JETT to R.drawable.jett_avatar, REYNA to R.drawable.reyna_avatar,
            RAZE to R.drawable.raze_avatar, BREACH to R.drawable.breach_avatar,
            SKYE to R.drawable.skye_avatar)

    fun getImageResource(agentName: String): Int? {
        return avatars[agentName]
    }
}