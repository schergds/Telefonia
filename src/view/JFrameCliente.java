package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import model.Sessao;
import rmi.ServicoSessao;

public class JFrameCliente extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JFrameCliente.class.getName());

    public JFrameCliente() {
        initComponents();
        jPanelPesquisa.setVisible(false);
    }

    private void initComponents() {
        jTextFieldServidor = new JTextField("localhost", 20);
        jButtonConectar = new JButton("Conectar");
        jTextFieldNumeroCelularPesquisa = new JTextField(20);
        jButtonPesquisar = new JButton("Pesquisar");
        jTextAreaResultadoPesquisa = new JTextArea(10, 50);
        jTextAreaResultadoPesquisa.setEditable(false);
        jTextAreaResultadoPesquisa.setLineWrap(true);
        jTextAreaResultadoPesquisa.setWrapStyleWord(true);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sessões em Telefonia - Cliente");

        JPanel jPanelConexao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jPanelConexao.add(new JLabel("Servidor"));
        jPanelConexao.add(jTextFieldServidor);
        jPanelConexao.add(jButtonConectar);

        JPanel jPanelCampo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jPanelCampo.add(new JLabel("Número celular para pesquisa"));
        jPanelCampo.add(jTextFieldNumeroCelularPesquisa);
        jPanelCampo.add(jButtonPesquisar);

        jPanelPesquisa = new JPanel(new BorderLayout());
        jPanelPesquisa.add(jPanelCampo, BorderLayout.NORTH);
        jPanelPesquisa.add(new JScrollPane(jTextAreaResultadoPesquisa), BorderLayout.CENTER);

        JPanel jPanelPrincipal = new JPanel(new BorderLayout());
        jPanelPrincipal.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        jPanelPrincipal.add(jPanelConexao, BorderLayout.NORTH);
        jPanelPrincipal.add(jPanelPesquisa, BorderLayout.CENTER);
        setContentPane(jPanelPrincipal);

        jButtonConectar.addActionListener(this::jButtonConectarActionPerformed);
        jButtonPesquisar.addActionListener(this::jButtonPesquisarActionPerformed);
        jTextFieldNumeroCelularPesquisa.addActionListener(this::jButtonPesquisarActionPerformed);

        pack();
        setLocationRelativeTo(null);
    }

    private void jButtonConectarActionPerformed(java.awt.event.ActionEvent evt) {
        String host = jTextFieldServidor.getText().trim();
        if (host.equals("")) {
            JOptionPane.showMessageDialog(this, "Informe o endereço do servidor!");
            jTextFieldServidor.requestFocus();
            return;
        }
        try {
            Registry registro = LocateRegistry.getRegistry(host, ServicoSessao.PORTA);
            servico = (ServicoSessao) registro.lookup(ServicoSessao.NOME);
            setTitle("Sessões em Telefonia - Cliente conectado a " + host);
            jPanelPesquisa.setVisible(true);
            pack();
            jTextFieldNumeroCelularPesquisa.requestFocus();
        } catch (RemoteException | NotBoundException e) {
            servico = null;
            jPanelPesquisa.setVisible(false);
            JOptionPane.showMessageDialog(this, "Não foi possível conectar ao servidor: " + e.getMessage());
        }
    }

    private void jButtonPesquisarActionPerformed(java.awt.event.ActionEvent evt) {
        String numero = jTextFieldNumeroCelularPesquisa.getText().trim();
        if (numero.equals("")) {
            JOptionPane.showMessageDialog(this, "Número de celular não informado");
            jTextFieldNumeroCelularPesquisa.requestFocus();
            return;
        }
        try {
            Sessao sessao = servico.buscarSessao(numero);
            if (sessao == null) {
                JOptionPane.showMessageDialog(this, "Número de celular não encontrado");
                jTextAreaResultadoPesquisa.setText("");
                jTextFieldNumeroCelularPesquisa.selectAll();
                jTextFieldNumeroCelularPesquisa.requestFocus();
                return;
            }
            jTextAreaResultadoPesquisa.setText(sessao.toString());
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erro na comunicação com o servidor: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new JFrameCliente().setVisible(true));
    }

    private ServicoSessao servico;
    private JButton jButtonConectar;
    private JButton jButtonPesquisar;
    private JPanel jPanelPesquisa;
    private JTextArea jTextAreaResultadoPesquisa;
    private JTextField jTextFieldNumeroCelularPesquisa;
    private JTextField jTextFieldServidor;
}
