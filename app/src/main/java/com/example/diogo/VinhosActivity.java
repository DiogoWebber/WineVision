package com.example.diogo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diogo.database.dao.VinhoDAO;
import com.example.diogo.database.model.VinhosModel;

import java.util.List;
public class VinhosActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_REGISTRO = 1;
    private RecyclerView recyclerView;
    private VinhosAdapter adapter;
    private VinhoDAO vinhoDAO;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.vinho));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vinholista); // Certifique-se de usar o layout correto

        btnRegistrar = findViewById(R.id.btnRegistrar);
        recyclerView = findViewById(R.id.recyclerViewVinhos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        vinhoDAO = new VinhoDAO(this);
        loadVinhos();

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(VinhosActivity.this, RegistroVinhosActivity.class);
                startActivityForResult(it, REQUEST_CODE_REGISTRO);
            }
        });
    }

    private void loadVinhos() {
        List<VinhosModel> vinhosList = vinhoDAO.getAll();
        adapter = new VinhosAdapter(vinhosList, this);
        recyclerView.setAdapter(adapter);
    }

    public void refreshVinhos() {
        VinhoDAO vinhoDAO = new VinhoDAO(this);
        List<VinhosModel> novosVinhos = vinhoDAO.getAll();
        adapter.updateVinhos(novosVinhos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REGISTRO && resultCode == RESULT_OK) {
            refreshVinhos();
        }
    }
}