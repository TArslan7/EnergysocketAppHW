
data class EnergyDataModel(
    val wifiSsid: String,
    val wifiStrength: Int,
    val totalPowerImportT1Kwh: Double,
    val totalPowerExportT1Kwh: Double,
    val activePowerW: Int,
    val activePowerL1W: Int
)
