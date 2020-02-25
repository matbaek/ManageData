package dk.offlines.managedata.utilities

import android.graphics.ImageFormat
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import dk.offlines.managedata.R
import java.text.NumberFormat

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, fileName: String) {
    val imageId = when(fileName) {
        "monster01" -> R.drawable.monster01
        "monster02" -> R.drawable.monster02
        "monster03" -> R.drawable.monster03
        "monster04" -> R.drawable.monster04
        "monster05" -> R.drawable.monster05
        "monster06" -> R.drawable.monster06
        "monster07" -> R.drawable.monster07
        "monster08" -> R.drawable.monster08
        "monster09" -> R.drawable.monster09
        "monster10" -> R.drawable.monster10
        "monster11" -> R.drawable.monster11
        "monster12" -> R.drawable.monster12
        else -> R.drawable.monster01
    }
    view.setImageResource(imageId)
}

@BindingAdapter("price")
fun itemPrice(view: TextView, value: Double) {
    val formatter = NumberFormat.getCurrencyInstance()
    val text = "${formatter.format(value)} / each"
    view.text = text
}