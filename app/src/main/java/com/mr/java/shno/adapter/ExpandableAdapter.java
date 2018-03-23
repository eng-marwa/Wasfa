package com.mr.java.shno.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


import com.mr.java.shno.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by java on 28/10/2017.
 */



public class ExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private ArrayList<String> args;
    public ExpandableAdapter(Context context, List<String> listDataHeader,
                             HashMap<String, List<String>> listChildData, ArrayList<String> args) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        this.args = args;
    }

    @Override
    public String getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition,  int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
         String childText =  getChild(groupPosition, childPosition);
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            convertView =   LayoutInflater.from(context).inflate(R.layout.child_item,parent,false);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        }else{
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        if(args!=null) {
            for (String s : args) {
                Pattern p = Pattern.compile(s);
                Matcher m = p.matcher(childText);

                if (m.find()) {
                    String coloredSpanned = getColoredSpanned(s, "#12B7A6");
                    childText = childText.replace(childText.substring(m.start(), m.end()), coloredSpanned);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        childViewHolder.childTextView.setText(Html.fromHtml(childText, Html.FROM_HTML_MODE_LEGACY));

                    } else {
                        childViewHolder.childTextView.setText(Html.fromHtml(childText));

                    }
                }
            }
        }

                childViewHolder.childTextView.setText(Html.fromHtml(childText));




        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Log.i("childSize",listDataChild.get(this.listDataHeader.get(groupPosition))
                .size()+"");

        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle =  getGroup(groupPosition);
        GroupViewHolder groupViewHolder = null;
        if (convertView == null) {
            convertView =   LayoutInflater.from(context).inflate(R.layout.group_item, parent,false);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        }else{
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.groupTextView.setText(headerTitle);
        return convertView;
    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder{
        @BindView(R.id.text1)
        TextView groupTextView;
        public GroupViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    } class ChildViewHolder{
        @BindView(R.id.text1)
        TextView childTextView;
        public ChildViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }
}
