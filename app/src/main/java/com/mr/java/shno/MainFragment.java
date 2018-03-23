package com.mr.java.shno;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainFragment extends Fragment {
    @BindView(R.id.editText)
    EditText emailText;
    @BindView(R.id.editText2)
    EditText passText;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressdialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage("Please Wait....");
        ((Shno)getActivity()).lockBtn.setImageResource(R.drawable.ic_lock_user_symbol_fill);
        ((Shno)getActivity()).lockBtn.setBackgroundResource(R.color.white);
        ((Shno)getActivity()).favBtn.setImageResource(R.drawable.ic_add_a_star_interface_symbol);
        ((Shno)getActivity()).favBtn.setBackgroundResource(R.color.colorPrimary);
        ((Shno)getActivity()).addBtn.setImageResource(R.drawable.ic_add_contact);
        ((Shno)getActivity()).addBtn.setBackgroundResource(R.color.colorPrimary);
        ((Shno)getActivity()).homeBtn.setImageResource(R.drawable.ic_home);
        ((Shno)getActivity()).homeBtn.setBackgroundResource(R.color.colorPrimary);

        mAuth = FirebaseAuth.getInstance();
        return rootView;
    }


    @OnClick(R.id.button2)
    public void loginExistingUser() {
        String email = emailText.getText().toString();
        String pass = passText.getText().toString();
        String message = "";
        if (TextUtils.isEmpty(email)) message += "\nالبريد الألكتروني مطلوب";
        if (TextUtils.isEmpty(pass)) message += "\nحقل كلمةالمرور مطلوب";
        if (TextUtils.isEmpty(message)) {
            Task<AuthResult> task = mAuth.signInWithEmailAndPassword(email, pass);
            progressdialog.show();

                   task .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "تم تسجيل الدخول", Toast.LENGTH_SHORT).show();
                                HomeFragment nextFrag= new HomeFragment();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.container, nextFrag)
                                        .addToBackStack(null)
                                        .commit();
                                progressdialog.dismiss();

                                ((Shno)getActivity()).homeBtn.setImageResource(R.drawable.ic_home_fill);
                                ((Shno)getActivity()).homeBtn.setBackgroundResource(R.color.white);
                                ((Shno)getActivity()).lockBtn.setImageResource(R.drawable.ic_lock_user_symbol);
                                ((Shno)getActivity()).lockBtn.setBackgroundResource(R.color.colorPrimary);
                            } else {
                                Toast.makeText(getActivity(), "البريد الالكتروني او كلمة السر غير صحيحة", Toast.LENGTH_SHORT).show();
                                progressdialog.dismiss();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "حدث خطأ يرجى المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                    progressdialog.dismiss();

                }
            });

        } else {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            emailText.setText("");
            passText.setText("");
        }
    }

    public boolean onBackKeyPressed() {
        return true;
    }
}
