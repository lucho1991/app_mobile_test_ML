package com.services.testmeli.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.services.testmeli.Model.Item;
import com.services.testmeli.Model.ListItems;
import com.services.testmeli.R;



public class ItemsRecyclerAdapter extends RecyclerView.Adapter<ItemsRecyclerAdapter.ViewHolder> implements View.OnClickListener{
    private ListItems results;
    private Context context;
    private View.OnClickListener onClickItemListener;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isLoading = false, isLastPage = false;

    public ItemsRecyclerAdapter(ListItems results, Context context) {
        this.results = results;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_format, parent, false);
        view.setOnClickListener(this);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holders, int position) {
        final ViewHolder holder = holders;
        Item item = results.getResults().get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(item.getThumbnail())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);

        holder.title.setText(item.getTitle());
        holder.price.setText(item.getPrice());
    }


    @Override
    public int getItemCount() {
        return results.getResults().size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, price;
        ImageView imageView;

        public ViewHolder(View itemView){
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.name_item_list);
            price = (TextView) itemView.findViewById(R.id.price_item_list);
            imageView = (ImageView) itemView.findViewById(R.id.icon_item_list);

        }
    }


    //Implement View.OnClickListener
    @Override
    public void onClick(View view) {
        if(onClickItemListener!=null){
            onClickItemListener.onClick((view));
        }
    }


    public void setOnClickListener(View.OnClickListener listener){
        this.onClickItemListener = listener;
    }

}
