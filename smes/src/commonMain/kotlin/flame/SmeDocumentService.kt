package flame

import cabinet.Attachment
import cabinet.FileUploadParam
import koncurrent.Later
import koncurrent.later.then
import koncurrent.later.andThen
import koncurrent.later.andZip
import koncurrent.later.zip
import koncurrent.later.catch
import sentinel.Sessioned

interface SmeDocumentService{
    fun update(params: Sessioned<Attachment>) : Later<SmeDto>
}