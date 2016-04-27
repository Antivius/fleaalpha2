package fi.benson.views;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.Date;

import fi.benson.R;

public class DetailActivity extends AppCompatActivity {

    Intent intent;
    public SimpleDraweeView draweeView,sellerDrawee;
    private TextView dTextView, dPriceView, dDescView, dCategoryView, dCreatedView, dConditionView;

    private TextView drawerName;
    ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        currentUser = ParseUser.getCurrentUser();
        initUi();


        sellerDrawee = (SimpleDraweeView) findViewById(R.id.imageusersmall);
        drawerName = (TextView) findViewById(R.id.textViewSellerName);

        if (!(currentUser == null)){
            drawerName.setText(currentUser.getUsername());
            sellerDrawee.setImageURI(Uri.parse("https://scontent-arn2-1.xx.fbcdn.net/v/l/t1.0-9/10450936_10153530497392044_420639374741255249_n.jpg?oh=1135da01b5f254290e4ce35f76230bca&oe=57B9DA3B"));

        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView chatImageView = (ImageView) findViewById(R.id.chatsBtn);
        if (chatImageView != null) {
            chatImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (currentUser != null) {
                        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                        installation.put("user",ParseUser.getCurrentUser());
                        installation.saveInBackground();

                        intent = new Intent(DetailActivity.this, ChatActivity.class);
                        intent.putExtra("channel", getIntent().getStringExtra("channel"));startActivity(intent);
                    } else {
                       intent = new Intent(DetailActivity.this, LoginActivity.class);
                       intent.putExtra("channel", getIntent().getStringExtra("channel"));
                       startActivity(intent);
                    }
                }
            });
        }


    }

    @SuppressLint("SetTextI18n")
    private void initUi() {

        draweeView = (SimpleDraweeView) findViewById(R.id.detailImage);
        dPriceView = (TextView) findViewById(R.id.textViewPrice);
        dTextView = (TextView) findViewById(R.id.textViewtitle);
        dDescView = (TextView) findViewById(R.id.textViewDesc);

        dCategoryView = (TextView) findViewById(R.id.textViewCategory);
        dCreatedView = (TextView) findViewById(R.id.textViewTime);
        dConditionView = (TextView) findViewById(R.id.textViewCondition);

        Uri uri = Uri.parse(getIntent().getStringExtra("url"));
        draweeView.setImageURI(uri);

        dTextView.setText(getIntent().getStringExtra("title"));

        dPriceView.setText(getIntent().getStringExtra("price") + "€");
        dPriceView.setTypeface(EasyFonts.droidSerifBold(this));

        dDescView.setText(getIntent().getStringExtra("desc"));
        dDescView.setTypeface(EasyFonts.droidSerifItalic(this));

        dCategoryView.setText(getIntent().getStringExtra("category"));
        dCategoryView.setTypeface(EasyFonts.droidSerifItalic(this));

        dConditionView.setText(getIntent().getStringExtra("condition"));
        dConditionView.setTypeface(EasyFonts.droidSerifItalic(this));

        Date date = new Date();
        date.setDate((int) getIntent().getLongExtra("date", -1));

        dCreatedView.setText(date.toString());
        dCreatedView.setTypeface(EasyFonts.droidSerifItalic(this));


    }


    public void addToMyFavorites(View View) {

        //// TODO: 4/13/16 associate with teh seller, not the item

        ParseObject favorite = new ParseObject("Favorite");
        favorite.put("favoriteKey", "favoriteKey");
        favorite.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(DetailActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

}