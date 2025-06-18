package com.uns.desfilesanpedritomanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uns.desfilesanpedritomanager.data.InMemoryPersonRepository
import com.uns.desfilesanpedritomanager.domain.ParticipantCategory
import com.uns.desfilesanpedritomanager.domain.Person
import com.uns.desfilesanpedritomanager.domain.Statistics
import com.uns.desfilesanpedritomanager.domain.TypePerson
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PersonViewModel(
    private val repository: InMemoryPersonRepository
) : ViewModel() {

    // Estados principales
    private val _allPersons = MutableStateFlow<List<Person>>(emptyList())
    val allPersons = _allPersons.asStateFlow()

    private val _filteredPersons = MutableStateFlow<List<Person>>(emptyList())
    val filteredPersons = _filteredPersons.asStateFlow()

    private val _statistics = MutableStateFlow(Statistics())
    val statistics = _statistics.asStateFlow()

    private val _uiState = MutableStateFlow(PersonUiState())
    val uiState = _uiState.asStateFlow()

    // Filtros y ordenamiento
    private val _currentTypeFilter = MutableStateFlow<TypePerson?>(null)
    val currentTypeFilter = _currentTypeFilter.asStateFlow()

    private val _currentCategoryFilter = MutableStateFlow<ParticipantCategory?>(null)
    val currentCategoryFilter = _currentCategoryFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    init {
        // Cargar datos iniciales
        viewModelScope.launch {
            _allPersons.value = repository.persons.value
            _filteredPersons.value = _allPersons.value
            updateStatistics()

            // Observar cambios en el repositorio
            repository.persons.collect { persons ->
                _allPersons.value = persons
                applyFilters()
                updateStatistics()
            }
        }
    }

    // Operaciones CRUD
    fun addPerson(person: Person) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            repository.addPerson(person)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = "Participante registrado exitosamente"
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }

    fun deletePerson(id: Int) {
        viewModelScope.launch {
            repository.deletePerson(id)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        message = "Participante eliminado exitosamente"
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message
                    )
                }
        }
    }

    fun togglePersonStatus(id: Int) {
        viewModelScope.launch {
            repository.togglePersonStatus(id)
        }
    }

    // Filtros y búsqueda
    fun setTypeFilter(type: TypePerson?) {
        _currentTypeFilter.value = type
    }

    fun setCategoryFilter(category: ParticipantCategory?) {
        _currentCategoryFilter.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearFilters() {
        _currentTypeFilter.value = null
        _currentCategoryFilter.value = null
        _searchQuery.value = ""
    }

    // Ordenamiento
    fun sortPersons(sortOption: SortOption) {
        val currentList = _filteredPersons.value
        val sortedList = when (sortOption) {
            SortOption.NAME_ASC -> currentList.sortedBy { it.name }
            SortOption.NAME_DESC -> currentList.sortedByDescending { it.name }
            SortOption.LASTNAME_ASC -> currentList.sortedBy { it.lastName }
            SortOption.LASTNAME_DESC -> currentList.sortedByDescending { it.lastName }
            SortOption.AGE_ASC -> currentList.sortedBy { it.age }
            SortOption.AGE_DESC -> currentList.sortedByDescending { it.age }
            SortOption.REGISTRATION_DATE_ASC -> currentList.sortedBy { it.registrationDate }
            SortOption.REGISTRATION_DATE_DESC -> currentList.sortedByDescending { it.registrationDate }
        }
        _filteredPersons.value = sortedList
    }

    // Métodos privados
    private fun filterPersons(
        persons: List<Person>,
        typeFilter: TypePerson?,
        categoryFilter: ParticipantCategory?,
        query: String
    ): List<Person> {
        return persons.filter { person ->
            val matchesType = typeFilter == null || person.type == typeFilter
            val matchesCategory = categoryFilter == null || person.category == categoryFilter
            val matchesQuery = query.isBlank() ||
                    person.fullName.contains(query, ignoreCase = true) ||
                    person.email.contains(query, ignoreCase = true) ||
                    person.phone.contains(query, ignoreCase = true)

            matchesType && matchesCategory && matchesQuery
        }
    }

    private fun applyFilters() {
        val persons = allPersons.value
        val filtered = filterPersons(
            persons,
            _currentTypeFilter.value,
            _currentCategoryFilter.value,
            _searchQuery.value
        )
        _filteredPersons.value = filtered
    }

    private fun updateStatistics() {
        _statistics.value = repository.getStatistics()
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }
}

// Estados y enums auxiliares
data class PersonUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null
)

enum class SortOption(val displayName: String) {
    NAME_ASC("Nombre (A-Z)"),
    NAME_DESC("Nombre (Z-A)"),
    LASTNAME_ASC("Apellido (A-Z)"),
    LASTNAME_DESC("Apellido (Z-A)"),
    AGE_ASC("Edad (Menor a Mayor)"),
    AGE_DESC("Edad (Mayor a Menor)"),
    REGISTRATION_DATE_ASC("Fecha de Registro (Antigua)"),
    REGISTRATION_DATE_DESC("Fecha de Registro (Reciente)")
}