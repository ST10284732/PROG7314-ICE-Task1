package vcmsa.projects.prog7314_icetask1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.util.concurrent.Executors
import com.github.kittinunf.result.Result

class MealsActivity : AppCompatActivity() {
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageTextView: TextView
    private lateinit var adapter: MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meals_activity)

        recyclerView = findViewById(R.id.mealsRecyclerView)
        messageTextView = findViewById(R.id.messageTextView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val categoryName = intent.getStringExtra("category_name") ?: ""
        fetchMealsByCategory(categoryName)

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun fetchMealsByCategory(category: String) {
        val url = "https://www.themealdb.com/api/json/v1/1/filter.php?c=$category"

        executor.execute {
            url.httpGet().responseString { _, _, result ->
                handler.post {
                    when (result) {
                        is Result.Success -> {
                            try {
                                val response = Gson().fromJson(result.get(), Meals::class.java)
                                val meals = response.meals
                                if (!meals.isNullOrEmpty()) {
                                    adapter = MealAdapter(meals) { selectedMeal ->
                                        // Handle meal click: open RecipeActivity
                                        val intent = Intent(this, RecipeActivity::class.java)
                                        intent.putExtra("meal_name", selectedMeal.strMeal)
                                        startActivity(intent)
                                    }
                                    recyclerView.adapter = adapter
                                    messageTextView.visibility = View.GONE
                                } else {
                                    messageTextView.text = "No meals found in category \"$category\"."
                                    messageTextView.visibility = View.VISIBLE
                                }
                            } catch (e: JsonSyntaxException) {
                                messageTextView.text = "Error parsing meals data."
                                messageTextView.visibility = View.VISIBLE
                            }
                        }
                        is Result.Failure -> {
                            messageTextView.text = "Error fetching meals."
                            messageTextView.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}