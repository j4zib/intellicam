package com.example.j4zib.intellicam;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Dialog extends AppCompatActivity {
    private static final String TAG = "Dialog";
    EditText name;
    CheckBox spamCheck;
    Button doneButton;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        final String test=getIntent().getStringExtra("id");
        Log.d(TAG, "onCreate: "+ test);
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(test);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users").document(test).update(
                        "name", name.getText(),
                        "spam", spamCheck.isChecked()?Integer.parseInt(getIntent().getStringExtra("spam"))+1:Integer.parseInt(getIntent().getStringExtra("spam"))
                );

            }
        });

    }
}
