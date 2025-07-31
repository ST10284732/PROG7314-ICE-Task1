package vcmsa.projects.prog7314_icetask1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var txtRecipeOutput: TextView
    private lateinit var inputEditText: EditText
    lateinit var messageTextView: TextView
    lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchMealCategories()
    }

    private fun fetchRecipe(search: String) {
        val url = "https://www.themealdb.com/api/json/v1/1/search.php?s=$search"
        txtRecipeOutput.text = "Searching for \"$search\"..."

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
                                    val formattedOutput = """
                                    Recipe: ${meal.strMeal}
                                    
                                    Instructions:
                                    ${meal.strInstructions}
                                    
                                    Ingredients:
                                    1. ${meal.strIngredient1} - ${meal.strMeasure1}
                                    2. ${meal.strIngredient2} - ${meal.strMeasure2}
                                    3. ${meal.strIngredient3} - ${meal.strMeasure3}
                                    4. ${meal.strIngredient4} - ${meal.strMeasure4}
                                    5. ${meal.strIngredient5} - ${meal.strMeasure5}
                                    6. ${meal.strIngredient6} - ${meal.strMeasure6}
                                    7. ${meal.strIngredient7} - ${meal.strMeasure7}
                                    8. ${meal.strIngredient8} - ${meal.strMeasure8}
                                    9. ${meal.strIngredient9} - ${meal.strMeasure9}
                                    10. ${meal.strIngredient10} - ${meal.strMeasure10}
                                """.trimIndent()
                                    txtRecipeOutput.text = formattedOutput
                                } else {
                                    txtRecipeOutput.text = "No meals found for \"$search\"."
                                }
                            } catch (e: JsonSyntaxException) {
                                Log.e("searchMealsByName", "JSON parsing error: ${e.message}")
                                txtRecipeOutput.text = "Error: Could not parse response."
                            }
                        }

                        is Result.Failure -> {
                            val ex = result.getException()
                            Log.e("searchMealsByName", "API error: ${ex.message}")
                            txtRecipeOutput.text = "Error: Could not fetch meals."
                        }
                    }
                }
            }
        }
    }

    private fun fetchMealCategories() {
        val url = "https://www.themealdb.com/api/json/v1/1/categories.php"

        url.httpGet().responseString { _, _, result ->
            when (result) {
                is Result.Success -> {
                    val json = result.get()
                    val categoryResponse = Gson().fromJson(json, MealRecipeCategories::class.java)
                    val categories = categoryResponse.categories

                    val adapter = CategoryAdapter(categories) { category ->
                        val intent = Intent(this, MealsActivity::class.java)
                        intent.putExtra("category_name", category.strCategory)
                        startActivity(intent)
                    }
                    recyclerView.adapter = adapter
                }

                is Result.Failure -> {
                    runOnUiThread {
                        Toast.makeText(this, "Failed to load categories", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
