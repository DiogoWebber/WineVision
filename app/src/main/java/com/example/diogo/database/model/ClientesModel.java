package com.example.diogo.database.model;

public class ClientesModel {

    public static final String TABLE_NAME = "tb_clientes";

    public static final String COLUNA_ID = "_id";
    public static final String COLUNA_CLIENTE = "nome";
    public static final String COLUNA_EMAIL = "email";
    public static final String COLUNA_TELEFONE = "telefone";
    public static final String COLUNA_ENDERECO = "endereco";
    public static final String COLUNA_NUMERO = "numero";
    public static final String COLUNA_DOCUMENTO = "documento";

    private long id;
    private String nome;
    private String email;
    private String telefone;
    private String endereco;
    private int numero;
    private String documento;

    // SQL para criar a tabela
    public static String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUNA_CLIENTE + " TEXT NOT NULL UNIQUE, " +
                    COLUNA_EMAIL + " TEXT NOT NULL, " +
                    COLUNA_TELEFONE + " TEXT NOT NULL, " +
                    COLUNA_ENDERECO + " TEXT NOT NULL, " +
                    COLUNA_NUMERO + " INTEGER NOT NULL, " +
                    COLUNA_DOCUMENTO + " TEXT NOT NULL" + ");";

    public static String DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public ClientesModel() {
    }

    public ClientesModel(long id, String nome, String email, String telefone, String endereco, int numero, String documento) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.numero = numero;
        this.documento = documento;
    }

    // Getters e Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
}
