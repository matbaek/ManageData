package dk.offlines.managedata.ui.shared

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import dk.offlines.managedata.data.Monster
import dk.offlines.managedata.data.MonsterRepository

class SharedViewModel(val app: Application) : AndroidViewModel(app) {
    private val dataRepository = MonsterRepository(app)
    val monsterData = dataRepository.monsterData

    val selectedMonster = MutableLiveData<Monster>()
    val activityTitel = MutableLiveData<String>()

    init {
        updateActivityTitel()
    }

    fun updateActivityTitel() {
        val signature = PreferenceManager.getDefaultSharedPreferences(app)
            .getString("signature", "Monster fan")
        activityTitel.value = "Stickers for $signature"
    }
}
