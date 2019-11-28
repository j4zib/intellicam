package com.example.j4zib.intellicam;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dialog extends AppCompatActivity {
    private static final String TAG = "Dialog";
    EditText name;
    CheckBox spamCheck;
    Button doneButton;
    FirebaseFirestore db;
    public ImageView image;
    StorageReference storageReference;
    FirebaseStorage storage;
    String test;
    TextView spamText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        name = findViewById(R.id.nameEditText);
        spamCheck = findViewById(R.id.tick);
        doneButton = findViewById(R.id.button);
        image = findViewById(R.id.imageView);
        spamText = findViewById(R.id.spamDialog);
        spamText.setText(getIntent().getStringExtra("spam"));
        test=getIntent().getStringExtra("id");
        if(getIntent().hasExtra("name"))
            name.setText(getIntent().getStringExtra("name"));
        else name.setText("unknown");
        Log.d(TAG, "onCreate: "+ test);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();
        storageReference.child(test+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .into(image);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users").document(test).update(
                        "name", name.getText().toString(),
                        "spam", spamCheck.isChecked()?Integer.parseInt(getIntent().getStringExtra("spam"))+1:Integer.parseInt(getIntent().getStringExtra("spam"))
                );
                finish();

            }
        });


    }
}
