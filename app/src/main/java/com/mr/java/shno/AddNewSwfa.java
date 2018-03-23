package com.mr.java.shno;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.mr.java.shno.util.FirebaseUtil;
import com.mr.java.shno.util.NetworkUtil;
import com.mr.java.shno.util.NonScrollListView;
import com.mr.java.shno.util.UploadFirebaseTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddNewSwfa extends AppCompatActivity {
    private static String imgUrl;
    @BindView(R.id.editText4)
    EditText nameView;
    @BindView(R.id.editText7)
    EditText ingView;
    @BindView(R.id.editText8)
    EditText stpView;
    @BindView(R.id.listView1)
    NonScrollListView listView;
    @BindView(R.id.listView2)
    NonScrollListView listView2;
    @BindView(R.id.editText3)
    TextView wasfaType;
    @BindView(R.id.editText5)
    TextView servingText;
    @BindView(R.id.editText6)
    TextView cooktimeText;
    @BindView(R.id.image)
    ImageButton imageButton;
    private ProgressDialog progressdialog;

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
                                Toast.makeText(AddNewSwfa.this, "اختار نوع الوجبة", Toast.LENGTH_SHORT).show();
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

    private List<String> ingredientsList;
    private List<String> stepList;
    private ArrayAdapter<String> inAdapter;
    private ArrayAdapter<String> stAdapter;
    private SQLiteDatabase db;
    private String wsfa;
    private StringBuilder sb = new StringBuilder();
    private StringBuilder sb1 = new StringBuilder();
    private StringBuilder sb2 = new StringBuilder();

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
        setContentView(R.layout.activity_add_new_swfa);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            progressdialog = new ProgressDialog(this);
            progressdialog.setMessage("Please Wait....");
            nameView.clearFocus();
            nameView.setFocusableInTouchMode(false);
            nameView.setFocusable(false);
            nameView.setFocusableInTouchMode(true);
            nameView.setFocusable(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ingredientsList = new ArrayList<>();
            stepList = new ArrayList<>();
            inAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientsList);
            stAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stepList);
            listView.setAdapter(inAdapter);
            listView2.setAdapter(stAdapter);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                listView.setNestedScrollingEnabled(true);
                listView2.setNestedScrollingEnabled(true);

            }
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) saveWasfa();
        if (item.getItemId() == android.R.id.home)
            startActivity(new Intent(AddNewSwfa.this, Shno.class).putExtra("activity", "home"));
        return super.onOptionsItemSelected(item);
    }

    public void gallery(View v) {
        try {
            System.out.println(Build.VERSION.SDK_INT);
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1520);
                } else {
                    addImage();
                }
            }else{
                addImage();
            }


        } catch (Exception e) {
        }
    }

    private void addImage() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1520);
            } else {
                addImage();
            }
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                imageButton.setImageURI(selectedImage);
                new UploadFirebaseTask().execute(selectedImage);
            }
        } catch (Exception e) {
        }
    }

    private void saveWasfa() {
        try {
            if (NetworkUtil.isNetworkAvailable(AddNewSwfa.this)) {
                String name = nameView.getText().toString().trim();
                int serving = 0;
                String wasfa = wasfaType.getText().toString().trim();
                if (!servingText.getText().toString().equals("")) {
                    serving = Integer.parseInt(servingText.getText().toString());
                }
                String cooktime = cooktimeText.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(wasfa) || TextUtils.isEmpty(cooktime)) {
                    Toast.makeText(AddNewSwfa.this, "جميع الحقول مطلوبه", Toast.LENGTH_SHORT).show();
                } else {
                    if (TextUtils.equals(wasfa, "الأطباق الرئيسية")) {
                        progressdialog.show();
                        FirebaseUtil.getMaindish().push().setValue(new Maindish(name, imgUrl, null, serving, cooktime, wasfa, ingredientsList, stepList))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressdialog.dismiss();
                                            Toast.makeText(AddNewSwfa.this, "تم إضافة وجبة", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(AddNewSwfa.this, Shno.class);
                                            i.putExtra("activity", "home");
                                            startActivity(i);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddNewSwfa.this, "حدث خطأ ما يرجى المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                                progressdialog.dismiss();

                            }
                        });
                    } else if (TextUtils.equals(wasfa, "المقبلات")) {
                        progressdialog.show();
                        FirebaseUtil.getEntrees().push().setValue(new Maindish(name, imgUrl, null, serving, cooktime, wasfa, ingredientsList, stepList))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressdialog.dismiss();
                                            Toast.makeText(AddNewSwfa.this, "تم إضافة وجبة", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(AddNewSwfa.this, Shno.class);
                                            i.putExtra("activity", "home");
                                            startActivity(i);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddNewSwfa.this, "حدث خطأ ما يرجى المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                                progressdialog.dismiss();

                            }
                        });
                    } else if (TextUtils.equals(wasfa, "الحلويات")) {
                        progressdialog.show();
                        FirebaseUtil.getSweet().push().setValue(new Maindish(name, imgUrl, null, serving, cooktime, wasfa, ingredientsList, stepList))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressdialog.dismiss();
                                            Toast.makeText(AddNewSwfa.this, "تم إضافة وجبة", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(AddNewSwfa.this, Shno.class);
                                            i.putExtra("activity", "home");
                                            startActivity(i);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddNewSwfa.this, "حدث خطأ ما يرجى المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                                progressdialog.dismiss();

                            }
                        });
                    }


                }
            } else {
                Toast.makeText(AddNewSwfa.this, "لا يوجد اتصال بالانترنت", Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
        }
    }


    public static void getDownloadedUri(String imageUrl) {
        imgUrl = imageUrl;
    }
}
