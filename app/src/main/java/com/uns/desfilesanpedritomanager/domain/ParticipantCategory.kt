package com.uns.desfilesanpedritomanager.domain

enum class ParticipantCategory(val value: String, val displayName: String, val color: String) {
    DANCER("dancer", "Danzante", "#FF6B6B"),
    MUSICIAN("musician", "Músico", "#4ECDC4"),
    ORGANIZER("organizer", "Organizador", "#45B7D1"),
    SPECTATOR("spectator", "Espectador", "#96CEB4"),
    VENDOR("vendor", "Vendedor", "#FFEAA7"),
    SECURITY("security", "Seguridad", "#DDA0DD");

    companion object {
        fun fromString(value: String): ParticipantCategory {
            return values().find { it.value == value } ?: SPECTATOR
        }
    }
}
