package com.example.nmi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.TrustAnchor;




public class MyaAdapter extends RecyclerView.Adapter<myViewholder> {
    private static final String TAG = "MyaAdapter";
    //

    FirebaseDatabase database;
    DatabaseReference databaseReference,likeRef;
    Boolean likechecker=false;
    String currentuserId;


    public List<MyItem>myList;
    public   Context context;
    OnItemClickListener listener;

    public MyaAdapter(List<MyItem> myList, Context context) {
        this.myList = myList;
        this.context = context;
    }


    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout,parent,false);


        database  = FirebaseDatabase.getInstance();

        likeRef= database.getReference("Likes");

        currentuserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        return  new myViewholder(v);



    }

    @Override
    public void onBindViewHolder(@NonNull final myViewholder holder, final int position) {

        final  String postId= myList.get(position).getUser_id();

        final MyItem myitemposition = myList.get(position);
        holder.headtext.setText(myitemposition.getPetname());
        Picasso.get().load(myitemposition.getImageurl()).placeholder(R.mipmap.ic_launcher_round).fit().centerCrop().into(holder.imageView);
        holder.setlikebuttonstatus(postId);
        holder.descText.setText(myitemposition.getDescription());







        holder.headtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.onItemClick(position);




                }
            }
        });

        holder.likeButton.setOnClickListener(new View.OnClickListener() {

            //
            @Override

            public void onClick(View view) {


                likechecker=true;


                likeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(likechecker.equals(true))

                        {
                            if(dataSnapshot.child(postId).hasChild(currentuserId)){


                                likeRef.child(postId).removeValue();
                                likechecker=false;
                            }
                            else{
                                likeRef.child(postId).setValue(true);
                                likechecker=false;
                            }

                            // holder.setlikebuttonstatus(postId);
                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





//                String liketId = databaseReference.push().getKey();
//                Map<String, String> like = new HashMap<>();
//                like.put("user_id",myitemposition.getUser_id());
//                databaseReference.child(liketId).setValue(like);

                //

                //

                // String likes_number = "10";
                // holder.updateLikeCount(likes_number); //string type

                // update status(True)

            }
        });



    }

    @Override
    public int getItemCount()  {
        return myList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    void setOnitemclicklistener(OnItemClickListener listener){

        this.listener=listener;

    }







}
