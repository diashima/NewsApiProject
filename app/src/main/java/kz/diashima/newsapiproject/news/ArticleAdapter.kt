package kz.diashima.newsapiproject.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.diashima.newsapiproject.databinding.ItemArticleBinding
import kz.diashima.newsapiproject.models.News

class ArticleAdapter(private val items: List<News.Article>) : RecyclerView.Adapter<ArticleAdapter.HotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HotViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class HotViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: News.Article){
            with(itemView){
                binding.articleTitle.text = article.title
                binding.articleDate.text = article.publishedAt.substring(0, 10)
                binding.authorName.text = article.author
            }
        }
    }
}