package flame

import cabinet.Attachment
import koncurrent.Later
import koncurrent.later.then
import koncurrent.later.andThen
import koncurrent.later.andZip
import koncurrent.later.zip
import koncurrent.later.catch
import sentinel.Sessioned

class SmeDocumentServiceFlix(options: SmeServiceOptions) : SmeServiceFlixBase(options), SmeDocumentService {
    override fun update(params: Sessioned<Attachment>): Later<SmeDto> = params.add()
}