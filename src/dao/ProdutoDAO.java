package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Produto;

public class ProdutoDAO {
    
    public void adicionarProduto(Produto produto) {
        String sql = "INSERT INTO produtos(nome, descricao, quantidade, unidade, preco) VALUES(?,?,?,?,?)";
        
        try (Connection conexao = ConexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            conexao.setAutoCommit(false);
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getQuantidade());
            stmt.setString(4, produto.getUnidade());
            stmt.setDouble(5, produto.getPreco());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setId(rs.getInt(1));
                }
            }
            
            conexao.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar produto: " + e.getMessage());
        }
    }
    
    public List<Produto> listarProdutos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos ORDER BY nome";
        
        try (Connection conexao = ConexaoBD.getConexao();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Produto p = new Produto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getDouble("quantidade"),
                    rs.getString("unidade"),
                    rs.getDouble("preco")
                );
                produtos.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }
    
    public Produto buscarPorId(int id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";
        Produto produto = null;
        
        try (Connection conexao = ConexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    produto = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getDouble("quantidade"),
                        rs.getString("unidade"),
                        rs.getDouble("preco")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto: " + e.getMessage());
        }
        return produto;
    }
    
    public void atualizarProduto(Produto produto) {
        String sql = "UPDATE produtos SET nome=?, descricao=?, quantidade=?, unidade=?, preco=? WHERE id=?";
        
        try (Connection conexao = ConexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            conexao.setAutoCommit(false);
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getQuantidade());
            stmt.setString(4, produto.getUnidade());
            stmt.setDouble(5, produto.getPreco());
            stmt.setInt(6, produto.getId());
            
            stmt.executeUpdate();
            conexao.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto: " + e.getMessage());
        }
    }
    
    public void removerProduto(int id) {
        String sql = "DELETE FROM produtos WHERE id=?";
        
        try (Connection conexao = ConexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            conexao.setAutoCommit(false);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            conexao.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover produto: " + e.getMessage());
        }
    }
    
    public List<Produto> buscarPorNome(String nome) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE nome LIKE ? ORDER BY nome";
        
        try (Connection conexao = ConexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto p = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getDouble("quantidade"),
                        rs.getString("unidade"),
                        rs.getDouble("preco")
                    );
                    produtos.add(p);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produtos por nome: " + e.getMessage());
        }
        return produtos;
    }
    
    public void atualizarEstoque(int idProduto, double quantidade, String tipoMovimentacao) {
        if ("saida".equalsIgnoreCase(tipoMovimentacao)) {
            if (!verificarEstoqueSuficiente(idProduto, quantidade)) {
                throw new RuntimeException("Estoque insuficiente para realizar a saída");
            }
        }

        String sql = tipoMovimentacao.equalsIgnoreCase("entrada") ?
            "UPDATE produtos SET quantidade = quantidade + ? WHERE id = ?" :
            "UPDATE produtos SET quantidade = quantidade - ? WHERE id = ?";
        
        try (Connection conexao = ConexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            conexao.setAutoCommit(false);
            stmt.setDouble(1, quantidade);
            stmt.setInt(2, idProduto);
            stmt.executeUpdate();
            conexao.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar estoque: " + e.getMessage());
        }
    }
    
    public boolean produtoExiste(String nome) {
        String sql = "SELECT COUNT(*) FROM produtos WHERE nome = ?";
        
        try (Connection conexao = ConexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, nome);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar existência do produto: " + e.getMessage());
        }
        return false;
    }
    
    public boolean verificarEstoqueSuficiente(int idProduto, double quantidade) {
        if (quantidade <= 0) {
            return false;
        }
        
        String sql = "SELECT quantidade FROM produtos WHERE id = ?";
        
        try (Connection conexao = ConexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, idProduto);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double estoqueAtual = rs.getDouble("quantidade");
                    return estoqueAtual >= quantidade;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar estoque: " + e.getMessage());
        }
        return false;
    }
}