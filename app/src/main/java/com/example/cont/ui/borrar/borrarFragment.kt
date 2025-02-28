package com.example.cont.ui.borrar

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cont.R
import com.example.cont.databinding.FragmentBorrarBinding
import com.example.cont.databinding.VentanitaEliminarBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class borrarFragment : Fragment() {
    private var _binding: FragmentBorrarBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: borrarViewModel
    private lateinit var contactAdapter: ContactsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBorrarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(borrarViewModel::class.java)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        contactAdapter = ContactsAdapter { contact ->
            showDeleteConfirmationDialog(contact.first)
        }
        binding.recyclerViewContacts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contactAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contactAdapter.submitList(contacts)
            updateEmptyViewVisibility(contacts.isEmpty())
        }
    }

    private fun updateEmptyViewVisibility(isEmpty: Boolean) {
        binding.emptyView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showDeleteConfirmationDialog(contactId: Long) {
        if (!isAdded || context == null) return

        try {
            // Obtener información del contacto
            val contact = viewModel.getContactById(contactId)
            if (contact != null) {
                // Usar el diálogo futurista rojo
                val dialogBinding = VentanitaEliminarBinding.inflate(layoutInflater)
                
                // Configurar el texto de confirmación con el nombre del contacto
                dialogBinding.textViewContactToDelete.text = "${contact.second} - ${contact.third}"
                
                // Crear el diálogo personalizado
                val dialog = Dialog(requireContext(), R.style.FuturisticDialogTheme_Red)
                dialog.setContentView(dialogBinding.root)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                
                // Agregar botones programáticamente
                val buttonContainer = dialogBinding.buttonContainerDelete
                
                // Botón Eliminar
                val deleteButton = Button(requireContext()).apply {
                    text = "ELIMINAR"
                    setTextColor(Color.parseColor("#FF3333"))
                    setBackgroundColor(Color.TRANSPARENT)
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.END
                        marginEnd = 16
                    }
                    setOnClickListener {
                        deleteContact(contactId)
                        dialog.dismiss()
                    }
                }
                
                // Botón Cancelar
                val cancelButton = Button(requireContext()).apply {
                    text = "CANCELAR"
                    setTextColor(Color.WHITE)
                    setBackgroundColor(Color.TRANSPARENT)
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.END
                    }
                    setOnClickListener {
                        dialog.dismiss()
                    }
                }
                
                // Agregar botones al contenedor
                buttonContainer.addView(cancelButton)
                buttonContainer.addView(deleteButton)
                
                dialog.show()
            } else {
                showError("No se pudo encontrar el contacto")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showError("Error al mostrar el diálogo de eliminación")
        }
    }

    private fun deleteContact(contactId: Long) {
        viewModel.deleteContact(contactId)
        showSuccess("Contacto eliminado")
    }

    private fun showError(message: String) {
        if (isAdded && context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSuccess(message: String) {
        if (isAdded && context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ContactsAdapter(
    private val onDeleteClick: (Triple<Long, String, String>) -> Unit
) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    private var contacts: List<Triple<Long, String, String>> = emptyList()

    fun submitList(newList: List<Triple<Long, String, String>>) {
        contacts = newList.toList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact_delete, parent, false)
        return ContactViewHolder(view, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        if (position < contacts.size) {
            holder.bind(contacts[position])
        }
    }

    override fun getItemCount() = contacts.size

    class ContactViewHolder(
        view: View,
        private val onDeleteClick: (Triple<Long, String, String>) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.textViewName)
        private val phoneTextView: TextView = view.findViewById(R.id.textViewPhone)
        private val deleteButton: ImageButton = view.findViewById(R.id.buttonDelete)

        fun bind(contact: Triple<Long, String, String>) {
            try {
                nameTextView.text = contact.second
                phoneTextView.text = contact.third
                deleteButton.setOnClickListener {
                    onDeleteClick(contact)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}