package com.example.diogo;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diogo.database.dao.ClienteDAO; // Import ClienteDAO
import com.example.diogo.database.dao.VendasDAO;
import com.example.diogo.database.model.ClientesModel; // Import ClientesModel
import com.example.diogo.database.model.VendasModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;

import java.util.ArrayList;
import java.util.List;

public class RelatorioActivity extends AppCompatActivity {

    private BarChart barChart;
    private VendasDAO vendasDAO;
    private ClienteDAO clienteDAO; // Reference to ClienteDAO
    private Spinner spinnerClientes;
    private List<ClientesModel> clientesList; // List to hold clients

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }
        setContentView(R.layout.activity_relatorio_cliente);

        // Initialize views and DAOs
        barChart = findViewById(R.id.barChart);
        vendasDAO = new VendasDAO(this);
        clienteDAO = new ClienteDAO(this);
        spinnerClientes = findViewById(R.id.spinnerClientes);

        // Configure client spinner
        clientesList = clienteDAO.getAll(); // Fetch all clients
        List<String> clientesNames = new ArrayList<>();
        for (ClientesModel cliente : clientesList) {
            clientesNames.add(cliente.getNome()); // Add client names for the spinner
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clientesNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClientes.setAdapter(adapter);

        // Spinner item selection listener
        spinnerClientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ClientesModel selectedClient = clientesList.get(position); // Get selected client model
                int clienteId = (int) selectedClient.getId(); // Get client ID
                configurarGrafico(clienteId); // Configure the chart with the client ID
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when no client is selected
            }
        });
    }

    private void configurarGrafico(int clienteId) {
        Log.d("RelatorioActivity", "ID do Cliente: " + clienteId);
        List<VendasModel> vendasList = vendasDAO.gerarRelatorioPorCliente(clienteId);
        Log.d("RelatorioActivity", "Number of vendas: " + vendasList.size());

        if (vendasList.isEmpty()) {
            // Handle case with no sales
            return;
        }

        // Initialize totals
        int totalQuantidade = 0;
        double totalGasto = 0.0;

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

        // Display total quantity and spent amount
        TextView totalQuantidadeTextView = findViewById(R.id.textViewTotalQuantidade);
        TextView totalGastoTextView = findViewById(R.id.textViewTotalGasto);
        totalQuantidadeTextView.setText("Total Quantidade: " + totalQuantidade);
        totalGastoTextView.setText("Total Gasto: R$ " + String.format("%.2f", totalGasto));

        // Create BarDataSet for quantity and expense
        BarDataSet quantidadeDataSet = new BarDataSet(quantidadeEntries, "Quantidade");
        quantidadeDataSet.setColor(ColorTemplate.MATERIAL_COLORS[0]);

        BarDataSet gastoDataSet = new BarDataSet(gastoEntries, "Total Gasto (R$)");
        gastoDataSet.setColor(ColorTemplate.MATERIAL_COLORS[1]);

        // Create BarData and set it to the chart
        BarData data = new BarData(quantidadeDataSet, gastoDataSet);
        data.setBarWidth(0.2f);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);

        // Configure X-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(vinhosLabels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(0xFFFFFFFF);

        // Configure Y-axis
        YAxis leftYAxis = barChart.getAxisLeft();
        leftYAxis.setTextColor(0xFFFFFFFF);

        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setTextColor(0xFFFFFFFF);

        // Configure legend
        Legend legend = barChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setTextSize(15f);
        legend.setTextColor(0xFFFFFFFF);
        legend.setYOffset(10f);

        // Additional configurations
        barChart.setExtraBottomOffset(30f);
        quantidadeDataSet.setValueTextColor(0xFFFFFFFF);
        gastoDataSet.setValueTextColor(0xFFFFFFFF);

        // Redraw the chart
        barChart.invalidate();
    }
}
