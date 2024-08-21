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

class MixedReceiver(directory: String) {

    private val base = File(directory)


    class Data(
        val data: Map<String, PartData>,
        val base: File
    ) {
        suspend fun text(name:String): String {
            val part = data[name]
            when (part) {
                is PartData.FormItem -> return part.value

                else -> throw IllegalArgumentException("Unsupported multipart form item. needed text")
            }
        }

        suspend fun files(): List<FileReceiver.Received> {
            return data.keys.mapNotNull { key->
                file(key)
            }
        }

        suspend fun file(name:String): FileReceiver.Received? {
            val part = data[name]
            var bytes = 0.0
            val tmp = base.newTmpFile()

            when (part) {
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

                else -> return null
            }
            part.dispose()

            val dst = File(tmp.parent, part.originalFileName)
            tmp.renameTo(dst)
            return FileReceiver.Received(dst, MemorySize(bytes, Multiplier.Unit, MemoryUnit.Bytes))
        }
    }




    suspend fun receive(multipart: MultiPartData): Data {
        val params = mutableMapOf<String, PartData>()

        var bytes = 0.0
        multipart.forEachPart { part ->
            params["${part.name}"] = part
        }

        return Data(params, base)
    }
}

tailrec fun File.newTmpFile(attempt: Int = 0): File {
    val file = File(this, "tmp-$attempt")
    if (!file.exists()) return file.also {
        it.parentFile.mkdirs()
        it.createNewFile()
    }
    return newTmpFile(attempt + 1)
}