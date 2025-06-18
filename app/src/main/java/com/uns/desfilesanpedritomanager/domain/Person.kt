package com.uns.desfilesanpedritomanager.domain

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Person(
    val id: Int = 0,
    val name: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val age: Int,
    val address: String,
    val type: TypePerson,
    val category: ParticipantCategory,
    val registrationDate: LocalDateTime = LocalDateTime.now(),
    val isActive: Boolean = true
) {
    val fullName: String
        get() = "$name $lastName"

    val formattedRegistrationDate: String
        get() = registrationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
}