package com.mktreports.chatfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

import java.util.List;

import javax.annotation.Nullable;

public class ContactActiviy extends AppCompatActivity {

    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_activiy);

        adapter = new GroupAdapter();
        RecyclerView rv = findViewById(R.id.recycler);

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                Intent intent = new Intent(ContactActiviy.this, ChatActivity.class);

                UserItem userItem = (UserItem) item;
                intent.putExtra("user", userItem.user);

                startActivity(intent);
            }
        });

        fetchUsers();
    }
    //buscar os dados no Firestor
    private void fetchUsers() {
        FirebaseFirestore.getInstance().collection("/users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null){
                            Log.e("Teste", e.getMessage(), e);
                            return;
                        }

                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc: documents){
                            User user = doc.toObject(User.class);
                            Log.d("Teste", user.getUsername());

                            adapter.add(new UserItem(user));
                        }
                    }
                });
    }

    private class UserItem extends Item {
        private final User user;

        private UserItem(User user){
            this.user = user;
        }


        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
            TextView textUsername = viewHolder.itemView.findViewById(R.id.textName);
            ImageView imgPhoto = viewHolder.itemView.findViewById(R.id.imgItemUser);

            textUsername.setText(user.getUsername());

            Picasso.get()
                    .load(user.getProfileUrl())
                    .into(imgPhoto);
        }

        @Override
        public int getLayout() {
            return R.layout.item_user;
        }
    }

}
