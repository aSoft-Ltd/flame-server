@file:Suppress("Since15")

package flame.installer

import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import java.io.File
import java.io.FileOutputStream

class FileReceiver(directory: String) {

    private val base = File(directory)

    tailrec fun File.newTmpFile(attempt: Int = 0): File {
        val file = File(this, "tmp-$attempt")
        if (!file.exists()) return file.also {
            it.parentFile.mkdirs()
            it.createNewFile()
        }
        return newTmpFile(attempt + 1)
    }

    suspend fun receive(name: String, multipart: MultiPartData): File {
        val tmp = base.newTmpFile()
        val params = mutableMapOf<String, String>()
        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> params["${part.name}"] = part.value
                is PartData.FileItem -> {
                    val provider = part.streamProvider()
                    val os = FileOutputStream(tmp)
                    provider.transferTo(os)
                    os.flush()
                    os.close()
                }

                else -> throw IllegalArgumentException("Unsupported multipart form item")
            }
            part.dispose()
        }
        val dst = File(tmp.parent, name)
        tmp.renameTo(dst)
        return dst
    }
}