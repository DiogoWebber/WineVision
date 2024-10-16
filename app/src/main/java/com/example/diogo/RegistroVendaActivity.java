package com.example.diogo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diogo.database.dao.ClienteDAO;
import com.example.diogo.database.dao.VendasDAO;
import com.example.diogo.database.dao.VinhoDAO;
import com.example.diogo.database.model.VendasModel;
import com.example.diogo.database.model.VinhosModel;

import java.util.List;
import java.util.logging.Logger;

public class RegistroVendaActivity extends AppCompatActivity {
    private static final Logger logger = Logger.getLogger(RegistroVendaActivity.class.getName());
    private Spinner spinnerCliente, spinnerVinho;
    private EditText editDataVenda;
    private ClienteDAO clienteDAO;
    private VinhoDAO vinhoDAO;
    private VendasDAO vendasDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendas);

        spinnerCliente = findViewById(R.id.spinnerCliente);
        spinnerVinho = findViewById(R.id.spinnerVinho);
        editDataVenda = findViewById(R.id.editDataVenda);

        clienteDAO = new ClienteDAO(this);
        vinhoDAO = new VinhoDAO(this);
        vendasDAO = new VendasDAO(this);

        loadSpinners();

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
        String dataVenda = editDataVenda.getText().toString();

        if (clienteSelecionado != null && vinhoSelecionado != null && !dataVenda.isEmpty()) {
            // Recupera a quantidade selecionada
            String[] vinhoInfo = vinhoSelecionado.split(" - ");
            String nomeVinho = vinhoInfo[0];
            String quantidadeStr = vinhoInfo[1].replace(" unidades", "");
            int quantidade = Integer.parseInt(quantidadeStr);

            VendasModel venda = new VendasModel(clienteSelecionado, nomeVinho, dataVenda, quantidade);
            long id = vendasDAO.insert(venda);
            boolean vendaRegistrada = id != -1; // true se a venda foi inserida com sucesso

            if (vendaRegistrada) {
                // Atualiza o estoque do vinho
                boolean estoqueAtualizado = vendasDAO.updateVinhoEstoque(nomeVinho, quantidade);
                if (estoqueAtualizado) {
                    Toast.makeText(this, "Venda registrada e estoque atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    logger.info("Venda registrada: " + venda); // Log de informação

                    // Set result and finish activity
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Finish current activity and return to VendasActivity
                } else {
                    Toast.makeText(this, "Venda registrada, mas erro ao atualizar estoque.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Erro ao registrar venda.", Toast.LENGTH_SHORT).show();
                logger.warning("Falha ao registrar venda para o cliente: " + clienteSelecionado + " com vinho: " + nomeVinho);
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
