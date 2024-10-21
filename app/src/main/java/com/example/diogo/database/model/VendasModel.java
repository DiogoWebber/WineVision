package com.example.diogo.database.model;

import java.sql.Date; // ou java.util.Date

public class VendasModel {

    public static final String TABLE_NAME = "tb_vendas";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CLIENTE = "cliente";
    public static final String COLUMN_VINHO = "vinho";
    public static final String ID_CLIENTE = "id_cliente";
    public static final String COLUMN_DATA_VENDA = "data_venda";
    public static final String COLUMN_QUANTIDADE = "quantidade";
    public static final String COLUMN_TOTALVENDA = "total_venda";

    // SQL para criar a tabela
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ID_CLIENTE + " INTEGER," + // Add id_cliente column
                    COLUMN_CLIENTE + " TEXT," +
                    COLUMN_VINHO + " TEXT," +
                    COLUMN_DATA_VENDA + " DATE," +
                    COLUMN_QUANTIDADE + " INTEGER," +
                    COLUMN_TOTALVENDA + " REAL)"; // Add total_venda column

    // Atributos
    private int id;
    private int idCliente; // New field for client ID
    private String cliente;
    private String vinho;
    private Date dataVenda; // Usando java.sql.Date ou java.util.Date
    private int quantidade;
    private double totalVenda; // New field for total sale value
    // Construtores
    public VendasModel() {}

    public VendasModel(Integer idCliente, String cliente, String vinho, Date dataVenda, int quantidade, double totalVenda) {
        this.idCliente = (idCliente != null) ? idCliente : 0; // Handle null case if needed
        this.cliente = cliente;
        this.vinho = vinho;
        this.dataVenda = dataVenda;
        this.quantidade = quantidade;
        this.totalVenda = totalVenda;
    }


    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdCliente() {
        return idCliente; // Getter for idCliente
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente; // Setter for idCliente
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getVinho() {
        return vinho;
    }

    public void setVinho(String vinho) {
        this.vinho = vinho;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getTotalVenda() {
        return totalVenda; // Getter for totalVenda
    }

    public void setTotalVenda(double totalVenda) {
        this.totalVenda = totalVenda; // Setter for totalVenda
    }
}
