package view;
 
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
 
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
 
import model.DAO;
import net.proteanit.sql.DbUtils;
 
//Importação da biblioteca responsavel pelo calendario
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDayChooser;
import com.toedter.calendar.JMonthChooser;
 
 
public class Reservas extends JDialog {
 
	public JButton imgCreate;
	public JButton imgUpdate;
	public JButton imgDelete;
 
	public Reservas() {
		setTitle("Reservas");
		setResizable(false);
		setBounds(new Rectangle(300, 100, 724, 446));
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/img/logo.png")));
		getContentPane().setLayout(null);
 
		JLabel tipoSala = new JLabel("Categoria:");
		tipoSala.setBounds(24, 29, 74, 14);
		getContentPane().add(tipoSala);
 
		JLabel andarSala = new JLabel("Andar:");
		andarSala.setBounds(24, 254, 57, 14);
		getContentPane().add(andarSala);
 
		JLabel numSala = new JLabel("Número:");
		numSala.setBounds(24, 203, 74, 14);
		getContentPane().add(numSala);
 
		imgCreate = new JButton("");
		imgCreate.setBackground(new Color(240, 240, 240));
		imgCreate.setBorderPainted(false);
		imgCreate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		imgCreate.setIcon(new ImageIcon(Reservas.class.getResource("/img/create.png")));
		imgCreate.setBounds(392, 342, 65, 54);
		getContentPane().add(imgCreate);
 
		imgCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adicionarReserva();
			}
		});
 
		imgUpdate = new JButton("");
		imgUpdate.setEnabled(false);
		imgUpdate.setBackground(new Color(240, 240, 240));
		imgUpdate.setBorderPainted(false);
		imgUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		imgUpdate.setIcon(new ImageIcon(Reservas.class.getResource("/img/update.png")));
		imgUpdate.setBounds(488, 342, 65, 54);
		getContentPane().add(imgUpdate);
 
		imgUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				atualizarReserva();
			}
		});
 
		imgDelete = new JButton("");
		imgDelete.setEnabled(false);
		imgDelete.setBackground(new Color(240, 240, 240));
		imgDelete.setBorderPainted(false);
		imgDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		imgDelete.setIcon(new ImageIcon(Reservas.class.getResource("/img/delete.png")));
		imgDelete.setBounds(581, 342, 65, 54);
		getContentPane().add(imgDelete);
 
		imgDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deletarReserva();
			}
		});
 
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(95, 54, 516, 90);
		getContentPane().add(scrollPane);
 
		tblReservas = new JTable();
		scrollPane.setViewportView(tblReservas);
 
		JButton btnPesquisar = new JButton("");
		btnPesquisar.setEnabled(false);
		btnPesquisar.setBackground(new Color(240, 240, 240));
		btnPesquisar.setBorderPainted(false);
		btnPesquisar.setIcon(new ImageIcon(Reservas.class.getResource("/img/search.png")));
		btnPesquisar.setBounds(265, 193, 43, 33);
		getContentPane().add(btnPesquisar);
 
		inputID = new JTextField();
		inputID.setEnabled(false);
		inputID.setBounds(24, 160, 40, 20);
		getContentPane().add(inputID);
		inputID.setColumns(10);
 
		// Deixar o campo ID invisível
		inputID.setVisible(false);
 
		inputCategoria = new JComboBox();
		inputCategoria.setToolTipText("");
		inputCategoria.setModel(new DefaultComboBoxModel(new String[] { "", "Sala de reunião", "Sala de conferência",
				"Espaço de eventos", "Escritório privado" }));
		inputCategoria.setBounds(95, 25, 516, 22);
		getContentPane().add(inputCategoria);
 
		inputCategoria.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscarSalaNaTabela();
			}
		});
 
		inputAndar = new JComboBox();
		inputAndar.setModel(
				new DefaultComboBoxModel(new String[] { "", "Subsolo", "Térreo", "1º andar", "2º andar", "3º andar" }));
		inputAndar.setBounds(95, 250, 160, 22);
		getContentPane().add(inputAndar);
 
		inputNum = new JTextField();
		inputNum.setBounds(95, 200, 160, 20);
		getContentPane().add(inputNum);
		inputNum.setColumns(10);
		
		ResponsavelReserva = new JLabel("Responsavel:");
		ResponsavelReserva.setBounds(369, 203, 74, 14);
		getContentPane().add(ResponsavelReserva);
		
		inputResponsavel = new JTextField();
		inputResponsavel.setBounds(451, 200, 86, 20);
		getContentPane().add(inputResponsavel);
		inputResponsavel.setColumns(10);
 
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnBuscarSala();
			}
		});
 
		tblReservas.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setarCaixasTexto();
				btnPesquisar.setEnabled(true);
				imgCreate.setEnabled(false);
				imgDelete.setEnabled(true);
			}
		});
 
		
		inputInicioReserva = new JDateChooser();
		inputInicioReserva.setBounds(441, 251, 137, 20);
		getContentPane().add(inputInicioReserva);
		
		
		inputFimReserva = new JDateChooser();
		inputFimReserva.setBounds(441, 294, 137, 20);
		getContentPane().add(inputFimReserva);		
		
		
		
		
	} // Fim do construtor
 
	// Criar um objeto da classe DAO para estabelecer conexão com banco
	DAO dao = new DAO();
	private JTable tblReservas;
	private JTextField inputID;
	private JComboBox inputCategoria;
	private JComboBox inputAndar;
	private JTextField inputNum;
	private JLabel ResponsavelReserva;
	private JTextField inputResponsavel;
    private JDatechooser inputInicioReserva;
    private JDatechooser inputFimReserva;
	
	private void adicionarReserva() {
		String create = "insert into reservas (idSala, responsavelReserva, inicioReserva, inicioReserva, fimReserva)"
				+ " values (?, ?, ?, ?, ?);";
 
		// Validação da Responsavel (tipo) da sala
		if (inputResponsavel.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Responsavel pela reserva é obrigatória!");
			inputCategoria.requestFocus();
		}
 
		// Validação do código da sala
	/*	else if (inputCod.getSelectedItem().equals("")) {
			JOptionPane.showMessageDialog(null, "Código da sala obrigatório!");
			inputCod.requestFocus();
		}
 
		// Validação do andar da sala
		else if (inputAndar.getSelectedItem().equals("")) {
			JOptionPane.showMessageDialog(null, "Andar da sala obrigatório!");
			inputAndar.requestFocus();
		}
 
		// Validação do número da sala
		else if (inputNum.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Número da sala obrigatório!");
			inputNum.requestFocus();
		}
 
		// Validação da ocupação máxima da sala
		else if (inputOcup.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Ocupação máxima obrigatória!");
			inputOcup.requestFocus();
		}*/
 
		else {
 
			try {
				// Estabelecer a conexão
				Connection conexaoBanco = dao.conectar();
 
				// Preparar a execusão do script SQL
				PreparedStatement executarSQL = conexaoBanco.prepareStatement(create);
 
				// Substituir os pontos de interrogação pelo conteúdo das caixas de texto
				// (inputs)
				executarSQL.setString(1, inputID.getText());
				executarSQL.setString(2, inputResponsavel.getText());
				
				//Formatar a data de reserva utilizando a biblioteca JCalendar para inserção CORRETA NO BANCO
				SimpleDateFormat formatadorReserva = new SimpleDateFormat("yyyymmdd");
				String dataInicioReservaFormatada = formatadorReserva.format(inputInicioReserva.getDate());
				executarSQL.setString(3, dataInicioReservaFormatada);
				
				SimpleDateFormat formatadorReserva2 = new SimpleDateFormat("yyyymmdd");
				String dataFimReservaFormatada = formatadorReserva.format(inputFimReserva.getDate());
				executarSQL.setString(4, dataFimReservaFormatada);
 
				
				// Executar os comandos SQL e inserir a sala no banco de dados
				executarSQL.executeUpdate();
 
				JOptionPane.showMessageDialog(null, "Sala reservada com sucesso!");
				limparCampos();
 
				//String readTabela = "select tipoSala as Categoria, andarSala as Andar, numeroSala as Número from reservas where tipoSala = ?;";
				//PreparedStatement executarReadSQL = conexaoBanco.prepareStatement(readTabela);
				//executarReadSQL.setString(1, inputCategoria.getSelectedItem().toString());
				//ResultSet resultadoExecucao = executarReadSQL.executeQuery();
				//tblReservas.setModel(DbUtils.resultSetToTableModel(resultadoExecucao));
 
				// ((DefaultTableModel) tblReservas.getModel()).setRowCount(0);
 
				conexaoBanco.close();
			}
 
			catch (SQLIntegrityConstraintViolationException error) {
				JOptionPane.showMessageDialog(null, "Sala já reservada");
			}
 
			catch (Exception e) {
				System.out.println(e);
			}
		}
	}
 
	private void buscarSalaNaTabela() {
		String readTabela = "select tipoSala as Categoria, andarSala as Andar, numeroSala as Número from reservas where tipoSala = ?;";
 
		try {
			// Estabelecer a conexão
			Connection conexaoBanco = dao.conectar();
 
			// Preparar a execução dos comandos SQL
			PreparedStatement executarSQL = conexaoBanco.prepareStatement(readTabela);
 
			// Substituir o ? pelo conteúdo da caixa de texto
			executarSQL.setString(1, inputCategoria.getSelectedItem().toString());
 
			// Executar o comando SQL
			ResultSet resultadoExecucao = executarSQL.executeQuery();
 
			// Exibir o resultado na tabela, utilização da biblioteca rs2xml para "popular"
			// a tabela
			tblReservas.setModel(DbUtils.resultSetToTableModel(resultadoExecucao));
 
			conexaoBanco.close();
		}
 
		catch (Exception e) {
			System.out.println(e);
		}
	}
 
	private void setarCaixasTexto() {
 
		// Criar uma variável para receber a linha da tabela
		int setarLinha = tblReservas.getSelectedRow();
 
		inputCategoria.setSelectedItem(tblReservas.getModel().getValueAt(setarLinha, 0).toString());
 
		// Setar o andar e o número da sala selecionada na linha específica da tabela
		// que o usuário clicou
		inputAndar.setSelectedItem(tblReservas.getModel().getValueAt(setarLinha, 1).toString());
 
		inputNum.setText(tblReservas.getModel().getValueAt(setarLinha, 2).toString());
	}
 
	// Criar método para buscar sala pelo botão Pesquisar
	private void btnBuscarSala() {
		String readBtn = "select * from Reservas where numeroSala = ? and andarSala = ?;";
 
		try {
			// Estabelecer a conexão
			Connection conexaoBanco = dao.conectar();
 
			// Preparar a execução do comando SQL
			PreparedStatement executarSQL = conexaoBanco.prepareStatement(readBtn);
 
			// Substituir o ponto de interrogação pelo conteúdo da caixa de texto (número da
			// sala)
			executarSQL.setString(1, inputNum.getText());
			executarSQL.setString(2, inputAndar.getSelectedItem().toString());
 
			// Executar o comando SQL e exibir o resultado no formulário Reservas (todos
			// os seus dados)
			ResultSet resultadoExecucao = executarSQL.executeQuery();
 
			if (resultadoExecucao.next()) {
				// Preencher os campos do formulário
				inputID.setText(resultadoExecucao.getString(1));
				// inputAndar.setSelectedItem(resultadoExecucao.getString(2));
				inputCod.setSelectedItem(resultadoExecucao.getString(5));
				inputOcup.setText(resultadoExecucao.getString(6));
 
				imgUpdate.setEnabled(true);
				imgDelete.setEnabled(true);
				imgCreate.setEnabled(false);
			}
 
			conexaoBanco.close();
		}
 
		catch (Exception e) {
			System.out.println(e);
		}
	}
 
	private void atualizarSala() {
		String update = "update Reservas set andarSala = ?, numeroSala = ?, tipoSala = ?, codigoSala = ?,"
				+ " ocupacaoSala = ? where idSala = ?;";
 
		// Validação da categoria (tipo) da sala
		if (inputCategoria.getSelectedItem().equals("")) {
			JOptionPane.showMessageDialog(null, "Categoria da sala obrigatória!");
			inputCategoria.requestFocus();
		}
 
		// Validação do código da sala
		else if (inputCod.getSelectedItem().equals("")) {
			JOptionPane.showMessageDialog(null, "Código da sala obrigatório!");
			inputCod.requestFocus();
		}
 
		// Validação do andar da sala
		else if (inputAndar.getSelectedItem().equals("")) {
			JOptionPane.showMessageDialog(null, "Andar da sala obrigatório!");
			inputAndar.requestFocus();
		}
 
		// Validação do número da sala
		else if (inputNum.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Número da sala obrigatório!");
			inputNum.requestFocus();
		}
 
		// Validação da ocupação máxima da sala
		else if (inputOcup.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Ocupação máxima obrigatória!");
			inputOcup.requestFocus();
		}
 
		else {
			try {
				// Estabelecer a conexão
				Connection conexaoBanco = dao.conectar();
 
				// Preparar a execusão do script SQL
				PreparedStatement executarSQL = conexaoBanco.prepareStatement(update);
 
				// Substituir os pontos de interrogação pelo conteúdo das caixas de texto
				// (inputs)
				executarSQL.setString(1, inputAndar.getSelectedItem().toString());
				executarSQL.setString(2, inputNum.getText());
				executarSQL.setString(3, inputCategoria.getSelectedItem().toString());
				executarSQL.setString(4, inputCod.getSelectedItem().toString());
				executarSQL.setString(5, inputOcup.getText());
				executarSQL.setString(6, inputID.getText());
 
				// Executar os comandos SQL e atualizar a sala no banco de dados
				executarSQL.executeUpdate();
 
				JOptionPane.showMessageDialog(null, "Dados da sala atualizados com sucesso!");
				limparCampos();
 
				String readTabela = "select tipoSala as Categoria, andarSala as Andar, numeroSala as Número from reservas where tipoSala = ?;";
				PreparedStatement executarReadSQL = conexaoBanco.prepareStatement(readTabela);
				executarReadSQL.setString(1, inputCategoria.getSelectedItem().toString());
				ResultSet resultadoExecucao = executarReadSQL.executeQuery();
				tblReservas.setModel(DbUtils.resultSetToTableModel(resultadoExecucao));
				
				conexaoBanco.close();
			}
 
			catch (SQLIntegrityConstraintViolationException error) {
				JOptionPane.showMessageDialog(null, "Ocorreu um erro. \nEsta sala já encontra-se cadastrada.");
			}
 
			catch (Exception e) {
				System.out.println(e);
			}
		}
	}
 
	private void deletarSala() {
		String delete = "delete from reservas where numeroSala = ? and andarSala = ?;";
 
		try {
			Connection conexaoBanco = dao.conectar();
 
			PreparedStatement executarSQL = conexaoBanco.prepareStatement(delete);
 
			executarSQL.setString(1, inputNum.getText());
			executarSQL.setString(2, inputAndar.getSelectedItem().toString());
 
			executarSQL.executeUpdate();
 
			JOptionPane.showMessageDialog(null, "Reserva deletada com sucesso!");
 
			limparCampos();
 
			DefaultTableModel designTabela = (DefaultTableModel) tblReservas.getModel();
 
			// Índice da linha que deseja excluir
			int posicaoLinha = 0;
 
			if (posicaoLinha >= 0 && posicaoLinha < designTabela.getRowCount()) {
				designTabela.removeRow(posicaoLinha);
			}
 
			conexaoBanco.close();
 
		} catch (Exception e) {
			System.out.println(e);
		}
	}
 
	private void limparCampos() {
 
		inputCod.setSelectedItem(null);
		inputAndar.setSelectedItem(null);
		inputNum.setText(null);
		inputOcup.setText(null);
		inputCategoria.requestFocus();
		imgCreate.setEnabled(true);
		imgDelete.setEnabled(false);
 
	}
 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Reservas dialog = new Reservas();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
tem menu de contexto
Redigir