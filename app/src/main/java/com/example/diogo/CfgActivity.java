package com.example.diogo;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CfgActivity extends AppCompatActivity {

    private LinearLayout btnConfigRed;
    private LinearLayout btnConfigBlack;
    private LinearLayout btnConfigBlueDisponivel;
    private LinearLayout btnConfigPurpleDisponivel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cfg);

        // Configuração para cores de reserva
        btnConfigRed = findViewById(R.id.btnConfigRed);
        btnConfigBlack = findViewById(R.id.btnConfigBlack);

        btnConfigRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cor vermelha
                int corReserva = getResources().getColor(R.color.red); // Use getColor() para obter a cor
                Intent it = new Intent();
                it.putExtra(Shared.KEY_COR_RESERVA, corReserva);
                setResult(RESULT_OK, it);
                finish();
            }
        });

        btnConfigBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cor azul
                int corReserva = getResources().getColor(R.color.black); // Use getColor() para obter a cor
                Intent it = new Intent();
                it.putExtra(Shared.KEY_COR_RESERVA, corReserva);
                setResult(RESULT_OK, it);
                finish();
            }
        });

        // Configuração para cores de mesas disponíveis
        btnConfigBlueDisponivel = findViewById(R.id.btnConfigBlueDisponivel);
        btnConfigPurpleDisponivel = findViewById(R.id.btnConfigPurpleDisponivel);

        btnConfigBlueDisponivel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cor vermelha para mesas disponíveis
                int corDisponivel = getResources().getColor(R.color.blue);
                Intent it = new Intent();
                it.putExtra(Shared.KEY_COR_DISPONIVEL, corDisponivel);
                setResult(RESULT_FIRST_USER, it);
                finish();
            }
        });

        btnConfigPurpleDisponivel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cor azul para mesas disponíveis
                int corDisponivel = getResources().getColor(R.color.purple);
                Intent it = new Intent();
                it.putExtra(Shared.KEY_COR_DISPONIVEL, corDisponivel);
                setResult(RESULT_FIRST_USER, it);
                finish();
            }
        });

    }
}
