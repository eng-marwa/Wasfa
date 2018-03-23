package com.mr.java.shno;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mr.java.shno.adapter.SearchRecyclerAdapter;
import com.mr.java.shno.util.FirebaseUtil;
import com.mr.java.shno.util.NetworkUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends Fragment {
    @BindView(R.id.editText)
    TextView mainingredient;
    @BindView(R.id.editText3)
    TextView wasfaType;
    private boolean flag;

    private ArrayList<Maindish> maindishesList, sweetList, entreesList;
    private ArrayList<String> maindishesKeyList,sweetKeyList,entreesKeyList,keydish;
    private String wsfa;
    private Set<String> set;
    private ArrayList<String> margs, sargs;
    private SearchRecyclerAdapter adapter;
    private ArrayList<Maindish> dish;
    private FirebaseUser currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        ((Shno) getActivity()).homeBtn.setImageResource(R.drawable.ic_home_fill);
        ((Shno) getActivity()).homeBtn.setBackgroundResource(R.color.white);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        try {
            mainingredient.clearFocus();
            if (NetworkUtil.isNetworkAvailable(getActivity())) {
                DatabaseReference maindish = FirebaseUtil.getMaindish();
                maindish.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        maindishesList = new ArrayList<>();
                        maindishesKeyList = new ArrayList<>();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Maindish value = data.getValue(Maindish.class);
                            String key = data.getKey();
                            maindishesList.add(value);
                            maindishesKeyList.add(key);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                DatabaseReference sweet = FirebaseUtil.getSweet();
                sweet.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        sweetList = new ArrayList<>();
                        sweetKeyList = new ArrayList<>();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Maindish value = data.getValue(Maindish.class);
                            String key = data.getKey();
                            sweetList.add(value);
                            sweetKeyList.add(key);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                DatabaseReference entrees = FirebaseUtil.getEntrees();
                entrees.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        entreesList = new ArrayList<>();
                        entreesKeyList = new ArrayList<>();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Maindish value = data.getValue(Maindish.class);
                            String key = data.getKey();
                            entreesList.add(value);
                            entreesKeyList.add(key);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            } else {
                Toast.makeText(getActivity(), "لا يوجد اتصال بالانترنت", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Log.e("e1",e.getMessage());
        }
        return rootView;
    }

    @OnClick(R.id.button)
    public void selectWasfa() {
        if(wasfaType.getText().toString()!=null && !wasfaType.getText().toString().equals("") ||
                mainingredient.getText().toString()!=null && !mainingredient.getText().toString().equals("")) {
            try {
                if (wsfa.equals("الأطباق الرئيسية")) {
                    dish = maindishesList;
                    keydish = maindishesKeyList;
                } else if (wsfa.equals("الحلويات")) {
                    dish = sweetList;
                    keydish = sweetKeyList;
                } else {
                    dish = entreesList;
                    keydish = entreesKeyList;
                }
                selectWasfa(wsfa, margs, dish, keydish);
            } catch (Exception e) {
            }
        }else{
            Toast.makeText(getActivity(),"لابد من ادخال بيانات صحيحة",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.editText3)
    public void selectwasfaType() {
        showMySingleDialog(R.array.main_spinner, "");
    }

    @OnClick(R.id.editText)
    public void selectMainIngredient() {
        try {
            if (wsfa != null) {
                if (wsfa.equals("الأطباق الرئيسية"))
                    showMyDialog(R.array.main_spinner, "main", "اختار مكونين رئيسيين على الأقل ");
                else if (wsfa.equals("الحلويات"))
                    showMyDialog(R.array.sweets, "main", "اختار مكونين رئيسيين على الأقل ");
                else
                    showMyDialog(R.array.main, "main", "اختار مكونين رئيسيين على الأقل ");
            } else {
                Toast.makeText(getActivity(), "ادخل نوع الوجبة", Toast.LENGTH_LONG).show();

            }
        }catch (Exception e){
        }
    }

    private void showMySingleDialog(int main_spinner, String title) {
        try {
            final String array[] = {"الأطباق الرئيسية", "المقبلات", "الحلويات"};
            final StringBuilder sb = new StringBuilder();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title)
                    .setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (array.length == 0) {
                                Toast.makeText(getActivity(), "اختار نوع الوجبة", Toast.LENGTH_SHORT).show();
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
        }catch (Exception e){
        }
    }

    public void showMyDialog(int resource, final String type, String title) {
        try {
            final String array[] = getResources().getStringArray(resource);
            final StringBuilder sb = new StringBuilder();
            set = new HashSet<>();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title)
                    .setMultiChoiceItems(resource, null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                            if (b) {
                                set.add(array[i]);
                            } else {
                                if (set.contains(array[i]))
                                    set.remove(array[i]);
                            }
                        }
                    }).setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int index) {
                    if (type.equals("main") && set.size() < 2) {
                        Toast.makeText(getActivity(), "اختار على الاقل صنفين رئيسين", Toast.LENGTH_SHORT).show();
                    } else {
                        for (String s : set) {
                            sb.append(s + "  ");
                        }
                        if (type.equals("main")) {
                            margs = new ArrayList<>(set);
                            mainingredient.setText(sb.toString());
                        } else {
                            sargs = new ArrayList<>(set);
                        }
                    }

                }
            });
            AlertDialog alertDialog = builder.create();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            builder.show();
        }catch (Exception e){
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem newwasfa = menu.findItem(R.id.nw);
        MenuItem sign = menu.findItem(R.id.sign);
        MenuItem fav = menu.findItem(R.id.fav);
        if (currentUser != null) {
            if (currentUser.getUid().equals("AqbNRvluOLUYstdKehCrlSmsaBi2")) {
                newwasfa.setVisible(true);
            } else {
                newwasfa.setVisible(false);

            }
        } else {
            sign.setVisible(false);
            newwasfa.setVisible(false);
            fav.setVisible(false);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fav:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new FavFragment())
                        .addToBackStack(null)
                        .commit();
                ((Shno) getActivity()).favBtn.setImageResource(R.drawable.ic_add_a_star_interface_symbol_fill);
                ((Shno) getActivity()).favBtn.setBackgroundResource(R.color.white);
                break;
            case R.id.nw:
                startActivity(new Intent(getActivity(), AddNewSwfa.class));

                break;
            case R.id.sign:
                currentUser = null;
                showDialog();
                Toast.makeText(getActivity(), "تم تسجيل خروج", Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new MainFragment())
                        .addToBackStack(null)
                        .commit();
                FirebaseAuth.getInstance().signOut();
                ((Shno) getActivity()).homeBtn.setImageResource(R.drawable.ic_home_fill);
                ((Shno) getActivity()).homeBtn.setBackgroundResource(R.color.white);
                ((Shno) getActivity()).lockBtn.setImageResource(R.drawable.ic_lock_user_symbol_fill);
                ((Shno) getActivity()).lockBtn.setBackgroundResource(R.color.white);
                ((Shno) getActivity()).lockBtn.setEnabled(true);
                break;
            default:
        }
        return true;
    }

    private void showDialog() {
        try {
            final ProgressDialog progressDoalog = new ProgressDialog(getActivity());
            progressDoalog.setMax(20);
            progressDoalog.setMessage("جاري تسجيل الخروج");
            progressDoalog.setCancelable(false);
            progressDoalog.show();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDoalog.dismiss();
                }
            }).start();
        }catch (Exception e){
        }
    }

    private void selectWasfa(String wsfa, ArrayList<String> margs, ArrayList<Maindish> maindishesList, ArrayList<String> maindishesKeyList) {
        try {
            if (NetworkUtil.isNetworkAvailable(getActivity())) {
                Set<Maindish> matchedMaindishSet = new HashSet<>();
                Set<String> matchMaindishKey = new HashSet<>();
                if (margs != null) {
                    if (margs.size() == 0) {
                        Toast.makeText(getActivity(), "اختار الصنف", Toast.LENGTH_SHORT).show();
                    } else if (wsfa.length() == 0) {
                        Toast.makeText(getActivity(), "اختار نوع الوجبة", Toast.LENGTH_SHORT).show();
                    } else {
                        if (TextUtils.equals(wsfa, "الأطباق الرئيسية")) {
                            for (int i = 0; i < maindishesList.size(); i++) {
                                if (maindishesList.get(i).getIngredients() != null) {
                                    if (checkArgs(margs, maindishesList.get(i).getIngredients())) {
                                        matchedMaindishSet.add(maindishesList.get(i));
                                        matchMaindishKey.add(maindishesKeyList.get(i));
                                        SearchWasfa searchWasfa = SearchWasfa.newInstance(new ArrayList<Maindish>(matchedMaindishSet), new ArrayList<String>(matchMaindishKey), margs);
                                        getActivity().getSupportFragmentManager().beginTransaction().
                                                replace(R.id.container, searchWasfa).addToBackStack(null).commit();
                                    }
                                }
                            }
                            if (matchedMaindishSet.size() == 0)
                                Toast.makeText(getActivity(), "لم تتوافر نتائج لبحثك", Toast.LENGTH_SHORT).show();


                        } else if (TextUtils.equals(wsfa, "المقبلات")) {
                            for (int i = 0; i < maindishesList.size(); i++) {
                                if (maindishesList.get(i).getIngredients() != null) {
                                    if (checkArgs(margs, maindishesList.get(i).getIngredients())) {
                                        matchedMaindishSet.add(maindishesList.get(i));
                                        matchMaindishKey.add(maindishesKeyList.get(i));
                                        SearchWasfa searchWasfa = SearchWasfa.newInstance(new ArrayList<Maindish>(matchedMaindishSet), new ArrayList<String>(matchMaindishKey), margs);
                                        getActivity().getSupportFragmentManager().beginTransaction().
                                                replace(R.id.container, searchWasfa).addToBackStack(null).commit();
                                    }
                                }
                            }
                            if (matchedMaindishSet.size() == 0)
                                Toast.makeText(getActivity(), "لم تتوافر نتائج لبحثك", Toast.LENGTH_SHORT).show();


                        } else {
                            for (int i = 0; i < maindishesList.size(); i++) {
                                if (maindishesList.get(i).getIngredients() != null) {
                                    if (checkArgs(margs, maindishesList.get(i).getIngredients())) {
                                        matchedMaindishSet.add(maindishesList.get(i));
                                        matchMaindishKey.add(maindishesKeyList.get(i));
                                        SearchWasfa searchWasfa = SearchWasfa.newInstance(new ArrayList<Maindish>(matchedMaindishSet), new ArrayList<String>(matchMaindishKey), margs);
                                        getActivity().getSupportFragmentManager().beginTransaction().
                                                replace(R.id.container, searchWasfa).addToBackStack(null).commit();
                                    }
                                }
                            }
                            if (matchedMaindishSet.size() == 0)
                                Toast.makeText(getActivity(), "لم تتوافر نتائج لبحثك", Toast.LENGTH_SHORT).show();

                        }
                    }

                }
            } else {
                Toast.makeText(getActivity(), "لا يوجد اتصال بالانترنت", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
        }
    }

    private boolean checkArgs(ArrayList<String> margs, List<String> ingredients) {
        boolean b = false;
        try {
            StringBuilder sb = new StringBuilder();
            for (String in : ingredients) {
                sb.append(in + " ");
            }

            for (int j = 0; j <margs.size() ; j++) {
                if (!sb.toString().contains(margs.get(j).trim())) {
                    b = false;
                    break;
                } else {
                    b = true;
                }
            }

        }catch (Exception e){
        }
        return b;
    }

}
