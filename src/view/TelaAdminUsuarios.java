package view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import dao.UsuarioDAO;
import dao.MovimentacaoDAO;
import model.Usuario;
import model.Movimentacao;

public class TelaAdminUsuarios extends JFrame {
    private JTable tabelaUsuarios;
    private JTable tabelaMovimentacoes;
    
    public TelaAdminUsuarios() {
        super("Administração de Usuários");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Painel principal
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        
        // Tabela de usuários
        tabelaUsuarios = new JTable(new UsuarioTableModel());
        tabelaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tabelaUsuarios.getSelectedRow();
                if (selectedRow != -1) {
                    int userId = (int) tabelaUsuarios.getValueAt(selectedRow, 0);
                    carregarMovimentacoesUsuario(userId);
                }
            }
        });
        
        // Tabela de movimentações
        tabelaMovimentacoes = new JTable(new MovimentacaoTableModel());
        
        // Divisão das tabelas
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tabelaUsuarios),
                new JScrollPane(tabelaMovimentacoes));
        splitPane.setResizeWeight(0.4);
        
        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        painelBotoes.add(btnFechar);
        
        // Layout
        painelPrincipal.add(splitPane, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
        
        add(painelPrincipal);
        carregarUsuarios();
        setVisible(true);
    }
    
    private void carregarUsuarios() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        List<Usuario> usuarios = usuarioDAO.listarUsuarios();
        ((UsuarioTableModel)tabelaUsuarios.getModel()).atualizar(usuarios);
    }
    
    private void carregarMovimentacoesUsuario(int userId) {
        MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();
        List<Movimentacao> movimentacoes = movimentacaoDAO.buscarPorUsuario(userId);
        ((MovimentacaoTableModel)tabelaMovimentacoes.getModel()).atualizar(movimentacoes);
    }
    
    // Modelo de tabela para usuários
    class UsuarioTableModel extends AbstractTableModel {
        private List<Usuario> usuarios;
        private String[] colunas = {"ID", "Nome", "Login", "Tipo"};
        
        public UsuarioTableModel() {
            usuarios = new java.util.ArrayList<>();
        }
        
        public void atualizar(List<Usuario> usuarios) {
            this.usuarios = usuarios;
            fireTableDataChanged();
        }
        
        @Override
        public int getRowCount() {
            return usuarios.size();
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
        public Object getValueAt(int rowIndex, int columnIndex) {
            Usuario usuario = usuarios.get(rowIndex);
            switch (columnIndex) {
                case 0: return usuario.getId();
                case 1: return usuario.getNome();
                case 2: return usuario.getLogin();
                case 3: return usuario.getTipo();
                default: return null;
            }
        }
    }
    
    // Modelo de tabela para movimentações
    class MovimentacaoTableModel extends AbstractTableModel {
        private List<Movimentacao> movimentacoes;
        private String[] colunas = {"ID", "Produto (ID)", "Nome do Produto", "Tipo", "Quantidade", "Data"};
        
        public MovimentacaoTableModel() {
            movimentacoes = new java.util.ArrayList<>();
        }
        
        public void atualizar(List<Movimentacao> movimentacoes) {
            this.movimentacoes = movimentacoes;
            fireTableDataChanged();
        }
        
        @Override
        public int getRowCount() {
            return movimentacoes.size();
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
        public Object getValueAt(int rowIndex, int columnIndex) {
            Movimentacao mov = movimentacoes.get(rowIndex);
            switch (columnIndex) {
                case 0: return mov.getId();
                case 1: return mov.getProdutoId();
                case 2: return mov.getProdutoNome();
                case 3: return mov.getTipo();
                case 4: return mov.getQuantidade();
                case 5: return mov.getDataFormatada();
                default: return null;
            }
        }
    }
}