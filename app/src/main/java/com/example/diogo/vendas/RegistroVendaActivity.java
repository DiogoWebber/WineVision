package com.example.diogo.vendas;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diogo.R;
import com.example.diogo.database.dao.ClienteDAO;
import com.example.diogo.database.dao.VendasDAO;
import com.example.diogo.database.dao.VinhoDAO;
import com.example.diogo.database.model.VendasModel;
import com.example.diogo.database.model.VinhosModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

public class RegistroVendaActivity extends AppCompatActivity {
    private static final Logger logger = Logger.getLogger(RegistroVendaActivity.class.getName());
    private static final String SELECT_VINO_MESSAGE = "Selecione um vinho";
    private Spinner spinnerCliente, spinnerVinho;
    private EditText textDataVenda;
    private ClienteDAO clienteDAO;
    private VinhoDAO vinhoDAO;
    private VendasDAO vendasDAO;
    private VinhosModel vinhoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.vinho));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendas);

        initializeViews();
        initializeDAOs();
        loadSpinners();

        textDataVenda.setOnClickListener(v -> showDatePicker());
        setSpinnerTouchListeners();

        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(v -> registrarVenda());
    }

    private void initializeViews() {
        spinnerCliente = findViewById(R.id.spinnerCliente);
        spinnerVinho = findViewById(R.id.spinnerVinho);
        textDataVenda = findViewById(R.id.textDataVenda);
    }

    private void initializeDAOs() {
        clienteDAO = new ClienteDAO(this);
        vinhoDAO = new VinhoDAO(this);
        vendasDAO = new VendasDAO(this);
    }

    private void setSpinnerTouchListeners() {
        spinnerCliente.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                showClienteDialog();
            }
            return true;
        });

        spinnerVinho.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                abrirDialogoSelecaoVinho();
            }
            return true;
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    textDataVenda.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void registrarVenda() {
        String clienteSelecionado = (String) spinnerCliente.getSelectedItem();
        String dataVenda = textDataVenda.getText().toString();

        if (vinhoSelecionado == null || clienteSelecionado == null || dataVenda.isEmpty()) {
            showToast("Selecione um cliente, um vinho e insira uma data válida.");
            logger.warning("Dados inválidos para registro de venda: cliente=" + clienteSelecionado + ", data=" + dataVenda);
            return;
        }

        try {
            java.sql.Date sqlDate = parseDate(dataVenda);
            String quantidadeStr = spinnerVinho.getSelectedItem().toString().split(" - ")[1].replace(" unidades", "");
            int quantidade = Integer.parseInt(quantidadeStr);

            if (quantidade > vinhoSelecionado.getEstoque()) {
                showToast("Quantidade maior que o estoque disponível.");
                return;
            }

            int clientId = clienteDAO.getClientIdByName(clienteSelecionado);

            // Calculate total sale value
            double totalVenda = calculateTotalVenda(quantidade);

            // Update VendasModel instantiation to include totalVenda
            VendasModel venda = new VendasModel(clientId, clienteSelecionado, vinhoSelecionado.getNome(), sqlDate, quantidade, totalVenda);
            long id = vendasDAO.insert(venda); // Insere a venda

            // Atualiza a quantidade do vinho selecionado
            vinhoSelecionado.setEstoque(vinhoSelecionado.getEstoque() - quantidade); // Subtrai a quantidade vendida

            long result = vinhoDAO.updateVinho(vinhoSelecionado); // Atualiza o vinho no banco de dados

            // Verifica se a venda foi registrada e se a atualização do estoque foi bem-sucedida
            if (id != -1 && result > 0) {
                showToast("Venda registrada e estoque atualizado com sucesso!");
                logger.info("Venda registrada: " + venda);
                setResult(RESULT_OK);
                finish();
            } else {
                showToast("Erro ao registrar venda ou atualizar estoque.");
            }
        } catch (ParseException e) {
            showToast("Erro ao processar a data.");
            logger.warning("Erro ao converter a data de venda: " + e.getMessage());
        }
    }


    private double calculateTotalVenda(int quantidade) {
        return vinhoSelecionado.getPreco() * quantidade; // Ensure getPreco() method exists in VinhosModel
    }


    private java.sql.Date parseDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date utilDate = dateFormat.parse(date);
        return new java.sql.Date(utilDate.getTime());
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void loadSpinners() {
        List<String> clientes = clienteDAO.getAllClientesNames();
        ArrayAdapter<String> clienteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clientes);
        clienteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCliente.setAdapter(clienteAdapter);

        ArrayAdapter<String> vinhoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{SELECT_VINO_MESSAGE});
        vinhoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVinho.setAdapter(vinhoAdapter);
    }

    private void showClienteDialog() {
        List<String> clientes = clienteDAO.getAllClientesNames();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecionar Cliente")
                .setItems(clientes.toArray(new String[0]), (dialog, which) -> spinnerCliente.setSelection(which));
        builder.create().show();
    }

    private void abrirDialogoSelecaoVinho() {
        List<VinhosModel> vinhosList = vinhoDAO.getAll();
        String[] vinhosDisponiveis = new String[vinhosList.size()];

        for (int i = 0; i < vinhosList.size(); i++) {
            VinhosModel vinho = vinhosList.get(i);
            vinhosDisponiveis[i] = vinho.getNome() + " - Estoque: " + vinho.getEstoque();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione o Vinho");

        builder.setItems(vinhosDisponiveis, (dialog, which) -> {
            vinhoSelecionado = vinhosList.get(which);
            promptForQuantidade(vinhoSelecionado);
        });

        builder.create().show();
    }

    private void promptForQuantidade(VinhosModel vinho) {
        AlertDialog.Builder quantidadeDialog = new AlertDialog.Builder(this);
        quantidadeDialog.setTitle("Quantidade para " + vinho.getNome());

        final EditText inputQuantidade = new EditText(this);
        inputQuantidade.setHint("Máximo: " + vinho.getEstoque());
        quantidadeDialog.setView(inputQuantidade);

        quantidadeDialog.setPositiveButton("OK", (dialog, which) -> {
            String quantidadeStr = inputQuantidade.getText().toString();
            if (!quantidadeStr.isEmpty()) {
                int quantidadeEscolhida = Integer.parseInt(quantidadeStr);
                if (quantidadeEscolhida <= vinho.getEstoque()) {
                    spinnerVinho.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                            new String[]{vinho.getNome() + " - " + quantidadeEscolhida + " unidades"}));
                } else {
                    showToast("Quantidade maior que o estoque disponível.");
                }
            }
        });

        quantidadeDialog.setNegativeButton("Cancelar", null);
        quantidadeDialog.create().show();
    }
}

