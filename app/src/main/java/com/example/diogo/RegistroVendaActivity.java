package com.example.diogo;
import java.sql.Date;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

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
    private Spinner spinnerCliente, spinnerVinho;
    private EditText textDataVenda;
    private ClienteDAO clienteDAO;
    private VinhoDAO vinhoDAO;
    private VendasDAO vendasDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.vinho));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendas);

        spinnerCliente = findViewById(R.id.spinnerCliente);
        spinnerVinho = findViewById(R.id.spinnerVinho);
        textDataVenda = findViewById(R.id.textDataVenda);  // Use the global variable here

        clienteDAO = new ClienteDAO(this);
        vinhoDAO = new VinhoDAO(this);
        vendasDAO = new VendasDAO(this);

        loadSpinners();

        textDataVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenha a data atual como valor padrão
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Abra o DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        RegistroVendaActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Formatar a data e mostrar no TextView
                                String selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                                textDataVenda.setText(selectedDate);
                            }
                        },
                        year, month, day);

                datePickerDialog.show();
            }
        });

        spinnerCliente.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showClienteDialog(); // Exibe o diálogo
                }
                return true;
            }
        });

        spinnerVinho.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    abrirDialogoSelecaoVinho();
                }
                return true;
            }
        });

        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarVenda();
            }
        });
    }


    private void registrarVenda() {
        String clienteSelecionado = (String) spinnerCliente.getSelectedItem();
        String vinhoSelecionado = (String) spinnerVinho.getSelectedItem();
        String dataVenda = textDataVenda.getText().toString();  // formato dd/MM/yyyy

        if (clienteSelecionado != null && vinhoSelecionado != null && !dataVenda.isEmpty()) {
            try {
                // Converter a string de data para java.sql.Date
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date utilDate = dateFormat.parse(dataVenda);
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

                // Recupera a quantidade selecionada
                String[] vinhoInfo = vinhoSelecionado.split(" - ");
                String nomeVinho = vinhoInfo[0];
                String quantidadeStr = vinhoInfo[1].replace(" unidades", "");
                int quantidade = Integer.parseInt(quantidadeStr);

                VendasModel venda = new VendasModel(clienteSelecionado, nomeVinho, sqlDate, quantidade);
                long id = vendasDAO.insert(venda);
                boolean vendaRegistrada = id != -1;

                if (vendaRegistrada) {
                    boolean estoqueAtualizado = vendasDAO.updateVinhoEstoque(nomeVinho, quantidade);
                    if (estoqueAtualizado) {
                        Toast.makeText(this, "Venda registrada e estoque atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        logger.info("Venda registrada: " + venda);
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(this, "Venda registrada, mas erro ao atualizar estoque.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Erro ao registrar venda.", Toast.LENGTH_SHORT).show();
                    logger.warning("Falha ao registrar venda para o cliente: " + clienteSelecionado + " com vinho: " + nomeVinho);
                }
            } catch (ParseException e) {
                Toast.makeText(this, "Erro ao processar a data.", Toast.LENGTH_SHORT).show();
                logger.warning("Erro ao converter a data de venda: " + e.getMessage());
            }
        } else {
            Toast.makeText(this, "Selecione um cliente, um vinho e insira uma data válida.", Toast.LENGTH_SHORT).show();
            logger.warning("Dados inválidos para registro de venda: cliente=" + clienteSelecionado + ", vinho=" + vinhoSelecionado + ", data=" + dataVenda);
        }
    }

    private void loadSpinners() {
        List<String> clientes = clienteDAO.getAllClientesNames();
        ArrayAdapter<String> clienteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clientes);
        clienteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCliente.setAdapter(clienteAdapter);

        ArrayAdapter<String> vinhoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Selecione um vinho"});
        vinhoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVinho.setAdapter(vinhoAdapter);
    }

    private void showClienteDialog() {
        List<String> clientes = clienteDAO.getAllClientesNames();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecionar Cliente")
                .setItems(clientes.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        spinnerCliente.setSelection(which);
                    }
                });
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

        builder.setItems(vinhosDisponiveis, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int whichVinho) {
                final VinhosModel vinhoSelecionado = vinhosList.get(whichVinho);

                AlertDialog.Builder quantidadeDialog = new AlertDialog.Builder(RegistroVendaActivity.this);
                quantidadeDialog.setTitle("Quantidade para " + vinhoSelecionado.getNome());

                final EditText inputQuantidade = new EditText(RegistroVendaActivity.this);
                inputQuantidade.setHint("Máximo: " + vinhoSelecionado.getEstoque());
                quantidadeDialog.setView(inputQuantidade);

                quantidadeDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String quantidadeStr = inputQuantidade.getText().toString();
                        if (!quantidadeStr.isEmpty()) {
                            int quantidadeEscolhida = Integer.parseInt(quantidadeStr);
                            if (quantidadeEscolhida <= vinhoSelecionado.getEstoque()) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistroVendaActivity.this, android.R.layout.simple_spinner_item, new String[]{vinhoSelecionado.getNome() + " - " + quantidadeEscolhida + " unidades"});
                                spinnerVinho.setAdapter(adapter);
                            } else {
                                Toast.makeText(RegistroVendaActivity.this, "Quantidade maior que o estoque disponível.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                quantidadeDialog.setNegativeButton("Cancelar", null);
                quantidadeDialog.create().show();
            }
        });

        builder.create().show();
    }
}
