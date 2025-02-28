package com.example.cont.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.cardview.widget.CardView
import com.example.cont.DatabaseHelper
import com.example.cont.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbHelper = DatabaseHelper(requireContext())

        setupNumberButtons()
        setupSaveButton()
        setupClearButton()

        return root
    }

    private fun setupNumberButtons() {
        val gridLayout = binding.gridLayout
        for (i in 0 until gridLayout.childCount) {
            val child = gridLayout.getChildAt(i)
            if (child is CardView) {
                child.setOnClickListener {
                    val textView = child.getChildAt(0) as TextView
                    val number = textView.text.toString()
                    if (isValidPhoneNumberInput(number)) {
                        appendNumber(number)
                    }
                }
            }
        }
    }

    private fun isValidPhoneNumberInput(number: String): Boolean {
        val currentText = binding.textDisplay.text.toString()
        return currentText.length < 15 // Limit phone number length
    }
    
    private fun appendNumber(number: String) {
        val currentText = binding.textDisplay.text.toString()
        val formattedNumber = formatPhoneNumber(currentText + number)
        binding.textDisplay.text = formattedNumber
    }
    
    private fun formatPhoneNumber(number: String): String {
        if (number.length <= 3) return number
        if (number.length <= 7) return "${number.substring(0, 3)}-${number.substring(3)}"
        return "${number.substring(0, 3)}-${number.substring(3, 6)}-${number.substring(6, number.length.coerceAtMost(15))}"
    }
    
    // Add a clear button function
    private fun setupClearButton() {
        binding.buttonClear?.setOnClickListener {
            binding.textDisplay.text = ""
        }
    }
    
    private fun setupSaveButton() {
        binding.fabSave.setOnClickListener {
            val phoneNumber = binding.textDisplay.text.toString()
            val name = binding.editTextName.text.toString()
    
            if (phoneNumber.isNotEmpty() && name.isNotEmpty()) {
                val id = dbHelper.addContact(name, phoneNumber)
                if (id != -1L) {
                    Toast.makeText(context, "Contacto guardado: $name - $phoneNumber", Toast.LENGTH_SHORT).show()
                    binding.textDisplay.text = ""
                    binding.editTextName.text.clear()
                } else {
                    Toast.makeText(context, "Error al guardar el contacto", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Por favor, ingrese nombre y número", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}