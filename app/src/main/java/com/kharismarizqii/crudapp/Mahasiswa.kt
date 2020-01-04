package com.kharismarizqii.crudapp

data class Mahasiswa(
    val id : String,
    val nama: String,
    val alamat : String
){
    constructor(): this("","",""){

    }
}