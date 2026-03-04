package nsk.modules.photosdrive.sdk

import nsk.core.module.HostContext
import nsk.core.module.HostEvent
import nsk.core.module.StorageService
import nsk.core.module.UserContext
import nsk.modules.photo.sdk.CoreApiClient


class RemoteHostContext(
    private val client: CoreApiClient,
    private val storage: RemoteStorageService,
    private val userToken: String
) : HostContext {

    private var cachedUser: UserContext? = null

    override fun currentUser(): UserContext {
        if (cachedUser == null) {
            val userData = client.getCurrentUser(userToken)
            cachedUser = RemoteUserContext(
                id = userData.id,
                name = userData.name,
                email = userData.email,
                roles = userData.roles
            )
        }
        return cachedUser!!
    }

    override fun storage(): StorageService = storage

    override fun publish(event: HostEvent) {
        client.publishEvent(
            mapOf(
                "type" to event.type,
                "userId" to currentUser().id,
                "timestamp" to System.currentTimeMillis()
            )
        )
    }
}