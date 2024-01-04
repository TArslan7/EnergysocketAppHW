data class EnergyStateModel(
    val wifiSsid: String,
    val wifiStrength: Int,
    val totalPowerImportT1Kwh: Double,
    val totalPowerExportT1Kwh: Double,
    val activePowerW: Int,
    val activePowerL1W: Int,
    val powerOn: Boolean,
    val switchLock: Boolean?
) 