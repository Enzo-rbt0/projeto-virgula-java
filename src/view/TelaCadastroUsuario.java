package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dao.UsuarioDAO;
import model.Usuario;

public class TelaCadastroUsuario extends JFrame {
    private JTextField campoNome, campoUsuario;
    private JPasswordField campoSenha, campoConfirmarSenha;
    
    public TelaCadastroUsuario() {
        super("Cadastro de Usuário");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.EAST;
        
        // Componentes
        JLabel labelTitulo = new JLabel("CADASTRO DE USUÁRIO");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel labelNome = new JLabel("Nome Completo:");
        campoNome = new JTextField(20);
        
        JLabel labelUsuario = new JLabel("Usuário:");
        campoUsuario = new JTextField(20);
        
        JLabel labelSenha = new JLabel("Senha:");
        campoSenha = new JPasswordField(20);
        
        JLabel labelConfirmar = new JLabel("Confirmar Senha:");
        campoConfirmarSenha = new JPasswordField(20);
        
        JButton botaoCadastrar = new JButton("Cadastrar");
        JButton botaoCancelar = new JButton("Cancelar");
        
        // Layout
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        painel.add(labelTitulo, constraints);
        
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridy = 1;
        painel.add(labelNome, constraints);
        
        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.WEST;
        painel.add(campoNome, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.EAST;
        painel.add(labelUsuario, constraints);
        
        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.WEST;
        painel.add(campoUsuario, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 3;
        painel.add(labelSenha, constraints);
        
        constraints.gridx = 1;
        painel.add(campoSenha, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 4;
        painel.add(labelConfirmar, constraints);
        
        constraints.gridx = 1;
        painel.add(campoConfirmarSenha, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoCancelar);
        painel.add(painelBotoes, constraints);
        
        // Listeners
        botaoCadastrar.addActionListener(e -> cadastrarUsuario());
        botaoCancelar.addActionListener(e -> {
            new TelaLogin();
            dispose();
        });
        
        add(painel);
        setVisible(true);
    }
    
    private void cadastrarUsuario() {
        String nome = campoNome.getText().trim();
        String usuario = campoUsuario.getText().trim();
        String senha = new String(campoSenha.getPassword()).trim();
        String confirmar = new String(campoConfirmarSenha.getPassword()).trim();
        
        // Validações
        if (nome.isEmpty() || usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Preencha todos os campos!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!senha.equals(confirmar)) {
            JOptionPane.showMessageDialog(this, 
                "As senhas não coincidem!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (senha.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                "A senha deve ter pelo menos 6 caracteres!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        // Verifica se usuário já existe
        if (usuarioDAO.usuarioExiste(usuario)) {
            JOptionPane.showMessageDialog(this, 
                "Este nome de usuário já está em uso!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Cria novo usuário com senha criptografada (padrão: tipo 'comum')
        String senhaCriptografada = hashSenha(senha);
        Usuario novoUsuario = new Usuario(0, nome, usuario, senhaCriptografada, "comum");
        
        if (usuarioDAO.adicionarUsuario(novoUsuario)) {
            JOptionPane.showMessageDialog(this, 
                "Usuário cadastrado com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            new TelaLogin();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Erro ao cadastrar usuário!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String hashSenha(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(senha.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
