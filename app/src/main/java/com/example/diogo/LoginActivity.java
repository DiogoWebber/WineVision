package com.example.diogo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editLoginAcesso;
    private EditText editSenhaAcesso;
    private Button btnEntrar;
    private TextView clickableText; // Alterado para TextView

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verifica se o usuário já está autenticado
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        boolean isAuthenticated = sharedPreferences.getBoolean("isAuthenticated", false);

        if (isAuthenticated) {
            Intent intent = new Intent(LoginActivity.this, ReservasActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Configura o layout da atividade de login
        setContentView(R.layout.activity_login);

        editLoginAcesso = findViewById(R.id.editLoginAcesso);
        editSenhaAcesso = findViewById(R.id.editSenhaAcesso);
        btnEntrar = findViewById(R.id.btnEntrar);
        clickableText = findViewById(R.id.clickableText); // Inicializa o TextView

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica de autenticação
                String login = editLoginAcesso.getText().toString();
                String senha = editSenhaAcesso.getText().toString();

                if ("Administrador".equals(login) && "Administrador".equals(senha)) {
                    // Salvar estado de autenticação e credenciais
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isAuthenticated", true);
                    editor.putString("ultimoLogin", login);
                    editor.putString("ultimaSenha", senha);
                    editor.apply();

                    Intent it = new Intent(LoginActivity.this, ReservasActivity.class);
                    startActivity(it);
                    finish();
                } else if ("Adm".equals(login) && "Adm123".equals(senha)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isAuthenticated", true);
                    editor.putString("ultimoLogin", login);
                    editor.putString("ultimaSenha", senha);
                    editor.apply();

                    Intent it = new Intent(LoginActivity.this, ReservasActivity.class);
                    startActivity(it);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login ou senha inválidos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configura o listener para o TextView clicável
        clickableText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(it);
            }
        });
    }
}
