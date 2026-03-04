package nsk.modules.photosdrive.model

data class Photo(
    val id: String,
    val name: String,
    val path: String,
    val size: Long,
    val contentType: String,
    val uploadedBy: String,
    val uploadedAt: Long = System.currentTimeMillis()
)