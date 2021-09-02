package kz.diashima.newsapiproject.news.top

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.diashima.newsapiproject.ApiClient
import kz.diashima.newsapiproject.ApiInterface
import kz.diashima.newsapiproject.Variables
import kz.diashima.newsapiproject.databinding.FragmentTopHeadlinesBinding
import kz.diashima.newsapiproject.models.News
import kz.diashima.newsapiproject.news.ArticleAdapter
import kz.diashima.newsapiproject.news.PageAdapter
import kz.diashima.newsapiproject.news.details.DetailsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopHeadlinesFragment : Fragment(), PageAdapter.OnItemClickListener, ArticleAdapter.OnItemClickListener {

    private var _binding: FragmentTopHeadlinesBinding? = null
    private val binding get() = _binding!!
    private var listInt = mutableListOf(1)
    private val scope = MainScope()
    private var job: Job? = null
    private var totalNews = 0
    private var currentPage = 1
    private val retrofit = ApiClient.getRetrofitClient()
    private val newsInterface = retrofit.create(ApiInterface::class.java)
    private lateinit var listArticles: List<News.Article>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopHeadlinesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchTop(1)
    }

    override fun onStart() {
        super.onStart()
        startUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopUpdates()
    }

    private fun searchTop(page: Int) {

        val call = newsInterface.getTopHeadlines("Apple", Variables.apiKey, 15, page)
        call.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        totalNews = response.body()!!.totalResults
                        Log.d("Test", response.body()!!.totalResults.toString())
                        if (response.body()!!.totalResults > 15) {
                            val pageCount =
                                kotlin.math.ceil(response.body()!!.totalResults / 15.0)

                            for (i in 2..pageCount.toInt()) {
                                if (!listInt.contains(i))
                                {
                                    listInt.add(i)
                                }
                            }
                        }
                        currentPage = page
                        val stringPage = "Page $currentPage"
                        binding.textPage.text = stringPage
                        listArticles = response.body()!!.articles
                        Log.d("Test", "News updated")
                        binding.recyclerTop.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = ArticleAdapter(listArticles, this@TopHeadlinesFragment)
                        }

                        binding.recyclerTopPage.apply {
                            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            adapter = PageAdapter(listInt, this@TopHeadlinesFragment)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun checkIfNewAppeared() : Boolean {
        var answer = false
        val call = newsInterface.getTopHeadlines("Apple", Variables.apiKey, 15, 1)
        call.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        if (totalNews != response.body()!!.totalResults) {
                            answer = true
                        }
                    }
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {

            }

        })
        return answer
    }

    private fun startUpdates() {
        stopUpdates()
        job = scope.launch {
            while(true) {
                delay(5000L)
                if (checkIfNewAppeared()) {
                    searchTop(currentPage)
                } else {
                    //showSnack("No new news")
                }
            }
        }
    }

    private fun stopUpdates() {
        job?.cancel()
        job = null
    }

    override fun onItemClick(position: Int) {
        searchTop(position + 1)
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