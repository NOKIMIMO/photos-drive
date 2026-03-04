package nsk.modules.photosdrive.sdk

import nsk.core.module.StorageService
import nsk.modules.photo.sdk.CoreApiClient

class RemoteStorageService(
    private val client: CoreApiClient
) : StorageService {

    override fun save(data: ByteArray, path: String) {
        client.saveStorage(path, data)
    }

    override fun list(path: String): List<String> {
        return client.listStorage(path)
    }

    override fun delete(path: String): Boolean {
        return client.deleteStorage(path)
    }

    override fun get(path: String): ByteArray? {
        return client.getStorage(path)
    }
}