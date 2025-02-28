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
                    appendNumber(number)
                }
            }
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

    private fun appendNumber(number: String) {
        val currentText = binding.textDisplay.text.toString()
        binding.textDisplay.text = currentText + number
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}