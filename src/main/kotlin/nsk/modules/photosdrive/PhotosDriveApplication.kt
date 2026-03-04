package nsk.modules.photosdrive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PhotosDriveApplication

fun main(args: Array<String>) {
    runApplication<PhotosDriveApplication>(*args)
}
