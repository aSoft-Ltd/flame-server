package flame

import cabinet.Attachment
import koncurrent.Later

interface SmeDocumentsService : SmeDocumentsScheme {
    fun update(params: Attachment): Later<SmeDto>
}