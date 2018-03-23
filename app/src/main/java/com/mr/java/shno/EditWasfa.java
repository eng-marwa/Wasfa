package com.mr.java.shno;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mr.java.shno.adapter.NestedRecyclerAdapter;
import com.mr.java.shno.db.DBHelper;
import com.mr.java.shno.db.Data;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mr.java.shno.util.FirebaseUtil;
import com.mr.java.shno.util.NetworkUtil;
import com.mr.java.shno.util.RecyclerItemTouchHelper;

import java.util.ArrayList;
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


public class EditWasfa extends AppCompatActivity {
    @BindView(R.id.editText4)
    EditText nameView;
    @BindView(R.id.editText7)
    EditText ingView;
    @BindView(R.id.editText8)
    EditText stpView;
    @BindView(R.id.listView1)
    RecyclerView listView;
    @BindView(R.id.listView2)
    RecyclerView listView2;
    @BindView(R.id.editText3)
    TextView wasfaType;
    @BindView(R.id.editText5)
    TextView servingText;
    @BindView(R.id.editText6)
    TextView cooktimeText;

    private String wsfa;
    private Maindish maindish;
    private NestedRecyclerAdapter stAdapter, inAdapter;
    @BindView(R.id.coord)
    CoordinatorLayout coordinate;
    private List<String> ingredientsList = new ArrayList<>();
    private List<String> stepList = new ArrayList<>();
    private String key;
    private BottomNavigationView navigation;
    private String from;
    private String category;

    @OnClick(R.id.editText3)
    public void selectMainIngredient() {
        showMySingleDialog(R.array.main_spinner, "");
    }

