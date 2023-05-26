package com.example.contactsapp.DAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.contactsapp.Contact;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

public class ContactDao {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public ContactDao(){
        db = FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    public interface OnContactsLoadedListener {
        void onContactsLoaded(LinkedList<Contact> contacts);
    }

    public void getAllContacts(OnContactsLoadedListener listener) {
        LinkedList<Contact> contacts = new LinkedList<>();

        DocumentReference docRef = db.collection("Users").document(currentUser.getEmail());
        docRef.collection("contacts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Contact c = new Contact(document.get("firstName").toString(),
                                        document.get("lastName").toString(),
                                        document.get("phoneNumber").toString(),
                                        document.get("email").toString(),
                                        document.get("profession").toString());
                                contacts.add(c);
                            }
                            listener.onContactsLoaded(contacts);
                        } else {
                            Log.d("not ok", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    // Add other CRUD methods
}
