package view;

import javax.swing.JDialog;

import java.awt.EventQueue;

import java.awt.Rectangle;

import java.awt.Toolkit;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.swing.JLabel;

import javax.swing.JOptionPane;

import javax.swing.JTextField;

import javax.swing.SingleSelectionModel;

import model.DAO;

import net.proteanit.sql.DbUtils;

import javax.swing.JPasswordField;

import javax.swing.ImageIcon;

import java.awt.Cursor;

import javax.swing.JComboBox;

import javax.swing.JButton;

import java.awt.event.ActionListener;

import java.awt.event.KeyAdapter;

import java.awt.event.KeyEvent;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import java.nio.file.attribute.AclEntry;

import java.awt.event.ActionEvent;

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

		setBounds(100, 100, 624, 437);

		// setBounds(550,250,564,395);

		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/img/logo.png")));

		getContentPane().setLayout(null);

		JLabel nomeFunc = new JLabel("Nome:");

		nomeFunc.setBounds(24, 58, 46, 14);

		getContentPane().add(nomeFunc);

		JLabel loginFunc = new JLabel("Login:");

		loginFunc.setBounds(24, 244, 46, 14);

		getContentPane().add(loginFunc);

		JLabel senhaFunc = new JLabel("Senha:");

		senhaFunc.setBounds(312, 244, 46, 14);

		getContentPane().add(senhaFunc);

		JLabel emailFunc = new JLabel("E-mail:");

		emailFunc.setBounds(312, 276, 46, 14);

		getContentPane().add(emailFunc);

		JLabel perfilFunc = new JLabel("Perfil:");

		perfilFunc.setBounds(24, 276, 46, 14);

		getContentPane().add(perfilFunc);

		inputNome = new JTextField();

		inputNome.setBounds(74, 55, 494, 20);

		getContentPane().add(inputNome);

		inputNome.setColumns(10);

		inputNome.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {

				buscarFuncionarioNaTabela();

			}

		});

		inputEmail = new JTextField();

		inputEmail.setColumns(10);

		inputEmail.setBounds(368, 273, 200, 20);

		getContentPane().add(inputEmail);

		inputLogin = new JTextField();

		inputLogin.setColumns(10);

		inputLogin.setBounds(74, 241, 200, 20);

		getContentPane().add(inputLogin);

		inputSenha = new JPasswordField();

		inputSenha.setBounds(368, 241, 200, 20);

		getContentPane().add(inputSenha);

		inputPerfil = new JComboBox();

		inputPerfil.setModel(

				new DefaultComboBoxModel(new String[] { " ", "Administrador", "Gerência", "Atendimento", "Suporte" }));

		inputPerfil.setBounds(74, 272, 106, 22);

		getContentPane().add(inputPerfil);

		btnCreate = new JButton("");

		btnCreate.setContentAreaFilled(false);

		btnCreate.setBorder(null);

		btnCreate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		btnCreate.setIcon(new ImageIcon(Funcionarios.class.getResource("/img/create.png")));

		btnCreate.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				adicionarFuncionario();

			}

		});

		btnCreate.setBounds(312, 328, 92, 59);

		getContentPane().add(btnCreate);

		btnUpdate = new JButton("");

		btnUpdate.setContentAreaFilled(false);

		btnUpdate.setBorder(null);

		btnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		btnUpdate.setIcon(new ImageIcon(Funcionarios.class.getResource("/img/update.png")));

		btnUpdate.setBounds(414, 328, 83, 59);

		getContentPane().add(btnUpdate);

		btnUpdate.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				UpdateFuncionario();

			}

		});

		btnDelete = new JButton("");

		btnDelete.setContentAreaFilled(false);

		btnDelete.setBorder(null);

		btnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		btnDelete.setIcon(new ImageIcon(Funcionarios.class.getResource("/img/delete.png")));

		btnDelete.setBounds(507, 328, 83, 59);

		getContentPane().add(btnDelete);

		btnDelete.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				deletarFuncionario();

			}

		});

		JScrollPane scrollPane = new JScrollPane();

		scrollPane.setBounds(74, 73, 494, 87);

		getContentPane().add(scrollPane);

		tblFuncionarios = new JTable();

		scrollPane.setViewportView(tblFuncionarios);

		tblFuncionarios.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				setarCaixasTexto();

			}

		});

		btnPesquisar = new JButton("");

		btnPesquisar.setContentAreaFilled(false);

		btnPesquisar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		btnPesquisar.setIcon(new ImageIcon(Funcionarios.class.getResource("/img/search.png")));

		btnPesquisar.setBorder(null);

		btnPesquisar.setBounds(144, 193, 34, 20);

		getContentPane().add(btnPesquisar);

		inputID = new JTextField();

		inputID.setEnabled(false);

		inputID.setBounds(74, 193, 60, 20);

		getContentPane().add(inputID);

		inputID.setColumns(10);

		JLabel idFunc = new JLabel("ID :");

		idFunc.setBounds(24, 196, 29, 14);

		getContentPane().add(idFunc);

		btnPesquisar.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				btnBuscarFuncionario();

			}

		});

	}

	DAO dao = new DAO();

	private JComboBox inputPerfil;

	public JButton btnCreate;

	public JButton btnUpdate;

	public JButton btnDelete;

	private JTable tblFuncionarios;

	private JButton btnPesquisar;

	public JTextField inputID;

	private void adicionarFuncionario() {

		String create = "insert into funcionario (nomeFunc, login, senha, Perfil, email) values (?, ?, md5(?), ?, ?)";

		if (inputNome.getText().isEmpty()) {

			JOptionPane.showMessageDialog(null, "Nome do usuário Obrigatório!");

		}

		else if (inputLogin.getText().isEmpty()) {

			JOptionPane.showMessageDialog(null, "Login de usuário Obrigatório!");

		}

		else if (inputSenha.getPassword().length == 0) {

			JOptionPane.showMessageDialog(null, "Senha do usuário Obrigatório!");

		}

		else if (inputEmail.getText().isEmpty()) {

			JOptionPane.showMessageDialog(null, "E-mail do usuário Obrigatório!");

		}

		else {

			try {

				// Estabelecer a conexao

				Connection conexaoBanco = dao.conectar();

				PreparedStatement executarSQL = conexaoBanco.prepareStatement(create);

				// Substituir os pontos de interrogação pelo conteudo das caixas de texto

				// (inputs)

				executarSQL.setString(1, inputNome.getText());

				executarSQL.setString(2, inputLogin.getText());

				executarSQL.setString(3, inputSenha.getText());

				executarSQL.setString(4, inputPerfil.getSelectedItem().toString());

				executarSQL.setString(5, inputEmail.getText());

				// Executar os comandos SQL e inserir o funcionario no banco de dados

				executarSQL.executeUpdate();

				JOptionPane.showMessageDialog(null, "Usuario cadastrado com sucesso");

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

	}

	private void buscarFuncionarioNaTabela() {

		String readTabela = "select idFuncionario as ID, nomeFunc as Nome, email as Email from funcionario where nomeFunc like ?;";

		try {

			// Estabelecer a conexão

			Connection conexaoBanco = dao.conectar();

			// Preparar a execução dos comandos SQL

			PreparedStatement executarSQL = conexaoBanco.prepareStatement(readTabela);

			// Substituir pelo ? conteudo da caixa de texto

			executarSQL.setString(1, inputNome.getText() + "%");

			// Executar o comando SQL

			ResultSet resultadoExecucao = executarSQL.executeQuery();

			// Exibir o resultado na tabela, utilização da biblioteca rs2xml para "popular"

			// a tabela

			tblFuncionarios.setModel(DbUtils.resultSetToTableModel(resultadoExecucao));

			conexaoBanco.close();

		}

		catch (Exception e) {

			System.out.println(e);

		}

	}

	private void setarCaixasTexto() {

		// Criar uma variavel para receber a linha da tabela

		int setarLinha = tblFuncionarios.getSelectedRow();

		inputNome.setText(tblFuncionarios.getModel().getValueAt(setarLinha, 1).toString());

		inputID.setText(tblFuncionarios.getModel().getValueAt(setarLinha, 0).toString());

	}

	private void btnBuscarFuncionario() {

		String readBtn = "select * from funcionario where idFuncionario = ?;";

		try {

			Connection conexaoBanco = dao.conectar();

			PreparedStatement executarSQL = conexaoBanco.prepareStatement(readBtn);

			executarSQL.setString(1, inputID.getText());

			// Executar o comando SQL e exibir o resultado no fomulario funcionario (todos

			// os seus dados)

			ResultSet resultadoExecucao = executarSQL.executeQuery();

			if (resultadoExecucao.next()) {

				// preencher os campos do formulario

				inputLogin.setText(resultadoExecucao.getString(3));

				inputSenha.setText(resultadoExecucao.getString(4));

				inputPerfil.setSelectedItem(resultadoExecucao.getString(5));

				inputEmail.setText(resultadoExecucao.getString(6));

			}

		}

		catch (Exception e) {

			System.out.println(e);

		}

	}

	private void limparCampos() {

		inputNome.setText(null);

		inputLogin.setText(null);

		inputSenha.setText(null);

		// inputPerfil.setSelectedItem(null);

		inputPerfil.setSelectedIndex(-1);

		inputEmail.setText(null);

		inputNome.requestFocus();

	}

	private void UpdateFuncionario() {

		String updateBtn = "update funcionario set nomeFunc = ?, login = ?, senha = md5(?), perfil = ?, email = ? where idFuncionario = ?;";

		if (inputNome.getText().isEmpty()) {

			JOptionPane.showMessageDialog(null, "Nome do usuário Obrigatório!");

		}

		else if (inputLogin.getText().isEmpty()) {

			JOptionPane.showMessageDialog(null, "Login de usuário Obrigatório!");

		}

		else if (inputSenha.getPassword().length == 0) {

			JOptionPane.showMessageDialog(null, "Senha do usuário Obrigatório!");

		}

		else if (inputEmail.getText().isEmpty()) {

			JOptionPane.showMessageDialog(null, "E-mail do usuário Obrigatório!");

		}

		else {

			try {

				Connection conexaoBanco = dao.conectar();

				PreparedStatement executarSQL = conexaoBanco.prepareStatement(updateBtn);

				executarSQL.setString(1, inputNome.getText());

				executarSQL.setString(2, inputLogin.getText());

				executarSQL.setString(3, inputSenha.getText());

				executarSQL.setString(4, inputPerfil.getSelectedItem().toString());

				executarSQL.setString(5, inputEmail.getText());

				executarSQL.setString(6, inputID.getText());

				executarSQL.executeUpdate();

				JOptionPane.showMessageDialog(null, "Usuario atualizado com sucesso");

				limparCampos();

				conexaoBanco.close();

			}

			catch (SQLIntegrityConstraintViolationException error) {

				JOptionPane.showMessageDialog(null, "Login e/ou email em uso. \nEscolha novos dados.");

				limparCampos();

			}

			catch (Exception e) {

				System.out.println(e);

			}
		}

	}

	private void deletarFuncionario() {

		String delete = "delete from funcionario where idFuncionario = ?;";

		try {

			Connection conexaoBanco = dao.conectar();

			PreparedStatement executarSQL = conexaoBanco.prepareStatement(delete);

			executarSQL.setString(1, inputID.getText());

			executarSQL.executeUpdate();

			JOptionPane.showMessageDialog(null, "Usuário deletado com sucesso.");

			limparCampos();

			conexaoBanco.close();

		}

		catch (Exception e) {

			System.out.print(e);

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
