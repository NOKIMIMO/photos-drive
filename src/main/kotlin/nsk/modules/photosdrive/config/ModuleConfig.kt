package nsk.modules.photosdrive.config


import jakarta.servlet.http.HttpServletRequest
import nsk.core.module.HostContext
import nsk.modules.photo.sdk.CoreApiClient
import nsk.modules.photosdrive.sdk.RemoteHostContext
import nsk.modules.photosdrive.sdk.RemoteStorageService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.web.context.WebApplicationContext

@Configuration
class ModuleConfig {

    private val log = LoggerFactory.getLogger(ModuleConfig::class.java)

    @Value("\${core.base-url}")
    private lateinit var coreBaseUrl: String

    @Value("\${server.port}")
    private var serverPort: Int = 0

    @Value("\${core.port}")
    private var corePort: Int = 0

    @Bean
    fun coreApiClient(): CoreApiClient {
        log.info("=== [photos-drive] Module configuration ===")
        log.info("  server.port   = {}", serverPort)
        log.info("  core.port     = {}", corePort)
        log.info("  core.base-url = {}", coreBaseUrl)
        log.info("==========================================")
        return CoreApiClient(coreBaseUrl)
    }

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