package com.example.diogo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ReservasActivity extends AppCompatActivity {

    private Button btnConfiguracoes;
    private Button btnLiberarMesa;
    private Button btnReservarTodas; // Novo botão para liberar todas as mesas
    private EditText etNumeroMesa;

    private LinearLayout mesa1, mesa2, mesa3, mesa4, mesa5, mesa6, mesa7, mesa8, mesa9;
    private Button btnMesaUm, btnMesaDois, btnMesaTres, btnMesaQuatro, btnMesaCinco, btnMesaSeis, btnMesaSete, btnMesaOito, btnMesaNove;

    private int corPadrao;
    private int corReservado;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        // Inicializa as cores e a UI
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        corPadrao = preferences.getInt(Shared.KEY_COR_DISPONIVEL, getColor(R.color.blue)); // Cor reservada padrão (vermelho)
        corReservado = preferences.getInt(Shared.KEY_COR_RESERVA, getColor(R.color.red)); // Cor reservada padrão (vermelho)

        inicializarUI();
        iniciarReservados();

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                logout();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                int corReservadoNova = data.getIntExtra(Shared.KEY_COR_RESERVA, getColor(R.color.red));
                salvarCorReservada();
                AtualizarCorReservados(corReservadoNova);
                corReservado = corReservadoNova;
            }
            if(resultCode == RESULT_FIRST_USER){
                int corPadraoNova = data.getIntExtra(Shared.KEY_COR_DISPONIVEL, getColor(R.color.green));
                salvarCorDisponivel();
                AtualizarCorDisponivel(corPadraoNova);
                corPadrao = corPadraoNova;
            }

        }
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

        Intent intent = new Intent(ReservasActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void inicializarUI() {
        mesa1 = findViewById(R.id.mesa1);
        mesa2 = findViewById(R.id.mesa2);
        mesa3 = findViewById(R.id.mesa3);
        mesa4 = findViewById(R.id.mesa4);
        mesa5 = findViewById(R.id.mesa5);
        mesa6 = findViewById(R.id.mesa6);
        mesa7 = findViewById(R.id.mesa7);
        mesa8 = findViewById(R.id.mesa8);
        mesa9 = findViewById(R.id.mesa9);

        btnMesaUm = findViewById(R.id.btnMesaUm);
        btnMesaDois = findViewById(R.id.btnMesaDois);
        btnMesaTres = findViewById(R.id.btnMesaTres);
        btnMesaQuatro = findViewById(R.id.btnMesaQuatro);
        btnMesaCinco = findViewById(R.id.btnMesaCinco);
        btnMesaSeis = findViewById(R.id.btnMesaSeis);
        btnMesaSete = findViewById(R.id.btnMesaSete);
        btnMesaOito = findViewById(R.id.btnMesaOito);
        btnMesaNove = findViewById(R.id.btnMesaNove);

        etNumeroMesa = findViewById(R.id.etNumeroMesa);
        btnLiberarMesa = findViewById(R.id.btnLiberarMesa);
        btnConfiguracoes = findViewById(R.id.btnConfiguracoes);
        btnReservarTodas = findViewById(R.id.btnReservarTodas);


        configurarListenersReservar();
        btnLiberarMesa.setOnClickListener(v -> liberarMesa());
        btnConfiguracoes.setOnClickListener(v -> {
            Intent it = new Intent(ReservasActivity.this, CfgActivity.class);
            startActivityForResult(it, 1);
        });
        btnReservarTodas.setOnClickListener(v -> reservarTodasAsMesas()); // Configura o listener para o novo botão
    }

    private void configurarListenersReservar() {
        configurarListenerReserva(mesa1, btnMesaUm);
        configurarListenerReserva(mesa2, btnMesaDois);
        configurarListenerReserva(mesa3, btnMesaTres);
        configurarListenerReserva(mesa4, btnMesaQuatro);
        configurarListenerReserva(mesa5, btnMesaCinco);
        configurarListenerReserva(mesa6, btnMesaSeis);
        configurarListenerReserva(mesa7, btnMesaSete);
        configurarListenerReserva(mesa8, btnMesaOito);
        configurarListenerReserva(mesa9, btnMesaNove);
    }

    private void configurarListenerReserva(LinearLayout mesa, Button button) {
        button.setOnClickListener(view -> {
            boolean mesaReservada = ((ColorDrawable) mesa.getBackground()).getColor() != corPadrao;
            mesa.setBackgroundColor(mesaReservada ? corPadrao : corReservado);
            button.setEnabled(mesaReservada);
            atualizarSharedPreferencesMesa(mesa, button);
        });
    }

    private void iniciarReservados() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        iniciarMesa(mesa1, btnMesaUm, preferences);
        iniciarMesa(mesa2, btnMesaDois, preferences);
        iniciarMesa(mesa3, btnMesaTres, preferences);
        iniciarMesa(mesa4, btnMesaQuatro, preferences);
        iniciarMesa(mesa5, btnMesaCinco, preferences);
        iniciarMesa(mesa6, btnMesaSeis, preferences);
        iniciarMesa(mesa7, btnMesaSete, preferences);
        iniciarMesa(mesa8, btnMesaOito, preferences);
        iniciarMesa(mesa9, btnMesaNove, preferences);

        AtualizarCorReservados(corReservado);
    }

    private void iniciarMesa(LinearLayout mesa, Button button, SharedPreferences preferences) {
        int corMesa = preferences.getInt(String.valueOf(mesa.getId()), corPadrao);
        mesa.setBackgroundColor(corMesa);
        restaurarEstadoBotao(button, preferences.getBoolean(String.valueOf(mesa.getId()) + "_button_enabled", true));
    }

    private void AtualizarCorReservados(int cor) {
        atualizarCorMesaReservados(mesa1, cor);
        atualizarCorMesaReservados(mesa2, cor);
        atualizarCorMesaReservados(mesa3, cor);
        atualizarCorMesaReservados(mesa4, cor);
        atualizarCorMesaReservados(mesa5, cor);
        atualizarCorMesaReservados(mesa6, cor);
        atualizarCorMesaReservados(mesa7, cor);
        atualizarCorMesaReservados(mesa8, cor);
        atualizarCorMesaReservados(mesa9, cor);
    }

    private void atualizarCorMesaReservados(LinearLayout mesa, int cor) {
        if (((ColorDrawable) mesa.getBackground()).getColor() != corPadrao) {
            mesa.setBackgroundColor(cor);
        }
    }

    private void AtualizarCorDisponivel(int cor) {
        AtualizarCorMesaDisponivel(mesa1, cor);
        AtualizarCorMesaDisponivel(mesa2, cor);
        AtualizarCorMesaDisponivel(mesa3, cor);
        AtualizarCorMesaDisponivel(mesa4, cor);
        AtualizarCorMesaDisponivel(mesa5, cor);
        AtualizarCorMesaDisponivel(mesa6, cor);
        AtualizarCorMesaDisponivel(mesa7, cor);
        AtualizarCorMesaDisponivel(mesa8, cor);
        AtualizarCorMesaDisponivel(mesa9, cor);
    }

    private void AtualizarCorMesaDisponivel(LinearLayout mesa, int cor) {
        if (((ColorDrawable) mesa.getBackground()).getColor() == corPadrao) {
            mesa.setBackgroundColor(cor);
        }
    }

    private void atualizarSharedPreferencesMesa(LinearLayout mesa, Button button) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        int corAtual = ((ColorDrawable) mesa.getBackground()).getColor();
        editor.putInt(String.valueOf(mesa.getId()), corAtual);
        editor.putBoolean(String.valueOf(mesa.getId()) + "_button_enabled", button.isEnabled());
        editor.apply();
    }

    private void salvarCorReservada() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Shared.KEY_COR_RESERVA, corReservado);
        editor.apply();
    }
    private void salvarCorDisponivel() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Shared.KEY_COR_DISPONIVEL, corPadrao);
        editor.apply();
    }

    private void liberarMesa() {
        String numeroMesaStr = etNumeroMesa.getText().toString().trim();
        if (numeroMesaStr.isEmpty()) {
            Toast.makeText(this, "Informe o número da mesa", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int numeroMesa = Integer.parseInt(numeroMesaStr);
            LinearLayout mesa;
            Button btnMesa;

            switch (numeroMesa) {
                case 1: mesa = mesa1; btnMesa = btnMesaUm; break;
                case 2: mesa = mesa2; btnMesa = btnMesaDois; break;
                case 3: mesa = mesa3; btnMesa = btnMesaTres; break;
                case 4: mesa = mesa4; btnMesa = btnMesaQuatro; break;
                case 5: mesa = mesa5; btnMesa = btnMesaCinco; break;
                case 6: mesa = mesa6; btnMesa = btnMesaSeis; break;
                case 7: mesa = mesa7; btnMesa = btnMesaSete; break;
                case 8: mesa = mesa8; btnMesa = btnMesaOito; break;
                case 9: mesa = mesa9; btnMesa = btnMesaNove; break;
                default:
                    Toast.makeText(this, "Número da mesa inválido", Toast.LENGTH_SHORT).show();
                    return;
            }

            mesa.setBackgroundColor(corPadrao);
            btnMesa.setEnabled(true);
            atualizarSharedPreferencesMesa(mesa, btnMesa);

            etNumeroMesa.setText("");

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Número da mesa inválido", Toast.LENGTH_SHORT).show();
        }
    }

    private void reservarTodasAsMesas() {
        if (isTodasMesasReservadas()) {
            Toast.makeText(this, "Todas as mesas já estão reservadas", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Todas mesas agora estão reservadas", Toast.LENGTH_SHORT).show();

        mesa1.setBackgroundColor(corReservado);
        mesa2.setBackgroundColor(corReservado);
        mesa3.setBackgroundColor(corReservado);
        mesa4.setBackgroundColor(corReservado);
        mesa5.setBackgroundColor(corReservado);
        mesa6.setBackgroundColor(corReservado);
        mesa7.setBackgroundColor(corReservado);
        mesa8.setBackgroundColor(corReservado);
        mesa9.setBackgroundColor(corReservado);

        btnMesaUm.setEnabled(false);
        btnMesaDois.setEnabled(false);
        btnMesaTres.setEnabled(false);
        btnMesaQuatro.setEnabled(false);
        btnMesaCinco.setEnabled(false);
        btnMesaSeis.setEnabled(false);
        btnMesaSete.setEnabled(false);
        btnMesaOito.setEnabled(false);
        btnMesaNove.setEnabled(false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    // Verifica se todas as mesas estão reservadas
    private boolean isTodasMesasReservadas() {
        return isMesaReservada(mesa1) && isMesaReservada(mesa2) &&
                isMesaReservada(mesa3) && isMesaReservada(mesa4) &&
                isMesaReservada(mesa5) && isMesaReservada(mesa6) &&
                isMesaReservada(mesa7) && isMesaReservada(mesa8) &&
                isMesaReservada(mesa9);
    }

    // Verifica se uma mesa está na cor de reservada
    private boolean isMesaReservada(LinearLayout mesa) {
        return ((ColorDrawable) mesa.getBackground()).getColor() == corReservado;
    }

    private void restaurarEstadoBotao(Button button, boolean enabled) {
        button.setEnabled(enabled);
    }
}
