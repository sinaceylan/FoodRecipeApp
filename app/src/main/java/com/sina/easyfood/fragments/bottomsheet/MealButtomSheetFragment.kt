package com.sina.easyfood.fragments.bottomsheet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sina.easyfood.R
import com.sina.easyfood.activities.MainActivity
import com.sina.easyfood.activities.MealActivity
import com.sina.easyfood.databinding.FragmentCategoriesBinding
import com.sina.easyfood.databinding.FragmentMealButtomSheetBinding
import com.sina.easyfood.fragments.HomeFragment
import com.sina.easyfood.videoModel.HomeViewModel

private const val MEAL_ID = "param1"


class MealButtomSheetFragment : BottomSheetDialogFragment() {
    private var mealId: String? = null
    private lateinit var binding: FragmentMealButtomSheetBinding
    private lateinit var viewModel: HomeViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)

        }
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealButtomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let {
            viewModel.getMealById(it)
        }

        observeBottomSheet()

        onBottomSheetDialogClick()

    }

    private fun onBottomSheetDialogClick() {
        binding.bottomSheet.setOnClickListener {
            if(mealName != null && mealThumb != null){
                val intent = Intent(activity,MealActivity::class.java)
                intent.apply {
                    putExtra(HomeFragment.MEAL_ID,mealId)
                    putExtra(HomeFragment.MEAL_NAME,mealName)
                    putExtra(HomeFragment.MEAL_THUMB,mealThumb)
                }
                startActivity(intent)
            }
        }
    }



    private var mealName:String?=null
    private var mealThumb:String?=null

    private fun observeBottomSheet() {
        viewModel.observeBottomSheetMeal().observe(viewLifecycleOwner, Observer { meal ->
            Glide.with(this).load(meal.strMealThumb).into(binding.imgButtomSheet)
            binding.tvBottomSheetArea.text = meal.strArea
            binding.tvBottomSheetCategory.text = meal.strCategory
            binding.tvBottomSheetMealName.text = meal.strMeal

            mealName = meal.strMeal
            mealThumb = meal.strMealThumb
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            MealButtomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID,param1)
                }
            }
    }
}