package nsk.modules.photosdrive.service
import nsk.core.module.HostContext
import nsk.core.module.HostEvent
import nsk.modules.photosdrive.model.Photo
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

data class PhotoUploadedEvent(
    override val type: String = "photo.uploaded",
    val photoId: String,
    val userId: String
) : HostEvent

@Service
class PhotoService {

    private val photos = ConcurrentHashMap<String, Photo>()

    fun upload(name: String, data: ByteArray, contentType: String, context: HostContext): Photo {
        val id = UUID.randomUUID().toString()
        val user = context.currentUser()

        val path = "photos/${user.id}/$id"
        context.storage().save(data, path)

        val photo = Photo(
            id = id,
            name = name,
            path = path,
            size = data.size.toLong(),
            contentType = contentType,
            uploadedBy = user.id
        )

        photos[id] = photo

        context.publish(PhotoUploadedEvent(photoId = id, userId = user.id))

        return photo
    }

    fun getById(id: String): Photo? = photos[id]

    fun listByUser(userId: String): List<Photo> =
        photos.values.filter { it.uploadedBy == userId }

    fun delete(id: String, context: HostContext): Boolean {
        val photo = photos.remove(id) ?: return false
        context.storage().delete(photo.path)
        return true
    }
}
