package com.example.diogo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity { // Extende AppCompatActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.vinho));
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Habilita a funcionalidade EdgeToEdge
        setContentView(R.layout.activity_main); // Define o layout da atividade

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                logout();
            }
        });
        // Inicializa os TextViews e define os listeners de clique
        TextView viewClientes = findViewById(R.id.viewClientes);
        viewClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClientesActivity.class);
                startActivity(intent);
            }
        });

        TextView viewVinhos = findViewById(R.id.viewVinhos);
        viewVinhos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VinhosActivity.class);
                startActivity(intent);
            }
        });

        TextView viewVendas = findViewById(R.id.viewVendas);
        viewVendas.setOnClickListener(v -> {
            // Ação para o clique no "VENDAS"
        });

        TextView viewRelatorioMensal = findViewById(R.id.viewRelatorioMensal);
        viewRelatorioMensal.setOnClickListener(v -> {
            // Ação para o clique no "RELATÓRIO MENSAL"
        });


    }
    private void limparSharedPreferences() {
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.clear(); // Limpa todas as preferências
        editor.apply(); // Ou use commit() se precisar de uma confirmação imediata
    }
    private void logout() {
        limparSharedPreferences();
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isAuthenticated", false); // Define o estado como não autenticado
        editor.apply();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
