package com.example.j4zib.intellicam;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonAdapter extends FirestoreRecyclerAdapter<Person, PersonAdapter.ViewHolder> {

    StorageReference storageReference;
    FirebaseStorage storage;


    public PersonAdapter(@NonNull FirestoreRecyclerOptions<Person> options) {
        super(options);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }



    @NonNull
    @Override
    public PersonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_main,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PersonAdapter.ViewHolder viewHolder, int i, @NonNull Person person) {
        final DocumentSnapshot snapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        viewHolder.name.setText(person.getName());
       // viewHolder.name.setText(snapshot.getId());



        storageReference.child(snapshot.getId()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(viewHolder.image);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public CircleImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.profile_image);
        }
    }
}
