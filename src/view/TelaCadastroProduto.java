package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;
import model.Produto;
import dao.ProdutoDAO;

public class TelaCadastroProduto extends JFrame {
    private JTextField campoNome, campoDescricao, campoUnidade;
    private JFormattedTextField campoQuantidade, campoPreco;
    private JButton botaoSalvar, botaoCancelar;
    private Produto produtoEmEdicao;
    private ProdutoDAO produtoDAO;

    public TelaCadastroProduto() {
        this(null);
    }

    public TelaCadastroProduto(Produto produto) {
        super(produto == null ? "Cadastrar Novo Produto" : "Editar Produto");
        this.produtoEmEdicao = produto;
        this.produtoDAO = new ProdutoDAO();

        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Formatadores
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        NumberFormatter nfQuantidade = new NumberFormatter(nf);
        nfQuantidade.setMinimum(0.0);
        nfQuantidade.setAllowsInvalid(false);

        NumberFormatter nfPreco = new NumberFormatter(nf);
        nfPreco.setMinimum(0.0);
        nfPreco.setAllowsInvalid(false);

        // Componentes
        JLabel labelTitulo = new JLabel(produto == null ? "CADASTRAR NOVO PRODUTO" : "EDITAR PRODUTO");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel labelNome = new JLabel("Nome:");
        campoNome = new JTextField(25);

        JLabel labelDescricao = new JLabel("Descrição:");
        campoDescricao = new JTextField(25);

        JLabel labelQuantidade = new JLabel("Quantidade:");
        campoQuantidade = new JFormattedTextField(nfQuantidade);
        campoQuantidade.setColumns(10);

        JLabel labelUnidade = new JLabel("Unidade:");
        campoUnidade = new JTextField(5);

        JLabel labelPreco = new JLabel("Preço Unitário (R$):");
        campoPreco = new JFormattedTextField(nfPreco);
        campoPreco.setColumns(10);

        botaoSalvar = new JButton("Salvar");
        botaoSalvar.addActionListener((ActionEvent e) -> salvarProduto());

        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener((ActionEvent e) -> dispose());

        // Se for edição, preencher campos
        if (produto != null) {
            campoNome.setText(produto.getNome());
            campoDescricao.setText(produto.getDescricao());
            campoQuantidade.setValue(produto.getQuantidade());
            campoUnidade.setText(produto.getUnidade());
            campoPreco.setValue(produto.getPreco());
        }

        // Layout
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(labelTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy++;
        painel.add(labelNome, gbc);
        gbc.gridx = 1;
        painel.add(campoNome, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        painel.add(labelDescricao, gbc);
        gbc.gridx = 1;
        painel.add(campoDescricao, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        painel.add(labelQuantidade, gbc);
        gbc.gridx = 1;
        painel.add(campoQuantidade, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        painel.add(labelUnidade, gbc);
        gbc.gridx = 1;
        painel.add(campoUnidade, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        painel.add(labelPreco, gbc);
        gbc.gridx = 1;
        painel.add(campoPreco, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoCancelar);
        painel.add(painelBotoes, gbc);

        add(painel);
        setVisible(true);
    }

    private void salvarProduto() {
        // Validação
        if (campoNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome do produto é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            campoNome.requestFocus();
            return;
        }

        if (campoUnidade.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "A unidade de medida é obrigatória!", "Erro", JOptionPane.ERROR_MESSAGE);
            campoUnidade.requestFocus();
            return;
        }

        try {
            String nome = campoNome.getText().trim();
            String descricao = campoDescricao.getText().trim();
            double quantidade = campoQuantidade.getValue() != null ? Double.parseDouble(campoQuantidade.getValue().toString()) : 0;
            String unidade = campoUnidade.getText().trim();
            double preco = campoPreco.getValue() != null ? Double.parseDouble(campoPreco.getValue().toString()) : 0;

            // Verificação de nome duplicado
            if (produtoEmEdicao == null) {
                // Cadastro novo
                if (produtoDAO.produtoExiste(nome)) {
                    JOptionPane.showMessageDialog(this, "Já existe um produto cadastrado com este nome!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Produto novoProduto = new Produto(0, nome, descricao, quantidade, unidade, preco);
                produtoDAO.adicionarProduto(novoProduto);
                JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Edição
                if (!produtoEmEdicao.getNome().equalsIgnoreCase(nome) && produtoDAO.produtoExiste(nome)) {
                    JOptionPane.showMessageDialog(this, "Já existe um produto com este nome!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                produtoEmEdicao.setNome(nome);
                produtoEmEdicao.setDescricao(descricao);
                produtoEmEdicao.setQuantidade(quantidade);
                produtoEmEdicao.setUnidade(unidade);
                produtoEmEdicao.setPreco(preco);
                produtoDAO.atualizarProduto(produtoEmEdicao);
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valores numéricos inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
