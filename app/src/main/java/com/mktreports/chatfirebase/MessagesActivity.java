package com.mktreports.chatfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import java.util.List;

import javax.annotation.Nullable;

public class MessagesActivity extends AppCompatActivity {
    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        RecyclerView rv = findViewById(R.id.recycller_contact);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GroupAdapter();
        rv.setAdapter(adapter);

        verifyAuthentication();

        fetchLastMessage();

    }

    private void fetchLastMessage() {
        String uid = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore.getInstance().collection("/last-messages")
                .document(uid)
                .collection("contacts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();
                        if (documentChanges != null){
                            for (DocumentChange doc: documentChanges){
                                if (doc.getType() == DocumentChange.Type.ADDED){
                                    Contact contact = doc.getDocument().toObject(Contact.class);
                                    adapter.add(new ContactItem(contact));
                                }
                            }
                        }
                    }
                });
    }

    //Verifica se esta logado.
    private void verifyAuthentication() {
        if (FirebaseAuth.getInstance().getUid() == null){
            Intent intent = new Intent(MessagesActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.contacts:
                Intent intent = new Intent(MessagesActivity.this, ContactActiviy.class);
                startActivity(intent);
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                verifyAuthentication();
        }
        return super.onOptionsItemSelected(item);
    }

    private class ContactItem extends Item {

        private final Contact contact;

        private ContactItem(Contact contact) {
            this.contact = contact;
        }

        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
            TextView username = viewHolder.itemView.findViewById(R.id.textName);
            TextView message = viewHolder.itemView.findViewById(R.id.textView);
            ImageView imgPhoto = viewHolder.itemView.findViewById(R.id.imgItemUser);

            username.setText(contact.getUsername());
            message.setText(contact.getLastMessage());
            Picasso.get().load(contact.getPhotoUrl()).into(imgPhoto);
        }

        @Override
        public int getLayout() {
            return R.layout.item_user_message;
        }
    }
}
