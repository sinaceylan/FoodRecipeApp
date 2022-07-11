package com.sina.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sina.easyfood.activities.CategoryMealsActivity
import com.sina.easyfood.activities.MainActivity
import com.sina.easyfood.activities.MealActivity
import com.sina.easyfood.adapters.CategoriesAdapter
import com.sina.easyfood.adapters.MostPopularAdapter
import com.sina.easyfood.databinding.FragmentHomeBinding
import com.sina.easyfood.fragments.bottomsheet.MealButtomSheetFragment
import com.sina.easyfood.pojo.MealsByCategory
import com.sina.easyfood.pojo.Meal
import com.sina.easyfood.videoModel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal:Meal
    private lateinit var popularItemsAdater:MostPopularAdapter
    private lateinit var categoriesAdapter:CategoriesAdapter


    companion object{
        const val MEAL_ID = "com.sina.easyfood.fragments.idMeal"
        const val MEAL_NAME = "com.sina.easyfood.fragments.nameMeal"
        const val MEAL_THUMB = "com.sina.easyfood.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.sina.easyfood.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        popularItemsAdater = MostPopularAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecycleView()

        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()

        prepareCategoriesRecycleView()
        viewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()

        onPopularItemLongClick()

    }

    private fun onPopularItemLongClick() {
        popularItemsAdater.onLongItemClick = { meal ->
            val mealBottomSheetFragment = MealButtomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal Info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity,CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecycleView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner,Observer{
            categories ->
                    categoriesAdapter.setCategoryList(categories)

        })
    }

    private fun onPopularItemClick() {
        popularItemsAdater.onItemClick = { meal ->
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecycleView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter = popularItemsAdater
        }
    }

    private fun observePopularItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner) { mealList ->
            popularItemsAdater.setMeals(mealList = mealList as ArrayList<MealsByCategory>)
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener{
            val intent = Intent(requireActivity(),MealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner
        ) { meal ->
            Glide.with(this@HomeFragment)
                .load(meal!!.strMealThumb)
                .into(binding.imgRandomMeal)

            this.randomMeal = meal
        }
    }


}