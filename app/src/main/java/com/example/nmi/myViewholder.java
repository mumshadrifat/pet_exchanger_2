package com.example.nmi;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.security.cert.TrustAnchor;

public class myViewholder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
    TextView headtext;
    public ImageView imageView;
    ImageButton likeButton, unlikeButton;
    TextView descText;
    TextView numberOfLikesTV;
    TextView phone;
    TextView price;
    TextView adress;
    FirebaseDatabase database;
    DatabaseReference databaseReference,likeRef;
    String currentuserId;
    int likecount;
//    MyaAdapter.OnItemClickListener listener;



    public myViewholder(@NonNull View itemView) {
        super(itemView);
        headtext=itemView.findViewById(R.id.headId);
        imageView=itemView.findViewById(R.id.imageId);

         descText=itemView.findViewById(R.id.descId);
         likeButton = itemView.findViewById(R.id.likebtnId);

         unlikeButton = itemView.findViewById(R.id.unlikebtnId);

         numberOfLikesTV = itemView.findViewById(R.id.no_like_id);

        database = FirebaseDatabase.getInstance();

        likeRef= database.getReference("Likes");

        currentuserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        itemView.setOnCreateContextMenuListener(this);





        // postRef= FirebaseDatabase.getInstance().getReference("MyItem").child("Likes");
       //  phone=itemView.findViewById(R.id.pnid);
        // price=itemView.findViewById(R.id.prcId);
        // adress=itemView.findViewById(R.id.addId);



    }

    public void set_title_tv(String title_text){
        headtext.setText(title_text);
    }
    public  void set_desc_text(String desc_text){
        descText.setText(desc_text);
    }
    public void set_price_text(String text){
        price.setText(text);

    }
    public void set_phone_text(String text){
        phone.setText(text);


    }
    public void set_address_text(String text){
        adress.setText(text);

    }
    public void  set_image(String imageUri)
    {
        Picasso.get().load(imageUri).placeholder(R.mipmap.ic_launcher_round).fit().centerCrop().into(imageView);
    }

    public void update_button_status(boolean value){

        if (value)
        {
            // thats mean already like so we need to show red Button
            likeButton.setVisibility(View.VISIBLE);
            unlikeButton.setVisibility(View.INVISIBLE);

        }else{
            // thats mean not like  yet so we need to show white Button
            likeButton.setVisibility(View.INVISIBLE);
            unlikeButton.setVisibility(View.VISIBLE);

        }
    }
    public void update_like_number(long countLike) {
        String s = String.valueOf(countLike);
        numberOfLikesTV.setText(s);
    }





    public void updateLikeCount(String s){

        numberOfLikesTV.setText(s);
      }



//    public void update_status(boolean value)
//    {
//        if (value){
//            // set Image button enable(false)
//            // color wala button enable
//           // likeButton.setImageDrawable(R.drawable.like);
//            likeButton.setImageResource(R.drawable.like);
//
//        }else{
//            // set ImageButton enable True
//            // red button enable(false)
//            likeButton.setImageResource(R.drawable.dislike);
//        }
//    }

    public void setlikebuttonstatus(final String postId) {
      likeRef.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if(dataSnapshot.child(postId).hasChild(currentuserId)){

                  likecount=(int)dataSnapshot.child(postId).getChildrenCount();
                  likeButton.setImageResource(R.drawable.like);
                  numberOfLikesTV.setText(Integer.toString(likecount));

               //   Toast.makeText(m,"sucess",Toast.LENGTH_LONG).show();
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });


    }


    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("choose action");
        MenuItem  delete= contextMenu.add(Menu.NONE,1,1,"delete");
        MenuItem  edit= contextMenu.add(Menu.NONE,2,2,"Edit");



    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }
}

