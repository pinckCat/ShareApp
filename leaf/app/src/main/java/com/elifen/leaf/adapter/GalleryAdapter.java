package com.elifen.leaf.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elifen.leaf.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elifen on 2017/4/19.
 */

public class GalleryAdapter extends
        RecyclerView.Adapter<GalleryAdapter.ViewHolder>{
    public final int TYPE_CAMERA = 1;      //显示的图片是添加按钮
    public final int TYPE_PICTURE = 2;      //显示的是图片
    private LayoutInflater mInflater;
    private Context mContext;
    private String imagePath;  //图片路径
    private List<String> list = new ArrayList<>();
    private int selectMax = 1;               //设置只能选择一张图片

    public GalleryAdapter(Context context,  onAddPicClickListener mOnAddPicClickListener)
    {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mOnAddPicClickListener = mOnAddPicClickListener;
    }

   //设置数据
    public void setList(List<String> list) {
        this.list = list;
    }
    //添加图片的最大数
    public void setSelectMax(int selectMax) {
        this.selectMax = selectMax;
    }

    /**
     * 点击添加图片跳转
     */
    private onAddPicClickListener mOnAddPicClickListener;

    public interface onAddPicClickListener {
        void onAddPicClick(int type, int position);
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.gv_filter_image,
                viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final GalleryAdapter.ViewHolder holder, int position) {
        //开始的时候显示添加的图标
        if (list.size() == 0) {
            holder.mImg.setImageResource(R.drawable.icon_addimage);
            holder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnAddPicClickListener.onAddPicClick(0, holder.getAdapterPosition());
                }
            });
            holder.ll_del.setVisibility(View.INVISIBLE);
            holder.tv_message.setVisibility(View.VISIBLE);
        } else {
            holder.ll_del.setVisibility(View.VISIBLE);
            holder.tv_message.setVisibility(View.INVISIBLE);
            holder.ll_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnAddPicClickListener.onAddPicClick(1, holder.getAdapterPosition());
                }
            });

            //将图片显示出来
            //holder.mImg.setImageResource(R.drawable.icon_addimage);
            String path = "";
            path = list.get(position);
            File file = new File(path);
            if (file.exists()) {
                Bitmap bm = BitmapFactory.decodeFile(path);
                holder.mImg.setImageBitmap(bm);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() < selectMax) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg;         //添加图片的imageView
        LinearLayout ll_del;    //删除图片
        TextView  tv_message;   //旁边的提示信息（添加图片）
        public ViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.fiv);
            ll_del = (LinearLayout) itemView.findViewById(R.id.ll_del);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
        }
    }
}
