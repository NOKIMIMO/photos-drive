package nsk.modules.photosdrive.sdk

import nsk.core.module.UserContext

data class RemoteUserContext(
    override val id: String,
    override val name: String,
    override val email: String,
    override val roles: List<String>
) : UserContext