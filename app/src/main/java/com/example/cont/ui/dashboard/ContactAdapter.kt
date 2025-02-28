package com.example.cont.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cont.databinding.ItemContactBinding

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
            binding.editTextName.setText(contact.second)
            binding.editTextPhoneNumber.setText(contact.third)
            binding.buttonSave.setOnClickListener {
                val updatedName = binding.editTextName.text.toString()
                val updatedPhoneNumber = binding.editTextPhoneNumber.text.toString()
                onContactUpdated(contact.first, updatedName, updatedPhoneNumber)
            }
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