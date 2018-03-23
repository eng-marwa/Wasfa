package com.mr.java.shno.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mr.java.shno.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by java on 10/11/2017.
 */

public class NestedRecyclerAdapter extends RecyclerView.Adapter<NestedRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<String> data;

    public NestedRecyclerAdapter(Context context, List<String> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.new_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public void removeItem(int position) {
        data.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        System.out.println("removed "+position);
        notifyItemRemoved(position);
    }

    public void restoreItem(String item, int position) {
        data.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        public TextView textView;
        @BindView(R.id.view_background)
        public RelativeLayout viewbackground;
        @BindView(R.id.view_foreground)
        public RelativeLayout viewforground;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
