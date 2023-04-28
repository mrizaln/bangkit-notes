package com.dicoding.mystudentdata

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dicoding.mystudentdata.database.Student
import com.dicoding.mystudentdata.database.StudentDao
import com.dicoding.mystudentdata.helper.InitialDataSource
import com.dicoding.mystudentdata.helper.SortType
import com.dicoding.mystudentdata.helper.SortUtils

class StudentRepository(private val studentDao: StudentDao) {
//    fun getAllStudent(sortType: SortType): LiveData<List<Student>> {
//        val query = SortUtils.getSortedQuery(sortType)
//        return studentDao.getAllStudent(query)            // <<< RawQuery chapter

    fun getAllStudent(sortType: SortType): LiveData<PagedList<Student>> {
        val query = SortUtils.getSortedQuery(sortType)
        val students = studentDao.getAllStudent(query)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(30)
            .setPageSize(10)
            .build()
        return LivePagedListBuilder(students, config).build()                // <<< Paging2 chapter
    }

    fun getAllStudentAndUniversity() = studentDao.getAllStudentAndUniversity()
    fun getAllUniversityAndStudent() = studentDao.getAllUniversityAndStudent()
    fun getAllStudentWithCourse() = studentDao.getAllStudentWithCourse()
}