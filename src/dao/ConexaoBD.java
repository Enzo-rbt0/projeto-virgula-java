package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
    private static final String URL = "jdbc:mysql://localhost:7175/estoque_lanchonete";
    private static final String USUARIO = "root";
    private static final String SENHA = "@Sooszicas2903";
    
    private static Connection conexao;
    
    public static Connection getConexao() {
        try {
            if(conexao == null || conexao.isClosed()) {
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            }
            return conexao;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void fecharConexao() {
        try {
            if(conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}