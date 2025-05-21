package view;

import javax.swing.*;
import dao.ProdutoDAO;
import dao.MovimentacaoDAO;
import model.Produto;
import model.Movimentacao;
import model.Usuario;
import java.awt.event.*;
import java.util.List;

public class TelaMovimentacaoEstoque extends JFrame {
    private Usuario usuario;
    
    public TelaMovimentacaoEstoque(String tipo, Usuario usuario) {
        this.usuario = usuario;
        setTitle(tipo.equals("entrada") ? "Entrada de Estoque" : "Saída de Estoque");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel lblProduto = new JLabel("Produto:");
        JComboBox<String> comboProdutos = new JComboBox<>();
        JLabel lblQuantidade = new JLabel("Quantidade:");
        JTextField txtQuantidade = new JTextField();
        JButton btnConfirmar = new JButton("Confirmar");

        // Carrega produtos com estoque
        ProdutoDAO produtoDAO = new ProdutoDAO();
        List<Produto> lista = produtoDAO.listarProdutos();
        for (Produto p : lista) {
            comboProdutos.addItem(String.format("%d - %s (Estoque: %d)", 
                p.getId(), 
                p.getNome(), 
                (int)p.getQuantidade()));
        }

        setLayout(null);
        lblProduto.setBounds(20, 20, 100, 25);
        comboProdutos.setBounds(120, 20, 260, 25);
        lblQuantidade.setBounds(20, 60, 100, 25);
        txtQuantidade.setBounds(120, 60, 100, 25);
        btnConfirmar.setBounds(120, 100, 100, 30);

        add(lblProduto);
        add(comboProdutos);
        add(lblQuantidade);
        add(txtQuantidade);
        add(btnConfirmar);

        btnConfirmar.addActionListener(e -> {
            try {
                if (comboProdutos.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Selecione um produto", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String selected = (String) comboProdutos.getSelectedItem();
                int idProduto = Integer.parseInt(selected.split(" - ")[0]);
                double quantidade = Double.parseDouble(txtQuantidade.getText());

                if (quantidade <= 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Quantidade deve ser maior que zero", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (tipo.equals("saida")) {
                    Produto produto = produtoDAO.buscarPorId(idProduto);
                    if (!produtoDAO.verificarEstoqueSuficiente(idProduto, quantidade)) {
                        JOptionPane.showMessageDialog(this,
                            "Estoque insuficiente! Disponível: " + (int)produto.getQuantidade() + " " + produto.getUnidade(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                produtoDAO.atualizarEstoque(idProduto, quantidade, tipo);
                
                Movimentacao movimentacao = new Movimentacao(
                    idProduto, 
                    tipo, 
                    quantidade, 
                    usuario.getId()
                );
                
                MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();
                boolean registrado = movimentacaoDAO.registrarMovimentacao(movimentacao);
                
                if (registrado) {
                    JOptionPane.showMessageDialog(this, 
                        "Movimentação registrada com sucesso!", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Quantidade inválida! Digite um valor numérico válido.", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao processar movimentação: " + ex.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}