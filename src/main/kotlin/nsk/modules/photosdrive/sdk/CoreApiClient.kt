package nsk.modules.photo.sdk

import nsk.modules.photosdrive.sdk.UserData
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate

class CoreApiClient(
    private val coreBaseUrl: String
) {
    private val restTemplate = RestTemplate()

    fun saveStorage(path: String, data: ByteArray) {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_OCTET_STREAM
        }
        val entity = HttpEntity(data, headers)
        restTemplate.postForEntity("$coreBaseUrl/internal/storage/$path", entity, Void::class.java)
    }

    fun deleteStorage(path: String): Boolean {
        restTemplate.delete("$coreBaseUrl/internal/storage/$path")
        return true
    }

    fun getStorage(path: String): ByteArray? {
        return try {
            restTemplate.getForObject("$coreBaseUrl/internal/storage/$path", ByteArray::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun listStorage(path: String): List<String> {
        return restTemplate.getForObject(
            "$coreBaseUrl/internal/storage/list/$path",
            Array<String>::class.java
        )?.toList() ?: emptyList()
    }

    fun publishEvent(event: Map<String, Any>) {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val entity = HttpEntity(event, headers)
        restTemplate.postForEntity("$coreBaseUrl/internal/events", entity, Void::class.java)
    }

    fun getCurrentUser(token: String): UserData {
        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer $token")
        }
        val entity = HttpEntity<Void>(null, headers)
        return restTemplate.exchange(
            "$coreBaseUrl/internal/user",
            HttpMethod.GET,
            entity,
            UserData::class.java
        ).body ?: throw IllegalStateException("User not found")
    }
}
