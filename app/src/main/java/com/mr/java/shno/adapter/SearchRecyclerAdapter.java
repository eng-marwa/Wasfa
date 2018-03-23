package com.mr.java.shno.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by java on 28/10/2017.
 */

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.MyViewHolder>{
    private Context context;
    private List<Maindish> maindishes;
    public SearchRecyclerAdapter(Context context) {
        this.context = context;
    }
    private ArrayList<String> args;
    private List<String> mainkey;
    public void setData(ArrayList<Maindish> maindishes, ArrayList<String> mainkey, ArrayList<String> args) {
        this.maindishes = maindishes;
        this.args =args;
        this.mainkey = mainkey;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_row,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            Collections.sort(maindishes);
            Maindish maindish = maindishes.get(position);
            System.out.println(maindish.getName());
            holder.textView.setText(maindish.getName());
            if (maindish != null) {
                Picasso.with(context).load(maindish.getImage()).into(holder.imageView);
            } else {
                Picasso.with(context).load(R.mipmap.ic_launcher).into(holder.imageView);

            }
        }catch (Exception e){
        }
    }
    public void delete(int position){
        maindishes.remove(position);
        notifyItemRemoved(position);
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
                    Intent i = new Intent(context,WasfatActivity.class);
                    int position = getAdapterPosition();
                    Maindish maindish = maindishes.get(position);
                    i.putExtra("maindish",maindish);
                    i.putExtra("key",mainkey.get(position));
                    System.out.println("mainkey=>"+mainkey.get(position));
                    i.putExtra("from","search");
                    i.putExtra("args",args);
                   ((AppCompatActivity)context).startActivityForResult(i,0);
                }
            });
        }
    }
}
