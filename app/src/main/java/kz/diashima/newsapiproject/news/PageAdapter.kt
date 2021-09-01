package kz.diashima.newsapiproject.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.diashima.newsapiproject.databinding.ItemPageBinding

class PageAdapter(val items: List<Int>) : RecyclerView.Adapter<PageAdapter.PageViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val binding = ItemPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class PageViewHolder(private val binding: ItemPageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(page: Int) {

        }
    }
}