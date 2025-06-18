package com.uns.desfilesanpedritomanager.domain

data class Statistics(
    val totalParticipants: Int = 0,
    val activeParticipants: Int = 0,
    val participantsByType: Map<TypePerson, Int> = emptyMap(),
    val participantsByCategory: Map<ParticipantCategory, Int> = emptyMap(),
    val averageAge: Double = 0.0,
    val ageRanges: Map<String, Int> = emptyMap(),
    val recentRegistrations: Int = 0
)