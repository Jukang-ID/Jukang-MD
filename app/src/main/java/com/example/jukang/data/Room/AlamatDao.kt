package com.example.jukang.data.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AlamatDao {
    @Insert
    suspend fun insert(data: Alamat)

    @Query("SELECT * FROM alamat WHERE namaUser = :namaUser  Limit 1")
    suspend fun getAlamat(namaUser: String): Alamat

    @Query("SELECT * FROM alamat WHERE id = 0")
    suspend fun getAlamatID(): Alamat

    @Delete
    suspend fun delete(data: Alamat)

    @Update
    suspend fun update(data: Alamat)
}