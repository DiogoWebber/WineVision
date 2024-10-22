package com.example.diogo;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diogo.database.dao.VendasDAO;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_cliente);

        barChart = findViewById(R.id.barChart);
        vendasDAO = new VendasDAO(this);

        // Exemplo: ID do cliente passado para a Activity
        int clienteId = getIntent().getIntExtra("CLIENTE_ID", -1);

        // Chama a função para configurar o gráfico
        configurarGrafico(clienteId);
    }

    private void configurarGrafico(int clienteId) {
        // Obtém os dados de vendas por cliente
        Log.d("RelatorioActivity", "ID do Cliente: " + clienteId); // Log do ID do cliente
        List<VendasModel> vendasList = vendasDAO.gerarRelatorioPorCliente(clienteId);
        Log.d("RelatorioActivity", "Number of vendas: " + vendasList.size()); // Log the number of vendas

        if (vendasList.isEmpty()) {
            // Handle the case when there are no sales
            // Maybe show a Toast or a message indicating no data
            return;
        }

        ArrayList<BarEntry> quantidadeEntries = new ArrayList<>();
        ArrayList<BarEntry> gastoEntries = new ArrayList<>();
        ArrayList<String> vinhosLabels = new ArrayList<>();

        // Itera sobre as vendas e cria as entradas para o gráfico
        for (int i = 0; i < vendasList.size(); i++) {
            VendasModel venda = vendasList.get(i);
            quantidadeEntries.add(new BarEntry(i, venda.getQuantidade()));
            gastoEntries.add(new BarEntry(i + 0.3f, (float) venda.getTotalVenda())); // Ajusta a posição da barra de gasto
            vinhosLabels.add(venda.getVinho());
        }

        // Cria os datasets para as barras de quantidade e gasto
        BarDataSet quantidadeDataSet = new BarDataSet(quantidadeEntries, "Quantidade");
        quantidadeDataSet.setColor(ColorTemplate.MATERIAL_COLORS[0]);

        BarDataSet gastoDataSet = new BarDataSet(gastoEntries, "Total Gasto (R$)");
        gastoDataSet.setColor(ColorTemplate.MATERIAL_COLORS[1]);

        // Adiciona os datasets ao BarData
        BarData data = new BarData(quantidadeDataSet, gastoDataSet);
        data.setBarWidth(0.2f); // Define a largura das barras (ajuste aqui)

        // Configura o gráfico com os dados
        barChart.setData(data);
        barChart.getDescription().setEnabled(false); // Remove a descrição do gráfico
        barChart.setFitBars(true);

        // Configura o eixo X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(vinhosLabels));
        xAxis.setGranularity(1f); // Evita rótulos duplicados
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-45f); // Rotaciona os rótulos se necessário
        xAxis.setTextColor(0xFFFFFFFF); // Define a cor do texto como branca

        // Configura o eixo Y (Quantidades)
        YAxis leftYAxis = barChart.getAxisLeft();
        leftYAxis.setTextColor(0xFFFFFFFF); // Define a cor do texto do eixo Y como branca

        // Configura o eixo Y (Total Gasto)
        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setTextColor(0xFFFFFFFF); // Define a cor do texto do eixo Y como branca

        Legend legend = barChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setTextSize(15f); // Tamanho do texto da legenda
        legend.setTextColor(0xFFFFFFFF); // Define a cor do texto da legenda como branca
        legend.setYOffset(10f); // Ajuste esse valor para descer a legenda

        // Ajusta o espaço extra na parte inferior do gráfico
        barChart.setExtraBottomOffset(30f); // Ajuste conforme necessário

        // Se você deseja adicionar números nas barras, você pode configurá-los assim
        quantidadeDataSet.setValueTextColor(0xFFFFFFFF); // Define a cor do texto de quantidade como branca
        gastoDataSet.setValueTextColor(0xFFFFFFFF); // Define a cor do texto de gasto como branca

        barChart.invalidate(); // Redesenha o gráfico
    }

}