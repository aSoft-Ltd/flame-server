package flame

import cabinet.Attachment
import koncurrent.Later
import sentinel.Sessioned

class SmeDocumentServiceFlix(options: SmeServiceOptions) : SmeServiceFlixBase(options), SmeDocumentService {
    override fun update(params: Sessioned<Attachment>): Later<SmeDto> = params.add()
}