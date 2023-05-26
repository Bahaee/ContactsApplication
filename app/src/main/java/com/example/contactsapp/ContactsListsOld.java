package com.example.contactsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.contactsapp.Adapters.MyAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

public class ContactsListsOld extends AppCompatActivity implements View.OnClickListener{

    FloatingActionButton fab_add;
    RecyclerView contactsRecycler;
    EditText barreRecherche;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    LinkedList<Contact> contacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_contacts);

        contactsRecycler=(RecyclerView) findViewById(R.id.list_contacts);
        barreRecherche=(EditText) findViewById(R.id.hint);
        fab_add=(FloatingActionButton) findViewById(R.id.fab_add);
        db = FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        System.out.println("hhh");
        fab_add.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("hhhx2");
        getContacts();
        System.out.println("hhhx3");


    }


    public void onClick(View view) {
        if(view.getId()==R.id.fab_add){
            Intent myintent= new Intent(this, ContactsAdd.class);
            startActivity(myintent);
        }
    }


    void getContacts(){
        contacts= new LinkedList<Contact>();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("Users").document(currentUser.getEmail());
        docRef.collection("contacts").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println("ha 4");
                        System.out.println("Nombre de documents retournés : " + task.getResult().size());
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Contact c = new Contact(document.get("firstName").toString(),
                                    document.get("lastName").toString(),
                                    document.get("phone").toString(),
                                    document.get("email").toString(),
                                    document.get("profession").toString()
                            );
                            System.out.println("ha 5");
                            contacts.add(c);
                        }



                        // use this setting to improve performance if you know that changes
                        // in content do not change the layout size of the RecyclerView
                        contactsRecycler.setHasFixedSize(true);

                        LinearLayoutManager layoutManager = new LinearLayoutManager(ContactsListsOld.this);
                        contactsRecycler.setLayoutManager(layoutManager);

                        MyAdapter myAdapter = new MyAdapter(contacts, ContactsListsOld.this);

                        contactsRecycler.setAdapter(myAdapter);

                    } else {
                        Log.d("not ok", "Error getting documents: ", task.getException());
                        System.out.println("hna error");
                    }
                    System.out.printf("hadshi khdam");
                });
    }


}