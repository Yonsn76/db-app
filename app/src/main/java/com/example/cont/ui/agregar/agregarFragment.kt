package com.example.cont.ui.agregar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import com.example.cont.databinding.FragmentAgregarBinding
import android.widget.EditText
import com.example.cont.R
import android.app.Dialog
import android.graphics.Color
import android.widget.Button
import android.widget.LinearLayout
import android.view.Gravity
import com.example.cont.ui.ContactsViewModel

class agregarFragment : Fragment() {

    private var _binding: FragmentAgregarBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContactsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgregarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize view

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
        val digits = number.replace(Regex("[^0-9]"), "")

        return when {
            digits.length <= 3 -> digits
            digits.length <= 6 -> "${digits.substring(0, 3)}-${digits.substring(3)}"
            digits.length <= 10 -> "${digits.substring(0, 3)}-${
                digits.substring(
                    3,
                    6
                )
            }-${digits.substring(6)}"

            else -> "${digits.substring(0, 3)}-${digits.substring(3, 6)}-${digits.substring(6, 10)}"
        }
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

            if (phoneNumber.isEmpty()) {
                Toast.makeText(context, "Por favor, ingrese un número", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dialog = Dialog(requireContext(), R.style.FuturisticDialogTheme)
            val dialogView = layoutInflater.inflate(R.layout.ventanita_nombre, null)
            val nameInput = dialogView.findViewById<EditText>(R.id.editTextDialogName)

            val innerLayout = dialogView.findViewById<LinearLayout>(R.id.notification_container)

            dialog.setContentView(dialogView)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            // Add a button programmatically
            val saveButton = Button(requireContext()).apply {
                text = "GUARDAR"
                setTextColor(resources.getColor(R.color.cian))
                setBackgroundColor(Color.TRANSPARENT)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.END
                    topMargin = 16
                }
                setOnClickListener {
                    val name = nameInput.text.toString()
                    if (name.isNotEmpty()) {
                        val id = viewModel.addContact(name, phoneNumber)
                        if (id != -1L) {
                            Toast.makeText(
                                context,
                                "Contacto guardado: $name - $phoneNumber",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.textDisplay.text = ""
                            dialog.dismiss()
                        } else {
                            Toast.makeText(
                                context,
                                "Error al guardar el contacto",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(context, "Por favor, ingrese un nombre", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            innerLayout.addView(saveButton)

            dialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}