package kz.diashima.newsapiproject.news.everything

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kz.diashima.newsapiproject.ApiClient
import kz.diashima.newsapiproject.ApiInterface
import kz.diashima.newsapiproject.Variables
import kz.diashima.newsapiproject.databinding.FragmentAllNewsBinding
import kz.diashima.newsapiproject.models.News
import kz.diashima.newsapiproject.news.ArticleAdapter
import kz.diashima.newsapiproject.news.PageAdapter
import kz.diashima.newsapiproject.news.details.DetailsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllNewsFragment : Fragment(), PageAdapter.OnItemClickListener, ArticleAdapter.OnItemClickListener {

    private var _binding: FragmentAllNewsBinding? = null
    private val binding get() = _binding!!
    private var listInt = mutableListOf(1)
    private var currentPage = 1
    private lateinit var listArticles: List<News.Article>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Test", "AllNewsFragment")
        searchAllNews(1)

        binding.swipeLayout.setOnRefreshListener {
            searchAllNews(currentPage)
        }
    }

    private fun searchAllNews(page: Int) {
        val retrofit = ApiClient.getRetrofitClient()
        val newsInterface = retrofit.create(ApiInterface::class.java)

        val call = newsInterface.getEverything("Apple", Variables.apiKey, 15, page)
        call.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        binding.swipeLayout.isRefreshing = false
                        if (response.body()!!.totalResults > 15) {
                            val pageCount =
                                kotlin.math.ceil(response.body()!!.totalResults / 15.0)

                            for (i in 2..pageCount.toInt()) {
                                if (!listInt.contains(i))
                                {
                                    Log.d("Test", "contains")
                                    listInt.add(i)
                                }
                            }
                        }
                        listArticles = response.body()!!.articles
                        currentPage = page
                        val stringPage = "Page $currentPage"
                        binding.textPage.text = stringPage
                        Log.d("Test", "News updated")
                        binding.recyclerAllNews.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = ArticleAdapter(response.body()!!.articles, this@AllNewsFragment)
                        }

                        binding.recyclerAllPages.apply {
                            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            adapter = PageAdapter(listInt, this@AllNewsFragment)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onItemClick(position: Int) {
        searchAllNews(position + 1)
    }

    override fun onItemArticleClick(position: Int) {
        val intent = Intent(activity, DetailsActivity::class.java)
        intent.putExtra("author", listArticles[position].author)
        intent.putExtra("content", listArticles[position].content)
        intent.putExtra("description", listArticles[position].description)
        intent.putExtra("source", listArticles[position].source.name)
        intent.putExtra("url", listArticles[position].url)
        intent.putExtra("title", listArticles[position].title)
        activity?.startActivity(intent)
    }
}