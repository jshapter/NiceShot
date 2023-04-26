package com.example.niceshot.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.niceshot.data.entities.Photo
import com.example.niceshot.data.entities.User

@Database(
    entities = [Photo::class, User::class],
    version = 1,
    exportSchema = false
)

abstract class NiceShotDatabase : RoomDatabase() {

    abstract fun dataDao(): DataDao

    companion object {
        @Volatile
        private var Instance: NiceShotDatabase? = null

        fun getDatabase(context: Context): NiceShotDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, NiceShotDatabase::class.java, "niceshot_database").allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {Instance = it}
            }
        }
    }
}