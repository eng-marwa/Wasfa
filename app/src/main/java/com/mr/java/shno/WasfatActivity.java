package com.mr.java.shno;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mr.java.shno.adapter.ExpandableAdapter;
import com.mr.java.shno.db.DBHelper;
import com.mr.java.shno.db.Data;
import com.mr.java.shno.util.FirebaseUtil;
import com.mr.java.shno.util.NetworkUtil;
import com.mr.java.shno.util.NonScrollExpandableListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mr.java.shno.db.Data.COL_CAT;
import static com.mr.java.shno.db.Data.COL_COOKING;
import static com.mr.java.shno.db.Data.COL_IMG;
import static com.mr.java.shno.db.Data.COL_INGREDIENTS;
import static com.mr.java.shno.db.Data.COL_KEY;
import static com.mr.java.shno.db.Data.COL_NAME;
import static com.mr.java.shno.db.Data.COL_SERVING;
import static com.mr.java.shno.db.Data.COL_STEPS;
import static com.mr.java.shno.db.Data.COL_TAG;
import static com.mr.java.shno.db.Data.COL_USER_ID;
import static com.mr.java.shno.db.Data.FAV_TABLE_NAME;

public class WasfatActivity extends AppCompatActivity {
    @BindView(R.id.expand)
    NonScrollExpandableListView expandableListView;
    private ArrayList<String> listDataHeader;
    private Maindish maindish;
    private SQLiteDatabase db;
    private ArrayList<String> args;
    @BindView(R.id.editText55)
    TextView servingText;
    @BindView(R.id.editText66)
    TextView cookingText;
    private String keys;
    @BindView(R.id.navigation_home)
    ImageView homeBtn;
    @BindView(R.id.navigation_add)
    ImageView addBtn;
    @BindView(R.id.navigation_lock)
    ImageView lockBtn;
    @BindView(R.id.navigation_fav)
    ImageView favBtn;
    @BindView(R.id.image)
    ImageView imageView;
    private FloatingActionButton fab;
    private BottomNavigationView navigation;
    private FirebaseAuth mAuth;
    String from="";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Toolbar toolbar;
@BindView(R.id.tooltext)
TextView tooltext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wasfat);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();



        Intent intent = getIntent();
        if (intent.hasExtra("maindish")) {
            maindish = intent.getExtras().getParcelable("maindish");
            tooltext.setText(maindish.getName());
//            Picasso.with(this).load(maindish.getImage()).into(imageView);
            cookingText.setText(maindish.getCookTime() + "دقيقة");
            servingText.setText(String.valueOf(maindish.getServing()));
            if(maindish.getImage()!=null)
            Picasso.with(WasfatActivity.this).load(maindish.getImage()).into(imageView);
            else
                Picasso.with(WasfatActivity.this).load(R.mipmap.ic_launcher).into(imageView);

        }
        if (intent.hasExtra("args")) {
            args = intent.getExtras().getStringArrayList("args");

        }
        if (intent.hasExtra("key")) {
            keys = intent.getExtras().getString("key");

        }

        if(intent.hasExtra("from")){
            from = intent.getExtras().getString("from");
        }

        listDataHeader = new ArrayList<String>();
        listDataHeader.add("المقادير");
        listDataHeader.add("طريقة التحضير");
        ExpandableAdapter adapter = new ExpandableAdapter(this, listDataHeader, getChildData(), args);
        expandableListView.setAdapter(adapter);
    }

    private void removeFromFav(String name) {
        SharedPreferences preferences = getSharedPreferences(FirebaseAuth.getInstance().getCurrentUser().getEmail(), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(name);
        editor.apply();
    }

    private void saveInFav() {
        String name = maindish.getName();
        if (!getNameFromShared(name)) {
            saveInShared(name);
            StringBuilder sb = new StringBuilder();
            List<String> ingredients = maindish.getIngredients();
            for (String ing : ingredients) {
                sb.append(ing + ",");
            }
            StringBuilder sb1 = new StringBuilder();
            List<String> steps = maindish.getSteps();
            for (String s : steps) {
                sb1.append(s + ",");
            }
            ContentValues values = new ContentValues();
            values.put(COL_NAME, maindish.getName());
            values.put(COL_CAT, maindish.getCategory());
            values.put(COL_IMG, maindish.getImage());
            values.put(COL_INGREDIENTS, sb.toString());
            values.put(COL_STEPS, sb1.toString());
            values.put(COL_TAG, maindish.getTag());
            values.put(COL_SERVING, maindish.getServing());
            values.put(COL_COOKING, maindish.getCookTime());
            values.put(COL_KEY, keys);
            values.put(COL_USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());
            Log.i("add",keys);
            long row = db.insert(FAV_TABLE_NAME, null, values);
            if (row > 0) {
                Toast.makeText(WasfatActivity.this, "تم اضافة الوجبة ", Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(WasfatActivity.this, "هذة الوجبة مضافة مسبقا الى المفضله", Toast.LENGTH_LONG).show();
        }
    }

    private boolean getNameFromShared(String name) {
        SharedPreferences preferences = getSharedPreferences(FirebaseAuth.getInstance().getCurrentUser().getEmail(), MODE_PRIVATE);

        if (preferences.getString(name, "").equals(name)) {
            return true;
        }
        return false;
    }


    private void saveInShared(String name) {
        SharedPreferences preferences = getSharedPreferences(FirebaseAuth.getInstance().getCurrentUser().getEmail(), MODE_PRIVATE);
        preferences.edit().putString(name, name).commit();
    }

    public HashMap<String, List<String>> getChildData() {
        HashMap<String, List<String>> listDataChild = new HashMap<>();
        listDataChild.put(listDataHeader.get(0), maindish.getIngredients());
        listDataChild.put(listDataHeader.get(1), maindish.getSteps());

        return listDataChild;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wasfa, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem delete = menu.findItem(R.id.delete);
        MenuItem edit = menu.findItem(R.id.edit);
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            if (firebaseUser.getUid().equals("AqbNRvluOLUYstdKehCrlSmsaBi2")) {
                delete.setVisible(true);
                edit.setVisible(true);
            } else {
                delete.setVisible(false);
                edit.setVisible(false);

            }
        }else{
            delete.setVisible(false);
            edit.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                if(mAuth.getCurrentUser()!=null){
                    if(mAuth.getCurrentUser().getUid().equals("AqbNRvluOLUYstdKehCrlSmsaBi2") && from.equals("search")){
                        deleteFromeFirebase();

                    }else if(mAuth.getCurrentUser().getUid().equals("AqbNRvluOLUYstdKehCrlSmsaBi2") && from.equals("fav")){
                        deleteFromeFirebase();
                        deleteFromFav();
                        removeFromFav(maindish.getName());

                    }
                }

                Intent intent = new Intent(WasfatActivity.this, Shno.class);
                intent.putExtra("activity","home");
                startActivity(intent);

                break;
            case R.id.edit:
                Intent i = new Intent(WasfatActivity.this, EditWasfa.class);
                i.putExtra("edit", maindish);
                i.putExtra("key", keys);
                i.putExtra("from", from);
                startActivity(i);
                break;
            case android.R.id.home:
                //startActivity(new Intent(WasfatActivity.this, HomeActivity.class));
                break;
            case R.id.fav:
//                startActivity(new Intent(WasfatActivity.this, FavActivity.class));
                break;
            case R.id.add_fav:
                if(checkUserAuth()) {
                    saveInFav();
                }else{
                    Intent in = new Intent(WasfatActivity.this, Shno.class);
                    in.putExtra("activity","lock");
                    startActivity(in);
                }
                break;
        }
        return true;
    }

    private boolean checkUserAuth() {
        if(mAuth.getCurrentUser()!=null){
            return true;
        }else{
            Toast.makeText(WasfatActivity.this,"لابد من تسجيل الدخول اولا",Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void deleteFromeFirebase() {
        try {
            if (NetworkUtil.isNetworkAvailable(WasfatActivity.this)) {
                if(maindish!=null) {
                    if (maindish.getCategory().equals("الأطباق الرئيسية")) {

                        Query query = FirebaseUtil.getMaindish().orderByChild("name").equalTo(maindish.getName());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("canceled", "onCancelled", databaseError.toException());
                            }
                        });
                    }else if (maindish.getCategory().equals("المقبلات")) {

                        Query query = FirebaseUtil.getEntrees().orderByChild("name").equalTo(maindish.getName());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("canceled", "onCancelled", databaseError.toException());
                            }
                        });
                }else {
                        if (maindish.getCategory().equals("الحلويات")) {
                            Query query = FirebaseUtil.getSweet().orderByChild("name").equalTo(maindish.getName());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("canceled", "onCancelled", databaseError.toException());
                                }
                            });
                        }
                    }
                setResult(RESULT_OK);
                Toast.makeText(this, "تم حذف الوجبة", Toast.LENGTH_SHORT).show();
            } }else {
                Toast.makeText(WasfatActivity.this, "لا يوجد اتصال بالانترنت", Toast.LENGTH_LONG).show();

            }
        }catch (Exception e){

        }
    }
    private void deleteFromFav(){
        db.delete(Data.FAV_TABLE_NAME,COL_NAME+"=?",new String[]{maindish.getName()});
    }
    @OnClick(R.id.navigation_home)
    public void homePressed() {
        homeBtn.setImageResource(R.drawable.ic_home_fill);
        homeBtn.setBackgroundResource(R.color.white);
        addBtn.setImageResource(R.drawable.ic_add_contact);
        addBtn.setBackgroundResource(R.color.colorPrimary);
        favBtn.setImageResource(R.drawable.ic_add_a_star_interface_symbol);
        favBtn.setBackgroundResource(R.color.colorPrimary);
        lockBtn.setImageResource(R.drawable.ic_lock_user_symbol);
        lockBtn.setBackgroundResource(R.color.colorPrimary);
        Intent i =new Intent(WasfatActivity.this,Shno.class);
        i.putExtra("activity","home");
        startActivity(i);
    }

    @OnClick(R.id.navigation_add)
    public void addPressed() {

        addBtn.setImageResource(R.drawable.ic_add_contact_fill);
        addBtn.setBackgroundResource(R.color.white);
        favBtn.setImageResource(R.drawable.ic_add_a_star_interface_symbol);
        favBtn.setBackgroundResource(R.color.colorPrimary);
        homeBtn.setImageResource(R.drawable.ic_home);
        homeBtn.setBackgroundResource(R.color.colorPrimary);
        lockBtn.setImageResource(R.drawable.ic_lock_user_symbol);
        lockBtn.setBackgroundResource(R.color.colorPrimary);
        Intent i =new Intent(WasfatActivity.this,Shno.class);
        i.putExtra("activity","add");
        startActivity(i);
    }

    @OnClick(R.id.navigation_lock)
    public void lockPressed() {
        favBtn.setImageResource(R.drawable.ic_add_a_star_interface_symbol);
        favBtn.setBackgroundResource(R.color.colorPrimary);
        addBtn.setImageResource(R.drawable.ic_add_contact);
        addBtn.setBackgroundResource(R.color.colorPrimary);
        homeBtn.setImageResource(R.drawable.ic_home);
        homeBtn.setBackgroundResource(R.color.colorPrimary);
        lockBtn.setImageResource(R.drawable.ic_lock_user_symbol_fill);
        lockBtn.setBackgroundResource(R.color.white);
        Intent i =new Intent(WasfatActivity.this,Shno.class);
        i.putExtra("activity","lock");
        startActivity(i);
    }

    @OnClick(R.id.navigation_fav)
    public void favPressed() {
        favBtn.setImageResource(R.drawable.ic_add_a_star_interface_symbol_fill);
        favBtn.setBackgroundResource(R.color.white);
        addBtn.setImageResource(R.drawable.ic_add_contact);
        addBtn.setBackgroundResource(R.color.colorPrimary);
        homeBtn.setImageResource(R.drawable.ic_home);
        homeBtn.setBackgroundResource(R.color.colorPrimary);
        lockBtn.setImageResource(R.drawable.ic_lock_user_symbol);
        lockBtn.setBackgroundResource(R.color.colorPrimary);
        Intent i =new Intent(WasfatActivity.this,Shno.class);
        i.putExtra("activity","fav");
        startActivity(i);
    }

}
