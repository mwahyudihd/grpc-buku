package com.example.api

import com.example.model.Buku
import com.example.repository.BukuRepository
import com.example.util.EncryptionUtil
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("/buku")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
class BukuResource {

    @Inject
    lateinit var repository: BukuRepository

    @GET
    fun listBuku(): Response {
        val bukuList = repository.listAll().map { buku ->
            buku.penulis = EncryptionUtil.decrypt(buku.penulis) // Dekripsi sebelum dikirim ke frontend
            buku
        }
        return Response.ok(bukuList).build()
    }

    @GET
    @Path("/{id}")
    fun getBuku(@PathParam("id") id: Long): Response {
        val buku = repository.findById(id) ?: return Response.status(Response.Status.NOT_FOUND).build()
        buku.penulis = EncryptionUtil.decrypt(buku.penulis) // Dekripsi sebelum dikirim
        return Response.ok(buku).build()
    }

    @POST
    @Transactional
    fun createBuku(buku: Buku): Response {
        buku.penulis = EncryptionUtil.encrypt(buku.penulis) // Enkripsi sebelum disimpan
        repository.persist(buku)
        return Response.status(Response.Status.CREATED).entity(buku).build()
    }

    @PUT
    @Path("/{id}")
    @Transactional
    fun updateBuku(@PathParam("id") id: Long, buku: Buku): Response {
        val existingBuku = repository.findById(id) ?: return Response.status(Response.Status.NOT_FOUND).build()
        existingBuku.judul = buku.judul
        existingBuku.penulis = EncryptionUtil.encrypt(buku.penulis) // Enkripsi sebelum update
        existingBuku.penerbit = buku.penerbit
        existingBuku.tahunTerbit = buku.tahunTerbit
        existingBuku.harga = buku.harga
        existingBuku.stok = buku.stok
        repository.persist(existingBuku)
        return Response.ok(existingBuku).build()
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    fun deleteBuku(@PathParam("id") id: Long): Response {
        if (repository.deleteById(id)) {
            return Response.noContent().build()
        }
        return Response.status(Response.Status.NOT_FOUND).build()
    }
}
