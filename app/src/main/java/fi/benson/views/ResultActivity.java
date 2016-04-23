package fi.benson.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import java.util.ArrayList;

import fi.benson.R;
import fi.benson.adapters.PostAdapter;
import fi.benson.models.Posts;

public class ResultActivity extends AppCompatActivity {


    private ArrayList<Posts> posts = new ArrayList<>();

    private RecyclerView recycler;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        if (posts.size() < 1) {
            Toast.makeText(ResultActivity.this, "None found", Toast.LENGTH_SHORT).show();
        }


        recycler = (RecyclerView) findViewById(R.id.resultRecycler);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setLayoutManager(mStaggeredLayoutManager);
        recycler.setHasFixedSize(true);
        postAdapter = new PostAdapter(ResultActivity.this, posts);
        recycler.setAdapter(postAdapter);
    }
}
