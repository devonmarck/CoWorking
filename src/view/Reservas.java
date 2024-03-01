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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;
import model.DAO;
import net.proteanit.sql.DbUtils;

public class Reservas extends JDialog {

	public JButton imgCreate;
	public JButton imgUpdate;
	public JButton imgDelete;
	public JButton btnVisualizarSalas;

	public Reservas() {
		setTitle("Reservas");
		setResizable(false);
		setBounds(new Rectangle(300, 100, 866, 800));
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/img/logo.png")));
		getContentPane().setLayout(null);

		JLabel tipoSala = new JLabel("Categoria:");
		tipoSala.setBounds(22, 256, 74, 14);
		getContentPane().add(tipoSala);

		JLabel andarSala = new JLabel("Andar:");
		andarSala.setBounds(22, 387, 57, 14);
		getContentPane().add(andarSala);

		JLabel numSala = new JLabel("Número:");
		numSala.setBounds(22, 339, 74, 14);
		getContentPane().add(numSala);

		imgCreate = new JButton("");
		imgCreate.setEnabled(false);
		imgCreate.setBackground(new Color(240, 240, 240));
		imgCreate.setBorderPainted(false);
		imgCreate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		imgCreate.setIcon(new ImageIcon(Reservas.class.getResource("/img/create.png")));
		imgCreate.setBounds(691, 363, 65, 54);
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
		imgUpdate.setBounds(673, 520, 65, 54);
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
		imgDelete.setBounds(766, 520, 65, 54);
		getContentPane().add(imgDelete);

		imgDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deletarReserva();
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 447, 738, 90);
		getContentPane().add(scrollPane);

		tblSalasReservadas = new JTable();
		scrollPane.setViewportView(tblSalasReservadas);

		inputID = new JTextField();
		inputID.setEnabled(false);
		inputID.setBounds(22, 296, 40, 20);
		getContentPane().add(inputID);
		inputID.setColumns(10);

		// --> DEIXAR INVISÍVEL
		inputID.setVisible(true);

		inputCategoria = new JTextField();
		inputCategoria.setEditable(false);
		inputCategoria.setBounds(93, 252, 160, 22);
		getContentPane().add(inputCategoria);

		inputAndar = new JTextField();
		inputAndar.setBounds(93, 383, 160, 22);
		inputAndar.setEditable(false);
		getContentPane().add(inputAndar);

		inputNum = new JTextField();
		inputNum.setBounds(93, 336, 160, 20);
		getContentPane().add(inputNum);
		inputNum.setColumns(10);

		inputNum.setEditable(false);

		responsavelReserva = new JLabel("Responsável:");
		responsavelReserva.setBounds(354, 339, 86, 14);
		getContentPane().add(responsavelReserva);

		inputResponsavel = new JTextField();
		inputResponsavel.setEditable(false);
		inputResponsavel.setBounds(454, 336, 155, 20);
		getContentPane().add(inputResponsavel);
		inputResponsavel.setColumns(10);

