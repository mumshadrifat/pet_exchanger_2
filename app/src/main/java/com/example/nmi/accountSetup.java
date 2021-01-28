package com.example.nmi;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class accountSetup extends AppCompatActivity implements View.OnClickListener {
    Button usersavebtn,choosebtn;
    ImageView userimage;
    EditText username;
    Uri Imageuri;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    StorageTask uploadtask;
    FirebaseAuth mAuth;
    public FirebaseAuth firebaseAuth;
    private String user_id;
    private  static  final int IMAGE_REQUEST =1;
    FirebaseUser firebaseUser ;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup);
        usersavebtn=findViewById(R.id.usersaveButton);
        userimage=findViewById(R.id.userImageId);
        username=findViewById(R.id.usernameId);
        choosebtn=findViewById(R.id.userchooseImage);
        choosebtn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        usersavebtn.setOnClickListener(this);
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference= database.getReference("Users");

        storageReference = FirebaseStorage.getInstance().getReference("Users");


    }

    @Override
    public void onClick(View v) {


  if(v.getId()==R.id.userchooseImage){
      databaseReference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              if (dataSnapshot.child(currentUser.getUid()).exists())
              {
                  Toast.makeText(getApplicationContext(),"your account already in databse",Toast.LENGTH_LONG).show();

              }else
              {
                  // user not foound
                  openfilechoser();
              }



          }



          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });


    }
    else{
        savedata();
        }
}



    private void savedata() {

        final String uname=username.getText().toString().trim();

        if(uname.isEmpty())
        {
            username.setError("please give your name");
            username.requestFocus();
            return;
        }

        StorageReference ref=storageReference.child(System.currentTimeMillis()+"-"+getExtension(Imageuri));

        ref.putFile(Imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(getApplicationContext(),"image save succsessfully",Toast.LENGTH_LONG).show();
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()  );
                        Uri downloadUri=uriTask.getResult();
                        users users = new users(uname,downloadUri.toString());

                        String postId = firebaseUser.getUid();
                        String liketId = databaseReference.push().getKey();


                        databaseReference.child(firebaseUser.getUid()).setValue(users);
                        username.setText(" ");


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });



    }
    public  String getExtension(Uri Imageuri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap =MimeTypeMap.getSingleton();

        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(Imageuri));
    }









    private void openfilechoser() {

        Intent intent=new Intent();
        intent.setType("Image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {


            Imageuri=data.getData();
            Picasso.get().load(Imageuri ).into(userimage);

        }


    }
}






