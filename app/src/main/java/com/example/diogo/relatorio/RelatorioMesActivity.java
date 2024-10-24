package com.example.diogo.relatorio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color; // Importar a classe Color
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.diogo.database.dao.VendasDAO;
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
import java.util.Date;
import java.util.List;

public class RelatorioMesActivity extends AppCompatActivity {

    private BarChart barChart;
    private VendasDAO vendasDAO;
    private Spinner spinnerMes, spinnerAno;
    private TextView totalQuantidadeTextView, totalGastoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_mes);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

        barChart = findViewById(R.id.barChart);
        spinnerMes = findViewById(R.id.spinnerMes);
        spinnerAno = findViewById(R.id.spinnerAno);
        vendasDAO = new VendasDAO(this);
        totalQuantidadeTextView = findViewById(R.id.textViewTotalQuantidade);
        totalGastoTextView = findViewById(R.id.textViewTotalGasto);
        Button buttonExportar = findViewById(R.id.buttonExportar); // Obtém o botão de exportação

        // Definindo a cor do texto como branco
        totalQuantidadeTextView.setTextColor(Color.WHITE);
        totalGastoTextView.setTextColor(Color.WHITE);

        // Configurar o Spinner com os meses
        setupSpinner();
        setupYearSpinner();

        // Adicionar listener para o Spinner
        spinnerMes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int mesSelecionado = position + 1; // meses começam em 1
                int anoSelecionado = Integer.parseInt(spinnerAno.getSelectedItem().toString());
                gerarGraficoPorMes(mesSelecionado, anoSelecionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Não faz nada
            }
        });

        // Add listener for the year spinner
        spinnerAno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int mesSelecionado = spinnerMes.getSelectedItemPosition() + 1; // meses começam em 1
                int anoSelecionado = Integer.parseInt(spinnerAno.getSelectedItem().toString());
                gerarGraficoPorMes(mesSelecionado, anoSelecionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Não faz nada
            }
        });
        // Listener para o botão de exportação
        buttonExportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportarGraficoComoImagem();
            }
        });
    }

    private void setupSpinner() {
        String[] meses = new String[]{
                "Janeiro", "Fevereiro", "Março", "Abril", "Maio",
                "Junho", "Julho", "Agosto", "Setembro", "Outubro",
                "Novembro", "Dezembro"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, meses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMes.setAdapter(adapter);
    }

    private void setupYearSpinner() {
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        int startYear = currentYear - 10; // Altere conforme necessário
        String[] anos = new String[11]; // Supondo 10 anos + ano atual

        for (int i = 0; i <= 10; i++) {
            anos[i] = String.valueOf(startYear + i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, anos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAno.setAdapter(adapter);

        // Definindo o ano atual como selecionado
        spinnerAno.setSelection(currentYear - startYear); // Isso seleciona o índice correspondente ao ano atual
    }


    private void gerarGraficoPorMes(int mes, int ano) {
        List<VendasModel> vendasList = vendasDAO.gerarRelatorioPorMes(mes, ano); // Update your DAO method to accept year

        // Limpar dados antigos
        barChart.clear();

        // Verifica se a lista de vendas está vazia
        if (vendasList.isEmpty()) {
            // Atualizar campos de quantidade total e total gasto para zero
            totalQuantidadeTextView.setText("Quantidade Total: 0");
            totalGastoTextView.setText("Total Gasto: R$ 0.00");
            return;
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        int totalQuantidade = 0;
        double totalGasto = 0.0;

        for (int i = 0; i < vendasList.size(); i++) {
            VendasModel venda = vendasList.get(i);
            entries.add(new BarEntry(i, venda.getQuantidade()));
            labels.add(venda.getVinho());
            totalQuantidade += venda.getQuantidade();
            totalGasto += venda.getTotalVenda();
        }

        // Atualizar os campos de texto com os totais
        totalQuantidadeTextView.setText("Quantidade Total: " + totalQuantidade);
        totalGastoTextView.setText("Total Gasto: R$ " + String.format("%.2f", totalGasto));

        BarDataSet dataSet = new BarDataSet(entries, "Quantidade");
        dataSet.setColor(ColorTemplate.MATERIAL_COLORS[0]);
        dataSet.setValueTextSize(16f);


        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(0xFFFFFFFF); // Cor branca

        YAxis leftYAxis = barChart.getAxisLeft();
        YAxis rightYAxis = barChart.getAxisRight();
        leftYAxis.setTextColor(0xFFFFFFFF); // Cor branca
        rightYAxis.setTextColor(0xFFFFFFFF); // Cor branca

        Legend legend = barChart.getLegend();
        legend.setTextColor(0xFFFFFFFF); // Cor branca
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM); // Alinha a legenda para baixo
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER); // Alinha a legenda ao centro
        legend.setYOffset(20f); // Aumenta a distância da legenda para baixo
        legend.setTextSize(16f); // Aumentar o tamanho da fonte da legenda


        // Adicionando offsets extras para mover a legenda para baixo
        barChart.setExtraOffsets(0f, 0f, 0f, 50f); // 50f adiciona espaço extra na parte inferior do gráfico

        barChart.invalidate(); // Atualiza o gráfico
    }
    private void exportarGraficoComoImagem() {
        // Captura a imagem do gráfico
        barChart.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(barChart.getDrawingCache());
        barChart.setDrawingCacheEnabled(false);

        // Obter os totais e o mês e ano selecionados
        String totalQuantidade = totalQuantidadeTextView.getText().toString();
        String totalGasto = totalGastoTextView.getText().toString();
        String mesSelecionado = (String) spinnerMes.getSelectedItem();
        String anoSelecionado = (String) spinnerAno.getSelectedItem();

        // Criar um novo bitmap para incluir os detalhes do relatório
        Bitmap reportBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight() + 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(reportBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null); // Desenhar o gráfico no novo bitmap

        // Configurar o paint para o texto
        Paint paint = new Paint();
        paint.setColor(Color.WHITE); // Cor do texto
        paint.setTextSize(40); // Tamanho do texto
        paint.setStyle(Paint.Style.FILL); // Estilo de preenchimento

        // Desenhar os detalhes na imagem
        canvas.drawText("" + totalQuantidade, 50, bitmap.getHeight() + 100, paint);
        canvas.drawText("" + totalGasto, 50, bitmap.getHeight() + 150, paint);
        canvas.drawText("Mês: " + mesSelecionado + " Ano: " + anoSelecionado, 50, bitmap.getHeight() + 200, paint);

        // Salvar a imagem no armazenamento
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File dir = new File(path + "/relatorios/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = "relatorio_" + new Date().getTime() + ".png";
        File file = new File(dir, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            reportBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();

            // Chama o método de compartilhamento
            compartilharImagem(file);
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao exportar imagem: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void compartilharImagem(File file) {
        Uri imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.putExtra(Intent.EXTRA_TEXT, "Veja meu relatório de vendas!");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Compartilhar Relatório"));
    }


}
