package com.example.jukang.data.Room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

@androidx.room.Database(entities = [historyRiwayat::class], version = 1, exportSchema = false)
abstract class Database:RoomDatabase() {
    abstract fun historyDao(): historycheckDAO

    companion object{
        @Volatile
        private var INSTANCE: Database? = null

        fun getDatabase(context: Context): Database {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    "history_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}

@androidx.room.Database(entities = [Alamat::class], version = 1, exportSchema = false)
abstract class AlamatDatabase:RoomDatabase(){
    abstract fun alamatdao():AlamatDao

    companion object{
        @Volatile
        private var INSTANCE: AlamatDatabase? = null

        fun getDatabase(context: Context): AlamatDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlamatDatabase::class.java,
                    "alamat_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


@androidx.room.Database(entities = [AlamatLengkap::class], version = 1, exportSchema = false)
abstract class AlamatLengkapDatabase:RoomDatabase(){

    abstract fun alamatLengkapDao():AlamatLengkapDao

    companion object{
        @Volatile
        private var INSTANCE: AlamatLengkapDatabase? = null

        fun getDatabase(context: Context): AlamatLengkapDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlamatLengkapDatabase::class.java,
                    "alamatLengkap_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}