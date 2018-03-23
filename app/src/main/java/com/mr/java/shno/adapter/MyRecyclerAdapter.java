package com.mr.java.shno.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mr.java.shno.Maindish;
import com.mr.java.shno.R;
import com.mr.java.shno.WasfatActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by java on 28/10/2017.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>{
    private Context context;
    private List<Maindish> maindishes;
    public MyRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Maindish> maindishes, ArrayList<String> args) {
        System.out.println("size  "+maindishes.size());
        this.maindishes = maindishes;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.fav_row,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Maindish maindish = maindishes.get(position);
        holder.textView.setText(maindish.getName());
        Picasso.with(context).load(maindish.getImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return maindishes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.thumbnail)
        ImageView imageView;
        @BindView(R.id.article_title)
        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Maindish maindish = maindishes.get(getAdapterPosition());
                    Intent i = new Intent(context,WasfatActivity.class);
                    i.putExtra("maindish",maindishes.get(getAdapterPosition()));
                    i.putExtra("key",maindish.getKey());
                    i.putExtra("from","fav");
                    context.startActivity(i);
                }
            });
        }
    }
}
