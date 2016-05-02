package fi.benson.customs;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.interceptors.ParseLogInterceptor;

import fi.benson.models.Message;

/**
 * Created by bkamau on 4/22/16.
 */
public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
        ParseObject.registerSubclass(Message.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("base") // should correspond to APP_ID env variable
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://qrash.herokuapp.com/parse/").build());

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
