package view;

import javax.swing.*;
import java.awt.*;
import model.Usuario;
import dao.ProdutoDAO;

public class TelaPrincipal extends JFrame {
    private Usuario usuario;

    public TelaPrincipal(Usuario usuario) {
        super("Sistema de Controle de Estoque - " + usuario.getNome());
        this.usuario = usuario;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();

        // Menu Usuário
        JMenu menuUsuario = new JMenu(usuario.getNome());
        JMenuItem itemLogout = new JMenuItem("Logout");
        menuUsuario.add(itemLogout);

        // Menu Produtos
        JMenu menuProdutos = new JMenu("Produtos");
        JMenuItem itemCadastrar = new JMenuItem("Cadastrar");
        JMenuItem itemListar = new JMenuItem("Listar");
        menuProdutos.add(itemCadastrar);
        menuProdutos.add(itemListar);

        // Menu Movimentações
        JMenu menuMovimentacoes = new JMenu("Movimentações");
        JMenuItem itemEntrada = new JMenuItem("Entrada");
        JMenuItem itemSaida = new JMenuItem("Saída");
        menuMovimentacoes.add(itemEntrada);
        menuMovimentacoes.add(itemSaida);

        // Menu Relatórios
        JMenu menuRelatorios = new JMenu("Relatórios");
        JMenuItem itemRelatorio = new JMenuItem("Estoque Atual");
        menuRelatorios.add(itemRelatorio);

        // Menu Ajuda
        JMenu menuAjuda = new JMenu("Ajuda");
        JMenuItem itemSobre = new JMenuItem("Sobre");
        JMenuItem itemAjuda = new JMenuItem("Ver Ajuda");
        menuAjuda.add(itemAjuda);
        menuAjuda.add(itemSobre);

        // Adiciona menus
        menuBar.add(menuUsuario);
        menuBar.add(menuProdutos);
        menuBar.add(menuMovimentacoes);
        menuBar.add(menuRelatorios);
        menuBar.add(menuAjuda);

        // Menu Admin (apenas para administradores)
        if ("admin".equals(usuario.getTipo())) {
            JMenu menuAdmin = new JMenu("Administração");
            JMenuItem itemUsuarios = new JMenuItem("Usuários");
            menuAdmin.add(itemUsuarios);
            menuBar.add(menuAdmin);

            itemUsuarios.addActionListener(e -> new TelaAdminUsuarios());
        }

        setJMenuBar(menuBar);

        // Painel principal
        JPanel painel = new JPanel(new BorderLayout());
        JLabel labelBoasVindas = new JLabel("Bem-vindo, " + usuario.getNome(), JLabel.CENTER);
        labelBoasVindas.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel labelAcesso = new JLabel("Tipo de acesso: " +
                ("admin".equals(usuario.getTipo()) ? "Administrador" : "Usuário"),
                JLabel.CENTER);
        labelAcesso.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel painelCentral = new JPanel(new GridLayout(2, 1));
        painelCentral.add(labelBoasVindas);
        painelCentral.add(labelAcesso);

        painel.add(painelCentral, BorderLayout.CENTER);
        add(painel);

        // Listeners
        itemLogout.addActionListener(e -> {
            new TelaLogin();
            dispose();
        });

        itemCadastrar.addActionListener(e -> new TelaCadastroProduto());
        itemListar.addActionListener(e -> new TelaListagemProdutos(usuario));
        itemEntrada.addActionListener(e -> new TelaMovimentacaoEstoque("entrada", usuario));
        itemSaida.addActionListener(e -> new TelaMovimentacaoEstoque("saida", usuario));

        // Abrir a tela de relatório
        itemRelatorio.addActionListener(e -> new TelaRelatorios());

        // Abrir a tela de ajuda
        itemAjuda.addActionListener(e -> new TelaAjuda());

        // Informações sobre o sistema
        itemSobre.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Sistema de Controle de Estoque\nVersão 1.0",
                    "Sobre", JOptionPane.INFORMATION_MESSAGE);
        });

        setVisible(true);
    }
}
