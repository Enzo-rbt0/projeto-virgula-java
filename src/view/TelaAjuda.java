package view;

import javax.swing.*;

public class TelaAjuda extends JFrame {
    public TelaAjuda() {
        setTitle("Ajuda");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea areaAjuda = new JTextArea();
        areaAjuda.setEditable(false);
        areaAjuda.setText(
            "Sistema de Controle de Estoque\n\n" +
            "- Menu Produtos: cadastre e visualize produtos.\n" +
            "- Menu Movimentações: registre entrada e saída de estoque.\n" +
            "- Menu Relatórios: veja um resumo do estoque atual.\n"
        );

        add(new JScrollPane(areaAjuda));
        setVisible(true);
    }
}