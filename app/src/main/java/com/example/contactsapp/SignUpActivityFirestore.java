package com.example.contactsapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivityFirestore extends AppCompatActivity{

    //Déclarer les variables
    EditText signupName, signupEmail, signupPhone, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Lier le Code JAVA avec le layout de l'activité à afficher
        setContentView(R.layout.activity_sign_up);

        //Instancier les variables
        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupPhone = findViewById(R.id.signup_phone);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        db = FirebaseFirestore.getInstance();

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String phone = signupPhone.getText().toString();
                String password = signupPassword.getText().toString();



                //Créer un abjet de type (clé-valeur)
                Map<String,Object> user = new HashMap<>();

                //Ajouter des éléments à la carte
                user.put("Name",name);
                user.put("Email",email);
                user.put("Phone",phone);
                user.put("Password",password);

                //On vérifie si les champs ne sont pas vides
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(SignUpActivityFirestore.this, "Enter your Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(SignUpActivityFirestore.this, "Enter your Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignUpActivityFirestore.this, "Enter your Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignUpActivityFirestore.this, "Enter your Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Ajouter un document à la collection
                db.collection("Users")
                        .add(user) //the user Map is being added as a new document to the "user" collection
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(SignUpActivityFirestore.this,"Successfully Registered ",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(SignUpActivityFirestore.this,"Failed to Register",Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });


    }
}
