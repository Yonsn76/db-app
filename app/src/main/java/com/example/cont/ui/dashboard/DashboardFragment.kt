package com.example.cont.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cont.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DashboardViewModel
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        contactAdapter = ContactAdapter { id, name, phoneNumber ->
            viewModel.updateContact(id, name, phoneNumber)
        }
        binding.recyclerViewContacts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contactAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contactAdapter.submitList(contacts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}