package fi.benson.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import co.dift.ui.SwipeToAction;
import fi.benson.R;
import fi.benson.models.Posts;

/**
 * Created by bkamau on 24/04/16.
 */
public class ProfileListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Posts> items;


    /** References to the views for each data item **/
    public class PostViewHolder extends SwipeToAction.ViewHolder<Posts> {
        public TextView titleView;
        public TextView authorView;
        public ImageView marksold;
        public SimpleDraweeView imageView;

        public PostViewHolder(View v) {
            super(v);

            titleView = (TextView) v.findViewById(R.id.title);
            authorView = (TextView) v.findViewById(R.id.author);
            marksold   = (ImageView) v.findViewById(R.id.marksold);
            imageView = (SimpleDraweeView) v.findViewById(R.id.image);
        }
    }

    /** Constructor **/
    public ProfileListAdapter(List<Posts> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_list, parent, false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Posts item = items.get(position);
        PostViewHolder vh = (PostViewHolder) holder;

        if (item.isSold()){
            vh.marksold.setImageResource(R.drawable.ic_sold);
        }
        vh.titleView.setText(item.getTitle());
        vh.authorView.setText(item.getDesc());
        vh.imageView.setImageURI(Uri.parse(item.getImageUrl()));
        vh.data = item;
    }
}