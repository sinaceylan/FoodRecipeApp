package com.sina.easyfood.videoModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sina.easyfood.pojo.MealsByCategory
import com.sina.easyfood.pojo.MealsByCategoryList
import com.sina.easyfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel: ViewModel() {
    val mealsLiveData = MutableLiveData<List<MealsByCategory>>()

    fun getMealsByCategory(categoryName: String){
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {
                response.body()?.let {
                    mealsList -> mealsLiveData.postValue(mealsList.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.e("CategoryMealsViewMOdel",t.message.toString())
            }

        })
    }

    fun observeMealsLiveData():LiveData<List<MealsByCategory>>{
        return mealsLiveData
    }
}


