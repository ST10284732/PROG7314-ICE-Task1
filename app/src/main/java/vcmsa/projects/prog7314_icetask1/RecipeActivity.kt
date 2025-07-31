package vcmsa.projects.prog7314_icetask1

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.util.concurrent.Executors
import android.os.Handler
import android.os.Looper
import android.widget.Button

class RecipeActivity : AppCompatActivity() {

    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var txtRecipeDetails: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_activity)

        txtRecipeDetails = findViewById(R.id.txtRecipeDetails)

        val mealName = intent.getStringExtra("meal_name")
        if (mealName != null) {
            fetchRecipe(mealName)
        } else {
            Toast.makeText(this, "No meal name provided", Toast.LENGTH_SHORT).show()
            finish()
        }
        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun fetchRecipe(search: String) {
        val url = "https://www.themealdb.com/api/json/v1/1/search.php?s=$search"
        txtRecipeDetails.text = "Loading recipe for \"$search\"..."

        executor.execute {
            url.httpGet().responseString { _, _, result ->
                handler.post {
                    when (result) {
                        is Result.Success -> {
                            try {
                                val mealResponse = Gson().fromJson(result.get(), MealRecipe::class.java)
                                val meals = mealResponse.meals
                                if (!meals.isNullOrEmpty()) {
                                    val meal = meals[0]
                                    val formattedOutput = buildString {
                                        appendLine("Recipe: ${meal.strMeal}")
                                        appendLine()
                                        appendLine("Instructions:")
                                        appendLine(meal.strInstructions ?: "No instructions available.")
                                        appendLine()
                                        appendLine("Ingredients:")

                                        fun addIngredient(number: Int, ingredient: String?, measure: String?) {
                                            if (!ingredient.isNullOrBlank()) {
                                                append("$number. $ingredient")
                                                if (!measure.isNullOrBlank()) {
                                                    append(" - $measure")
                                                }
                                                appendLine()
                                            }
                                        }

                                        addIngredient(1, meal.strIngredient1, meal.strMeasure1)
                                        addIngredient(2, meal.strIngredient2, meal.strMeasure2)
                                        addIngredient(3, meal.strIngredient3, meal.strMeasure3)
                                        addIngredient(4, meal.strIngredient4, meal.strMeasure4)
                                        addIngredient(5, meal.strIngredient5, meal.strMeasure5)
                                        addIngredient(6, meal.strIngredient6, meal.strMeasure6)
                                        addIngredient(7, meal.strIngredient7, meal.strMeasure7)
                                        addIngredient(8, meal.strIngredient8, meal.strMeasure8)
                                        addIngredient(9, meal.strIngredient9, meal.strMeasure9)
                                        addIngredient(10, meal.strIngredient10, meal.strMeasure10)
                                    }
                                    txtRecipeDetails.text = formattedOutput
                                } else {
                                    txtRecipeDetails.text = "No recipe found for \"$search\"."
                                }
                            } catch (e: JsonSyntaxException) {
                                Log.e("RecipeActivity", "JSON parsing error: ${e.message}")
                                txtRecipeDetails.text = "Error: Could not parse response."
                            }
                        }

                        is Result.Failure -> {
                            val ex = result.getException()
                            Log.e("RecipeActivity", "API error: ${ex.message}")
                            txtRecipeDetails.text = "Error: Could not fetch recipe."
                        }
                    }
                }
            }
        }
    }
}