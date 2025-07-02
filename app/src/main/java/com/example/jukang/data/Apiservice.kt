package com.example.jukang.data

import ORSResponse
import RouteRequest
import com.example.jukang.data.response.BeritaResponse
import com.example.jukang.data.response.DetailTukang
import com.example.jukang.data.response.DetailTukangResponse
import com.example.jukang.data.response.History
import com.example.jukang.data.response.Login
import com.example.jukang.data.response.OrderMidtransResponse
import com.example.jukang.data.response.Orm
import com.example.jukang.data.response.Payment
import com.example.jukang.data.response.PaymentOrderRequest
import com.example.jukang.data.response.PendaftaranReq
import com.example.jukang.data.response.PendaftaranResponse
import com.example.jukang.data.response.Register
import com.example.jukang.data.response.Transaksi
import com.example.jukang.data.response.TransaksiData
import com.example.jukang.data.response.TransaksiItem
import com.example.jukang.data.response.Tukang
import com.example.jukang.data.response.TukangDomisili
import com.example.jukang.data.response.TukangReq
import com.example.jukang.data.response.UpdateStatusReq
import com.example.jukang.data.response.UpdateStatusTransaksiResponse
import com.example.jukang.data.response.UploadResponse
import com.example.jukang.data.response.Uploadfoto
import com.example.jukang.data.response.UserByEmail
import com.example.jukang.data.response.loginRequest
import com.example.jukang.data.response.paymentReq
import com.example.jukang.data.response.registerRequest
import com.example.jukang.data.response.requestTukang
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface Apiservice {
    @GET("cnn/terbaru")
    suspend fun getNews(): BeritaResponse
}

interface ApiService2 {
    @POST("register")
    fun register(
        @Body registerReq: registerRequest
    ): Call<Register>

    @GET("tukangbylokasi")
    suspend fun getTukangByLokasi(
        @Query("domisili") domisili: String
    ): TukangDomisili

    @GET("register")
    suspend fun getUserByEmail(
        @Query("username") username: String
    ): UserByEmail

    @GET("register")
    fun getRegister(): Call<Register>

    @POST("users/login")
    fun login(
        @Body loginReq: loginRequest
    ): Call<Login>

    @GET("tukang")
    suspend fun getTukang(
        @Query("domisili") domisili: String?=null,
        @Query("booked") booked: Boolean? = null
    ): Tukang

    @GET("detailtukang/{id}")
    suspend fun getDetailTukang(
        @Path("id") id: String
    ): DetailTukangResponse

    @GET("tukang/pesan/{id}")
    suspend fun getPEsananTukang(
        @Path("id") id: String
    ): TransaksiData


    @POST("transaksi/buat")
    fun getTokenMidtrans(
        @Body transaksi: PaymentOrderRequest
    ):Call<OrderMidtransResponse>

    @POST("addtransaksi")
    fun addtransaksi(
        @Body transaksi: paymentReq
    ): Call<Payment>

    @POST("tukang/{id}")
    fun updateTukang(
        @Body tukang: TukangReq,
        @Path("id") idTukang: String
    ): Call<Tukang>

    @POST("search")
    fun search(
        @Body search: requestTukang
    ): Call<TukangDomisili>

    @GET("transaksi/{id}")
    suspend fun getTransaksibyId(
        @Path("id") id: String
    ): TransaksiItem

    @POST("/pendaftaran")
    fun addPEndaftaran(
        @Body pendaftaran: PendaftaranReq
    ): Call<PendaftaranResponse>

    @GET("riwayat/{id}")
    suspend fun getTransaksi(
        @Path("id") idUser: String
    ): History

    @Multipart
    @POST("upload")
    fun uploadPhoto(
        @Part photo: MultipartBody.Part
    ): Call<UploadResponse> // Ganti sesuai response-mu

    @POST("transaksi/update")
    fun updateTransaksiStatus(
        @Body transaksi: UpdateStatusReq
    ): Call<UpdateStatusTransaksiResponse>
}

interface ApiService3 {
    @Headers("Authorization: ")
    @POST("v2/directions/driving-car")
    fun getRoute(@Body body: RouteRequest): Call<Orm>
}