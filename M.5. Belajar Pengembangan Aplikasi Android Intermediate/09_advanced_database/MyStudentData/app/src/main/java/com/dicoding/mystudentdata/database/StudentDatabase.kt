package com.dicoding.mystudentdata.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dicoding.mystudentdata.helper.InitialDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Student::class, University::class, Course::class, CourseStudentCrossRef::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = StudentDatabase.MyAutoMigration::class)
    ],
    exportSchema = true
)
abstract class StudentDatabase : RoomDatabase() {
    @RenameColumn(tableName = "Student", fromColumnName = "graduate", toColumnName = "isGraduate")
    class MyAutoMigration : AutoMigrationSpec

    abstract fun studentDao(): StudentDao

    companion object {
        @Volatile
        private var INSTANCE: StudentDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context, applicationScope: CoroutineScope): StudentDatabase {
            val callback = object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    INSTANCE?.let { database ->
                        applicationScope.launch {
                            val studentDao = database.studentDao()
                            studentDao.insertStudent(InitialDataSource.getStudents())
                            studentDao.insertUniversity(InitialDataSource.getUniversities())
                            studentDao.insertCourse(InitialDataSource.getCourses())
                            studentDao.insertCourseStudentCrossRef(InitialDataSource.getCourseStudentRelation())
                        }
                    }
                }
            }

            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext, StudentDatabase::class.java, "student_database"
                ).run {
                    fallbackToDestructiveMigration()
//                    addCallback(callback)     // 1. using callback
                    createFromAsset("student_database.db")      // 2. using asset
                    build()
                }.also {
                    INSTANCE = it
                }
            }
        }
    }
}