    private void showMySingleDialog(int main_spinner, String title) {
        try {
            final String array[] = {"الأطباق الرئيسية", "المقبلات", "الحلويات"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title)
                    .setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (array.length == 0) {
                                Toast.makeText(EditWasfa.this, "اختار نوع الوجبة", Toast.LENGTH_SHORT).show();
                            } else {
                                wsfa = array[i];
                            }
                        }
                    }).setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int index) {

                    wasfaType.setText(wsfa);

                }
            });
            builder.show();
        } catch (Exception e) {
        }
    }

    @OnClick(R.id.button3)
    public void addIngredients() {
        try {
            String s = ingView.getText().toString();
            if (!s.equals("")) {
                ingredientsList.add(s);
                inAdapter.notifyDataSetChanged();
                ingView.setText("");
            }
        } catch (Exception e) {
        }
    }

    @OnClick(R.id.button4)
    public void addSteps() {
        try {
            String s = stpView.getText().toString();
            if (!s.equals("")) {
                stepList.add(s);
                stAdapter.notifyDataSetChanged();
                stpView.setText("");
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wasfa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        nameView.clearFocus();
        setupRecycler();


    }


    private void editWasfa() {
        try {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (from != null || !from.equals("")) {
                if (currentUser != null) {
                    if (currentUser.getUid().equals("AqbNRvluOLUYstdKehCrlSmsaBi2") && from.equals("fav")) {
                        editOnline();
                        editOffline();
                    } else if (currentUser.getUid().equals("AqbNRvluOLUYstdKehCrlSmsaBi2") && from.equals("search")) {
                        editOnline();
                    }


                }
            }
        } catch (Exception e) {
        }
    }


    private void setupRecycler() {
        try {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
            listView.setLayoutManager(layoutManager);
            listView.setHasFixedSize(true);
            listView.setItemAnimator(new DefaultItemAnimator());
            listView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new com.mr.java.shno.util.RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, new RecyclerItemTouchHelper.RecyclerItemTouchHelperListener() {
                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
                    if (viewHolder instanceof NestedRecyclerAdapter.MyViewHolder) {
                        // get the removed item name to display it in snack bar

                        String name = maindish.getIngredients().get(viewHolder.getAdapterPosition());
                        // backup of removed item for undo purpose
                        final String deletedItem = maindish.getIngredients().get(viewHolder.getAdapterPosition());
                        final int deletedIndex = viewHolder.getAdapterPosition();

                        // remove the item from recycler view
                        inAdapter.removeItem(viewHolder.getAdapterPosition());
                        // showing snack bar with Undo option
                        Snackbar snackbar = Snackbar
                                .make(coordinate, name + " removed !", Snackbar.LENGTH_LONG);
                        snackbar.setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // undo is selected, restore the deleted item
                                inAdapter.restoreItem(deletedItem, deletedIndex);
                            }
                        });
                        snackbar.setActionTextColor(Color.YELLOW);
                        snackbar.show();
                    }

                }
            });
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(listView);

            GridLayoutManager layoutManager1 = new GridLayoutManager(this, 1);
            listView2.setLayoutManager(layoutManager1);
            listView2.setHasFixedSize(true);
            listView2.setItemAnimator(new DefaultItemAnimator());
            listView2.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new com.mr.java.shno.util.RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, new com.mr.java.shno.util.RecyclerItemTouchHelper.RecyclerItemTouchHelperListener() {
                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
                    if (viewHolder instanceof NestedRecyclerAdapter.MyViewHolder) {
                        // get the removed item name to display it in snack bar
                        String name = maindish.getSteps().get(viewHolder.getAdapterPosition());
                        // backup of removed item for undo purpose
                        final String deletedItem = maindish.getSteps().get(viewHolder.getAdapterPosition());
                        final int deletedIndex = viewHolder.getAdapterPosition();

                        // remove the item from recycler view
                        stAdapter.removeItem(viewHolder.getAdapterPosition());
                        // showing snack bar with Undo option
                        Snackbar snackbar = Snackbar
                                .make(coordinate, name + " removed !", Snackbar.LENGTH_LONG);
                        snackbar.setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // undo is selected, restore the deleted item
                                stAdapter.restoreItem(deletedItem, deletedIndex);
                            }
                        });
                        snackbar.setActionTextColor(Color.YELLOW);
                        snackbar.show();
                    }

                }
            });
            new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(listView2);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            Intent intent = getIntent();
            if (intent.hasExtra("edit")) {
                maindish = intent.getExtras().getParcelable("edit");
                key = intent.getExtras().getString("key");
                from = intent.getExtras().getString("from");
                category = maindish.getCategory();
                System.out.println("xxxxxxxxp" + key + "  " + category);
                if (maindish.getSteps() != null)
                    stepList.addAll(maindish.getSteps());
                if (maindish.getIngredients() != null)
                    ingredientsList.addAll(maindish.getIngredients());
                nameView.setText(maindish.getName());
                cooktimeText.setText(maindish.getCookTime());
                servingText.setText(String.valueOf(maindish.getServing()));
                wasfaType.setText(maindish.getCategory());
                inAdapter = new NestedRecyclerAdapter(this, ingredientsList);
                stAdapter = new NestedRecyclerAdapter(this, stepList);
                listView.setAdapter(inAdapter);
                listView2.setAdapter(stAdapter);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    listView.setNestedScrollingEnabled(true);
                    listView2.setNestedScrollingEnabled(true);

                }

            }
        } catch (Exception e) {
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            editWasfa();
        }
        return super.onOptionsItemSelected(item);
    }


    private void editOnline() {
        try {
            if (NetworkUtil.isNetworkAvailable(EditWasfa.this)) {

                String name = nameView.getText().toString();
                String cat = wasfaType.getText().toString();
                int serving = 0;
                if (!servingText.getText().toString().equals("")) {
                    serving = Integer.parseInt(servingText.getText().toString());

                }
                String time = cooktimeText.getText().toString();
                Log.i("wasfa edit stp", stepList.toString());
                Log.i("wasfa edit ing", ingredientsList.toString());
                maindish.setName(name);
                maindish.setCategory(cat);
                maindish.setIngredients(ingredientsList);
                maindish.setSteps(stepList);
                maindish.setCookTime(time);
                maindish.setServing(serving);
                if (category.equals("الأطباق الرئيسية")) {
                    FirebaseUtil.getMaindish().child(key)
                            .setValue(maindish)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EditWasfa.this, "تم تعديل الوجبة", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(EditWasfa.this, Shno.class);
                                        intent.putExtra("activity","home");
                                        startActivity(intent);                                    }
                                }
                            });
                } else if (category.equals("المقبلات")) {
                    System.out.println("mok" + key);
                    FirebaseUtil.getEntrees().child(key)
                            .setValue(maindish)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EditWasfa.this, "تم تعديل الوجبة", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(EditWasfa.this, Shno.class);
                                        intent.putExtra("activity","home");
                                        startActivity(intent);                                    }
                                }
                            });
                } else {
                    FirebaseUtil.getSweet().child(key)
                            .setValue(maindish)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EditWasfa.this, "تم تعديل الوجبة", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(EditWasfa.this, Shno.class);
                                        intent.putExtra("activity","home");
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            }
        } catch (Exception e) {
        }
    }

    private void editOffline() {
        try {
            DBHelper dbhelper = new DBHelper(EditWasfa.this);
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            String name = nameView.getText().toString();
            String cat = wasfaType.getText().toString();
            int serving = 0;
            if (!servingText.getText().toString().equals("")) {
                serving = Integer.parseInt(servingText.getText().toString());
            }
            String time = cooktimeText.getText().toString();
            Log.i("wasfa edit stp", stepList.toString());
            Log.i("wasfa edit ing", ingredientsList.toString());
            Maindish maindish = new Maindish(name, null, null, serving, time, cat, ingredientsList, stepList);
            ContentValues values = new ContentValues();
            values.put(COL_NAME, maindish.getName());
            values.put(COL_CAT, maindish.getCategory());
            values.put(COL_IMG, maindish.getImage());
            if (ingredientsList != null)
                values.put(COL_INGREDIENTS, ingredientsList.toString());
            if (stepList != null)
                values.put(COL_STEPS, stepList.toString());
            values.put(COL_TAG, maindish.getTag());
            values.put(COL_SERVING, maindish.getServing());
            values.put(COL_COOKING, maindish.getCookTime());
            values.put(COL_USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());
            System.out.println(key);
            db.update(Data.FAV_TABLE_NAME, values, COL_KEY + "=?", new String[]{key});


        } catch (Exception e) {
        }
    }
}
