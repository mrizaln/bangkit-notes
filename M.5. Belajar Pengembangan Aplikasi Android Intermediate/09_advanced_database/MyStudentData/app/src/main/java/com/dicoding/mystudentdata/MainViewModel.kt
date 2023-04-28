package com.dicoding.mystudentdata

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.dicoding.mystudentdata.database.Student
import com.dicoding.mystudentdata.helper.SortType

class MainViewModel(private val studentRepository: StudentRepository) : ViewModel() {
    private val _sort = MutableLiveData<SortType>()

    init {
        _sort.value = SortType.ASCENDING
    }

    fun changeSortType(sortType: SortType) {
        _sort.value = sortType
    }

//    fun getAllStudent(): LiveData<List<Student>> =
//        _sort.switchMap { studentRepository.getAllStudent(it) }       // <<< RawQuery chapter

    fun getAllStudent(): LiveData<PagedList<Student>> =
        _sort.switchMap { studentRepository.getAllStudent(it) }

    fun getAllStudentAndUniversity() = studentRepository.getAllStudentAndUniversity()
    fun getAllUniversityAndStudent() = studentRepository.getAllUniversityAndStudent()
    fun getAllStudentWithCourse() = studentRepository.getAllStudentWithCourse()
}

class ViewModelFactory(private val repository: StudentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}