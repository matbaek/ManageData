package dk.offlines.managedata.data

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dk.offlines.managedata.utilities.FileHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonsterRepository(val app: Application): AppCompatActivity() {

    val monsterData = MutableLiveData<List<Monster>>()
    private val monsterDao = MonsterDatabase.getDatabase(app).monsterDao()

    private val listType = Types.newParameterizedType(
        List::class.java, Monster::class.java
    )

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val data = monsterDao.getAll()
            if(data.isEmpty()) {
                getMonsterData()
            } else {
                monsterData.postValue(data)
                withContext(Dispatchers.Main) {
                    Toast.makeText(app, "Using local data", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    suspend fun getMonsterData() {
        withContext(Dispatchers.Main) {
            Toast.makeText(app, "Using assets data", Toast.LENGTH_LONG).show()

            val text = FileHelper.getTextFromAssets(app, "monster_data.json")

            val moshi = Moshi.Builder().build()
            val adapter: JsonAdapter<List<Monster>> = moshi.adapter(listType)

            monsterData.value = adapter.fromJson(text) ?: emptyList()
        }

        monsterDao.deleteAll()
        monsterDao.insertMonsters(monsterData.value ?: emptyList())
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

    private fun saveDataToCache(monsterData: List<Monster>) {
        if(ContextCompat.checkSelfPermission(app, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val moshi = Moshi.Builder().build()
            val listType = Types.newParameterizedType(List::class.java, Monster::class.java)
            val adapter: JsonAdapter<List<Monster>> = moshi.adapter(listType)
            val json = adapter.toJson(monsterData)
            FileHelper.saveTextToFile(app, json)
        }
    }

    private fun readDataFromCache(): List<Monster> {
        val json = FileHelper.readTextFile(app)
        if(json == null) {
            return emptyList()
        }

        val moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, Monster::class.java)
        val adapter: JsonAdapter<List<Monster>> = moshi.adapter(listType)
        return adapter.fromJson(json) ?: emptyList()
    }
}