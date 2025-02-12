package com.example.api

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import org.jboss.logging.Logger

@Path("/grpc-proxy")
@Consumes("application/grpc-web+proto")
@Produces("application/grpc-web+proto")
class GrpcWebProxyResource {

    private val log: Logger = Logger.getLogger(GrpcWebProxyResource::class.java)

    @OPTIONS
    @Path("/{service}/{method}")
    fun preflight(): Response {
        return Response.ok()
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, X-Grpc-Web, Accept, TE")
            .header("Access-Control-Expose-Headers", "Grpc-Status, Grpc-Message, Grpc-Encoding, Grpc-Accept-Encoding")
            .build()
    }

    @POST
    @Path("/{service}/{method}")
    fun proxyGrpc(
        @PathParam("service") service: String,
        @PathParam("method") method: String,
        body: ByteArray
    ): Response {
        val grpcUrl = "http://localhost:50051/$service/$method"  // Pastikan ini sesuai dengan server gRPC
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

            Response.ok(responseBytes)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Expose-Headers", "Grpc-Status, Grpc-Message, Grpc-Encoding, Grpc-Accept-Encoding")
                .header("Content-Type", "application/grpc-web+proto")
                .build()
        } catch (e: IOException) {
            log.error("Error forwarding gRPC request: ${e.message}", e)
            Response.status(502).entity("Error forwarding gRPC request: ${e.message}").build()
        } finally {
            connection.disconnect()
        }
    }
}