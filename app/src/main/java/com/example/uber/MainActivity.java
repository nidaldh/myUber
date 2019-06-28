package com.example.uber;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    private Button mLogin, mRefistarion;
    private LinearLayout root;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String tmpemail;
    private String tmpname;
    private String tmpphone;
    private String tmppassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLogin = findViewById(R.id.login);
        mRefistarion = findViewById(R.id.registration);
        root = findViewById(R.id.rootLayout);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginDialog();
            }
        });

        mRefistarion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegistrationDialog();
            }
        });
        
    }

    private void showLoginDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Login");
        dialog.setMessage("please enter your email and Password");
        LayoutInflater inflater = LayoutInflater.from(this);
        View login = inflater.inflate(R.layout.login, null);
        final MaterialEditText email = login.findViewById(R.id.email);
        final MaterialEditText password = login.findViewById(R.id.password);
        if (!TextUtils.isEmpty(tmpemail)) {
            email.setText(tmpemail);
        }
        dialog.setView(login);


        dialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                tmpemail=email.getText().toString();
                tmppassword=password.getText().toString();
                if (TextUtils.isEmpty(tmpemail)) {
                    Snackbar.make(root, "Please enter email", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (tmppassword.length() < 6) {
                    Snackbar.make(root, "Please enter password", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(getBaseContext(),DriverLoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Baaad", "Error adding document"+ e.getMessage());
                        Snackbar.make(root, e.getMessage(), Snackbar.LENGTH_SHORT).show();

                    }
                });

            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();

    }

    private void showRegistrationDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Registration");
        dialog.setMessage("please signup with your email");
        LayoutInflater inflater = LayoutInflater.from(this);
        View registartion = inflater.inflate(R.layout.registartion, null);

        final MaterialEditText email = registartion.findViewById(R.id.email);
        final MaterialEditText password = registartion.findViewById(R.id.password);
        final MaterialEditText name = registartion.findViewById(R.id.name);
        final MaterialEditText phone = registartion.findViewById(R.id.phone);
        if (!TextUtils.isEmpty(tmpemail)) {
            email.setText(tmpemail);
        }
        if (!TextUtils.isEmpty(tmpname)) {
            name.setText(tmpname);
        }

        if (!TextUtils.isEmpty(tmpphone)) {
            phone.setText(tmpphone);
        }
        dialog.setView(registartion);

        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                tmpemail=email.getText().toString();
                tmppassword=password.getText().toString();
                tmpname=name.getText().toString();
                tmpphone=phone.getText().toString();
                if (TextUtils.isEmpty(tmpemail)) {
                    Snackbar.make(root, "Please enter email", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(tmpname)) {
                    Snackbar.make(root, "Please enter name", Snackbar.LENGTH_SHORT).show();
                    return;

                }
                if (tmppassword.length() < 6) {
                    Snackbar.make(root, "Please enter password and must be more than 6 char", Snackbar.LENGTH_SHORT).show();
                    return;

                }
                if (TextUtils.isEmpty(tmpphone)) {
                    Snackbar.make(root, "Please enter phone", Snackbar.LENGTH_SHORT).show();
                    return;

                }
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();
                                user.setEmail(email.getText().toString());
                                user.setName(name.getText().toString());
//                                user.setPassword(password.getText().toString());
                                user.setPhone(phone.getText().toString());

                                db.collection("User")
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(MainActivity.this, "DocumentSnapshot added with ID: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                                                Log.d("Dood", "DocumentSnapshot added with ID: " + documentReference.getId());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(root, "Error adding document"+ e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                                Log.d("Baaad", "Error adding document"+ e.getMessage());
                                            }
                                        });
                                dialogInterface.dismiss();
                                Intent intent = new Intent(getBaseContext(),DriverLoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Baaad", "Error adding document"+ e.getMessage());

                    }
                });

            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }
}
