package fi.benson.views;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import co.dift.ui.SwipeToAction;
import fi.benson.R;
import fi.benson.adapters.ProfileListAdapter;
import fi.benson.models.Posts;

public class ProfileActivity extends AppCompatActivity {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;

    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;


    private LinearLayout mLlTitleContainer;
    private FrameLayout mFlTitleContainer;
    private AppBarLayout mAblAppBar;
    private TextView mTvToolbarTitle;
    private Toolbar mTbToolbar;
    private TextView pName;
    private TextView pTitle;
    private ImageView profImageview;

    private SimpleDraweeView mIvPlaceholder;
    private SimpleDraweeView profCircleImageViewDrawee;

    private static ParseUser currentUser;
    private boolean noPostsReturned;


    private RecyclerView recyclerView;
    private ProfileListAdapter adapter;
    private SwipeToAction swipeToAction;

    List<Posts> posts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        currentUser = ParseUser.getCurrentUser();

        initUi();

        if (!(currentUser == null)){
            pName.setText(currentUser.getUsername());
            pTitle.setText(currentUser.getUsername());
            profCircleImageViewDrawee.setImageURI(Uri.parse("https://scontent-arn2-1.xx.fbcdn.net/v/l/t1.0-9/10450936_10153530497392044_420639374741255249_n.jpg?oh=1135da01b5f254290e4ce35f76230bca&oe=57B9DA3B"));
            mIvPlaceholder.setImageURI(Uri.parse("https://scontent-arn2-1.xx.fbcdn.net/v/l/t1.0-9/10450936_10153530497392044_420639374741255249_n.jpg?oh=1135da01b5f254290e4ce35f76230bca&oe=57B9DA3B"));

        }else {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        }


        mTbToolbar.setTitle("");

        // AppBar monitoring
        mAblAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                handleAlphaOnTitle(percentage);
                handleToolbarTitleVisibility(percentage);
            }
        });

        initParallaxValues();
    }

    public void initUi(){

        pTitle = (TextView) findViewById(R.id.profileTile);
        pName = (TextView) findViewById(R.id.ProfileName);
        profCircleImageViewDrawee = (SimpleDraweeView) findViewById(R.id.profileCircleImage);
        mIvPlaceholder = (SimpleDraweeView) findViewById(R.id.main_iv_placeholder);
        mLlTitleContainer = (LinearLayout) findViewById(R.id.main_ll_title_container);
        mFlTitleContainer = (FrameLayout) findViewById(R.id.main_fl_title);
        mAblAppBar = (AppBarLayout) findViewById(R.id.main_abl_app_bar);
        mTvToolbarTitle = (TextView) findViewById(R.id.profileTile);
        mTbToolbar = (Toolbar) findViewById(R.id.main_tb_toolbar);



        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new ProfileListAdapter(this.posts);
        recyclerView.setAdapter(adapter);

        swipeToAction = new SwipeToAction(recyclerView, new SwipeToAction.SwipeListener<Posts>() {


            @Override
            public boolean swipeLeft(final Posts itemData) {

                markAsSold(itemData.getObjectId());
                displaySnackbar(itemData.getObjectId() + " sold", "Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        markAsNotSold(itemData.getObjectId());
                    }
                });
                return true;
            }

            @Override
            public boolean swipeRight(Posts itemData) {
                return true;
            }

            @Override
            public void onClick(Posts itemData) {

            }

            @Override
            public void onLongClick(Posts itemData) {

            }
        });

        loadData();

    }

    // Set up automatic sliding animation
    private void initParallaxValues() {
        CollapsingToolbarLayout.LayoutParams petDetailsLp =
                (CollapsingToolbarLayout.LayoutParams) mIvPlaceholder.getLayoutParams();

        CollapsingToolbarLayout.LayoutParams petBackgroundLp =
                (CollapsingToolbarLayout.LayoutParams) mFlTitleContainer.getLayoutParams();

        petDetailsLp.setParallaxMultiplier(0.9f);
        petBackgroundLp.setParallaxMultiplier(0.3f);

        mIvPlaceholder.setLayoutParams(petDetailsLp);
        mFlTitleContainer.setLayoutParams(petBackgroundLp);
    }

    // Display processing ToolBar
    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTvToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTvToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    // Control Table Display
    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mLlTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mLlTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    // Set the gradient animation
    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }




    public void loadData() {

        if (!(currentUser == null)){

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
            query.whereEqualTo("sellerId", currentUser.getObjectId());

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null){
                        for (int i = 0; i < objects.size(); i++){
                            ParseObject object = objects.get(i);
                            Posts post = new Posts();
                            post.setImageUrl(object.getParseFile("image").getUrl());
                            post.setTitle(object.getString("title"));
                            post.setDesc(object.getString("desc"));
                            post.setObjectId(object.getObjectId());
                            posts.add(post);
                        }
                    }else {
                        noPostsReturned = true;
                    }


                    adapter.notifyDataSetChanged();
                }
            });
        }else {
            displaySnackbar("Your items will appear here",null,null);
        }



    }



    private void markAsSold(String objectId){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        // Retrieve the object by id
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject post, ParseException e) {
                if (e == null) {
                    post.put("sold", true);
                    post.saveInBackground();
                }
            }
        });
    }


    private void markAsNotSold(String objectId){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");

        // Retrieve the object by id
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject post, ParseException e) {
                if (e == null) {
                    post.put("sold", true);
                    post.saveInBackground();
                }
            }
        });
    }

    private void displaySnackbar(String text, String actionName, View.OnClickListener action) {
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG)
                .setAction(actionName, action);

        View v = snack.getView();
        v.setBackgroundColor(getResources().getColor(R.color.register));
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_action)).setTextColor(Color.BLACK);

        snack.show();
    }


}
