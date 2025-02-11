package com.example.service

import buku.BukuOuterClass
import buku.BukuServiceGrpc.BukuServiceImplBase
import com.example.model.Buku
import com.example.repository.BukuRepository
import io.grpc.stub.StreamObserver
import io.quarkus.grpc.GrpcService
import jakarta.inject.Inject
import jakarta.inject.Singleton
import jakarta.enterprise.context.ApplicationScoped
import java.util.concurrent.Executors
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.asCoroutineDispatcher
import jakarta.transaction.Transactional
import kotlinx.coroutines.runBlocking

@GrpcService
@Singleton
class BukuService : BukuServiceImplBase() {

    @Inject
    lateinit var repository: BukuRepository

    private val dispatcher: CoroutineDispatcher = Executors.newFixedThreadPool(4).asCoroutineDispatcher()

    @Transactional
    override fun createBuku(request: BukuOuterClass.Buku, responseObserver: StreamObserver<BukuOuterClass.Buku>) {
        runBlocking {
            val buku = Buku().apply {
                judul = request.judul
                penulis = request.penulis
                penerbit = request.penerbit
                tahunTerbit = request.tahunTerbit
                harga = request.harga
                stok = request.stok
            }
            repository.persist(buku)
            val response = request.toBuilder().setIdBuku(buku.id!!.toInt()).build()
            responseObserver.onNext(response)
            responseObserver.onCompleted()
        }
    }

    @Transactional
    override fun readBuku(request: BukuOuterClass.IdRequest, responseObserver: StreamObserver<BukuOuterClass.Buku>) {
        runBlocking {
            val buku = repository.findById(request.id.toLong())
            if (buku != null) {
                val response = BukuOuterClass.Buku.newBuilder()
                    .setIdBuku(buku.id!!.toInt())
                    .setJudul(buku.judul)
                    .setPenulis(buku.penulis)
                    .setPenerbit(buku.penerbit)
                    .setTahunTerbit(buku.tahunTerbit)
                    .setHarga(buku.harga)
                    .setStok(buku.stok)
                    .build()
                responseObserver.onNext(response)
            }
            responseObserver.onCompleted()
        }
    }

    @Transactional
    override fun updateBuku(request: BukuOuterClass.Buku, responseObserver: StreamObserver<BukuOuterClass.Buku>) {
        runBlocking {
            val buku = repository.findById(request.idBuku.toLong())
            if (buku != null) {
                buku.judul = request.judul
                buku.penulis = request.penulis
                buku.penerbit = request.penerbit
                buku.tahunTerbit = request.tahunTerbit
                buku.harga = request.harga
                buku.stok = request.stok
                repository.persist(buku)

                responseObserver.onNext(request)
            }
            responseObserver.onCompleted()
        }
    }

    @Transactional
    override fun deleteBuku(request: BukuOuterClass.IdRequest, responseObserver: StreamObserver<BukuOuterClass.Empty>) {
        runBlocking {
            repository.deleteById(request.id.toLong())
            responseObserver.onNext(BukuOuterClass.Empty.getDefaultInstance())
            responseObserver.onCompleted()
        }
    }

    @Transactional
    override fun listBuku(request: BukuOuterClass.Empty, responseObserver: StreamObserver<BukuOuterClass.BukuList>) {
        runBlocking {  
            val bukuList = repository.listAll().map {
                BukuOuterClass.Buku.newBuilder()
                    .setIdBuku(it.id!!.toInt())
                    .setJudul(it.judul)
                    .setPenulis(it.penulis)
                    .setPenerbit(it.penerbit)
                    .setTahunTerbit(it.tahunTerbit)
                    .setHarga(it.harga)
                    .setStok(it.stok)
                    .build()
            }

            val response = BukuOuterClass.BukuList.newBuilder().addAllBuku(bukuList).build()
            responseObserver.onNext(response)
            responseObserver.onCompleted()
        }
    }

}