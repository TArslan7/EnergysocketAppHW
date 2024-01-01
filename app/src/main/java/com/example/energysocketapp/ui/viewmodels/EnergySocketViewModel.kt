import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class EnergySocketViewModel(private val repository: EnergySocketRepository) : ViewModel() {

    private val _energyData = MutableLiveData<EnergyDataModel>()
    val energyData: LiveData<EnergyDataModel> = _energyData

    private val _energyState = MutableLiveData<EnergyStateModel>()
    val energyState: LiveData<EnergyStateModel> = _energyState

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    private val _powerToggleSuccess = MutableLiveData<Boolean>()
    val powerToggleSuccess: LiveData<Boolean> = _powerToggleSuccess

    private val _switchLockToggleSuccess = MutableLiveData<Boolean>()
    val switchLockToggleSuccess: LiveData<Boolean> = _switchLockToggleSuccess

    private val _brightnessChangeSuccess = MutableLiveData<Boolean>()
    val brightnessChangeSuccess: LiveData<Boolean> = _brightnessChangeSuccess

    init {
        fetchData()
        fetchState()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                val dataResult = repository.fetchEnergyData()
                if (dataResult is Result.Success) {
                    _energyData.postValue(dataResult.data)
                } else if (dataResult is Result.Error) {
                    _error.postValue(dataResult.exception)
                }
            } catch (e: Exception) {
                _error.postValue(e)
            }
        }
    }

    private fun fetchState() {
        viewModelScope.launch {
            try {
                val stateResult = repository.fetchEnergyState()
                if (stateResult is Result.Success) {
                    _energyState.postValue(stateResult.data)
                } else if (stateResult is Result.Error) {
                    _error.postValue(stateResult.exception)
                }
            } catch (e: Exception) {
                _error.postValue(e)
            }
        }
    }


    private val _energyCost = MutableLiveData<Double>()
    val energyCost: LiveData<Double> = _energyCost

    private var energyRate: Double = 0.0 // Tarief voor energiekosten

    // Functie om het energiekostentarief in te stellen
    fun setEnergyRate(rate: Double) {
        energyRate = rate
        calculateEnergyCost()
    }



    // Functie om energiekosten te berekenen
    private fun calculateEnergyCost() {
        val energyData = _energyData.value
        if (energyData != null) {
            val cost = energyData.totalPowerImportT1Kwh * energyRate
            _energyCost.postValue(cost)
        }
    }

    private var isRequestInProgress = false

    fun isRequestInProgress(): Boolean {
        return isRequestInProgress
    }
    fun togglePower(powerOn: Boolean) {
        if (isRequestInProgress()) {
            // Als er al een verzoek bezig is, verlaat de functie en blokkeer verdere interactie
            return
        }

        isRequestInProgress = true // Markeer het verzoek als bezig

        val powerRequest = PowerRequest(
            wifiSsid = "YourSSID",
            wifiStrength = 80,
            totalPowerImportT1Kwh = 0.0,
            totalPowerExportT1Kwh = 0.0,
            activePowerW = 0,
            activePowerL1W = 0,
            powerOn = powerOn // Toewijzing in de constructeur
        )

        // Stel de boolean waarde in op basis van de powerOn variabele die je hebt
        powerRequest.powerOn = powerOn // Stel de boolean in de PowerRequest in op basis van je powerOn variabele

        viewModelScope.launch {
            try {
                val result = repository.togglePower(powerRequest)
                if (result is Result.Error) {
                    _error.postValue(result.exception)
                } else if (result is Result.Success) {
                    _powerToggleSuccess.postValue(true)
                    // Voer UI-updates uit na succesvolle wijziging van de stroomstatus
                }
            } catch (e: Exception) {
                _error.postValue(e)
            } finally {
                isRequestInProgress = false // Markeer het verzoek als voltooid
            }
        }
    }


    fun toggleSwitchLock(newLockStatus: Boolean) {
        viewModelScope.launch {
            try {
                val result = repository.toggleSwitchLock(SwitchLockRequest(newLockStatus))
                if (result is Result.Error) {
                    _error.postValue(result.exception)
                } else if (result is Result.Success) {
                    // Update UI na succesvolle wijziging van switch_lock-status
                    _switchLockToggleSuccess.postValue(true)
                }
            } catch (e: Exception) {
                _error.postValue(e)
            }
        }
    }

    fun changeBrightness(brightness: Int) {
        viewModelScope.launch {
            try {
                val result = repository.changeBrightness(BrightnessRequest(brightness))
                if (result is Result.Error) {
                    _error.postValue(result.exception)
                } else if (result is Result.Success) {
                    _brightnessChangeSuccess.postValue(true)
                    // Voer UI-updates uit na succesvolle wijziging van de helderheid
                }
            } catch (e: Exception) {
                _error.postValue(e)
            }
        }
    }
}