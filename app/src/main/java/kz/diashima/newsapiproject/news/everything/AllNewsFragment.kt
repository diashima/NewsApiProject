package kz.diashima.newsapiproject.news.everything

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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllNewsFragment : Fragment() {

    private var _binding: FragmentAllNewsBinding? = null
    private val binding get() = _binding!!
    private var listInt = mutableListOf(1)

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
        searchTop()
    }

    private fun searchTop() {
        val retrofit = ApiClient.getRetrofitClient(requireContext())
        val newsInterface = retrofit.create(ApiInterface::class.java)

        val call = newsInterface.getEverything("Apple", Variables.apiKey, 15, 1)
        call.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                if (response.code() == 200) {
                    if (response.body() != null) {

                        if (response.body()!!.totalResults > 15) {
                            val pageCount =
                                kotlin.math.ceil(response.body()!!.totalResults / 15.0)

                            for (i in 2..pageCount.toInt()) {
                                listInt.add(i)
                            }
                        }

                        Log.d("Test", "News updated")
                        binding.recyclerAllNews.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = ArticleAdapter(response.body()!!.articles)
                        }

                        binding.recyclerAllPages.apply {
                            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            adapter = PageAdapter(listInt)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}