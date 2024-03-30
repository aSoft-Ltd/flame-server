package flame

interface SmeService : SmeScheme {
    override val documents: SmeDocumentsService
}