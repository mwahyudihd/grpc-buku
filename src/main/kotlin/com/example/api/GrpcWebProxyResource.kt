package com.example.api

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import org.jboss.logging.Logger

@Path("/grpc-proxy")
class GrpcWebProxyResource {

    private val log: Logger = Logger.getLogger(GrpcWebProxyResource::class.java)

    @POST
    @Path("/{service}/{method}")
    @Produces("application/grpc-web+proto")
    @Consumes("application/grpc-web+proto")
    fun proxyGrpc(
        @PathParam("service") service: String,
        @PathParam("method") method: String,
        body: ByteArray
    ): ByteArray {
        val grpcUrl = "http://localhost:50051/$service/$method"
        log.info("Forwarding gRPC request to: $grpcUrl")

        val url = URL(grpcUrl)
        val connection = url.openConnection() as HttpURLConnection

        return try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/grpc-web+proto")
            connection.setRequestProperty("Accept", "application/grpc-web+proto")
            connection.setRequestProperty("TE", "trailers")
            connection.setRequestProperty("X-Grpc-Web", "1") // WAJIB untuk gRPC-Web
            connection.doOutput = true
            connection.doInput = true

            // Kirim request ke gRPC Server
            connection.outputStream.use { it.write(body) }

            // Baca response dari gRPC server
            val responseBytes = connection.inputStream.use { it.readBytes() }

            log.info("gRPC response received, size: ${responseBytes.size} bytes")
            responseBytes
        } catch (e: IOException) {
            log.error("Error forwarding gRPC request: ${e.message}", e)
            throw WebApplicationException("Error forwarding gRPC request: ${e.message}", 502)
        } finally {
            connection.disconnect()
        }
    }
}