package com.mr.java.shno;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mr.java.shno.adapter.SearchRecyclerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchWasfa extends Fragment {
    @BindView(R.id.hrv)
    RecyclerView recyclerView;
    private SearchRecyclerAdapter adapter;

    public SearchWasfa() {
        // Required empty public constructor
    }


    public static SearchWasfa newInstance(ArrayList<Maindish> maindishesList, ArrayList<String> maindishesKeyList, ArrayList<String> args) {
        SearchWasfa fragment = new SearchWasfa();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("param1", args);
        bundle.putParcelableArrayList("param2", maindishesList);
        bundle.putStringArrayList("param3", maindishesKeyList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_wasfa, container, false);
        ButterKnife.bind(this, rootView);
        ((Shno) getActivity()).homeBtn.setImageResource(R.drawable.ic_home);
        ((Shno) getActivity()).homeBtn.setBackgroundResource(R.color.colorPrimary);
        setupRecycler();
        return rootView;
    }

    private void setupRecycler() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new SearchRecyclerAdapter(getActivity());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList<Maindish> maindishesList = bundle.getParcelableArrayList("param2");
            ArrayList<String> maindishesKeyList = bundle.getStringArrayList("param3");
            ArrayList<String> arg = bundle.getStringArrayList("param1");
            System.out.println("Search=>"+maindishesKeyList);
            adapter.setData(maindishesList,maindishesKeyList,arg);
            adapter.notifyDataSetChanged();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                recyclerView.setBackgroundColor(getResources().getColor(R.color.white, null));
            } else {
                //noinspection deprecation
                recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
            }
        }
    }


            public boolean onBackKeyPressed(){
        return true;
            }




}

