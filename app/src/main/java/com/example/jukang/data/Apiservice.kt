package com.example.jukang.data

import ORSResponse
import RouteRequest
import com.example.jukang.data.response.BeritaResponse
import com.example.jukang.data.response.DetailTukang
import com.example.jukang.data.response.History
import com.example.jukang.data.response.Login
import com.example.jukang.data.response.Orm
import com.example.jukang.data.response.Payment
import com.example.jukang.data.response.Register
import com.example.jukang.data.response.Transaksi
import com.example.jukang.data.response.Tukang
import com.example.jukang.data.response.TukangReq
import com.example.jukang.data.response.loginRequest
import com.example.jukang.data.response.paymentReq
import com.example.jukang.data.response.registerRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface Apiservice {
    @GET("cnn/terbaru")
    suspend fun getNews(): BeritaResponse
}

interface ApiService2 {
    @POST("register")
    fun register(
        @Body registerReq : registerRequest
    ): Call<Register>

    @GET("register")
    fun getRegister(): Call<Register>

    @POST("users/login")
    fun login(
        @Body loginReq : loginRequest
    ): Call<Login>

    @GET("tukang")
    suspend fun getTukang(): Tukang

    @GET("detailtukang/{id}")
    suspend fun getDetailTukang(
        @Path("id") id:String
    ):DetailTukang

    @POST("addtransaksi")
     fun addtransaksi(
        @Body transaksi: paymentReq
    ): Call<Payment>

     @POST("tukang/{id}")
     fun updateTukang(
         @Body tukang: TukangReq,
         @Path("id") idTukang:String
     ): Call<Tukang>

     @GET("riwayat/{id}")
        suspend fun getTransaksi(
            @Path("id") idUser:String
        ): History
}

interface ApiService3 {
    @Headers("Authorization: 5b3ce3597851110001cf6248b9ad4c9761324324805eeef615700410")
    @POST("v2/directions/driving-car")
    fun getRoute(@Body body: RouteRequest): Call<Orm>
}