package fi.benson.views;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fi.benson.R;
import fi.benson.camera.ImageUtility;

public class UploadActivity extends AppCompatActivity{

    private  EditText     editText_title, editText_desc, editText_price;
    private  EditText     etPrice,etTitle,etDesc;
    private  Button       submit,btnPrice,btnTitle,btnDesc;
    private  LinearLayout lPrice,lTitle,lDesc;
    private  ImageView    imageView;
    private  Point        mSize;
    private  Bitmap       bitmap;
    private  LinearLayout addresslLayout;
    private  String       imageUrl;
    private  String       title;
    private  String       desc;
    private  Double       price;
    private  String       category = "General";
    private  String       condition = "Good";
    private  String       address = "my address 123";
    private  String       sellerId;
    private  String       sellerName;
    private  String       channel;
    private  boolean      sold = false;
    private  double       latitude = 60.45481499999999;
    private  double       longitude = 22.288123333333335;

    private int i = -1;
    private ParseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        currentUser = ParseUser.getCurrentUser();
        sellerId = currentUser.getObjectId();
        sellerName = currentUser.getUsername();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initUi();


      //  setCategoryAndCondionSpinners();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void initUi(){

        //Get passed image and load it
        String path = getIntent().getStringExtra("bitmapUrl");
        Uri uri = Uri.parse(path);
        Display display = getWindowManager().getDefaultDisplay();

        mSize = new Point();
        display.getSize(mSize);

        bitmap = ImageUtility.decodeSampledBitmapFromPath(uri.getPath(), mSize.x, mSize.x);
        imageView = (ImageView) findViewById(R.id.uploadImage);
        if (imageView != null) {
            imageView.setImageBitmap(bitmap);
        }


        lPrice   = (LinearLayout) findViewById(R.id.upload_price_layout);
        lTitle   = (LinearLayout) findViewById(R.id.upload_title_layout);
        lDesc    = (LinearLayout) findViewById(R.id.upload_desc_layout);
        etPrice  = (EditText) findViewById(R.id.et_upload_price);
        etTitle  = (EditText) findViewById(R.id.et_uploat_title);
        etDesc   = (EditText) findViewById(R.id.et_upload_desc);
        btnPrice = (Button) findViewById(R.id.btnUploadPrice);
        btnTitle = (Button) findViewById(R.id.btnUploadTitle);
        btnDesc  = (Button) findViewById(R.id.btnUploadDesc);



        // Edit text listeners
        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    btnPrice.setEnabled(false);
                } else {
                    btnPrice.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    btnTitle.setEnabled(false);
                } else {
                    btnTitle.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    btnDesc.setEnabled(false);
                } else {
                    btnDesc.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etTitle.requestFocus();
        etPrice.requestFocus();



        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = Double.valueOf(etPrice.getText().toString());
                lPrice.setVisibility(View.GONE);
                lTitle.setVisibility(View.VISIBLE);
            }
        });

        btnTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = etTitle.getText().toString();
                lTitle.setVisibility(View.GONE);
                lDesc.setVisibility(View.VISIBLE);
            }
        });

        btnDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        /*

        editText_desc  = (EditText) findViewById(R.id.et_desc);
        editText_title = (EditText) findViewById(R.id.et_title);
        editText_price = (EditText) findViewById(R.id.et_price);
        submit         = (Button) findViewById(R.id.btn_post);

        addresslLayout = (LinearLayout) findViewById(R.id.advancedLayout);
        addresslLayout.setVisibility(View.GONE);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
            }
        });

        */


    }


    /*

    //toogle the address layout visibility
    public void onShowAdvClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.ShowAdvCheckbox:
                if (checked) {
                    addresslLayout.setVisibility(View.VISIBLE);
                } else
                    addresslLayout.setVisibility(View.GONE);
                break;

        }
    } */

    public void setCategoryAndCondionSpinners() {
        //category spinner
        String[] list = getResources().getStringArray(R.array.categories);
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner1);
        spinner.setItems(list);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                category = item;
            }
        });
        spinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });


        //contion spinner
        final String[] conditions = getResources().getStringArray(R.array.conditions);
        MaterialSpinner conditionSpinner = (MaterialSpinner) findViewById(R.id.spinnerCondition);
        conditionSpinner.setItems(conditions);
        conditionSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                condition = item;
            }
        });
        spinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });

    }

    public void uploadPost(){

       showDialog();

        //create channel key
        channel = UUID.randomUUID().toString();

        //save user geopoint
        final ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);

        //save the image file, on success, save data aswell
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
        final ParseFile pFile = new ParseFile(stream.toByteArray());

        pFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                //create a complete object to be saved
                ParseObject object = new ParseObject("Post");

                object.put("image",     pFile);
                object.put("price",     Double.valueOf(editText_price.getText().toString()));
                object.put("title",     editText_title.getText().toString() );
                object.put("desc",      editText_desc.getText().toString());
                object.put("category",  category);
                object.put("condition", condition);
                object.put("sellerId",  sellerId);
                object.put("sellerName",sellerName);
                object.put("channel",   channel);
                object.put("address",   address);
                object.put("point",     point);
                object.put("sold",      sold);


                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){

                        }else {
                            Toast.makeText(UploadActivity.this, "Error:" + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    public void showDialog(){
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#006767"));
        pDialog.setTitleText("Saving your post");
        pDialog.setCancelable(false);
        pDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        pDialog.dismiss();
                        finish();

                    }
                }, 3000);

    }


}
