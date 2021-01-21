package com.walidkh.android.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.walidkh.android.asteroidradar.R
import com.walidkh.android.asteroidradar.databinding.FragmentMainBinding
import com.walidkh.android.asteroidradar.network.AsteroidDateApiFilter


class MainFragment : Fragment() {


    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)


        val application = requireNotNull(this.activity).application
        val viewModelFactory = MainViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.asteroidRecyclerView.apply {
            this.layoutManager = LinearLayoutManager(this.context)

            this.adapter = AsteroidAdapter(
                AsteroidAdapter.OnClickListener {
                    viewModel.displayAsteroidDetails(it)
                }
            )

            this.addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    (this.layoutManager as LinearLayoutManager).orientation
                )
            )
        }

        viewModel.asteroidsList.observe(viewLifecycleOwner, Observer { asteroids ->
            asteroids?.apply {
                (binding.asteroidRecyclerView.adapter as AsteroidAdapter).submitList(asteroids)
            }
        })

        viewModel.asteroidsListOfToday.observe(viewLifecycleOwner, Observer {})
        viewModel.asteroidsListOfTheWeek.observe(viewLifecycleOwner, Observer {})

        viewModel.pictureOfTheDay.observe(viewLifecycleOwner, Observer {
            it?.let {

                val url = it.url
                Picasso.get().load(url).into(binding.activityMainImageOfTheDay)
            }
        })

        viewModel.navigateToDetails.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayAsteroidDetailsComplete()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val filteredList = viewModel.getAsteroidsByFilter(
            when(item.itemId) {
                R.id.show_week -> AsteroidDateApiFilter.SHOW_WEEK
                R.id.show_today -> AsteroidDateApiFilter.SHOW_TODAY
                else -> AsteroidDateApiFilter.SHOW_ALL
            }
        )
        val listData = filteredList.value
        (binding.asteroidRecyclerView.adapter as AsteroidAdapter).submitList(listData)

        return true
    }
}