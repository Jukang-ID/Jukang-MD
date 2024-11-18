package com.example.jukang.data.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AlamatLengkapDao {
    @Insert
    suspend fun insert(data: AlamatLengkap)

    @Query("SELECT * FROM alamatlengkap WHERE namaUser = :namaUser  Limit 1")
    suspend fun getAlamat(namaUser: String): AlamatLengkap

    @Update
    suspend fun update(data: AlamatLengkap)
}