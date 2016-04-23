package fi.benson.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by bkamau on 4/23/16.
 */
public class ResultsArray implements Parcelable {

    public static final Parcelable.Creator<ResultsArray> CREATOR = new Parcelable.Creator<ResultsArray>() {
        @Override
        public ResultsArray createFromParcel(Parcel source) {
            return new ResultsArray(source);
        }

        @Override
        public ResultsArray[] newArray(int size) {
            return new ResultsArray[size];
        }
    };
    ArrayList<Posts> arrayList;

    public ResultsArray() {
    }

    protected ResultsArray(Parcel in) {
        this.arrayList = in.createTypedArrayList(Posts.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(arrayList);
    }
}
