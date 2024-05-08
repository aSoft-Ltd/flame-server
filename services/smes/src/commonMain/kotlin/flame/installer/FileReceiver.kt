@file:Suppress("Since15")

package flame.installer

import epsilon.MemorySize
import epsilon.MemoryUnit
import epsilon.Multiplier
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    class Received(
        val file: File,
        val size: MemorySize
    )

    suspend fun receive(name: String, multipart: MultiPartData): Received {
        val tmp = base.newTmpFile()
        val params = mutableMapOf<String, String>()
        var bytes = 0.0
        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> params["${part.name}"] = part.value
                is PartData.FileItem -> withContext(Dispatchers.IO) {
                    val provider = part.streamProvider()
                    val os = FileOutputStream(tmp)
                    provider.readBytes().let {
                        bytes += it.size.toDouble()
                        os.write(it)
                    }
                    provider.close()
                    os.flush()
                    os.close()
                }

                else -> throw IllegalArgumentException("Unsupported multipart form item")
            }
            part.dispose()
        }
        val dst = File(tmp.parent, name)
        tmp.renameTo(dst)
        return Received(dst, MemorySize(bytes, Multiplier.Unit, MemoryUnit.Bytes))
    }
}