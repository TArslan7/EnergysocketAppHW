import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.energysocketapp.R

class EnergyFragment : Fragment() {

    private lateinit var energySocketViewModel: EnergySocketViewModel
    private lateinit var buttonTogglePower: Button
    private lateinit var buttonToggleLock: Button
    private lateinit var buttonCalculateCost: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_energy, container, false)

        buttonToggleLock = view.findViewById(R.id.buttonToggleLock)
        buttonTogglePower = view.findViewById(R.id.buttonTogglePower)

        // Rest van je fragmentcode...

        buttonTogglePower.setOnClickListener {
            if (!energySocketViewModel.isRequestInProgress()) {
                val currentPowerStatus = energySocketViewModel.energyState.value
                val powerOn = currentPowerStatus?.powerOn ?: false
                energySocketViewModel.togglePower(powerOn)
            } else {
                // Toon bijvoorbeeld een melding dat er al een verzoek bezig is
                Toast.makeText(requireContext(), "Een verzoek is al bezig...", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private lateinit var editTextEnergyRate: EditText
    private lateinit var textViewEnergyCost: TextView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextEnergyRate = view.findViewById(R.id.editTextEnergyRate)
        buttonCalculateCost = view.findViewById(R.id.buttonCalculateCost) // Nieuw toegevoegd
        textViewEnergyCost = view.findViewById(R.id.textViewEnergyCost)



        // Ophalen van opgeslagen energietarief bij het starten van de fragment
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val savedEnergyRate = sharedPreferences.getFloat("energy_rate", 0.0f)
        if (savedEnergyRate != 0.0f) {
            editTextEnergyRate.setText(savedEnergyRate.toString())
        }

        buttonCalculateCost.setOnClickListener {
            val enteredRate = editTextEnergyRate.text.toString().toDoubleOrNull()
            if (enteredRate != null) {
                saveEnergyRateToPreferences(enteredRate)
                calculateAndDisplayCost(enteredRate)
            } else {
                Toast.makeText(requireContext(), "Voer een geldig tarief in!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveEnergyRateToPreferences(rate: Double) {
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("energy_rate", rate.toFloat())
        editor.apply()
    }

    private fun calculateAndDisplayCost(rate: Double) {
        val energyData = energySocketViewModel.energyData.value
        if (energyData != null) {
            val cost = energyData.totalPowerImportT1Kwh * rate
            showCostOnUI(cost)
        }
    }


    private fun showCostOnUI(cost: Double) {
        val formattedCost = String.format("%.2f", cost) // Formateer de kostprijs naar 2 decimalen
        val displayText = "Energiekosten: $formattedCost" // Combineer tekst met berekende kosten
        textViewEnergyCost.text = displayText // Toon de kosten in de TextView
    }

}