package dk.offlines.managedata.ui.shared

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import dk.offlines.managedata.data.Monster
import dk.offlines.managedata.data.MonsterRepository

class SharedViewModel(app: Application) : AndroidViewModel(app) {
    private val dataRepository = MonsterRepository(app)
    val monsterData = dataRepository.monsterData

    val selectedMonster = MutableLiveData<Monster>()
}
