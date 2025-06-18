package com.uns.desfilesanpedritomanager.data

import com.uns.desfilesanpedritomanager.domain.ParticipantCategory
import com.uns.desfilesanpedritomanager.domain.Person
import com.uns.desfilesanpedritomanager.domain.Statistics
import com.uns.desfilesanpedritomanager.domain.TypePerson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class InMemoryPersonRepository {

    private val _persons = MutableStateFlow<List<Person>>(emptyList())
    val persons: StateFlow<List<Person>> = _persons.asStateFlow()

    private var nextId = 1

    init {
        // Datos de ejemplo para demostración
        addSampleData()
    }

    private fun addSampleData() {
        val samplePersons = listOf(
            Person(
                id = nextId++,
                name = "María",
                lastName = "González López",
                email = "maria.gonzalez@email.com",
                phone = "555-0101",
                age = 25,
                address = "Calle Principal 123",
                type = TypePerson.STUDENT,
                category = ParticipantCategory.DANCER,
                registrationDate = LocalDateTime.now().minusDays(2)
            ),
            Person(
                id = nextId++,
                name = "Carlos",
                lastName = "Rodríguez Pérez",
                email = "carlos.rodriguez@email.com",
                phone = "555-0102",
                age = 35,
                address = "Avenida Central 456",
                type = TypePerson.PROFESSOR,
                category = ParticipantCategory.MUSICIAN,
                registrationDate = LocalDateTime.now().minusDays(1)
            ),
            Person(
                id = nextId++,
                name = "Ana",
                lastName = "Martínez Silva",
                email = "ana.martinez@email.com",
                phone = "555-0103",
                age = 28,
                address = "Plaza Mayor 789",
                type = TypePerson.COMMUNITY_MEMBER,
                category = ParticipantCategory.ORGANIZER,
                registrationDate = LocalDateTime.now().minusHours(5)
            )
        )
        _persons.value = samplePersons
    }

    suspend fun addPerson(person: Person): Result<Person> {
        return try {
            // Verificar si ya existe una persona con el mismo email
            val existingPerson = _persons.value.find { it.email == person.email }
            if (existingPerson != null) {
                return Result.failure(Exception("Ya existe una persona registrada con este email"))
            }

            val newPerson = person.copy(id = nextId++)
            _persons.value = _persons.value + newPerson
            Result.success(newPerson)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePerson(person: Person): Result<Person> {
        return try {
            val currentList = _persons.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == person.id }
            if (index != -1) {
                currentList[index] = person
                _persons.value = currentList
                Result.success(person)
            } else {
                Result.failure(Exception("Persona no encontrada"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePerson(id: Int): Result<Boolean> {
        return try {
            val currentList = _persons.value.toMutableList()
            val removed = currentList.removeIf { it.id == id }
            if (removed) {
                _persons.value = currentList
                Result.success(true)
            } else {
                Result.failure(Exception("Persona no encontrada"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun togglePersonStatus(id: Int): Result<Person> {
        return try {
            val currentList = _persons.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == id }
            if (index != -1) {
                val updatedPerson = currentList[index].copy(isActive = !currentList[index].isActive)
                currentList[index] = updatedPerson
                _persons.value = currentList
                Result.success(updatedPerson)
            } else {
                Result.failure(Exception("Persona no encontrada"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getStatistics(): Statistics {
        val persons = _persons.value
        val activePersons = persons.filter { it.isActive }

        return Statistics(
            totalParticipants = persons.size,
            activeParticipants = activePersons.size,
            participantsByType = persons.groupingBy { it.type }.eachCount(),
            participantsByCategory = persons.groupingBy { it.category }.eachCount(),
            averageAge = if (persons.isNotEmpty()) persons.map { it.age }.average() else 0.0,
            ageRanges = getAgeRanges(persons),
            recentRegistrations = persons.count {
                ChronoUnit.HOURS.between(it.registrationDate, LocalDateTime.now()) <= 24
            }
        )
    }

    private fun getAgeRanges(persons: List<Person>): Map<String, Int> {
        return persons.groupBy { person ->
            when (person.age) {
                in 0..17 -> "0-17"
                in 18..25 -> "18-25"
                in 26..35 -> "26-35"
                in 36..50 -> "36-50"
                else -> "50+"
            }
        }.mapValues { it.value.size }
    }
}
