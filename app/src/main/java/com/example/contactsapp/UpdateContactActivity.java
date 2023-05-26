package com.example.contactsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateContactActivity extends AppCompatActivity {

    private static final String TAG = "UpdateContactActivity";

    private EditText  firstNameEditText;
    private EditText  lastNameEditText;
    private EditText  professionEditText;
    private EditText emailEditText;
    private TextView phoneNumberTextView;
    private TextView titleNameTextView;
    private Button verifyUpdateButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_update);

        // Initialiser mAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialiser la référence à Firestore
        db = FirebaseFirestore.getInstance();

        // Référencer les vues du layout
        firstNameEditText = findViewById(R.id.firstNameContact);
        lastNameEditText = findViewById(R.id.lastNameContact);
        professionEditText = findViewById(R.id.professionContact);
        emailEditText = findViewById(R.id.emailContact);
        phoneNumberTextView = findViewById(R.id.phoneNumberContact);
        titleNameTextView = findViewById(R.id.titleName);
        verifyUpdateButton = findViewById(R.id.verifyUpdate);

        // Récupérer le contact à mettre à jour
        contact = (Contact) getIntent().getSerializableExtra("contact");

        if (contact != null) {
            // Pré-remplir les champs avec les valeurs actuelles du contact
            firstNameEditText.setText(contact.getFirstNameContact());
            lastNameEditText.setText(contact.getLastNameContact());
            professionEditText.setText(contact.getProfessionContact());
            emailEditText.setText(contact.getEmailContact());
            phoneNumberTextView.setText(contact.getPhoneNumberContact());
        }

        // Définir le clic du bouton de mise à jour du contact
        verifyUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
            }
        });
    }

    private void updateContact() {
        // Récupérez les nouvelles valeurs des champs depuis les EditText
        String newFirstName = firstNameEditText.getText().toString();
        String newLastName = lastNameEditText.getText().toString();
        String newProfession = professionEditText.getText().toString();
        String newEmail = emailEditText.getText().toString();
        String newPhoneNumber = phoneNumberTextView.getText().toString();

        // Mettez à jour les valeurs du contact dans Firestore en utilisant son ID
        db.collection("Users")
                .document(mAuth.getCurrentUser().getEmail())
                .collection("contacts")
                .document(contact.getPhoneNumberContact())
                .update("firstName", newFirstName,
                        "lastName", newLastName,
                        "profession", newProfession,
                        "email", newEmail,
                        "phone", newPhoneNumber)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Mettez à jour les TextView avec les nouvelles valeurs du contact
                            if (contact != null) {
                                contact.setFirstNameContact(newFirstName);
                                contact.setLastNameContact(newLastName);
                                contact.setProfessionContact(newProfession);
                                contact.setEmailContact(newEmail);
                                contact.setPhoneNumberContact(newPhoneNumber);
                            }

                            Toast.makeText(UpdateContactActivity.this, "Le contact a été mis à jour avec succès", Toast.LENGTH_SHORT).show();
                            finish(); // Fermer l'activité de mise à jour du contact
                        } else {
                            Log.d(TAG, "Échec de la mise à jour du contact", task.getException());
                            Toast.makeText(UpdateContactActivity.this, "Échec de la mise à jour du contact", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}