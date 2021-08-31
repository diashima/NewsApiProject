package kz.diashima.newsapiproject.news.top

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kz.diashima.newsapiproject.ApiClient
import kz.diashima.newsapiproject.ApiInterface
import kz.diashima.newsapiproject.Variables
import kz.diashima.newsapiproject.databinding.FragmentTopHeadlinesBinding
import kz.diashima.newsapiproject.models.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopHeadlinesFragment : Fragment() {

    private var _binding: FragmentTopHeadlinesBinding? = null
    private val binding get() = _binding!!

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
        searchTop()
    }

    private fun searchTop() {
        val retrofit = ApiClient.getRetrofitClient(requireContext())
        val newsInterface = retrofit.create(ApiInterface::class.java)

        val call = newsInterface.getTopHeadlines("Apple", Variables.apiKey, 15, 1)
        call.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                Log.d("Test", response.body().toString())
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}