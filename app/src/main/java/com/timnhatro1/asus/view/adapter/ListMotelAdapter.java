package com.timnhatro1.asus.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.timnhatro1.asus.view.activity.R;
import com.timnhatro1.asus.interactor.model.motel.MotelModel;
import com.timnhatro1.asus.utils.ItemAnimation;
import com.timnhatro1.asus.utils.StringUtils;
import com.timnhatro1.asus.utils.Tools;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListMotelAdapter extends RecyclerView.Adapter<ListMotelAdapter.ViewHolder> {

    private List<MotelModel> listMotel;
    private Context context;
    private OnClickItemListener onClickItemListener;

    public ListMotelAdapter(List<MotelModel> listMotel, Context context,OnClickItemListener onClickItemListener) {
        this.listMotel = listMotel;
        this.context = context;
        this.onClickItemListener = onClickItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_motel_note,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        MotelModel model = listMotel.get(position);
        String[] listPicture = model.getListPicture().split(" ");
        if (listPicture.length > 0) {
            Tools.loadImage(context,listPicture[0], holder.mIvPictureFirst, R.drawable.default_image_thumbnail,R.drawable.default_image_thumbnail,true);
        }
        holder.mTvTitle.setText(model.getTitle());
        holder.mTvAddress.setText(model.getAddress());
        holder.mTvPrice.setText(model.getPrice());
        holder.mTvTimePost.setText(Tools.howLongFrom(model.getTimePost()));
        holder.mViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickItemListener!=null)
                    onClickItemListener.onClickItem(position);
            }
        });
        if (model.isSave()) {
            if (!StringUtils.isEmpty(model.getNote())) {
                holder.mLLNoted.setVisibility(View.VISIBLE);
                holder.mTvNote.setText(model.getNote());
            } else {
                holder.mLLNoted.setVisibility(View.GONE);
            }
        } else {
            holder.mLLNoted.setVisibility(View.GONE);
        }
        setAnimation(holder.itemView,position);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return listMotel == null? 0 : listMotel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_picture_first)
        ImageView mIvPictureFirst;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_price)
        TextView mTvPrice;
        @BindView(R.id.tv_time_post)
        TextView mTvTimePost;
        @BindView(R.id.tv_address)
        TextView mTvAddress;
        @BindView(R.id.view_item_motel)
        View mViewItem;
        @BindView(R.id.ll_noted)
        View mLLNoted;
        @BindView(R.id.tv_note)
        TextView mTvNote;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public interface OnClickItemListener {
        void onClickItem(int position);
    }
    private int lastPosition = -1;
    private boolean on_attach = true;

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, ItemAnimation.FADE_IN);
            lastPosition = position;
        }
    }
}
