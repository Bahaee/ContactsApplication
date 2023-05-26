package com.example.contactsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ContactsAdd extends AppCompatActivity {

    EditText addFirstName, addLastName, addPhone, addEmail, addProfession;
    Button addButton;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Lier mon code java avec le layout
        setContentView(R.layout.activity_contacts_add);

        //Instancier les variables
        addFirstName = findViewById(R.id.add_firstname);
        addLastName = findViewById(R.id.add_lastname);
        addPhone = findViewById(R.id.add_phone);
        addEmail = findViewById(R.id.add_email);
        addProfession = findViewById(R.id.add_profession);
        addButton = findViewById(R.id.add_contact_button);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstName = addFirstName.getText().toString();
                String lastName = addLastName.getText().toString();
                String email = addEmail.getText().toString().trim();
                String phone = addPhone.getText().toString().trim();
                String profession = addProfession.getText().toString();


                //On vérifie si les champs ne sont pas vides
                if (TextUtils.isEmpty(firstName)) {
                    Toast.makeText(ContactsAdd.this, "Enter The First Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(lastName)) {
                    Toast.makeText(ContactsAdd.this, "Enter The Last Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(ContactsAdd.this, "Enter The Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ContactsAdd.this, "Enter The Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(profession)) {
                    Toast.makeText(ContactsAdd.this, "Enter The Profession", Toast.LENGTH_SHORT).show();
                    return;
                }

                //On crée l'objet MAP qui contient les données à ajouter
                Map<String, Object> contactData = new HashMap<>();
                contactData.put("firstName", firstName);
                contactData.put("lastName", lastName);
                contactData.put("email", email);
                contactData.put("phone", phone);
                contactData.put("profession", profession);


                db.collection("Users")
                        .document(mAuth.getCurrentUser().getEmail())
                        .collection("contacts")
                        .document(phone)
                        .set(contactData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ContactsAdd.this, "Contact added successfully", Toast.LENGTH_SHORT).show();
                                // Réinitialiser les champs de saisie après l'ajout réussi
                                addFirstName.setText(firstName);
                                addLastName.setText(lastName);
                                addEmail.setText(email);
                                addPhone.setText(phone);
                                addProfession.setText(profession);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ContactsAdd.this, "Failed to add contact", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }


}