package com.example.nmi;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class viewProfile extends AppCompatActivity {

     ImageView proImage;
     TextView proName;
     FirebaseAuth mAuth;
     FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference databaseReference,DusereRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        proName=findViewById(R.id.proNameId);
        proImage=findViewById(R.id.proImageId);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database=FirebaseDatabase.getInstance();
        DusereRef= database.getReference("Users");

         DusereRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 if (dataSnapshot.child(currentUser.getUid()).exists())
                 {
                     // user found
                     users u = dataSnapshot.child(currentUser.getUid()).getValue(users.class);
                    // Log.d(TAG, "onDataChange: username: "+u.getUsername());
                     Picasso.get().load(u.getUserImage()).placeholder(R.mipmap.ic_launcher_round).fit().centerCrop().into(proImage);
                     proName.setText(u.getUsername());
                 }else
                 {
                     // user not foound
                 }



             }



             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });




    }

}
