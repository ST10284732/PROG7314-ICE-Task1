package vcmsa.projects.prog7314_icetask1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MealAdapter(
    private val meals: List<Meal>,
    private val onItemClick: (Meal) -> Unit
) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.mealImageView)
        val textView: TextView = itemView.findViewById(R.id.mealNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meals, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]
        holder.textView.text = meal.strMeal
        Glide.with(holder.itemView.context).load(meal.strMealThumb).into(holder.imageView)

        holder.itemView.setOnClickListener {
            onItemClick(meal)
        }
    }

    override fun getItemCount() = meals.size
}