package com.example.contactsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.contactsapp.Adapters.MyAdapter;
import com.example.contactsapp.DAO.ContactDao;
import com.example.contactsapp.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;

public class ContactsList extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab_add;
    RecyclerView contactsRecycler;
    EditText barreRecherche;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    MyAdapter myAdapter;
    LinkedList<Contact> contacts;
    ProgressDialog mProgressDialog;
    ContactDao cda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_contacts);

        contactsRecycler = findViewById(R.id.list_contacts);
        barreRecherche = findViewById(R.id.hint);
        fab_add = findViewById(R.id.fab_add);
        fab_add.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        contactsRecycler.setLayoutManager(layoutManager);

        barreRecherche.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    myAdapter = new MyAdapter(contacts, ContactsList.this);
                    contactsRecycler.setAdapter(myAdapter);
                } else {
                    LinkedList<Contact> filteredContacts = filterContacts(contacts, s.toString());
                    myAdapter = new MyAdapter(filteredContacts, ContactsList.this);
                    contactsRecycler.setAdapter(myAdapter);
                }
            }

            private LinkedList<Contact> filterContacts(LinkedList<Contact> contacts, String query) {
                LinkedList<Contact> filteredList = new LinkedList<>();

                for (Contact contact : contacts) {
                    String fullName = contact.getFirstNameContact() + " " + contact.getLastNameContact();
                    if (fullName.toLowerCase().startsWith(query.toLowerCase())) {
                        filteredList.add(contact);
                    }
                }

                return filteredList;
            }
        });

        cda = new ContactDao();
        getContacts();
    }

    void getContacts() {
        showProgressDialog();

        cda.getAllContacts(new ContactDao.OnContactsLoadedListener() {
            @Override
            public void onContactsLoaded(LinkedList<Contact> contacts) {
                ContactsList.this.contacts = contacts;

                myAdapter = new MyAdapter(contacts, ContactsList.this);
                contactsRecycler.setAdapter(myAdapter);

                hideProgressDialog();
            }
        });
    }

    private LinkedList<Contact> filterContacts(LinkedList<Contact> contacts, String query) {
        LinkedList<Contact> filteredList = new LinkedList<>();

        for (Contact contact : contacts) {
            String fullName = contact.getFirstNameContact() + " " + contact.getLastNameContact();
            if (fullName.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(contact);
            }
        }

        return filteredList;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_add) {
            Intent i = new Intent(this, ContactDetails.class);
            startActivity(i);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading Contacts...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}