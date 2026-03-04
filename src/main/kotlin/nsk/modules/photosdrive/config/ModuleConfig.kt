package nsk.modules.photosdrive.config


import jakarta.servlet.http.HttpServletRequest
import nsk.core.module.HostContext
import nsk.modules.photo.sdk.CoreApiClient
import nsk.modules.photosdrive.sdk.RemoteHostContext
import nsk.modules.photosdrive.sdk.RemoteStorageService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.web.context.WebApplicationContext

@Configuration
class ModuleConfig {

    @Value("\${core.base-url:http://localhost:8080}")
    private lateinit var coreBaseUrl: String

    @Bean
    fun coreApiClient(): CoreApiClient = CoreApiClient(coreBaseUrl)

    @Bean
    fun remoteStorageService(client: CoreApiClient): RemoteStorageService =
        RemoteStorageService(client)

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
    fun hostContext(
        client: CoreApiClient,
        storage: RemoteStorageService,
        request: HttpServletRequest
    ): HostContext {
        val token = request.getHeader("Authorization")?.removePrefix("Bearer ") ?: ""
        return RemoteHostContext(client, storage, token)
    }
}