package fi.benson.views;


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
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.Date;

import fi.benson.R;

public class DetailActivity extends AppCompatActivity {

    Intent intent;
    public SimpleDraweeView draweeView;
    private TextView dTextView, dPriceView, dDescView, dCategoryView, dCreatedView, dConditionView;

    ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        currentUser = ParseUser.getCurrentUser();
        initUi();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView chatImageView = (ImageView) findViewById(R.id.chatsBtn);
        if (chatImageView != null) {
            chatImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (currentUser != null) {
                      //  intent = new Intent(DetailActivity.this, ChatActivity.class);
                       // intent.putExtra("channel", getIntent().getStringExtra("channel"));
                      //  startActivity(intent);
                    } else {
                       // intent = new Intent(DetailActivity.this, LoginActivity.class);
                      //  intent.putExtra("channel", getIntent().getStringExtra("channel"));
                      //  startActivity(intent);
                    }
                }
            });
        }


    }

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

        dPriceView.setText(getIntent().getStringExtra("price"));
        dPriceView.setTypeface(EasyFonts.droidSerifBold(this));

        dDescView.setText(getIntent().getStringExtra("desc"));
        dDescView.setTypeface(EasyFonts.droidSerifItalic(this));

        dCategoryView.setText(getIntent().getStringExtra("category"));
        dCategoryView.setTypeface(EasyFonts.ostrichBlack(this));

        dConditionView.setText(getIntent().getStringExtra("condition"));
        dConditionView.setTypeface(EasyFonts.ostrichBlack(this));

        Date date = new Date();
        date.setDate((int) getIntent().getLongExtra("date", -1));

        dCreatedView.setText(date.toString());
        dCreatedView.setTypeface(EasyFonts.ostrichBlack(this));


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