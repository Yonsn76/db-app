package com.example.cont.ui.editar

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cont.databinding.ItemContactBinding
import com.example.cont.databinding.VentanitaEditarBinding

class ContactAdapter(private val onContactUpdated: (Long, String, String) -> Unit) :
    ListAdapter<Triple<Long, String, String>, ContactAdapter.ContactViewHolder>(ContactDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ContactViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Triple<Long, String, String>) {
            binding.textViewName.text = contact.second
            binding.textViewPhoneNumber.text = contact.third
            
            binding.buttonEdit.setOnClickListener {
                showEditDialog(contact)
            }
        }

        private fun showEditDialog(contact: Triple<Long, String, String>) {
            val context = binding.root.context
            val dialogBinding = VentanitaEditarBinding.inflate(LayoutInflater.from(context))
            
            dialogBinding.editTextName.setText(contact.second)
            dialogBinding.editTextPhoneNumber.setText(contact.third)

            // Usar diálogo personalizado en lugar de AlertDialog
            val dialog = Dialog(context, com.example.cont.R.style.FuturisticDialogTheme_Green)
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            
            // Agregar botones programáticamente
            val buttonContainer = dialogBinding.buttonContainerEdit
            
            // Botón Guardar
            val saveButton = Button(context).apply {
                text = "GUARDAR"
                setTextColor(Color.parseColor("#33FF33"))
                setBackgroundColor(Color.TRANSPARENT)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.END
                    marginEnd = 16
                }
                setOnClickListener {
                    val newName = dialogBinding.editTextName.text.toString()
                    val newPhone = dialogBinding.editTextPhoneNumber.text.toString()
                    if (newName.isNotEmpty() && newPhone.isNotEmpty()) {
                        onContactUpdated(contact.first, newName, newPhone)
                        dialog.dismiss()
                    }
                }
            }
            
            // Botón Cancelar
            val cancelButton = Button(context).apply {
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
            buttonContainer.addView(saveButton)
            
            dialog.show()
        }
    }

    class ContactDiffCallback : DiffUtil.ItemCallback<Triple<Long, String, String>>() {
        override fun areItemsTheSame(oldItem: Triple<Long, String, String>, newItem: Triple<Long, String, String>): Boolean {
            return oldItem.first == newItem.first
        }

        override fun areContentsTheSame(oldItem: Triple<Long, String, String>, newItem: Triple<Long, String, String>): Boolean {
            return oldItem == newItem
        }
    }
}