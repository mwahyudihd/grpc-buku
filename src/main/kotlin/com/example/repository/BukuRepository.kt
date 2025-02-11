package com.example.repository

import com.example.model.Buku
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class BukuRepository : PanacheRepository<Buku>