package com.mr.java.shno;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Shno extends AppCompatActivity {
    @BindView(R.id.navigation_home)
    ImageView homeBtn;
    @BindView(R.id.navigation_add)
    ImageView addBtn;
    @BindView(R.id.navigation_lock)
    ImageView lockBtn;
    @BindView(R.id.navigation_fav)
    ImageView favBtn;
    private ProgressDialog progressdialog;
    private Handler handle;
    private FirebaseUser currentUser;
    FirebaseAuth.AuthStateListener listener;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shno);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent.hasExtra("activity")) {
            System.out.println("activity");
            String activity = intent.getExtras().getString("activity");
            if (activity.equals("lock")) {
                System.out.println("lock");

                getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainFragment()).commit();
                lockBtn.setImageResource(R.drawable.ic_lock_user_symbol_fill);
                lockBtn.setBackgroundResource(R.color.white);

            } else if (activity.equals("home")) {
                System.out.println("why");
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                homeBtn.setImageResource(R.drawable.ic_home_fill);
                homeBtn.setBackgroundResource(R.color.white);
            } else if (activity.equals("add")) {

                getSupportFragmentManager().beginTransaction().replace(R.id.container, new RegisterFragment()).commit();
                addBtn.setImageResource(R.drawable.ic_add_contact_fill);
                addBtn.setBackgroundResource(R.color.white);

            } else if (activity.equals("fav")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new FavFragment()).commit();
                favBtn.setImageResource(R.drawable.ic_add_a_star_interface_symbol_fill);
                favBtn.setBackgroundResource(R.color.white);
            }

        } else {
            if (findViewById(R.id.container) != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                homeBtn.setImageResource(R.drawable.ic_home_fill);
                homeBtn.setBackgroundResource(R.color.white);
            }
        }
        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        mAuth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {

                    lockBtn.setEnabled(false);
                } else {

                    lockBtn.setEnabled(true);
                }
            }
        };

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
    }

    @OnClick(R.id.navigation_add)
    public void addPressed() {
        addBtn.setImageResource(R.drawable.ic_add_contact_fill);
        addBtn.setBackgroundResource(R.color.white);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new RegisterFragment()).commit();
        favBtn.setImageResource(R.drawable.ic_add_a_star_interface_symbol);
        favBtn.setBackgroundResource(R.color.colorPrimary);
        homeBtn.setImageResource(R.drawable.ic_home);
        homeBtn.setBackgroundResource(R.color.colorPrimary);
        lockBtn.setImageResource(R.drawable.ic_lock_user_symbol);
        lockBtn.setBackgroundResource(R.color.colorPrimary);

    }

    @OnClick(R.id.navigation_lock)
    public void lockPressed() {
        Log.i("lo", currentUser == null ? "y" : "n");
        if (currentUser == null) {
            lockBtn.setEnabled(true);
            lockBtn.setImageResource(R.drawable.ic_lock_user_symbol_fill);
            lockBtn.setBackgroundResource(R.color.white);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainFragment()).commit();
        }

        favBtn.setImageResource(R.drawable.ic_add_a_star_interface_symbol);
        favBtn.setBackgroundResource(R.color.colorPrimary);
        addBtn.setImageResource(R.drawable.ic_add_contact);
        addBtn.setBackgroundResource(R.color.colorPrimary);
        homeBtn.setImageResource(R.drawable.ic_home);
        homeBtn.setBackgroundResource(R.color.colorPrimary);
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
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new FavFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
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
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(listener);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.removeAuthStateListener(listener);
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (!fragment.isVisible()) continue;

                if (fragment instanceof SearchWasfa && ((SearchWasfa) fragment).onBackKeyPressed()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                } else if (fragment instanceof FavFragment && ((FavFragment) fragment).onBackKeyPressed()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).addToBackStack(null).commit();

                } else if (fragment instanceof MainFragment && ((MainFragment) fragment).onBackKeyPressed()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).addToBackStack(null).commit();

                } else if (fragment instanceof RegisterFragment && ((RegisterFragment) fragment).onBackKeyPressed()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).addToBackStack(null).commit();

                } else {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }
                    super.onBackPressed();
                }

            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

        }
    }
}
