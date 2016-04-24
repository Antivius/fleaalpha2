package fi.benson.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import fi.benson.R;
import fi.benson.models.Posts;
import fi.benson.views.DetailActivity;


/**
 * Created by benson on 3/14/16.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Setup Views
        public SimpleDraweeView draweeView;

        List<Posts> itemPosts = new ArrayList<Posts>();
        Context ctx;


        public ViewHolder(final View itemView, Context ctx, List<Posts> itemPosts) {

            super(itemView);
            this.itemPosts = itemPosts;
            this.ctx = ctx;
            itemView.setOnClickListener(this);


            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.placeImage);

        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            Posts itemPost = this.itemPosts.get(position);

            Intent intent = new Intent(this.ctx, DetailActivity.class);
            intent.putExtra("url",      itemPost.getImageUrl());
            intent.putExtra("title",    itemPost.getTitle());
            intent.putExtra("price",    itemPost.getPrice());
            intent.putExtra("desc",     itemPost.getDesc());
            intent.putExtra("address",  itemPost.getAddress());
            intent.putExtra("latitude", itemPost.getLatitude());
            intent.putExtra("longitude",itemPost.getLongitude());
            intent.putExtra("category", itemPost.getCategory());
            intent.putExtra("created",  itemPost.getCreatedAt());
            intent.putExtra("condition",itemPost.getCondition());
            intent.putExtra("channel",  itemPost.getChannel());
            intent.putExtra("sellerId", itemPost.getSellerId());
            intent.putExtra("isSold",   itemPost.isSold());



            ctx.startActivity(intent);
        }
    }


}