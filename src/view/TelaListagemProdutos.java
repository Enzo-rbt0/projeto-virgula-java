package view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import dao.ProdutoDAO;
import model.Produto;
import model.Usuario;

public class TelaListagemProdutos extends JFrame {
    private JTable tabela;
    private ProdutoTableModel tableModel;
    private JTextField campoBusca;
    private Usuario usuario;
    
    public TelaListagemProdutos(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Lista de Produtos");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Painel superior (busca)
        JPanel painelSuperior = new JPanel(new BorderLayout(10, 10));
        campoBusca = new JTextField(20);
        JButton botaoBusca = new JButton("Buscar");
        
        painelSuperior.add(new JLabel("Buscar produto:"), BorderLayout.WEST);
        painelSuperior.add(campoBusca, BorderLayout.CENTER);
        painelSuperior.add(botaoBusca, BorderLayout.EAST);
        
        // Tabela
        tableModel = new ProdutoTableModel();
        tabela = new JTable(tableModel);
        tabela.setAutoCreateRowSorter(true);
        
        // Painel de botões
        JPanel painelBotoes = new JPanel();
        
        // Apenas admin pode editar/excluir
        if ("admin".equals(usuario.getTipo())) {
            JButton btnEditar = new JButton("Editar");
            JButton btnExcluir = new JButton("Excluir");
            
            painelBotoes.add(btnEditar);
            painelBotoes.add(btnExcluir);
            
            btnEditar.addActionListener(e -> editarProduto());
            btnExcluir.addActionListener(e -> excluirProduto());
        }
        
        // Layout
        setLayout(new BorderLayout(10, 10));
        add(painelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
        
        // Listeners
        botaoBusca.addActionListener(e -> atualizarTabela());
        campoBusca.addActionListener(e -> atualizarTabela());
        
        atualizarTabela();
        setVisible(true);
    }
    
    private void atualizarTabela() {
        ProdutoDAO dao = new ProdutoDAO();
        List<Produto> produtos;
        
        String busca = campoBusca.getText().trim();
        if (busca.isEmpty()) {
            produtos = dao.listarProdutos();
        } else {
            produtos = dao.buscarPorNome(busca);
        }
        
        tableModel.atualizar(produtos);
    }
    
    private void editarProduto() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada != -1) {
            int idProduto = (int) tableModel.getValueAt(linhaSelecionada, 0);
            Produto produto = new ProdutoDAO().buscarPorId(idProduto);
            if (produto != null) {
                new TelaCadastroProduto(produto);
                dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar.");
        }
    }
    
    private void excluirProduto() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada != -1) {
            int idProduto = (int) tableModel.getValueAt(linhaSelecionada, 0);
            int confirmacao = JOptionPane.showConfirmDialog(
                this, 
                "Tem certeza que deseja excluir este produto?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                new ProdutoDAO().removerProduto(idProduto);
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
                atualizarTabela();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.");
        }
    }
    
    class ProdutoTableModel extends AbstractTableModel {
        private List<Produto> produtos;
        private String[] colunas = {"ID", "Nome", "Descrição", "Quantidade", "Unidade", "Preço"};
        
        public ProdutoTableModel() {
            produtos = new ArrayList<>();
        }
        
        public void atualizar(List<Produto> produtos) {
            this.produtos = produtos;
            fireTableDataChanged();
        }
        
        @Override
        public int getRowCount() {
            return produtos.size();
        }
        
        @Override
        public int getColumnCount() {
            return colunas.length;
        }
        
        @Override
        public String getColumnName(int column) {
            return colunas[column];
        }
        
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) return Integer.class;
            if (columnIndex == 3) return Double.class;
            if (columnIndex == 5) return Double.class;
            return String.class;
        }
        
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Produto produto = produtos.get(rowIndex);
            switch (columnIndex) {
                case 0: return produto.getId();
                case 1: return produto.getNome();
                case 2: return produto.getDescricao();
                case 3: return produto.getQuantidade();
                case 4: return produto.getUnidade();
                case 5: return produto.getPreco();
                default: return null;
            }
        }
    }
}