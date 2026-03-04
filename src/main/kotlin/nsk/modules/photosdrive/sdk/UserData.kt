package nsk.modules.photosdrive.sdk

data class UserData(
    val id: String,
    val name: String,
    val email: String,
    val roles: List<String> = emptyList()
)