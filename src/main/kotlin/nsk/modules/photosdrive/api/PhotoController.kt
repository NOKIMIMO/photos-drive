package nsk.modules.photosdrive.api
import nsk.core.module.HostContext
import nsk.modules.photosdrive.model.Photo
import nsk.modules.photosdrive.service.PhotoService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/photos")
class PhotoController(
    private val photoService: PhotoService,
    private val hostContext: HostContext
) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(@RequestParam("file") file: MultipartFile): Photo {
        return photoService.upload(
            name = file.originalFilename ?: "unknown",
            data = file.bytes,
            contentType = file.contentType ?: "application/octet-stream",
            context = hostContext
        )
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): Photo? =
        photoService.getById(id)

    @GetMapping
    fun listMyPhotos(): List<Photo> =
        photoService.listByUser(hostContext.currentUser().id)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): Boolean =
        photoService.delete(id, hostContext)
}
