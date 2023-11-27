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
import com.timnhatro1.asus.interactor.model.notify.RegisterNotify;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotifyRegisterAdapter extends RecyclerView.Adapter<NotifyRegisterAdapter.ViewHolder> {
    private Context context;
    private List<RegisterNotify> listData;
    private OnClickDelete onClickDelete;

    public NotifyRegisterAdapter(Context context, List<RegisterNotify> listData, OnClickDelete onClickDelete) {
        this.context = context;
        this.listData = listData;
        this.onClickDelete = onClickDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dang_ky_khu_vuc,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final RegisterNotify model = listData.get(position);
        holder.mTvTitle.setText("Khu vực: "+model.getNameArea());
        holder.mTvTimeRegister.setText("Đăng ký từ: " +model.getDateRegister());
        holder.mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickDelete!=null)
                    onClickDelete.onClickDeleteNotify(position,model.getCodeArea(),model.getNameArea());
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickDelete!=null)
                    onClickDelete.onClickItem(model.getCodeArea());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData == null ? 0 : listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_picture_first)
        ImageView mIvPicture;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_time_register)
        TextView mTvTimeRegister;
        @BindView(R.id.iv_delete)
        ImageView mIvDelete;
        @BindView(R.id.ll_view_container)
        View mView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnClickDelete {
        void onClickDeleteNotify(int position,String codeArea,String nameArea);
        void onClickItem(String codeArea);
    }
}
