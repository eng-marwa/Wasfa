package com.mr.java.shno;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
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

public class RegisterFragment extends Fragment {
    @BindView(R.id.editText3)
    EditText remailText;
    @BindView(R.id.editText4)
    EditText rpassText;
    @BindView(R.id.editText5)
    EditText rpassText1;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private BottomNavigationView navigation;
    private ProgressDialog progressdialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, rootView);
        ((Shno)getActivity()).addBtn.setImageResource(R.drawable.ic_add_contact_fill);
        ((Shno)getActivity()).addBtn.setBackgroundResource(R.color.white);
        ((Shno)getActivity()).favBtn.setImageResource(R.drawable.ic_add_a_star_interface_symbol);
        ((Shno)getActivity()).favBtn.setBackgroundResource(R.color.colorPrimary);
        ((Shno)getActivity()).homeBtn.setImageResource(R.drawable.ic_home);
        ((Shno)getActivity()).homeBtn.setBackgroundResource(R.color.colorPrimary);
        ((Shno)getActivity()).lockBtn.setImageResource(R.drawable.ic_lock_user_symbol);
        ((Shno)getActivity()).lockBtn.setBackgroundResource(R.color.colorPrimary);
        mAuth = FirebaseAuth.getInstance();
        progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage("Please Wait....");
        return rootView;
    }

    @OnClick(R.id.button3)
    public void register() {
        String email = remailText.getText().toString();
        String pass = rpassText.getText().toString();
        String pass1 = rpassText1.getText().toString();
        String message = "";
        if (TextUtils.isEmpty(email)) message += "\nالبريد الألكتروني مطلوب";
        if (TextUtils.isEmpty(pass)) message += "\nحقل كلمةالمرور مطلوب";
        if (!TextUtils.equals(pass, pass1)) message += "\nحقل كلمتي المرور غير متطابق";
        if (pass.length()<6) message += "كلمة المرور لابد ان تكون اكثر من سته احرف ";
        if (TextUtils.isEmpty(message)) {
            Task<AuthResult> task = mAuth.createUserWithEmailAndPassword(email, pass);
            progressdialog.show();
            task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "تم اضافة مستخدم", Toast.LENGTH_SHORT).show();
                        HomeFragment nextFrag = new HomeFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, nextFrag)
                                .addToBackStack(null)
                                .commit();
                        ((Shno)getActivity()).addBtn.setImageResource(R.drawable.ic_add_contact);
                        ((Shno)getActivity()).addBtn.setBackgroundResource(R.color.colorPrimary);
                        progressdialog.dismiss();

                    } else {
                        Toast.makeText(getActivity(), "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                        progressdialog.dismiss();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "الرجاء ادخال بيانات صحيحة", Toast.LENGTH_SHORT).show();
                    progressdialog.dismiss();

                }
            });
        } else {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            remailText.setText("");
            rpassText.setText("");
            rpassText1.setText("");
        }
    }


    public boolean onBackKeyPressed() {
        return true;
    }
}
