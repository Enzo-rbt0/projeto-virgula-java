package view;

import javax.swing.*;
import dao.ProdutoDAO;
import model.Produto;
import java.util.List;

public class TelaRelatorios extends JFrame {
    public TelaRelatorios() {
        setTitle("Relatórios de Estoque");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ProdutoDAO dao = new ProdutoDAO();
        List<Produto> produtos = dao.listarProdutos();

        int totalProdutos = produtos.size();
        double totalEstoque = produtos.stream().mapToDouble(p -> p.getQuantidade() * p.getPreco()).sum();

        JLabel lblResumo = new JLabel("<html><h3>Total de produtos: " + totalProdutos +
                "<br>Valor total em estoque: R$ " + String.format("%.2f", totalEstoque) + "</h3></html>", JLabel.CENTER);

        add(lblResumo);
        setVisible(true);
    }
}
