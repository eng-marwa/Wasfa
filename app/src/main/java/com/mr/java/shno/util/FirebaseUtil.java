package com.mr.java.shno.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by java on 08/11/2017.
 */

public class FirebaseUtil {
    public static DatabaseReference getMaindish(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference maindishRef = database.getReference("maindish");
        maindishRef.keepSynced(true);
        return maindishRef;
    }
    public static DatabaseReference getSweet(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference sweetRef = database.getReference("sweet");
        return sweetRef;
    }
    public static DatabaseReference getEntrees(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference entreesRef = database.getReference("entrees");
        return entreesRef;
    }
}
