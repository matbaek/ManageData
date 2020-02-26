package dk.offlines.managedata.utilities

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import com.squareup.moshi.Moshi
import java.io.File
import kotlin.contracts.contract

class FileHelper: AppCompatActivity() {
    companion object {
        fun getTextFromAssets(context: Context, fileName: String): String {
            return context.assets.open(fileName).use {
                it.bufferedReader().use {
                    it.readText()
                }
            }
        }

        fun saveTextToFile(app: Application, json: String?) {
            val file = File(app.getExternalFilesDir("monsters"), "monsters.json")
            file.writeText(json ?: "", Charsets.UTF_8)
        }

        fun readTextFile(app: Application): String? {
            val file = File(app.getExternalFilesDir("monsters"), "monsters.json")
            return if(file.exists()) {
                file.readText()
            } else  null
        }
    }
}