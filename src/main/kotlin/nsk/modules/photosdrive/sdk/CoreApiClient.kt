package nsk.modules.photo.sdk

import nsk.modules.photosdrive.sdk.UserData
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate

class CoreApiClient(
    private val coreBaseUrl: String
) {
    private val log = LoggerFactory.getLogger(CoreApiClient::class.java)
    private val restTemplate = RestTemplate()

    fun saveStorage(path: String, data: ByteArray) {
        log.info("[core] POST /internal/storage/{} ({} bytes)", path, data.size)
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_OCTET_STREAM
        }
        val entity = HttpEntity(data, headers)
        restTemplate.postForEntity("$coreBaseUrl/internal/storage/$path", entity, Void::class.java)
        log.debug("[core] saveStorage OK — path={}", path)
    }

    fun deleteStorage(path: String): Boolean {
        log.info("[core] DELETE /internal/storage/{}", path)
        restTemplate.delete("$coreBaseUrl/internal/storage/$path")
        log.debug("[core] deleteStorage OK — path={}", path)
        return true
    }

    fun getStorage(path: String): ByteArray? {
        log.info("[core] GET /internal/storage/{}", path)
        return try {
            val result = restTemplate.getForObject("$coreBaseUrl/internal/storage/$path", ByteArray::class.java)
            log.debug("[core] getStorage OK — path={}, size={}", path, result?.size)
            result
        } catch (e: Exception) {
            log.warn("[core] getStorage FAILED — path={}, error={}", path, e.message)
            null
        }
    }

    fun listStorage(path: String): List<String> {
        log.info("[core] GET /internal/storage/list/{}", path)
        val result = restTemplate.getForObject(
            "$coreBaseUrl/internal/storage/list/$path",
            Array<String>::class.java
        )?.toList() ?: emptyList()
        log.debug("[core] listStorage OK — path={}, count={}", path, result.size)
        return result
    }

    fun publishEvent(event: Map<String, Any>) {
        log.info("[core] POST /internal/events — type={}", event["type"])
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val entity = HttpEntity(event, headers)
        restTemplate.postForEntity("$coreBaseUrl/internal/events", entity, Void::class.java)
        log.debug("[core] publishEvent OK — event={}", event)
    }

    fun getCurrentUser(token: String): UserData {
        log.info("[core] GET /internal/user — resolving current user")
        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer $token")
        }
        val entity = HttpEntity<Void>(null, headers)
        val user = restTemplate.exchange(
            "$coreBaseUrl/internal/user",
            HttpMethod.GET,
            entity,
            UserData::class.java
        ).body ?: throw IllegalStateException("User not found")
        log.debug("[core] getCurrentUser OK — userId={}, email={}", user.id, user.email)
        return user
    }
}
