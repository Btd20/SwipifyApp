package com.example.swipifyapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.property.HorizontalAlignment
import java.io.File

class ReportsActivity : AppCompatActivity() {

    private lateinit var dbHelper: SwipifyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        // Solicitar permisos si no están concedidos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        dbHelper = SwipifyDatabase(this)

        val btnUserReport: Button = findViewById(R.id.btnUserReport)
        val btnBackToMain: Button = findViewById(R.id.btnBackToMain)

        btnUserReport.setOnClickListener {
            Log.d("ReportsActivity", "Botón Generar informe de usuarios clicado")
            generateReport()
        }

        btnBackToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun generateReport() {
        Log.d("ReportsActivity", "Generando informe")
        try {
            val numUsers = dbHelper.getUserCount()
            val numPlaylists = dbHelper.getPlaylistCount()

            // Crear gráfico de barras
            val barChart = createBarChart(numUsers, numPlaylists)

            // Guardar el gráfico como bitmap
            val bitmap = getBitmapFromView(barChart)

            // Generar PDF
            val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/Report.pdf"
            savePdfWithImage(pdfPath, bitmap, "Informe de Usuarios y Playlists")

            Toast.makeText(this, "Informe generado", Toast.LENGTH_SHORT).show()
            openPdf(pdfPath)
        } catch (e: Exception) {
            Log.e("ReportsActivity", "Error al generar el informe", e)
        }
    }

    fun createBarChart(numUsers: Int, numPlaylists: Int): BarChart {
        val barChart = BarChart(this)

        val entries = listOf(
            BarEntry(0f, numUsers.toFloat()),
            BarEntry(1f, numPlaylists.toFloat())
        )
        val dataSet = BarDataSet(entries, "Usuarios y Playlists")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()

        val barData = BarData(dataSet)
        barChart.data = barData

        val labels = listOf("Usuarios", "Playlists")
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.granularity = 1f
        barChart.xAxis.setDrawGridLines(false)
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.setDrawGridLines(false)
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.invalidate()

        // Ajustar el tamaño del gráfico
        barChart.layoutParams = ViewGroup.LayoutParams(800, 600) // Ajusta según sea necesario

        return barChart
    }

    fun getBitmapFromView(view: View): Bitmap {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(view.layoutParams.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(view.layoutParams.height, View.MeasureSpec.EXACTLY)
        view.measure(widthSpec, heightSpec)
        val measuredWidth = view.measuredWidth
        val measuredHeight = view.measuredHeight
        view.layout(0, 0, measuredWidth, measuredHeight)

        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun savePdfWithImage(filePath: String, bitmap: Bitmap, title: String) {
        val pdfFile = File(filePath)
        val writer = PdfWriter(pdfFile)
        val pdfDoc = PdfDocument(writer)
        pdfDoc.defaultPageSize = PageSize.A4 // Configurar el tamaño de página
        val document = Document(pdfDoc)

        // Añadir color de fondo
        document.setBackgroundColor(ColorConstants.DARK_GRAY)

        // Añadir el logo
        val logoBitmap = BitmapFactory.decodeResource(resources, R.drawable.swipifybanner_sinfondo)
        val logoImageData = ImageDataFactory.create(bitmapToByteArray(logoBitmap))
        val logoImage = Image(logoImageData).scaleToFit(100f, 100f).setHorizontalAlignment(HorizontalAlignment.CENTER)
        document.add(logoImage)

        // Añadir el título
        val titleParagraph = Paragraph(title).setFontSize(20f).setBold().setFontColor(ColorConstants.WHITE).setMarginTop(10f).setHorizontalAlignment(HorizontalAlignment.CENTER)
        document.add(titleParagraph)

        // Añadir el gráfico
        val imageData = ImageDataFactory.create(bitmapToByteArray(bitmap))
        val image = Image(imageData).setHorizontalAlignment(HorizontalAlignment.CENTER).setMarginTop(10f)
        document.add(image)

        document.close()
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = java.io.ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun openPdf(filePath: String) {
        val file = File(filePath)
        val uri: Uri = FileProvider.getUriForFile(this, "${packageName}.provider", file)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("ReportsActivity", "No se pudo abrir el PDF", e)
            Toast.makeText(this, "No se pudo abrir el PDF", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permisos otorgados
            Log.d("ReportsActivity", "Permisos de escritura concedidos")
        } else {
            // Permisos no otorgados
            Log.d("ReportsActivity", "Permisos de escritura no concedidos")
        }
    }
}
