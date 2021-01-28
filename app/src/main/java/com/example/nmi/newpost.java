package com.example.nmi;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nmi.model.LikeModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class newpost extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "newpost";

    Button choosebtn,savebtn,displaybtn;
    ImageView image;
    EditText pname,description,phon_number,adress,price;
    ProgressBar progressBarz;
    Uri Imageuri;
    FirebaseDatabase database;
    DatabaseReference databaseReference, likeRef;
    StorageReference storageReference;
    StorageTask  uploadtask;
    public FirebaseAuth firebaseAuth;
    private String user_id;

    private  static  final int IMAGE_REQUEST =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.post_new);
        choosebtn=findViewById(R.id.chooseButtonId);
        savebtn=findViewById(R.id.saveButton);
        pname=findViewById(R.id.captionId);
        description=findViewById(R.id.descriptionId);
        phon_number=findViewById(R.id.phoneId);
        price=findViewById(R.id.priceId);
        adress=findViewById(R.id.addressId);

        progressBarz=findViewById(R.id.prgressId);
        image=findViewById(R.id.imageid);
        savebtn.setOnClickListener(this);
        choosebtn.setOnClickListener(this);
        displaybtn=findViewById(R.id.displayButton);
        // StorageReference storageReference;
        displaybtn.setOnClickListener(this);
        database = FirebaseDatabase.getInstance();
        databaseReference= database.getReference("MyItem");

        storageReference =FirebaseStorage.getInstance().getReference("MyItem");

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.saveButton){
            if(uploadtask !=null && uploadtask.isInProgress()){
                Toast.makeText(getApplicationContext(),"uploading in progressing",Toast.LENGTH_LONG).show();
            }
            else {
                savedata();
            }

        }
        else if(v.getId()== R.id.chooseButtonId){


            openfilechoser();

        }
        else if(v.getId()== R.id.displayButton){
            Intent intent2=new Intent(newpost.this,MainActivity.class);
            startActivity(intent2);
        }


    }

    private void savedata() {

        final String petname=pname.getText().toString().trim();
        final String des=description.getText().toString().trim();
        final String phonx=phon_number.getText().toString().trim();
        final String pricex=price.getText().toString().trim();
        final String adds=adress.getText().toString().trim();


        if(petname.isEmpty())
        {
            pname.setError("pet name is missing");
            pname.requestFocus();
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
                            MyItem myItem = new MyItem(petname,downloadUri.toString(), des,phonx,pricex,adds);
                            myItem.setUser_id(user_id);
                            String postId = databaseReference.push().getKey();
                            String liketId = databaseReference.push().getKey();
                            myItem.setPostId(postId);
//                            likeRef = database.getReference("likes");
//                            likeRef.child(postId).setValue(myItem);
                              // String email = firebaseAuth.getCurrentUser().getEmail();
                              // Log.d(TAG, "onSuccess: user email "+email);

                        // need to create a unique post id

                            databaseReference.child(postId).setValue(myItem); //.child(user_id)

                            pname.setText(" ");
                            phon_number.setText("");
                            description.setText("");
                            price.setText("");
                            adress.setText(" ");


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
            Picasso.get().load(Imageuri ).into(image);
            //StorageReference imagename=storageReference.child("image"+Image)
            //image.setImageURI(Imageuri);
        }


    }
}
