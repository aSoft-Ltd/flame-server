package flame

import cabinet.Attachment
import koncurrent.Later
import sentinel.Sessioned

class SmeDocumentsServiceFlix(options: SmeServiceOptions) : SmeServiceFlixBase(options), SmeDocumentsService {
    override fun update(params: Attachment): Later<SmeDto> = params.add()
}