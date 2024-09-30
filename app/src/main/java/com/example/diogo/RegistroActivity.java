package com.example.diogo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    private TextView textPrincipal;
    private EditText editEmail;
    private EditText editConfirmaEmail;
    private EditText editSenha;
    private EditText editConfirmaSenha;
    private Button btnRegistrar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        editEmail = findViewById(R.id.editEmail);
        editConfirmaEmail = findViewById(R.id.editConfirmaEmail);
        editSenha = findViewById(R.id.editSenha);
        editConfirmaSenha = findViewById(R.id.editConfirmaSenha);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editEmail.getText().toString().isEmpty()) {
                    mensagemErroApresentar("Campo e-mail obrigatório!");
                } else if (editSenha.getText().toString().isEmpty()) {
                    mensagemErroApresentar("Campo senha obrigatório!");
                } else if (editConfirmaEmail.getText().toString().isEmpty()) {
                    mensagemErroApresentar("Campo confirma e-mail obrigatório!");
                } else if (editConfirmaSenha.getText().toString().isEmpty()) {
                    mensagemErroApresentar("Campo confirma senha obrigatório!");
                } else if (editEmail.getText().toString().equals(editConfirmaEmail.getText().toString())) {
                    mensagemErroApresentar("Campo de e-mail difere!");
                } else if (editSenha.getText().toString().equals(editConfirmaSenha.getText().toString())) {
                    mensagemErroApresentar("Campo de senha obrigatório!");
                } else {

                }
            }
        });
    }

    private void mensagemErroApresentar(String mensagem) {
        Toast.makeText(RegistroActivity.this, "mensagem", Toast.LENGTH_SHORT).show();
    }
}