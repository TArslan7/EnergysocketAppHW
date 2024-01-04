import okhttp3.ResponseBody

class EnergySocketRepository(private val service: EnergySocketService) {

    suspend fun fetchEnergyData(): Result<EnergyDataModel> {
        return try {
            val data = service.getEnergyData()
            Result.Success(data)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun fetchEnergyState(): Result<EnergyStateModel> {
        return try {
            val state = service.getEnergyState()
            Result.Success(state)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun togglePower(powerRequest: PowerRequest): Result<ResponseBody> {
        return try {
            val response = service.togglePower(powerRequest)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun toggleSwitchLock(switchLockRequest: SwitchLockRequest): Result<ResponseBody> {
        return try {
            val response = service.toggleSwitchLock(switchLockRequest)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun changeBrightness(brightnessRequest: BrightnessRequest): Result<ResponseBody> {
        return try {
            val response = service.changeBrightness(brightnessRequest)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}