package com.example.diogo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editLoginAcesso;
    private EditText editSenhaAcesso;
    private Button btnEntrar;
    private Button btnRegistrar;

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

        // Carregar os últimos login e senha salvos
        String ultimoLogin = sharedPreferences.getString("ultimoLogin", "");
        String ultimaSenha = sharedPreferences.getString("ultimaSenha", "");

        editLoginAcesso.setText(ultimoLogin);
        editSenhaAcesso.setText(ultimaSenha);

        btnEntrar = findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtém o texto digitado nos campos de login e senha
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
                    finish(); // Fecha a LoginActivity para que não possa ser retornada
                } else if ("Adm".equals(login) && "Adm123".equals(senha)) {
                    // Salvar estado de autenticação e credenciais
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isAuthenticated", true);
                    editor.putString("ultimoLogin", login);
                    editor.putString("ultimaSenha", senha);
                    editor.apply();

                    Intent it = new Intent(LoginActivity.this, ReservasActivity.class);
                    startActivity(it);
                    finish(); // Fecha a LoginActivity para que não possa ser retornada
                } else {
                    Toast.makeText(LoginActivity.this, "Login ou senha inválidos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(LoginActivity.this, ReservasActivity.class);
                startActivity(it);
            }
        });

    }
}
