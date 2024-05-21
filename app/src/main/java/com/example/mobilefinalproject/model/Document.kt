package com.example.mobilefinalproject.model

class Document (
    val id: Int,
    val doc_url: String,
    val doc_name: String,
    val memory: String
){
    companion object {
        fun getDoc(): List<Document> {
            val doc =  listOf(
                Document(1, "https://www.tutorialspoint.com/html/html_tutorial.pdf", "tai_lieu_hoc_tap.pdf", "32Mb"),
                Document(2, "https://kotlinlang.org/docs/kotlin-reference.pdf", "Document2.pdf", "50Mb"),
                Document(3, "https://www.tutorialspoint.com/html/html_tutorial.pdf", "Advance_code.pdf", "20Mb")
            )
            return doc
        }
    }



}