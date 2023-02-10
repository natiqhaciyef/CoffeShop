package com.natiqhaciyef.coffeshop.ui.view.homescreen

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.natiqhaciyef.coffeshop.R
import com.natiqhaciyef.coffeshop.data.model.Categories
import com.natiqhaciyef.coffeshop.data.model.CoffeeModel
import com.natiqhaciyef.coffeshop.databinding.FragmentHomeBinding
import com.natiqhaciyef.coffeshop.ui.adapter.CategoryAdapter
import com.natiqhaciyef.coffeshop.ui.adapter.CoffeeAdapter
import com.natiqhaciyef.coffeshop.ui.viewmodel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private val categories = Categories.list
    private lateinit var coffeeAdapter: CoffeeAdapter
    private var coffeeList = mutableListOf<CoffeeModel>()
    val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        observeLiveData()

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { }
                return false
            }
        })
    }

    private fun setup() {
        categoryAdapter = CategoryAdapter(requireContext(), categories)
        binding.categoriesRecyclerView.adapter = categoryAdapter
        binding.categoriesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.searchBar.queryHint =
                Html.fromHtml(getString(R.string.search_view_hint), Html.FROM_HTML_MODE_COMPACT)
        } else {
            binding.searchBar.queryHint = Html.fromHtml(getString(R.string.search_view_hint))
        }
    }

    private fun observeLiveData() {
        viewModel.coffeeLiveData.observe(viewLifecycleOwner) {
            it.data?.let {
                coffeeList = it.toMutableList()
                coffeeAdapter = CoffeeAdapter(coffeeList, requireContext())
                binding.coffeesRecyclerView.adapter = coffeeAdapter
                binding.coffeesRecyclerView.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            }
        }
    }
}