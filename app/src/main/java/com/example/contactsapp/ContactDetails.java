package com.example.contactsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import androidx.appcompat.widget.AppCompatImageButton;
import android.Manifest;


import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ContactDetails extends AppCompatActivity {

    private static final String TAG = "ContactDetails";

    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView professionTextView;
    private TextView emailTextView;
    private TextView phoneNumberTextView;
    private TextView titleNameTextView;
    private Button deleteContactButton;
    private Button updateContactButton;

    private ImageButton callButton;
    private ImageButton sendSmsButton;
    private ImageButton sendEmailButton;
    private ImageButton whatsappButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Contact contact;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        // Initialiser mAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialiser la référence à Firestore
        db = FirebaseFirestore.getInstance();

        // Référencer les vues du layout
        firstNameTextView = findViewById(R.id.firstNameContact);
        lastNameTextView = findViewById(R.id.lastNameContact);
        professionTextView = findViewById(R.id.professionContact);
        emailTextView = findViewById(R.id.emailContact);
        phoneNumberTextView = findViewById(R.id.phoneNumberContact);
        titleNameTextView = findViewById(R.id.titleName);
        deleteContactButton = findViewById(R.id.deleteContact);
        updateContactButton = findViewById(R.id.updateContact);

        //Initialiser nos bouttons
        callButton = findViewById(R.id.callButton);
        sendSmsButton = findViewById(R.id.sendSmsButton);
        sendEmailButton = findViewById(R.id.sendEmailButton);
        whatsappButton = findViewById(R.id.sendWhatsappButton);



        // Récupérer le contact à afficher
        contact = (Contact) getIntent().getSerializableExtra("contact");

        if (contact != null) {
            // Afficher les détails du contact dans les TextView
            firstNameTextView.setText(contact.getFirstNameContact());
            lastNameTextView.setText(contact.getLastNameContact());
            professionTextView.setText(contact.getProfessionContact());
            emailTextView.setText(contact.getEmailContact());
            phoneNumberTextView.setText(contact.getPhoneNumberContact());
            titleNameTextView.setText(contact.getFirstNameContact() + ' ' + contact.getLastNameContact());
        }

        if (contact != null) {
            // Récupérer les détails du contact depuis Firestore
            getContactDetails();
        } else {
            // Gérer le cas où l'objet contact est null (par exemple, afficher un message d'erreur)
            Toast.makeText(this, "Le contact est introuvable", Toast.LENGTH_SHORT).show();
            finish(); // Terminer l'activité et revenir à l'écran précédent
        }

        // Définir le clic du bouton de suppression du contact
        deleteContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact();
            }
        });

        //récupérer l'intent envoyé depuis l'activité UpdateContactActivity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("updatedContact")) {
            Contact updatedContact = intent.getParcelableExtra("updatedContact");

            // Utilisez les valeurs du contact mis à jour pour mettre à jour les TextView
            updateFields(updatedContact);
        }

        // Définir le clic du bouton de mise à jour du contact
        updateContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactDetails.this, UpdateContactActivity.class);
                intent.putExtra("contact", contact);
                startActivityForResult(intent,1); // Utilisez startActivityForResult() pour démarrer UpdateContactActivity
            }
        });

        //Définir le clic boutton de l'appel téléphonique
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = contact.getPhoneNumberContact();
                if (!phoneNumber.isEmpty()) {
                    // Vérifier si la permission CALL_PHONE est accordée
                    if (ActivityCompat.checkSelfPermission(ContactDetails.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        // Lancer l'intent de l'appel téléphonique
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        startActivity(intent);
                    } else {
                        // Demander la permission CALL_PHONE à l'utilisateur
                        ActivityCompat.requestPermissions(ContactDetails.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    }
                } else {
                    Toast.makeText(ContactDetails.this, "Le numéro de téléphone est indisponible", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Définir le clic boutton d'envoie des SMS
        sendSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = contact.getPhoneNumberContact();
                String message = "Votre message ici"; // Remplacez par votre propre message

                Uri uri = Uri.parse("smsto:" + phoneNumber);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", message);

                startActivity(intent);
            }
        });

        //Définir le clic boutton d'envoie d'email
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        //Définir le clic boutton d'envoie de message sur WhatsApp
        whatsappButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageWhatsApp();
            }
        });
    }

    private void getContactDetails() {
        // Récupérer les détails du contact depuis Firestore en utilisant l'ID
        // Remplacez "contacts" par le nom de votre collection Firestore
        db.collection("Users")
                .document(mAuth.getCurrentUser().getEmail())
                .collection("contacts")
                .document(contact.getPhoneNumberContact()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Récupérer les valeurs des champs du contact
                                String firstName = document.getString("firstName");
                                String lastName = document.getString("lastName");
                                String profession = document.getString("profession");
                                String email = document.getString("email");
                                String phoneNumber = document.getString("phone");

                                // Afficher les détails du contact dans les TextView correspondants
                                firstNameTextView.setText(firstName);
                                lastNameTextView.setText(lastName);
                                professionTextView.setText(profession);
                                emailTextView.setText(email);
                                phoneNumberTextView.setText(phoneNumber);
                                titleNameTextView.setText(firstName + ' ' + lastName);
                            } else {
                                Log.d(TAG, "Le document n'existe pas");
                            }
                        } else {
                            Log.d(TAG, "Erreur lors de la récupération du document : ", task.getException());
                        }
                    }
                });
    }

    private void deleteContact() {
        // Supprimer le contact de Firestore en utilisant son ID
        // Remplacez "contacts" par le nom de votre collection Firestore
        db.collection("Users")
                .document(mAuth.getCurrentUser().getEmail())
                .collection("contacts")
                .document(contact.getPhoneNumberContact())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ContactDetails.this, "Le contact a été supprimé avec succès", Toast.LENGTH_SHORT).show();
                            // Terminer l'activité et revenir à l'écran précédent
                            finish();
                        } else {
                            Toast.makeText(ContactDetails.this, "Erreur lors de la suppression du contact", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Erreur lors de la suppression du contact : ", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called");
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Récupérez le contact mis à jour depuis les données de l'intent
                Contact updatedContact = (Contact) data.getSerializableExtra("updatedContact");

                // Mettez à jour les champs avec les nouvelles valeurs du contact
                updateFields(updatedContact);
            }
        }
    }

    private void updateFields(Contact contact) {
        // Mettre à jour les champs avec les valeurs du contact
        firstNameTextView.setText(contact.getFirstNameContact());
        lastNameTextView.setText(contact.getLastNameContact());
        professionTextView.setText(contact.getProfessionContact());
        emailTextView.setText(contact.getEmailContact());
        phoneNumberTextView.setText(contact.getPhoneNumberContact());
        titleNameTextView.setText(contact.getFirstNameContact() + ' ' + contact.getLastNameContact());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission CALL_PHONE accordée, lancer l'intent de l'appel téléphonique
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + contact.getPhoneNumberContact()));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Permission refusée, impossible de passer un appel", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendEmail() {

        //Initialiser l'email du contact actuel
        String email = contact.getEmailContact();

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(ContactDetails.this, "Aucune application de messagerie trouvée.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMessageWhatsApp() {
        String phoneNumber = contact.getPhoneNumberContact();

        if (!phoneNumber.startsWith("+")) {
            phoneNumber = "+212" + phoneNumber.substring(1);
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(ContactDetails.this, "WhatsApp n'est pas installé.", Toast.LENGTH_SHORT).show();
        }
    }
}
