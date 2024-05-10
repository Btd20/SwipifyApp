package com.example.swipifyapp

import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream

class PdfReportActivity : AppCompatActivity() {

    private val fileName = "Informe.pdf"
    private val tituloText = "Informe en PDF"
    private val descripcionText = "Este es un ejemplo de informe en PDF generado desde la aplicación."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        generatePdf()
    }

    private fun generatePdf() {
        val document = Document()

        try {
            val filePath = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file = File(filePath, fileName)
            val fileOutputStream = FileOutputStream(file)

            PdfWriter.getInstance(document, fileOutputStream)
            document.open()

            // Agregar contenido al PDF
            document.add(Paragraph(tituloText))
            document.add(Paragraph(descripcionText))

            document.close()

            showToast("Informe PDF generado con éxito: ${file.absolutePath}")
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("Error al generar el informe PDF")
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
