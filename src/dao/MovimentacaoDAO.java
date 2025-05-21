package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Movimentacao;

public class MovimentacaoDAO {
    
    public boolean registrarMovimentacao(Movimentacao movimentacao) {
        String sql = "INSERT INTO movimentacoes (produto_id, tipo, quantidade, usuario_id) VALUES (?,?,?,?)";
        
        try (Connection conexao = ConexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, movimentacao.getProdutoId());
            stmt.setString(2, movimentacao.getTipo());
            stmt.setDouble(3, movimentacao.getQuantidade());
            stmt.setInt(4, movimentacao.getUsuarioId());
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Movimentacao> buscarPorUsuario(int usuarioId) {
        String sql = "SELECT m.*, p.nome as produto_nome FROM movimentacoes m " +
                    "JOIN produtos p ON m.produto_id = p.id " +
                    "WHERE m.usuario_id = ? " +
                    "ORDER BY m.data DESC";
        List<Movimentacao> movimentacoes = new ArrayList<>();
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Movimentacao mov = new Movimentacao(
                        rs.getInt("id"),
                        rs.getInt("produto_id"),
                        rs.getString("produto_nome"),
                        rs.getString("tipo"),
                        rs.getDouble("quantidade"),
                        rs.getInt("usuario_id"),
                        rs.getTimestamp("data") != null ? 
                            rs.getTimestamp("data").toLocalDateTime() : 
                            null
                    );
                    movimentacoes.add(mov);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movimentacoes;
    }
}