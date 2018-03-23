package com.mr.java.shno;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mr.java.shno.adapter.MyRecyclerAdapter;
import com.mr.java.shno.db.DBHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mr.java.shno.db.Data.COL_CAT;
import static com.mr.java.shno.db.Data.COL_COOKING;
import static com.mr.java.shno.db.Data.COL_ID;
import static com.mr.java.shno.db.Data.COL_IMG;
import static com.mr.java.shno.db.Data.COL_INGREDIENTS;
import static com.mr.java.shno.db.Data.COL_KEY;
import static com.mr.java.shno.db.Data.COL_NAME;
import static com.mr.java.shno.db.Data.COL_SERVING;
import static com.mr.java.shno.db.Data.COL_STEPS;
import static com.mr.java.shno.db.Data.COL_TAG;
import static com.mr.java.shno.db.Data.COL_USER_ID;
import static com.mr.java.shno.db.Data.FAV_TABLE_NAME;


public class FavFragment extends Fragment {

    private SQLiteDatabase db;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.coord)
    FrameLayout coord;
    MyRecyclerAdapter adapter;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fav, container, false);
        ButterKnife.bind(this, rootView);
        DBHelper dbHelper = new DBHelper(getActivity());
         db = dbHelper.getReadableDatabase();
        setupRecycler();
        ((Shno)getActivity()).favBtn.setImageResource(R.drawable.ic_add_a_star_interface_symbol_fill);
        ((Shno)getActivity()).favBtn.setBackgroundResource(R.color.white);
        ((Shno)getActivity()).addBtn.setImageResource(R.drawable.ic_add_contact);
        ((Shno)getActivity()).addBtn.setBackgroundResource(R.color.colorPrimary);
        ((Shno)getActivity()).homeBtn.setImageResource(R.drawable.ic_home);
        ((Shno)getActivity()).homeBtn.setBackgroundResource(R.color.colorPrimary);
        ((Shno)getActivity()).lockBtn.setImageResource(R.drawable.ic_lock_user_symbol);
        ((Shno)getActivity()).lockBtn.setBackgroundResource(R.color.colorPrimary);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            viewAllFav();
        }else{
            ImageView img = new ImageView(getActivity());
            img.setImageResource(R.mipmap.no_persons);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.gravity= Gravity.CENTER;
            layoutParams.setMargins(50,20,50,20);
            img.setLayoutParams(layoutParams);
            coord.addView(img);
            Toast.makeText(getActivity(),"الرجاء تسجيل الدخول اولا",Toast.LENGTH_LONG).show();
        }
        return rootView;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void viewAllFav() {
        File f = new File(
                "/data/data/"+getActivity().getPackageName()+"/shared_prefs/"+FirebaseAuth.getInstance().getCurrentUser().getEmail()+".xml");
        if (f.exists()) {
            List<Maindish> maindishes = new ArrayList<>();
            String cols[] = {COL_ID, COL_NAME, COL_IMG, COL_INGREDIENTS, COL_STEPS, COL_TAG, COL_CAT, COL_SERVING, COL_COOKING , COL_KEY};
            String args[] ={FirebaseAuth.getInstance().getCurrentUser().getUid()};
            Cursor cursor = db.query(true, FAV_TABLE_NAME, cols, COL_USER_ID+"=?",args , null, null, COL_NAME, null);
            if (cursor.moveToFirst()) {
                do {
                    String tags = cursor.getString(cursor.getColumnIndex(COL_TAG));
                    String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
                    String image = cursor.getString(cursor.getColumnIndex(COL_IMG));
                    String cat = cursor.getString(cursor.getColumnIndex(COL_CAT));
                    String ingredient = cursor.getString(cursor.getColumnIndex(COL_INGREDIENTS));
                    String steps = cursor.getString(cursor.getColumnIndex(COL_STEPS));
                    int servig = cursor.getInt(cursor.getColumnIndex(COL_SERVING));
                    String time = cursor.getString(cursor.getColumnIndex(COL_COOKING));
                    int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                    String key = cursor.getString(cursor.getColumnIndex(COL_KEY));
                    Log.i("key from fav",key);
                    String newStr = ingredient.substring(1, ingredient.length() - 1);
                    List<String> ingredients = Arrays.asList(newStr.split(","));
                    List<String> dishsteps = Arrays.asList((steps.substring(1, steps.length() - 1)).split(","));
                    maindishes.add(new Maindish(id, name, image, tags, cat, servig, time, ingredients, dishsteps,key));

                } while (cursor.moveToNext());
                adapter.setData(maindishes, null);
                recyclerView.setAdapter(adapter);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    recyclerView.setBackgroundColor(getResources().getColor(R.color.white, null));
                } else {
                    //noinspection deprecation
                    recyclerView.setBackgroundColor(getResources().getColor(R.color.white));

                }
            }
        }
    }
    private void setupRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new MyRecyclerAdapter(getActivity());

    }


    public boolean onBackKeyPressed() {
        return true;
    }
}
