package dk.offlines.managedata.data

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dk.offlines.managedata.LOG_TAG
import dk.offlines.managedata.R
import dk.offlines.managedata.utilities.FileHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MonsterRepository(val app: Application): AppCompatActivity() {

    val monsterData = MutableLiveData<List<Monster>>()

    private val listType = Types.newParameterizedType(
        List::class.java, Monster::class.java
    )

    init {
        getMonsterData()
        Log.i(LOG_TAG, "Network status: ${networkAvailable()}")
    }

    fun getMonsterData() {
        val text = FileHelper.getTextFromAssets(app, "monster_data.json")

        val moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<List<Monster>> = moshi.adapter(listType)

        monsterData.value = adapter.fromJson(text) ?: emptyList()
    }

    @Suppress("DEPRECATION")
    private fun networkAvailable(): Boolean {
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting ?: false
    }

    fun getMonsterImage(fileName: String): Drawable {
        assets.open(fileName).use {
            return Drawable.createFromStream(it, null)
        }
    }
}