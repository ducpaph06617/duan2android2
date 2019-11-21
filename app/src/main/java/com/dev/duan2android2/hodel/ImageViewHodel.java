package com.dev.duan2android2.hodel;


import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.dev.duan2android2.R;

public class ImageViewHodel extends RecyclerView.ViewHolder {
    public ImageView img;




    public ImageViewHodel(View itemView) {
        super(itemView);
        img=itemView.findViewById(R.id.my_image_view);

    }
}
