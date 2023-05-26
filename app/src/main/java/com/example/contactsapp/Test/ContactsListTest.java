package com.example.contactsapp.Test;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsapp.Contact;
import com.example.contactsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ContactsListTest extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Contact> contactArrayList;
    MyAdapterTest myAdapter;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstaceState) {

        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_contactslist);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching your list of contacts");
        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        contactArrayList = new ArrayList<Contact>();
        myAdapter = new MyAdapterTest(ContactsListTest.this,contactArrayList);
        recyclerView.setAdapter(myAdapter);

        GetContactsFromDB();

    }

    private void GetContactsFromDB(){
        DocumentReference docRef = db.collection("Users").document(currentUser.getEmail());
                docRef.collection("contacts")
                .orderBy("firstName", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                Log.e("FireStore error", error.getMessage());
                            }
                        }

                        for(DocumentChange dc : value.getDocumentChanges()){

                            if(dc.getType() == DocumentChange.Type.ADDED){
                                contactArrayList.add(dc.getDocument().toObject(Contact.class));
                            }

                            myAdapter.notifyDataSetChanged();

                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                        }
                    }
                });
    }
}
