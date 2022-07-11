package com.sina.easyfood.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sina.easyfood.pojo.Meal

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(meal:Meal)

    @Delete
    suspend fun delete(meal: Meal)

    @Query("SELECT * FROM mealInformation")
    fun getAllMeals():LiveData<List<Meal>>
}