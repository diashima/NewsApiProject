package kz.diashima.newsapiproject.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.diashima.newsapiproject.databinding.ItemArticleBinding
import kz.diashima.newsapiproject.models.News

class ArticleAdapter(private val items: List<News.Article>, private val listener: OnItemClickListener) : RecyclerView.Adapter<ArticleAdapter.HotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HotViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class HotViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }
        fun bind(article: News.Article){
            with(itemView){
                binding.articleTitle.text = article.title
                binding.articleDate.text = article.publishedAt.substring(0, 10)
                binding.authorName.text = article.author
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemArticleClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemArticleClick(position: Int)
    }
}