package com.example.model

import io.quarkus.hibernate.orm.panache.PanacheEntity
import jakarta.persistence.Entity

@Entity
class Buku : PanacheEntity() {
    lateinit var judul: String
    lateinit var penulis: String
    lateinit var penerbit: String
    var tahunTerbit: Int = 0
    lateinit var harga: String
    var stok: Int = 0
}