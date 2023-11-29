package flame

import cabinet.Attachment
import cabinet.FileUploadParam
import koncurrent.Later
import sentinel.Sessioned

interface SmeDocumentService{
    fun update(params: Sessioned<Attachment>) : Later<SmeDto>
}