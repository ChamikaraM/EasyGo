package com.example.chamiya.egot;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Chamiya on 2/23/2018.
 */

public class ListOnlineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    com.example.chamiya.egot.itemClickListener itemClickListener;
    public TextView txtEmail;
    public ListOnlineViewHolder(View itemView){
        super(itemView);
        txtEmail = (TextView)itemView.findViewById(R.id.txt_email);
        itemView.setOnClickListener(this);   //not in the tuto
    }

    public void setItemClickListener(com.example.chamiya.egot.itemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition());
    }
}
