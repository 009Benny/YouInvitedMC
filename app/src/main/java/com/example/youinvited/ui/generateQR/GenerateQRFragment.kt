package com.example.youinvited.ui.generateQR

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.youinvited.R
import com.example.youinvited.models.InvitedClass
import com.example.youinvited.ui.mapEdit.MapEditFragmentArgs
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import kotlinx.android.synthetic.main.fragment_generate_q_r.*

class GenerateQRFragment : Fragment() {
    var link:String = ""
    val args: GenerateQRFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.link = args.qrText.toString()
        return inflater.inflate(R.layout.fragment_generate_q_r, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        imgQR.setImageBitmap(this.generateQRCode(this.link))
    }

    private fun generateQRCode(text:String):Bitmap{
        val width = 500
        val height = 500
        val bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888)
        val codeWritter = MultiFormatWriter()
        try{
            val bitMatrix = codeWritter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x,y, if (bitMatrix[x,y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e:WriterException){
            Log.d("Error", e.message.toString())
        }
        return bitmap
    }
}