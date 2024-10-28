package com.example.diogo.relatorio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.diogo.R;
import com.example.diogo.database.dao.ClienteDAO;
import com.example.diogo.database.dao.VendasDAO;
import com.example.diogo.database.model.ClientesModel;
import com.example.diogo.database.model.VendasModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.Legend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RelatorioActivity extends AppCompatActivity {

    private BarChart barChart;
    private VendasDAO vendasDAO;
    private ClienteDAO clienteDAO;
    private Spinner spinnerClientes;
    private List<ClientesModel> clientesList;
    private int totalQuantidade; // Moved to class level
    private double totalGasto;    // Moved to class level

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_cliente);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

        // Initialize views and DAOs
        barChart = findViewById(R.id.barChart);
        vendasDAO = new VendasDAO(this);
        clienteDAO = new ClienteDAO(this);
        spinnerClientes = findViewById(R.id.spinnerClientes);

        // Set up client spinner
        clientesList = clienteDAO.getAll();
        List<String> clientesNames = new ArrayList<>();
        for (ClientesModel cliente : clientesList) {
            clientesNames.add(cliente.getNome());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clientesNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClientes.setAdapter(adapter);

        // Listener for spinner selection
        spinnerClientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ClientesModel selectedClient = clientesList.get(position);
                int clienteId = (int) selectedClient.getId();
                configurarGrafico(clienteId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Button to export chart image
        Button exportButton = findViewById(R.id.buttonGerarRelatorio);
        exportButton.setOnClickListener(v -> {
            ClientesModel selectedClient = clientesList.get(spinnerClientes.getSelectedItemPosition());
            String clienteNome = selectedClient.getNome();
            exportChart(clienteNome, totalQuantidade, totalGasto);
        });
    }

    private void configurarGrafico(int clienteId) {
        List<VendasModel> vendasList = vendasDAO.gerarRelatorioPorCliente(clienteId);

        if (vendasList.isEmpty()) {
            return; // Handle case with no sales
        }

        // Initialize totals
        totalQuantidade = 0;
        totalGasto = 0.0;

        ArrayList<BarEntry> quantidadeEntries = new ArrayList<>();
        ArrayList<BarEntry> gastoEntries = new ArrayList<>();
        ArrayList<String> vinhosLabels = new ArrayList<>();

        for (int i = 0; i < vendasList.size(); i++) {
            VendasModel venda = vendasList.get(i);
            quantidadeEntries.add(new BarEntry(i, venda.getQuantidade()));
            gastoEntries.add(new BarEntry(i + 0.3f, (float) venda.getTotalVenda()));
            vinhosLabels.add(venda.getVinho());

            totalQuantidade += venda.getQuantidade();
            totalGasto += venda.getTotalVenda();
        }

        // Display total quantity and spending
        TextView totalQuantidadeTextView = findViewById(R.id.textViewTotalQuantidade);
        TextView totalGastoTextView = findViewById(R.id.textViewTotalGasto);
        totalQuantidadeTextView.setText("Quantidade Total: " + totalQuantidade);
        totalGastoTextView.setText("Gasto Total: R$ " + String.format("%.2f", totalGasto));

        // Create datasets
        BarDataSet quantidadeDataSet = new BarDataSet(quantidadeEntries, "Quantidade");
        quantidadeDataSet.setColor(ColorTemplate.MATERIAL_COLORS[0]);

        BarDataSet gastoDataSet = new BarDataSet(gastoEntries, "Gasto Total (R$)");
        gastoDataSet.setColor(ColorTemplate.MATERIAL_COLORS[1]);

        // Set data on chart
        BarData data = new BarData(quantidadeDataSet, gastoDataSet);
        data.setBarWidth(0.2f);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);

        // Configure X axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(vinhosLabels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);

        // Configure Y axis
        YAxis leftYAxis = barChart.getAxisLeft();
        leftYAxis.setTextColor(Color.WHITE);
        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setTextColor(Color.WHITE);

        // Configure legend
        Legend legend = barChart.getLegend();
        legend.setTextColor(Color.WHITE);

        // Refresh chart
        barChart.invalidate();
    }

    private void exportChart(String clienteNome, int totalQuantidade, double totalGasto) {
        // Define quanto da parte inferior deve ser cortada
        int heightCut = 220; // Altura a ser cortada na parte inferior

        // Captura a tela da view completa
        int fullHeight = barChart.getRootView().getHeight();
        Bitmap bitmap = Bitmap.createBitmap(barChart.getRootView().getWidth(), fullHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Desenha a view inteira no canvas
        barChart.getRootView().draw(canvas);

        // Recorta a parte inferior do bitmap
        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), fullHeight - heightCut);

        // Define o local para salvar a imagem
        String filePath = getExternalFilesDir(null) + "/screenshot_image.png";
        File file = new File(filePath);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            croppedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            compartilharImagem(file, clienteNome, totalQuantidade, totalGasto);
        } catch (IOException e) {
            Log.e("RelatorioActivity", "Erro ao exportar captura de tela", e);
            Toast.makeText(this, "Erro ao exportar captura de tela", Toast.LENGTH_SHORT).show();
        }
    }



    private void compartilharImagem(File file, String clienteNome, int totalQuantidade, double totalGasto) {
        Uri imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");

        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Compartilhar Relatório"));
    }
}
