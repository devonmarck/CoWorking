package view;

import javax.swing.JDialog;

import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import model.DAO;
import net.proteanit.sql.DbUtils;

import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import java.awt.Cursor;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

public class Funcionarios extends JDialog {
	private JTextField inputNome;
	private JTextField inputEmail;
	private JTextField inputLogin;
	private JPasswordField inputSenha;

	public Funcionarios() {
		setTitle("Funcionários");
		setResizable(false);
		setBounds(new Rectangle(300, 100, 614, 403));
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/img/logo.png")));
		getContentPane().setLayout(null);
		
		JLabel nomeFunc = new JLabel("Nome:");
		nomeFunc.setBounds(24, 58, 46, 14);
		getContentPane().add(nomeFunc);
		
		JLabel loginFunc = new JLabel("Login:");
		loginFunc.setBounds(24, 147, 46, 14);
		getContentPane().add(loginFunc);
		
		JLabel senhaFunc = new JLabel("Senha:");
		senhaFunc.setBounds(299, 147, 46, 14);
		getContentPane().add(senhaFunc);
		
		JLabel emailFunc = new JLabel("E-mail:");
		emailFunc.setBounds(299, 200, 46, 14);
		getContentPane().add(emailFunc);
		
		JLabel perfilFunc = new JLabel("Perfil:");
		perfilFunc.setBounds(24, 200, 46, 14);
		getContentPane().add(perfilFunc);
		
		inputNome = new JTextField();
		inputNome.setBounds(74, 55, 427, 20);
		getContentPane().add(inputNome);
		inputNome.setColumns(10);
		
		
		inputNome.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				buscarFuncionarioNaTabela();
			}
		});
		
		
		
		
		
		inputEmail = new JTextField();
		inputEmail.setColumns(10);
		inputEmail.setBounds(353, 197, 149, 20);
		getContentPane().add(inputEmail);
		
		inputLogin = new JTextField();
		inputLogin.setColumns(10);
		inputLogin.setBounds(74, 144, 200, 20);
		getContentPane().add(inputLogin);
		
		inputSenha = new JPasswordField();
		inputSenha.setBounds(353, 144, 149, 20);
		getContentPane().add(inputSenha);
		
		inputPerfil = new JComboBox();
		inputPerfil.setModel(new DefaultComboBoxModel(new String[] {"", "Administrador", "Gerencia", "Atendimento", "Suporte"}));
		inputPerfil.setBounds(74, 196, 200, 22);
		getContentPane().add(inputPerfil);
		
		JButton btnCreate = new JButton("");
		btnCreate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnCreate.setIcon(new ImageIcon(Funcionarios.class.getResource("/img/create.png")));
		btnCreate.setBounds(165, 288, 89, 56);
		getContentPane().add(btnCreate);
		
		getContentPane().add(btnCreate);
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adicionarFuncionario();	
			}
		});
		
		JButton btnUpdate = new JButton("");
		btnUpdate.setIcon(new ImageIcon(Funcionarios.class.getResource("/img/update.png")));
		btnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnUpdate.setBounds(287, 288, 89, 56);
		getContentPane().add(btnUpdate);
		
		JButton btnDelete = new JButton("");
		btnDelete.setIcon(new ImageIcon(Funcionarios.class.getResource("/img/delete.png")));
		btnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnDelete.setBounds(412, 288, 89, 56);
		getContentPane().add(btnDelete);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(74, 74, 427, 62);
		getContentPane().add(scrollPane);
		
		tblFuncionarios = new JTable();
		scrollPane.setViewportView(tblFuncionarios);

	}
	
	//Criar um objeto da classe DAO para estabelecer conexão com banco
	DAO dao = new DAO();
	private JComboBox inputPerfil;
	private JTable tblFuncionarios;
	
	private void adicionarFuncionario() {
		String create = "insert into funcionario (nomeFunc, login, senha,perfil, email) values (?, ?, md5(?), ?, ?);";
		
		
		try {
			// Estabelecer a conexão
			Connection conexaoBanco = dao.conectar();
			
			// Preparar a execusão do script SQL
			PreparedStatement executarSQL = conexaoBanco.prepareStatement(create);
			
			//Substituir os pontos de interrogação pelo conteúdo das caixas de texto (inputs)
			executarSQL.setString(1, inputNome.getText());
			executarSQL.setString(2, inputLogin.getText());
			executarSQL.setString(3, inputSenha.getText());
			

			executarSQL.setString(4, inputPerfil.getSelectedItem().toString());
			
			executarSQL.setString(5, inputEmail.getText());
			
			//Executar os comandos SQL e inserir o funcionamento no banco de dados
			executarSQL.executeUpdate();
			
			JOptionPane.showMessageDialog(null, "Usuario cadstrado com sucesso");
			
			
			
			limparCampos();
			conexaoBanco.close();
		}
		
		catch (SQLIntegrityConstraintViolationException error) {
			JOptionPane.showMessageDialog(null, "Login em uso. \nEscolha outro nome de usuário");
			limparCampos();
			
		}
		
		catch (Exception e) {
			System.out.println(e);
			
		}
		
	}
	
	
private void buscarFuncionarioNaTabela()	{
	String readTabela = "select idFuncionario as ID, nomeFunc as Nome, email as Email from funcionario"
	+ " where nomeFunc  like ?; ";		
	

	try {
				//Estabelecer a conexão
				Connection conexaoBanco = dao.conectar();
				
				//Preparar a execução dos comandos SQL
				PreparedStatement executarSQL = conexaoBanco.prepareStatement(readTabela);
				
				//Substituir pelo ? conteudo da caixa de texto
				executarSQL.setString(1, inputNome.getText() + "%");
				
				//Executar o comando SQL
				ResultSet resultadoExecucao = executarSQL.executeQuery();
				
				//Exibir o resultado na tabela, utilização da biblioteca rs2xml para "popular"
				//a tabela
				tblFuncionarios.setModel(DbUtils.resultSetToTableModel(resultadoExecucao));
				
				conexaoBanco.close();
				
			}
			
			catch (Exception e) {
				System.out.println(e);
	
		}
}
	
	
	
private void setarCaixasTexto() {
	//Criar uma variavel para receber a linha da tabela
	//int setarLinha = tblFuncionarios.getSelectedRow();
	
}
		
	private void limparCampos() {
		
		inputNome.setText(null);
		inputLogin.setText(null);
		inputSenha.setText(null);
		//inputPerfil.setSelectedItem(null);
		inputPerfil.setSelectedIndex(-1);
		inputEmail.setText(null);
		
	}	
	
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Funcionarios dialog = new Funcionarios();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
