package com.mr.java.shno.util;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.mr.java.shno.AddNewSwfa;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/**
 * Created by MarwaTalaat on 5/5/2017.
 */

public class UploadFirebaseTask extends AsyncTask<Uri, Void, Uri> {
    private String url;
    private Uri downloadUrl;


    @Override
    protected Uri doInBackground(Uri... params) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        Uri uri = params[0];
        if (uri != null) {
            StorageReference storageRef = mStorageRef.child("images/" + new File(uri.getPath()).getName());
            storageRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(com.google.firebase.storage.UploadTask.TaskSnapshot taskSnapshot) {
                            //noinspection VisibleForTests
                             downloadUrl = taskSnapshot.getDownloadUrl();

                                    AddNewSwfa.getDownloadedUri(String.valueOf(downloadUrl));


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }

                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(com.google.firebase.storage.UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                           @SuppressWarnings("VisibleForTests") double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        }
                    });
        }
        return downloadUrl;
    }


}
