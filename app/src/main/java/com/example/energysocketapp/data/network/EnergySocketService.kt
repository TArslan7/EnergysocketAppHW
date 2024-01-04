import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface EnergySocketService {

    @GET("/api/v1/data")
    suspend fun getEnergyData(): EnergyDataModel

    @GET("/api/v1/state")
    suspend fun getEnergyState(): EnergyStateModel

    @PUT("/api/v1/state")
    suspend fun togglePower(@Body request: PowerRequest): ResponseBody

    @PUT("/api/v1/state")
    suspend fun toggleSwitchLock(@Body request: SwitchLockRequest): ResponseBody

    @PUT("/api/v1/state")
    suspend fun changeBrightness(@Body request: BrightnessRequest): ResponseBody
}
