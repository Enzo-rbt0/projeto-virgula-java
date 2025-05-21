package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import dao.UsuarioDAO;
import model.Usuario;

public class TelaLogin extends JFrame {
    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    
    public TelaLogin() {
        super("Login - Sistema de Estoque");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        
        // Componentes
        JLabel labelTitulo = new JLabel("CONTROLE DE ESTOQUE");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        JLabel labelUsuario = new JLabel("Usuário:");
        campoUsuario = new JTextField(15);
        
        JLabel labelSenha = new JLabel("Senha:");
        campoSenha = new JPasswordField(15);
        
        JButton botaoLogin = new JButton("Login");
        JButton botaoCadastrar = new JButton("Cadastrar");
        
        // Layout
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        painel.add(labelTitulo, constraints);
        
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridy = 1;
        painel.add(labelUsuario, constraints);
        
        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.WEST;
        painel.add(campoUsuario, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.EAST;
        painel.add(labelSenha, constraints);
        
        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.WEST;
        painel.add(campoSenha, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        painelBotoes.add(botaoLogin);
        painelBotoes.add(botaoCadastrar);
        painel.add(painelBotoes, constraints);
        
        // Listeners
        botaoLogin.addActionListener(e -> autenticarUsuario());
        botaoCadastrar.addActionListener(e -> {
            new TelaCadastroUsuario();
            dispose();
        });
        
        add(painel);
        setVisible(true);
    }
    
    private void autenticarUsuario() {
        String usuario = campoUsuario.getText().trim();
        String senha = new String(campoSenha.getPassword()).trim();
        
        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Preencha todos os campos!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuarioAutenticado = usuarioDAO.autenticar(usuario, senha);
        
        if (usuarioAutenticado != null) {
            new TelaPrincipal(usuarioAutenticado);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Usuário ou senha inválidos!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}