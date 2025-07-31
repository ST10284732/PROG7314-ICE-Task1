package vcmsa.projects.prog7314_icetask1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CategoryAdapter(
    private val categories: List<MealCategory>,
    private val onClick: (MealCategory) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.txtCategoryName)
        val categoryImage: ImageView = itemView.findViewById(R.id.imgCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryName.text = category.strCategory
        Glide.with(holder.itemView.context)
            .load(category.strCategoryThumb)
            .into(holder.categoryImage)

        holder.itemView.setOnClickListener { onClick(category) }
    }

    override fun getItemCount(): Int = categories.size
}