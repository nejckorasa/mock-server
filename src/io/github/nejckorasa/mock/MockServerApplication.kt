package io.github.nejckorasa.mock

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.jackson.JacksonConverter
import io.ktor.jackson.jackson
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.pipeline.PipelineContext
import org.slf4j.event.Level
import java.time.LocalDate
import java.time.LocalTime

@Suppress("BlockingMethodInNonBlockingContext")
@KtorExperimentalAPI
fun main() {

    var mapper = ObjectMapper()

    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            jackson {
                register(
                    ContentType.Application.Json, JacksonConverter(ObjectMapper()
                        .apply { enable(SerializationFeature.INDENT_OUTPUT) }
                        .also { mapper = it })
                )
            }
        }

        intercept(ApplicationCallPipeline.Call) {

            val response: Response =
                when {
                    call.request.uri.contains("just-the-body") -> Response(call.receiveText())
                    call.request.uri.contains("just-the-json-body") -> call.receiveText().let {
                        Response(mapper.readValue(it, Object::class.java), it)
                    }
                    else -> buildRequestData(call.request).let {
                        Response(it, mapper.writeValueAsString(it))
                    }
                }

            log.info("At: ${LocalTime.now()} Data: \n ${response.logData}")
            call.respond(response.obj)
        }

    }.start(wait = true)
}

@KtorExperimentalAPI
private suspend fun PipelineContext<Unit, ApplicationCall>.buildRequestData(request: ApplicationRequest): RequestData =
    RequestData(
        method = request.httpMethod.value,
        uri = request.uri,
        path = request.path(),
        queryParameters = request.queryParameters.entries(),
        body = call.receiveText().ifBlank { null },
        port = request.local.port,
        scheme = request.local.scheme,
        version = request.local.version,
        host = request.local.host,
        remoteHost = request.local.remoteHost,
        time = LocalTime.now().toString(),
        headers = request.headers.entries(),
        cookies = request.cookies.rawCookies
    )

data class RequestData(
    val method: String,
    val uri: String,
    val path: String,
    val queryParameters: Set<Map.Entry<String, List<String>>>,
    val body: String?,
    val port: Int,
    val scheme: String,
    val version: String,
    val host: String,
    val remoteHost: String,
    val time: String,
    val headers: Set<Map.Entry<String, List<String>>>,
    val cookies: Map<String, String>
)


data class Response(val obj: Any, val logData: String = obj.toString())