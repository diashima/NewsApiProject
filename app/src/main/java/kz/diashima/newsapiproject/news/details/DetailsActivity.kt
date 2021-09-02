package kz.diashima.newsapiproject.news.details

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kz.diashima.newsapiproject.R
import kz.diashima.newsapiproject.models.ArticleDb

class DetailsActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance("https://newsproject-8273f-default-rtdb.europe-west1.firebasedatabase.app/")
    val reference = database.getReference("articles")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title

        val author = intent.getStringExtra("author")
        val description = intent.getStringExtra("content")
        val source = intent.getStringExtra("source")
        val title = intent.getStringExtra("title")

        findViewById<TextView>(R.id.certainTitle).text = title
        findViewById<TextView>(R.id.certainSource).text = source
        findViewById<TextView>(R.id.certainAuthor).text = author
        findViewById<TextView>(R.id.scrollingText).text = description

        val article = ArticleDb(source, author, title, description)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    reference.setValue(article)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


            Snackbar.make(view, "Статья добавлена в избранное", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun saveArticle() {

    }
}