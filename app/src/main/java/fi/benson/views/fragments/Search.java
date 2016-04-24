package fi.benson.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import fi.benson.R;
import fi.benson.models.Posts;
import fi.benson.views.ResultActivity;

/**
 * Created by bkamau on 04/04/16.
 */
public class Search extends Fragment {

    ArrayList<Posts> posts = new ArrayList<Posts>();
    private View rootView;
    private String searchable = "";
    private MaterialAnimatedSwitch pinElectronic, pinGameConsole,
            pinBookMusic, pinClothingFasion,
            pinCarMotors, pinSportLeisure, pinHomeGarden,
            pinNew, pinAlmostNew, pinGood, pinSatisfactory, pinPoor;
    private Button btnClear, btnSearch;
    private EditText et_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        initUi(rootView);

        return rootView;

    }

    public void initUi(View view) {

        pinElectronic = (MaterialAnimatedSwitch) view.findViewById(R.id.pinElectronic);
        pinGameConsole = (MaterialAnimatedSwitch) view.findViewById(R.id.pinGamesConsoles);
        pinBookMusic = (MaterialAnimatedSwitch) view.findViewById(R.id.pinBooksMusic);
        pinClothingFasion = (MaterialAnimatedSwitch) view.findViewById(R.id.pinClothingFasion);
        pinCarMotors = (MaterialAnimatedSwitch) view.findViewById(R.id.pinCarMotor);
        pinSportLeisure = (MaterialAnimatedSwitch) view.findViewById(R.id.pinSportsLeisure);
        pinHomeGarden = (MaterialAnimatedSwitch) view.findViewById(R.id.pinHomeGarden);
        pinNew = (MaterialAnimatedSwitch) view.findViewById(R.id.pinNew);
        pinAlmostNew = (MaterialAnimatedSwitch) view.findViewById(R.id.pinAlmostNew);
        pinGood = (MaterialAnimatedSwitch) view.findViewById(R.id.pinGood);
        pinSatisfactory = (MaterialAnimatedSwitch) view.findViewById(R.id.pinSatisfactory);
        pinPoor = (MaterialAnimatedSwitch) view.findViewById(R.id.pinPoor);


        et_search = (EditText) view.findViewById(R.id.advanced_search_view);

        btnClear = (Button) view.findViewById(R.id.btnClear);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryFilter();

            }
        });


    }


    public void queryFilter() {

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");

        if (et_search.getText().length() > 0) {
            query.whereMatches("title", et_search.getText().toString());
        }
        if (pinElectronic.isChecked()) {
            query.whereEqualTo("category", "Electronics");
        }

        if (pinGameConsole.isChecked()) {
            query.whereEqualTo("category", "Games & Console");
        }

        if (pinBookMusic.isChecked()) {
            query.whereEqualTo("category", "Books & Music");
        }
        if (pinClothingFasion.isChecked()) {
            query.whereEqualTo("category", "Clothing & Fashion");
        }
        if (pinCarMotors.isChecked()) {
            query.whereEqualTo("category", "Cars & Motors");
        }
        if (pinSportLeisure.isChecked()) {
            query.whereEqualTo("category", "Sports & Leisure");
        }
        if (pinHomeGarden.isChecked()) {
            query.whereEqualTo("category", "Home & Garden");
        }
        if (pinNew.isChecked()) {
            query.whereEqualTo("condition", "New");
        }
        if (pinAlmostNew.isChecked()) {
            query.whereEqualTo("condition", "Almost new");
        }
        if (pinGood.isChecked()) {
            query.whereEqualTo("condition", "Good");
        }
        if (pinSatisfactory.isChecked()) {
            query.whereEqualTo("condition", "Satisfactory");
        }
        if (pinPoor.isChecked()) {
            query.whereEqualTo("condition", "Poor");
        }


        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject object = objects.get(i);
                        Posts post = new Posts();
                        post.setImageUrl(object.getParseFile("image").getUrl());
                        posts.add(post);
                    }
                }
                Intent i = new Intent(getContext(), ResultActivity.class);
                i.putExtra("posts", posts);
                startActivity(i);
            }
        });


    }

}