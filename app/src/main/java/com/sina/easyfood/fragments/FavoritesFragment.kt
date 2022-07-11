package com.sina.easyfood.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sina.easyfood.R
import com.sina.easyfood.activities.MainActivity
import com.sina.easyfood.adapters.FavoritesMealsAdapter
import com.sina.easyfood.databinding.FragmentFavoritesBinding
import com.sina.easyfood.videoModel.HomeViewModel


class FavoritesFragment : Fragment() {
private lateinit var binding:FragmentFavoritesBinding
private lateinit var viewModel:HomeViewModel
private lateinit var favoritesAdapter: FavoritesMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        observeFavorites()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedMeal = favoritesAdapter.differ.currentList[position]
                viewModel.deleteMeal(deletedMeal)

                Snackbar.make(requireView(),"Meal Deleted",Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        viewModel.insertMeal(favoritesAdapter.differ.currentList[position])
                    }
                ).show()

            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)

    }

    private fun prepareRecyclerView() {
        favoritesAdapter = FavoritesMealsAdapter()
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = favoritesAdapter
        }

    }

    private fun observeFavorites() {
        viewModel.observeFavoritesMealsLiveData().observe(requireActivity(), Observer{ meals -> meals.forEach{
            favoritesAdapter.differ.submitList(meals)
        } })
    }


}