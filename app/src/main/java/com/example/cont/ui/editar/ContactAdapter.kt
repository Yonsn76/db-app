package com.example.cont.ui.editar

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
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

            AlertDialog.Builder(context)
                .setTitle("Editar Contacto")
                .setView(dialogBinding.root)
                .setPositiveButton("Guardar") { _: DialogInterface, _: Int ->
                    val newName = dialogBinding.editTextName.text.toString()
                    val newPhone = dialogBinding.editTextPhoneNumber.text.toString()
                    if (newName.isNotEmpty() && newPhone.isNotEmpty()) {
                        onContactUpdated(contact.first, newName, newPhone)
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
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