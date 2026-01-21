package com.fajrin222280062.kelas_c.kelascti2022

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @GET("pertemuan4.php?laman=daftarfakultas")
    fun getDaftarFakultas(): Call<List<DataValue>>

    @GET("pertemuan4.php?laman=daftarprodi")
    fun getDaftarProdi(@Query("idfakultas") idFakultas: String): Call<List<DataValue>>

    @GET("pertemuan4.php?laman=daftarmahasiswa")
    fun getDaftarMahasiswa(
        @Query("idFakultas") idFakultas: String,
        @Query("idProdi") idProdi: String
    ): Call<List<DataValue>>

    @FormUrlEncoded
    // import retrofit2.http.FormUrlEncoded
    @POST("pertemuan4.php?laman=tambahmahasiswa")
    fun tambahMahasiswa(
        @Field("Nim") nim: String,
        @Field("Nama") nama: String,
        @Field("Kelas") kelas: String,
        @Field("Nohp") nohp: String,
        @Field("Jeniskelamin") jeniskelamin: String,
        @Field("TempatLahir") tempatLahir: String,
        @Field("TanggalLahir") tanggalLahir: String,
        @Field("Alamat") alamat: String,
        @Field("idFakultas") idFakultas: String,
        @Field("idProdi") idProdi: String
    ): Call<DataValue>

    @FormUrlEncoded
    @POST("pertemuan4.php?laman=ubahmahasiswa")
    fun ubahMahasiswa(
        @Field("Nim") nim: String,
        @Field("Nama") nama: String,
        @Field("Kelas") kelas: String,
        @Field("Nohp") nohp: String,
        @Field("Jeniskelamin") jeniskelamin: String,
        @Field("TempatLahir") tempatLahir: String,
        @Field("TanggalLahir") tanggalLahir: String,
        @Field("Alamat") alamat: String,
        @Field("idFakultas") idFakultas: String,
        @Field("idProdi") idProdi: String
    ): Call<DataValue>

    @FormUrlEncoded
    @POST("pertemuan4.php?laman=hapusmahasiswa")
    fun hapusMahasiswa(@Field("Nim") nim: String): Call<DataValue>
}
