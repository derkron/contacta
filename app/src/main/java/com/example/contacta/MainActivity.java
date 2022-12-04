package com.example.contacta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText jetcodigo,jetusuario,jetfacebook,jetlinkedin,jettwitter;
    CheckBox jcbred;
    Switch jswfacebook,jswtwitter,jswlinkedin;
    String codigo,usuario,usuarioface,usuariotwi,usuariolink,redes;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Ocultar barra de titulo y asociar objetos Java con Xml
        getSupportActionBar().hide();
        jetcodigo=findViewById(R.id.etCodigo);
        jetusuario=findViewById(R.id.etUsuario);
        jetfacebook=findViewById(R.id.etFacebook);
        jetlinkedin=findViewById(R.id.etLinkedin);
        jettwitter=findViewById(R.id.etTwitter);
        jcbred=findViewById(R.id.cbRed);
        jswfacebook=findViewById(R.id.swFacebook);
        jswtwitter=findViewById(R.id.swTwitter);
        jswlinkedin=findViewById(R.id.swLinkedin);
        jswfacebook.setEnabled(false);
        jswtwitter.setEnabled(false);
        jswlinkedin.setEnabled(false);
        jetfacebook.setEnabled(false);
        jettwitter.setEnabled(false);
        jetlinkedin.setEnabled(false);


    }
    public void Adicionar(View view){
        codigo=jetcodigo.getText().toString();
        usuario=jetusuario.getText().toString();
        usuarioface=jetfacebook.getText().toString();
        usuariotwi=jettwitter.getText().toString();
        usuariolink=jetlinkedin.getText().toString();

        if (codigo.isEmpty() || usuario.isEmpty()){
            Toast.makeText(this, "Todos los elementos son requeridos", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
        else{

            // Create a new user with a first and last name
            Map<String, Object> red = new HashMap<>();
            red.put("codigo", codigo);
            red.put("usuario", usuario);
            red.put("redes", redes);
            red.put("usuario facebook", usuarioface);
            red.put("usuario twitter", usuariotwi);
            red.put("usuario linkedin", usuariolink);
            red.put("disponible", "si");



            // Add a new document with a generated ID
            db.collection("contacta")
                    .add(red)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(MainActivity.this, "Elementos agregados con exito", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                           // Log.w(TAG, "Error adding document", e);
                            Toast.makeText(MainActivity.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }

    public void Disponible (View view){
        if (jcbred.isChecked()){
            jswfacebook.setEnabled(true);
            jswtwitter.setEnabled(true);
            jswlinkedin.setEnabled(true);
            if (jswfacebook.isChecked()){
                jetfacebook.setEnabled(true);
                redes="FACEBOOK, ";
            }
            if (jswtwitter.isChecked()){
                jettwitter.setEnabled(true);
                redes+=" TWITTER,  ";
            }
            if (jswlinkedin.isChecked()){
                jetlinkedin.setEnabled(true);
                redes+=" LINKEDIN";
            }


        }
    }

}