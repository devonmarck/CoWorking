package view;

import javax.swing.JDialog;

import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JLabel;
import javax.swing.JTextField;

import model.DAO;

import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import java.awt.Cursor;
import javax.swing.JComboBox;

public class Funcionarios extends JDialog {
	private JTextField inputNome;
	private JTextField inputEmail;
	private JTextField inputLogin;
	private JPasswordField inputSenha;
	private JLabel imgCreate;
	private JLabel imgUpdate;
	private JLabel imgDelete;

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
		loginFunc.setBounds(24, 127, 46, 14);
		getContentPane().add(loginFunc);
		
		JLabel senhaFunc = new JLabel("Senha:");
		senhaFunc.setBounds(299, 127, 46, 14);
		getContentPane().add(senhaFunc);
		
		JLabel emailFunc = new JLabel("E-mail:");
		emailFunc.setBounds(299, 200, 46, 14);
		getContentPane().add(emailFunc);
		
		JLabel perfilFunc = new JLabel("Perfil:");
		perfilFunc.setBounds(24, 200, 46, 14);
		getContentPane().add(perfilFunc);
		
		inputNome = new JTextField();
		inputNome.setBounds(74, 55, 479, 20);
		getContentPane().add(inputNome);
		inputNome.setColumns(10);
		
		inputEmail = new JTextField();
		inputEmail.setColumns(10);
		inputEmail.setBounds(353, 197, 200, 20);
		getContentPane().add(inputEmail);
		
		inputLogin = new JTextField();
		inputLogin.setColumns(10);
		inputLogin.setBounds(74, 124, 200, 20);
		getContentPane().add(inputLogin);
		
		inputSenha = new JPasswordField();
		inputSenha.setBounds(353, 124, 200, 20);
		getContentPane().add(inputSenha);
		
		imgCreate = new JLabel("");
		imgCreate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		imgCreate.setIcon(new ImageIcon(Funcionarios.class.getResource("/img/create.png")));
		imgCreate.setBounds(304, 290, 65, 54);
		getContentPane().add(imgCreate);
		
		imgUpdate = new JLabel("");
		imgUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		imgUpdate.setIcon(new ImageIcon(Funcionarios.class.getResource("/img/update.png")));
		imgUpdate.setBounds(398, 290, 65, 54);
		getContentPane().add(imgUpdate);
		
		imgDelete = new JLabel("");
		imgDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		imgDelete.setIcon(new ImageIcon(Funcionarios.class.getResource("/img/delete.png")));
		imgDelete.setBounds(488, 290, 65, 54);
		getContentPane().add(imgDelete);
		
		inputPerfil = new JComboBox();
		inputPerfil.setBounds(74, 196, 200, 22);
		getContentPane().add(inputPerfil);

	}
	
	//Criar um objeto da classe DAO para estabelecer conexão com banco
	DAO dao = new DAO();
	private JComboBox inputPerfil;
	
	private void adicionarFuncionario() {
		String create = "insert into funcionario (nomeFunc, login, senha, perfil, email) values (?, ?, md5(?), ?, ?);";
		
		
		try {
			// Estabelecer a conexão
			Connection conexaoBanco = dao.conectar();
			
			// Preparar a execusão do script SQL
			PreparedStatement executarSQL = conexaoBanco.prepareStatement(create);
			
			//Substituir os pontos de interrogação pelo conteúdo das caixas de texto (inputs)
			executarSQL.setString(1, inputNome.getText());
			executarSQL.setString(2, inputLogin.getText());
			executarSQL.setString(3, inputSenha.getText());
			//Trocar o componente do perfil 
			executarSQL.setString(5, inputEmail.getText());
		} 
		
		catch (Exception e) {
						
		}
		
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
