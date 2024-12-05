package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Item::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class InventoryDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext, // Usar applicationContext para evitar fugas de memoria
                    InventoryDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration() // Permitir destrucción de datos en caso de migración incompatible
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
