package com.timnhatro1.asus.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.timnhatro1.asus.view.activity.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChoosePictureRecyclerAdapter extends RecyclerView.Adapter<ChoosePictureRecyclerAdapter.ViewHoder> {

    private Context context;
    private List<Bitmap> listBitmap;

    public ChoosePictureRecyclerAdapter(Context context, List<Bitmap> listBitmap) {
        this.context = context;
        this.listBitmap = listBitmap;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_picture_post_motel,parent,false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, final int position) {
        Bitmap bitmap = listBitmap.get(position);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_image_thumbnail)
                .error(R.drawable.default_image_thumbnail)
                .dontAnimate();
        Glide.with(context).asBitmap().load(bitmap).apply(options).into(holder.mIvImage);
        holder.mIvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listBitmap.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    public List<Bitmap> getListBitmap() {
        return listBitmap;
    }

    @Override
    public int getItemCount() {
        return listBitmap == null ? 0 : listBitmap.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        ImageView mIvImage;
        @BindView(R.id.iv_remove)
        ImageView mIvRemove;
        public ViewHoder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