		tblSalasReservadas.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setarCaixasTextoReservasExistentes();
				imgUpdate.setEnabled(true);
				imgDelete.setEnabled(true);
				inputResponsavel.setEditable(true);
			}
		});

		inputInicioReserva = new JDateChooser();
		inputInicioReserva.setBounds(196, 48, 155, 20);
		getContentPane().add(inputInicioReserva);

		inputFimReserva = new JDateChooser();
		inputFimReserva.setBounds(493, 48, 155, 20);
		getContentPane().add(inputFimReserva);

		inicioReserva = new JLabel("Início da reserva:");
		inicioReserva.setBounds(96, 52, 101, 14);
		getContentPane().add(inicioReserva);

		fimReserva = new JLabel("Fim da reserva:");
		fimReserva.setBounds(393, 52, 101, 14);
		getContentPane().add(fimReserva);

		btnVisualizarSalas = new JButton("");
		btnVisualizarSalas.setBackground(new Color(240, 240, 240));
		btnVisualizarSalas.setBorderPainted(false);
		btnVisualizarSalas.setIcon(new ImageIcon(Reservas.class.getResource("/img/search.png")));
		btnVisualizarSalas.setBounds(658, 30, 57, 54);
		getContentPane().add(btnVisualizarSalas);

		btnVisualizarSalas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnVisualizarSalasDisponiveis();
				btnVisualizarSalasReservadas();
			}
		});

		JScrollPane scrollPaneSalasDisponiveis = new JScrollPane();
		scrollPaneSalasDisponiveis.setBounds(93, 94, 667, 133);
		getContentPane().add(scrollPaneSalasDisponiveis);

		tblSalasDisponiveis = new JTable();
		scrollPaneSalasDisponiveis.setViewportView(tblSalasDisponiveis);

		inputIDReserva = new JTextField();
		inputIDReserva.setEnabled(false);
		inputIDReserva.setBounds(22, 554, 40, 20);
		getContentPane().add(inputIDReserva);
		inputIDReserva.setColumns(10);

		// --> DEIXAR INVISÍVEL
		inputIDReserva.setVisible(true);

		tblSalasDisponiveis.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setarCaixasTextoParaNovaReserva();
				imgCreate.setEnabled(true);
				inputResponsavel.setEditable(true);
			}
		});

	}

	DAO dao = new DAO();
	private JTable tblSalasReservadas;
	private JTextField inputID;
	private JTextField inputCategoria;
	private JTextField inputAndar;
	private JTextField inputNum;
	private JLabel responsavelReserva;
	private JTextField inputResponsavel;
	private JDateChooser inputInicioReserva;
	private JDateChooser inputFimReserva;
	private JLabel inicioReserva;
	private JLabel fimReserva;
	private JTable tblSalasDisponiveis;
	private JTextField inputIDReserva;

	private void btnVisualizarSalasDisponiveis() {
		String query = "select idSala, tipoSala as Categoria, andarSala as Andar, numeroSala as Número from salas where"
				+ " emReforma = 'Não' and not exists (select 1 from reservas where salas.idSala = reservas.idSala"
				+ " and (? between inicioReserva and fimReserva or ? between inicioReserva and fimReserva));";

		try {

			if (inputInicioReserva.getDate() == null || inputFimReserva.getDate() == null) {
				JOptionPane.showMessageDialog(null, "Por favor, selecione datas de início e fim da reserva.");
				return;
			}

			Connection conexaoBanco = dao.conectar();
			PreparedStatement executarSQL = conexaoBanco.prepareStatement(query);

			java.sql.Timestamp inicioTimestamp = new java.sql.Timestamp(inputInicioReserva.getDate().getTime());
			java.sql.Timestamp fimTimestamp = new java.sql.Timestamp(inputFimReserva.getDate().getTime());

			executarSQL.setTimestamp(1, inicioTimestamp);
			executarSQL.setTimestamp(2, fimTimestamp);

			ResultSet resultadoExecucao = executarSQL.executeQuery();
			tblSalasDisponiveis.setModel(DbUtils.resultSetToTableModel(resultadoExecucao));
			conexaoBanco.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setarCaixasTextoParaNovaReserva() {
		int setarLinha = tblSalasDisponiveis.getSelectedRow();
		inputID.setText(tblSalasDisponiveis.getModel().getValueAt(setarLinha, 0).toString());
		inputCategoria.setText(tblSalasDisponiveis.getModel().getValueAt(setarLinha, 1).toString());
		inputAndar.setText(tblSalasDisponiveis.getModel().getValueAt(setarLinha, 2).toString());
		inputNum.setText(tblSalasDisponiveis.getModel().getValueAt(setarLinha, 3).toString());

	}

	private void adicionarReserva() {
		String create = "insert into reservas (idSala, responsavelReserva, inicioReserva, fimReserva) VALUES (?, ?, ?, ?);";

		if (inputResponsavel.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Responsável pela reserva é obrigatório!");
			inputResponsavel.requestFocus();
		} else {

			try {
				Connection conexaoBanco = dao.conectar();
				PreparedStatement executarSQL = conexaoBanco.prepareStatement(create);

				executarSQL.setInt(1, Integer.parseInt(inputID.getText()));
				executarSQL.setString(2, inputResponsavel.getText());
				executarSQL.setTimestamp(3, new java.sql.Timestamp(inputInicioReserva.getDate().getTime()));
				executarSQL.setTimestamp(4, new java.sql.Timestamp(inputFimReserva.getDate().getTime()));

				executarSQL.executeUpdate();
				JOptionPane.showMessageDialog(null, "Sala reservada com sucesso!");

				String query = "select idSala, tipoSala as Categoria, andarSala as Andar, numeroSala as Número from salas where"
						+ " emReforma = 'Não' and not exists (select 1 from reservas where salas.idSala = reservas.idSala"
						+ " and (? between inicioReserva and fimReserva or ? between inicioReserva and fimReserva));";

				PreparedStatement atualizarTabelaSalasDisponiveis = conexaoBanco.prepareStatement(query);

				java.sql.Timestamp inicioTimestamp1 = new java.sql.Timestamp(inputInicioReserva.getDate().getTime());
				java.sql.Timestamp fimTimestamp1 = new java.sql.Timestamp(inputFimReserva.getDate().getTime());

				atualizarTabelaSalasDisponiveis.setTimestamp(1, inicioTimestamp1);
				atualizarTabelaSalasDisponiveis.setTimestamp(2, fimTimestamp1);

				ResultSet resultadoExecucaoSalasDisponiveis = atualizarTabelaSalasDisponiveis.executeQuery();
				tblSalasDisponiveis.setModel(DbUtils.resultSetToTableModel(resultadoExecucaoSalasDisponiveis));

				String query2 = "SELECT r.idReserva, s.tipoSala AS Categoria, s.andarSala AS Andar, s.numeroSala AS Número, r.responsavelReserva AS Responsável "
						+ "FROM salas s " + "INNER JOIN reservas r ON s.idSala = r.idSala "
						+ "WHERE s.emReforma = 'Não' AND (? BETWEEN r.inicioReserva AND r.fimReserva OR ? BETWEEN r.inicioReserva AND r.fimReserva);";

				PreparedStatement atualizarTabelaSalasReservadas = conexaoBanco.prepareStatement(query2);

				Timestamp inicioTimestamp2 = new Timestamp(inputInicioReserva.getDate().getTime());
				Timestamp fimTimestamp2 = new Timestamp(inputFimReserva.getDate().getTime());

				atualizarTabelaSalasReservadas.setTimestamp(1, inicioTimestamp2);
				atualizarTabelaSalasReservadas.setTimestamp(2, fimTimestamp2);

				ResultSet resultadoExecucaoSalasReservadas = atualizarTabelaSalasReservadas.executeQuery();
				tblSalasReservadas.setModel(DbUtils.resultSetToTableModel(resultadoExecucaoSalasReservadas));

				conexaoBanco.close();
			}

			catch (SQLIntegrityConstraintViolationException error) {
				JOptionPane.showMessageDialog(null, "Sala já reservada");
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void btnVisualizarSalasReservadas() {
		String query = "SELECT r.idReserva, s.tipoSala AS Categoria, s.andarSala AS Andar, s.numeroSala AS Número, r.responsavelReserva AS Responsável "
				+ "FROM salas s " + "INNER JOIN reservas r ON s.idSala = r.idSala "
				+ "WHERE s.emReforma = 'Não' AND (? BETWEEN r.inicioReserva AND r.fimReserva OR ? BETWEEN r.inicioReserva AND r.fimReserva);";

		try {
			if (inputInicioReserva.getDate() == null || inputFimReserva.getDate() == null) {
				JOptionPane.showMessageDialog(null, "Por favor, selecione datas de início e fim da reserva.");
				return;
			}

			Connection conexaoBanco = dao.conectar();
			PreparedStatement executarSQL = conexaoBanco.prepareStatement(query);

			java.sql.Timestamp inicioTimestamp = new java.sql.Timestamp(inputInicioReserva.getDate().getTime());
			java.sql.Timestamp fimTimestamp = new java.sql.Timestamp(inputFimReserva.getDate().getTime());

			executarSQL.setTimestamp(1, inicioTimestamp);
			executarSQL.setTimestamp(2, fimTimestamp);

			ResultSet resultadoExecucao = executarSQL.executeQuery();
			tblSalasReservadas.setModel(DbUtils.resultSetToTableModel(resultadoExecucao));
			conexaoBanco.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setarCaixasTextoReservasExistentes() {
		int setarLinha = tblSalasReservadas.getSelectedRow();
		inputIDReserva.setText(tblSalasReservadas.getModel().getValueAt(setarLinha, 0).toString());
		inputCategoria.setText(tblSalasReservadas.getModel().getValueAt(setarLinha, 1).toString());
		inputAndar.setText(tblSalasReservadas.getModel().getValueAt(setarLinha, 2).toString());
		inputNum.setText(tblSalasReservadas.getModel().getValueAt(setarLinha, 3).toString());
		inputResponsavel.setText(tblSalasReservadas.getModel().getValueAt(setarLinha, 4).toString());

	}

	private void atualizarReserva() {
		// Adicione o código para atualizar a reserva no banco de dados
		String update = "update reservas set responsavelReserva = ?, inicioReserva = ?, fimReserva = ? where idReserva = ?;";

		try {
			Connection conexaoBanco = dao.conectar();
			PreparedStatement executarSQL = conexaoBanco.prepareStatement(update);
			executarSQL.setString(1, inputResponsavel.getText());
			executarSQL.setDate(2, new java.sql.Date(inputInicioReserva.getDate().getTime()));
			executarSQL.setDate(3, new java.sql.Date(inputFimReserva.getDate().getTime()));
			executarSQL.setInt(4, Integer.parseInt(inputIDReserva.getText()));
			executarSQL.executeUpdate();
			JOptionPane.showMessageDialog(null, "Reserva atualizada com sucesso!");

			String query = "SELECT r.idReserva, s.tipoSala AS Categoria, s.andarSala AS Andar, s.numeroSala AS Número, r.responsavelReserva AS Responsável "
					+ "FROM salas s " + "INNER JOIN reservas r ON s.idSala = r.idSala "
					+ "WHERE s.emReforma = 'Não' AND (? BETWEEN r.inicioReserva AND r.fimReserva OR ? BETWEEN r.inicioReserva AND r.fimReserva);";

			PreparedStatement atualizarTabela = conexaoBanco.prepareStatement(query);

			java.sql.Timestamp inicioTimestamp = new java.sql.Timestamp(inputInicioReserva.getDate().getTime());
			java.sql.Timestamp fimTimestamp = new java.sql.Timestamp(inputFimReserva.getDate().getTime());

			atualizarTabela.setTimestamp(1, inicioTimestamp);
			atualizarTabela.setTimestamp(2, fimTimestamp);

			ResultSet resultadoExecucao = atualizarTabela.executeQuery();
			tblSalasReservadas.setModel(DbUtils.resultSetToTableModel(resultadoExecucao));
			conexaoBanco.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deletarReserva() {
		// Adicione o código para deletar a reserva do banco de dados
		String delete = "delete from reservas where idReserva = ?;";

		try {
			Connection conexaoBanco = dao.conectar();
			PreparedStatement executarSQL = conexaoBanco.prepareStatement(delete);
			executarSQL.setInt(1, Integer.parseInt(inputIDReserva.getText()));
			executarSQL.executeUpdate();
			JOptionPane.showMessageDialog(null, "Reserva deletada com sucesso!");

			String query = "SELECT r.idReserva, s.tipoSala AS Categoria, s.andarSala AS Andar, s.numeroSala AS Número, r.responsavelReserva AS Responsável "
					+ "FROM salas s " + "INNER JOIN reservas r ON s.idSala = r.idSala "
					+ "WHERE s.emReforma = 'Não' AND (? BETWEEN r.inicioReserva AND r.fimReserva OR ? BETWEEN r.inicioReserva AND r.fimReserva);";

			PreparedStatement atualizarTabela = conexaoBanco.prepareStatement(query);

			java.sql.Timestamp inicioTimestamp = new java.sql.Timestamp(inputInicioReserva.getDate().getTime());
			java.sql.Timestamp fimTimestamp = new java.sql.Timestamp(inputFimReserva.getDate().getTime());

			atualizarTabela.setTimestamp(1, inicioTimestamp);
			atualizarTabela.setTimestamp(2, fimTimestamp);

			ResultSet resultadoExecucao = atualizarTabela.executeQuery();
			tblSalasReservadas.setModel(DbUtils.resultSetToTableModel(resultadoExecucao));
			conexaoBanco.close();

			conexaoBanco.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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