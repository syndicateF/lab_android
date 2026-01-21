package com.fajrin222280062.kelas_c.kelascti2022

import com.google.gson.annotations.SerializedName

data class DataValue(
    @SerializedName("sukses") val sukses: Boolean? = null,
    @SerializedName("notif") val notif: String? = null,

    @SerializedName("idfakultas") var idFakultas: String? = null,
    @SerializedName("namafakultas") var namaFakultas: String? = null,

    @SerializedName("idprodi") var idProdi: String? = null,
    @SerializedName("namaprodi") var namaProdi: String? = null,

    @SerializedName("Nim") var nim: String? = null,
    @SerializedName("Nama") var nama: String? = null,
    @SerializedName("Kelas") var kelas: String? = null,
    @SerializedName("Nohp") var nohp: String? = null,
    @SerializedName("Jeniskelamin") var jeniskelamin: String? = null,
    @SerializedName("TempatLahir") var tempatLahir: String? = null,
    @SerializedName("TanggalLahir") var tanggalLahir: String? = null,
    @SerializedName("Alamat") var alamat: String? = null
)