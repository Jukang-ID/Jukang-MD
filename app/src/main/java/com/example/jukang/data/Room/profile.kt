package com.example.jukang.data.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface profileDAO {
    @Insert
    suspend fun insertdata(data: profileLengkap)

    @Query("SELECT * FROM profile WHERE id_user = :idUser LIMIT 1")
    suspend fun checkProfile(idUser:String):profileLengkap

    @Update
    suspend fun updatedata(data: profileLengkap)
}