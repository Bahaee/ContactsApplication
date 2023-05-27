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

    private RecyclerView contactsRecycler;
    private EditText searchEditText;
    private MyAdapter myAdapter;
    private LinkedList<Contact> contacts;
    private ContactDao contactDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_contacts);

        contactsRecycler = findViewById(R.id.list_contacts);
        searchEditText = findViewById(R.id.hint);
        findViewById(R.id.fab_add).setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        contactsRecycler.setLayoutManager(layoutManager);

        contactDao = new ContactDao();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterContacts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        getContacts();
    }

    private void getContacts() {
        contactDao.getAllContacts(new ContactDao.OnContactsLoadedListener() {
            @Override
            public void onContactsLoaded(LinkedList<Contact> loadedContacts) {
                contacts = loadedContacts;
                myAdapter = new MyAdapter(contacts, ContactsList.this);
                contactsRecycler.setAdapter(myAdapter);
            }
        });
    }

    private void filterContacts(String query) {
        LinkedList<Contact> filteredContacts = new LinkedList<>();

        if (query.isEmpty()) {
            filteredContacts.addAll(contacts);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Contact contact : contacts) {
                String fullName = contact.getFirstNameContact() + " " + contact.getLastNameContact();
                if (fullName.toLowerCase().startsWith(lowerCaseQuery)) {
                    filteredContacts.add(contact);
                }
            }
        }

        myAdapter = new MyAdapter(filteredContacts, ContactsList.this);
        contactsRecycler.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_add) {
            startActivity(new Intent(this, ContactDetails.class));
        }
    }
}
