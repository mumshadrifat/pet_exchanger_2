package com.example.nmi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<MyItem> listitems;
    private MyaAdapter myadapter;
    private DatabaseReference databaseReference, likeReference;
    private FirebaseAuth mAuth;
    private FloatingActionButton addPostBtn;
    private Home homeFragment;
    private Notification_fragment notificationFragment;
    private accountFragment accountFragment;

    private ImageButton likebtn, comment;
    private TextView no_of_like;
    BottomNavigationView mainbottomNav;

    //Firebase
    FirebaseUser currentUser;
    FirebaseRecyclerOptions<MyItem> options;
    FirebaseRecyclerAdapter<MyItem, myViewholder> fireAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("MyItem");
        databaseReference.keepSynced(true);
        likeReference = FirebaseDatabase.getInstance().getReference("Likes");
        likeReference.keepSynced(true);


        options = new FirebaseRecyclerOptions.Builder<MyItem>().setQuery(databaseReference, MyItem.class)
                .build();

        fireAdapter = new FirebaseRecyclerAdapter<MyItem, myViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final myViewholder holder, int position, @NonNull final MyItem myItem) {


                try {
                    holder.set_title_tv(myItem.getPetname());
                    holder.set_desc_text(myItem.getDescription());

                    Picasso.get().load(myItem.getImageurl()).placeholder(R.mipmap.ic_launcher_round).fit().centerCrop().into(holder.imageView);
                } catch (NullPointerException e) {
                    Log.d(TAG, "onBindViewHolder: null pointer: " + e.getMessage());
                }

                // like reference read all likes
                likeReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // check postID in myitem
                        if (myItem.getPostId() != null) {
                            if (dataSnapshot.child(myItem.getPostId()).exists()) {
                                long likeCount = dataSnapshot.child(myItem.getPostId()).getChildrenCount();
                                Log.d(TAG, "onDataChange:Like number:  " + likeCount);
                                holder.update_like_number(likeCount);

                                // check my current user id in databse
                                if (dataSnapshot.child(myItem.getPostId()).child(currentUser.getUid()).exists()) {
                                    holder.update_button_status(true);
                                } else {
                                    // current uid not exists in post id
                                    // thats means current not liked yet
                                    holder.update_button_status(false);
                                }

                            } else {
                                // missing post id in database
                                holder.update_button_status(false);
                                holder.update_like_number(0);
                            }

                        } else {
                            Log.d(TAG, "onDataChange:post id missing ");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                // button click

                holder.likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // need to remove from database
                        if (holder.likeButton.getVisibility() == View.VISIBLE) {
                            likeReference.child(myItem.getPostId()).child(currentUser.getUid()).removeValue();
                            holder.update_button_status(false);
                        }

                        // update button status

                    }
                });

                holder.unlikeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // need to add cuurent user uid into database
                        // update button status
                        if (holder.unlikeButton.getVisibility() == View.VISIBLE) {
                            likeReference.child(myItem.getPostId()).child(currentUser.getUid()).setValue(currentUser.getUid());
                            holder.update_button_status(true);
                        }
                    }
                });
                // data binding end here


            }

            @NonNull
            @Override
            public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new myViewholder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.rowlayout, parent, false));

            }
        };

        recyclerView = (RecyclerView) findViewById(R.id.rId);
        listitems = new ArrayList<>();

        // myadapter = new MyaAdapter(listitems, MainActivity.this);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(fireAdapter);
        //  myadapter.setOnitemclicklistener(this);

        mainbottomNav = findViewById(R.id.mainBottomNav);
        //initializeFragment();

//        mainbottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
//
//                switch (item.getItemId()) {
//
//                    case R.id.bottom_action_home:
//
//                        replaceFragment(homeFragment, currentFragment);
//                        return true;
//
//                    case R.id.bottom_action_account:
//
//                        replaceFragment(accountFragment, currentFragment);
//                        return true;
//
//                    case R.id.bottom_action_notif:
//
//                        replaceFragment(notificationFragment, currentFragment);
//                        return true;
//
//                    default:
//                        return false;
//
//
//                }
//
//            }
//        });


        addPostBtn = findViewById(R.id.add_post_btn);
        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newPostIntent = new Intent(MainActivity.this, newpost.class);
                startActivity(newPostIntent);

            }
        });


//        databaseReference= FirebaseDatabase.getInstance().getReference("MyItem");
////        databaseReference.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////
////
////                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
////
////                    MyItem myItem = dataSnapshot1.getValue(MyItem.class);
////                    //Log.d(TAG, "onDataChange:check: "+myItem.getPetname());
////                    listitems.add(myItem);
////                    recyclerView.setAdapter(myadapter);
////
////                    myadapter.notifyDataSetChanged();
////
////
////                }
//////                try {
//////                    myadapter = new MyaAdapter(listitems, MainActivity.this);
//////                    recyclerView.setAdapter(myadapter);
//////                }
//////                catch (Exception e) {
//////
//////                }Toast.makeText(getApplicationContext(),"probelm in set adapter",Toast.LENGTH_LONG).show();
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////                Toast.makeText(getApplicationContext(),"Databse error",Toast.LENGTH_LONG).show();
////            }
////        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId() == R.id.signoutId) {

            FirebaseAuth.getInstance().signOut();

            Intent in = new Intent(getApplicationContext(), SignIn.class);
            startActivity(in);


        } else if (item.getItemId() == R.id.action_settings_btn) {


            Intent in = new Intent(getApplicationContext(), accountSetup.class);
            startActivity(in);


        } else if (item.getItemId() == R.id.action_logout_btn) {

            Intent in = new Intent(getApplicationContext(), SensorActivity.class);
            startActivity(in);


        } else if (item.getItemId() == R.id.viewprofile) {

            Intent in = new Intent(getApplicationContext(), viewProfile.class);
            startActivity(in);


        }


        return super.onOptionsItemSelected(item);
    }


//    @Override
//    public void onItemClick(int position) {
//
//
//     Toast.makeText(getApplicationContext(),"yes",Toast.LENGTH_LONG);
//
//
//
//    }


    @Override
    protected void onStart() {
        super.onStart();
        fireAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        fireAdapter.stopListening();
    }
}





