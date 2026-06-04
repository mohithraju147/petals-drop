package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Customer::class, DeliveryRecord::class, PaymentRecord::class], version = 1, exportSchema = false)
abstract class PetalDropDatabase : RoomDatabase() {
    abstract fun petalDropDao(): PetalDropDao

    companion object {
        @Volatile
        private var INSTANCE: PetalDropDatabase? = null

        fun getDatabase(context: Context): PetalDropDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PetalDropDatabase::class.java,
                    "petaldrop_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
