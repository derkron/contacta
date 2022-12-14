package com.example.contacta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText jetcodigo,jetusuario,jetfacebook,jetlinkedin,jettwitter;
    CheckBox jcbred;
    Switch jswfacebook,jswtwitter,jswlinkedin;
    boolean respuesta;
    String codigo,usuario,usuarioface,usuariotwi,usuariolink,redes,ident_doc;
    Button jbtnCancelar;

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
        jbtnCancelar=findViewById(R.id.btnCancelar);


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
            red.put("disponible","si");



            // Add a new document with a generated ID
            db.collection("contacta")
                    .add(red)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(MainActivity.this, "Elementos agregados con exito", Toast.LENGTH_SHORT).show();
                            Limpiar_campos();

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
   public void Consultar (View view) {
       respuesta = false;
       codigo = jetcodigo.getText().toString();
       if (codigo.isEmpty()) {
           Toast.makeText(this, "codigo es requerido", Toast.LENGTH_SHORT).show();
           jetcodigo.requestFocus();
       } else {
           db.collection("contacta")
                   .whereEqualTo("codigo", codigo)
                   .get()
                   .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           if (task.isSuccessful()) {

                               for (QueryDocumentSnapshot document : task.getResult()) {
                                   respuesta = true;
                                   ident_doc = document.getId();
                                   jetusuario.setText(document.getString("usuario"));



                                   if (document.getString("disponible").equals("si")) {
                                       jcbred.setChecked(true);
                                       jswfacebook.setChecked(true);
                                       jswtwitter.setChecked(true);
                                       jswlinkedin.setChecked(true);
                                       jetfacebook.setText(document.getString("usuario facebook"));
                                       jettwitter.setText(document.getString("usuario twitter"));
                                       jetlinkedin.setText(document.getString("usuario linkedin"));
                                   } else
                                       jcbred.setChecked(false);
                                   //Log.d(TAG, document.getId() + " => " + document.getData());
                               }
                           } else {
                               //Log.w(TAG, "Error getting documents.", task.getException());
                           }
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
            else{
                jetfacebook.setEnabled(false);
                jetfacebook.setText("");
            }
            if (jswtwitter.isChecked()){
                jettwitter.setEnabled(true);
                redes+=" TWITTER,  ";
            }
            else{
                jettwitter.setEnabled(false);
                jettwitter.setText("");
            }
            if (jswlinkedin.isChecked()){
                jetlinkedin.setEnabled(true);
                redes+=" LINKEDIN";
            }
            else{
                jetlinkedin.setEnabled(false);
                jetlinkedin.setText("");
            }
        }
        else{
            jswfacebook.setEnabled(false);
            jswtwitter.setEnabled(false);
            jswlinkedin.setEnabled(false);
            jswlinkedin.setChecked(false);
            jswtwitter.setChecked(false);
            jswfacebook.setChecked(false);
            jetlinkedin.setEnabled(false);
            jettwitter.setEnabled(false);
            jetfacebook.setEnabled(false);
        }

    }
    public void Anular(View View){
        codigo=jetcodigo.getText().toString();
        usuario=jetusuario.getText().toString();
        if  (codigo.isEmpty() || usuario.isEmpty()){
            Toast.makeText(this, "Los campos son necesarios", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
        else{
            if(respuesta==true){

                // Create a new user with a first and last name
                Map<String, Object> red = new HashMap<>();
                red.put("codigo", codigo);
                red.put("usuario", usuario);
                red.put("redes", redes);
                red.put("usuario facebook", usuarioface);
                red.put("usuario twitter", usuariotwi);
                red.put("usuario linkedin", usuariolink);
                red.put("disponible","no");

                // Modify a new document with a generated ID
                db.collection("contacta").document(ident_doc)
                        .set(red)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Documento anulado ...", Toast.LENGTH_SHORT).show();
                                Limpiar_campos();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error anulando documento...", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else{
                Toast.makeText(this, "Debe primero consultar", Toast.LENGTH_SHORT).show();
                jetcodigo.requestFocus();
            }
        }
    }
    public void  Cancelar(View view){
        Limpiar_campos();
    }
    private void Limpiar_campos() {
        jetcodigo.setText("");
        jetusuario.setText("");
        jcbred.setChecked(false);
        jetlinkedin.setText("");
        jettwitter.setText("");
        jetfacebook.setText("");
        jswfacebook.setEnabled(false);
        jswtwitter.setEnabled(false);
        jswlinkedin.setEnabled(false);
        jetfacebook.setEnabled(false);
        jettwitter.setEnabled(false);
        jetlinkedin.setEnabled(false);
        jetcodigo.requestFocus();

        respuesta=false;
    }
}

