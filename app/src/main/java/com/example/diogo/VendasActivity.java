package com.example.diogo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diogo.database.dao.VendasDAO; // Certifique-se de que você tenha um DAO para vendas
import com.example.diogo.database.model.VendasModel;

import java.util.List;

public class VendasActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_REGISTRO = 1;
    private RecyclerView recyclerView;
    private VendasAdapter adapter;
    private VendasDAO vendasDAO;
    private Button btnRegistrarVenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.vinho));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendaslista); // Crie um layout para a lista de vendas

        btnRegistrarVenda = findViewById(R.id.btnRegistrarVenda);
        recyclerView = findViewById(R.id.recyclerViewVendas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        vendasDAO = new VendasDAO(this);
        loadVendas();

        btnRegistrarVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(VendasActivity.this, RegistroVendaActivity.class);
                startActivityForResult(it, REQUEST_CODE_REGISTRO);
            }
        });
    }

    private void loadVendas() {
        List<VendasModel> vendasList = vendasDAO.getAll(); // Supondo que você tenha um método getAll no seu DAO
        adapter = new VendasAdapter(vendasList, this);
        recyclerView.setAdapter(adapter);
    }

    public void refreshVendas() {
        List<VendasModel> novasVendas = vendasDAO.getAll();
        adapter.updateVendas(novasVendas);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REGISTRO && resultCode == RESULT_OK) {
            refreshVendas(); // Chama refreshVendas quando retorna de RegistroVendaActivity
        }
    }
}
