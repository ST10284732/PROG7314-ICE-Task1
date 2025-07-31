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

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = findViewById(R.id.categoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchMealCategories()
    }

    private fun fetchMealCategories() {
        val url = "https://www.themealdb.com/api/json/v1/1/categories.php"

        url.httpGet().responseString { _, _, result ->
            when (result) {
                is Result.Success -> {
                    val json = result.get()
                    try {
                        val categoryResponse = Gson().fromJson(json, MealRecipeCategories::class.java)
                        val categories = categoryResponse.categories

                        Log.d("API_SUCCESS", "Parsed ${categories.size} categories")

                        runOnUiThread {
                            val adapter = CategoryAdapter(categories) { category ->
                                val intent = Intent(this, MealsActivity::class.java)
                                intent.putExtra("category_name", category.strCategory)
                                startActivity(intent)
                            }
                            recyclerView.adapter = adapter
                        }

                    } catch (e: Exception) {
                        Log.e("JSON_ERROR", "Failed to parse categories: ${e.message}")
                        runOnUiThread {
                            Toast.makeText(this, "Parsing error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                is Result.Failure -> {
                    val error = result.getException()
                    Log.e("API_ERROR", "Failed to fetch categories: ${error.message}")
                    runOnUiThread {
                        Toast.makeText(this, "Failed to load categories", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
