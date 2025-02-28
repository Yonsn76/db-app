package com.example.cont.ui.borrar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cont.R
import com.example.cont.databinding.FragmentBorrarBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class borrarFragment : Fragment() {
    private var _binding: FragmentBorrarBinding? = null
    private val binding get() = _binding!!
    private lateinit var borrarViewModel: borrarViewModel
    private lateinit var adapter: ContactsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        borrarViewModel = ViewModelProvider(this).get(borrarViewModel::class.java)
        _binding = FragmentBorrarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupDeleteAllButton()

        return root
    }

    private fun setupRecyclerView() {
        adapter = ContactsAdapter { contact ->
            showDeleteConfirmationDialog(contact.first)
        }
        binding.recyclerViewContacts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@borrarFragment.adapter
        }

        borrarViewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            adapter.submitList(contacts)
            binding.emptyView.visibility = if (contacts.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupDeleteAllButton() {
        binding.buttonDeleteAll.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete_all_confirmation_title)
                .setMessage(R.string.delete_all_confirmation_message)
                .setPositiveButton(R.string.delete) { _, _ ->
                    borrarViewModel.deleteAllContacts()
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }
    }

    private fun showDeleteConfirmationDialog(contactId: Long) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_confirmation_title)
            .setMessage(R.string.delete_confirmation_message)
            .setPositiveButton(R.string.delete) { _, _ ->
                borrarViewModel.deleteContact(contactId)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
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
        contacts = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact_delete, parent, false)
        return ContactViewHolder(view, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount() = contacts.size

    class ContactViewHolder(
        view: View,
        private val onDeleteClick: (Triple<Long, String, String>) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.textViewName)
        private val phoneTextView: TextView = view.findViewById(R.id.textViewPhone)
        private val deleteButton: Button = view.findViewById(R.id.buttonDelete)

        fun bind(contact: Triple<Long, String, String>) {
            nameTextView.text = contact.second
            phoneTextView.text = contact.third
            deleteButton.setOnClickListener { onDeleteClick(contact) }
        }
    }
}