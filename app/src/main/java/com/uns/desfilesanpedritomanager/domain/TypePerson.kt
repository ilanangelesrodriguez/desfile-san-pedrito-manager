package com.uns.desfilesanpedritomanager.domain

enum class TypePerson(val value: String, val displayName: String) {
    STUDENT("student", "Estudiante"),
    PROFESSOR("professor", "Profesor"),
    COMMUNITY_MEMBER("community", "Miembro de la Comunidad"),
    VOLUNTEER("volunteer", "Voluntario");

    companion object {
        fun fromString(value: String): TypePerson {
            return values().find { it.value == value } ?: STUDENT
        }
    }
}
