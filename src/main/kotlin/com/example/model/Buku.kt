package com.example.model

import io.quarkus.hibernate.orm.panache.PanacheEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
class Buku : PanacheEntity() {
    var judul: String = ""

    @Column(columnDefinition = "TEXT") // Pastikan cukup besar untuk data terenkripsi
    var penulis: String = ""

    var penerbit: String = ""
    var tahunTerbit: Int = 0
    var harga: String = ""
    var stok: Int = 0
}
