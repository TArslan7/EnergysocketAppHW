data class PowerRequest(
    val wifiSsid: String,
    val wifiStrength: Int,
    val totalPowerImportT1Kwh: Double,
    val totalPowerExportT1Kwh: Double,
    val activePowerW: Int,
    val activePowerL1W: Int,
    var powerOn: Boolean // Voeg powerOn toe als een veld van het type Boolean in PowerRequest
)