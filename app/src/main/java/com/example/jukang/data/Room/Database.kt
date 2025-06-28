package com.example.jukang.data.Room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

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


@androidx.room.Database(entities = [AlamatLengkap::class], version = 2, exportSchema = false)
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
                )
                    .addMigrations(MIGRATION_1_2) // ← Tambahkan migrasi
                    .build()
                INSTANCE = instance
                instance
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE alamatlengkap ADD COLUMN lat TEXT")
                database.execSQL("ALTER TABLE alamatlengkap ADD COLUMN lon TEXT")
            }
        }
    }

}

@androidx.room.Database(entities = [profileLengkap::class], version = 1, exportSchema = false)
abstract class profileDatabase:RoomDatabase(){
    abstract fun profiledao():profileDAO

    companion object{
        @Volatile
        private var INSTANCE: profileDatabase? = null

        fun getDatabase(Context: Context): profileDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    Context.applicationContext,
                    profileDatabase::class.java,
                    "profile_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}