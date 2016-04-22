package fi.benson.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import fi.benson.R;
import fi.benson.models.Posts;


/**
 * Created by benson on 3/14/16.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Bitmap myBitmap;
    private List<Posts> list;
    private Context context;

    public PostAdapter(Context context, List<Posts> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse_posts, parent, false);
        return new ViewHolder(view, context, list);
    }

    @Override
    public void onBindViewHolder(final PostAdapter.ViewHolder holder, int position) {

        final Posts post = list.get(position);

        Uri uri = Uri.parse(post.getImageUrl());
        holder.draweeView.setImageURI(uri);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        //Setup Views
        public SimpleDraweeView draweeView;


        public ViewHolder(final View itemView, Context ctx, List<Posts> itemPosts) {

            super(itemView);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.placeImage);

        }
    }


}