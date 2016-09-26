package controller;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.BancodeDados;
import model.Configuracoes;

@SuppressWarnings("serial")
public class PanelRelatorios extends JPanel {

	private String usuarioLogado;

	public PanelRelatorios(String strUsuarioLogado) {
		usuarioLogado = strUsuarioLogado;

		JPanel pRange = new JPanel();
		pRange.setLayout(null);
		pRange.setBorder(javax.swing.BorderFactory.createTitledBorder("Composição"));
		pRange.setBounds(8, 5, 420, 225);

		JLabel lbDataDe = new JLabel("Período De:");
		lbDataDe.setBounds(10, 25, 100, 20);
		JLabel lbDataAte = new JLabel("Até:");
		lbDataAte.setBounds(240, 25, 100, 20);

		final JFormattedTextField dataDe = new JFormattedTextField();
		dataDe.setBounds(120, 25, 80, 20);
		final JFormattedTextField dataAte = new JFormattedTextField();
		dataAte.setBounds(300, 25, 80, 20);

		try {
			dataDe.setFormatterFactory(
					new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-##-####")));
			dataAte.setFormatterFactory(
					new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-##-####")));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		JLabel lbVara = new JLabel("Vara:");
		lbVara.setBounds(10, 55, 50, 20);
		final JTextField tfVara = new JTextField();
		tfVara.setBounds(120, 55, 80, 20);

		JLabel lbUsuario = new JLabel("Usuário:");
		lbUsuario.setBounds(10, 85, 50, 20);
		final JTextField tfUsuario = new JTextField();
		tfUsuario.setBounds(120, 85, 80, 20);

		JLabel lbMensagem = new JLabel("Mensagem:");
		lbMensagem.setBounds(10, 115, 80, 20);
		final JTextField tfMensagem = new JTextField();
		tfMensagem.setBounds(120, 115, 255, 20);

		JLabel lbRelatorios = new JLabel("Relatórios:");
		lbRelatorios.setBounds(10, 145, 80, 20);

		final JComboBox cbRelatorios = new JComboBox();
		cbRelatorios.addItem("Registro Log");
		cbRelatorios.addItem("Audiências Concluídas");
		cbRelatorios.addItem("Audiências Transmitidas");
		cbRelatorios.setBounds(120, 145, 180, 20);

		JButton btGerar = new JButton("Gerar Relatório");
		btGerar.setBounds(120, 175, 150, 25);
		btGerar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				gravaPDF(
						gerarRelatorio(dataDe.getText(), dataAte.getText(), tfVara.getText(), tfUsuario.getText(),
								tfMensagem.getText(), (String) cbRelatorios.getSelectedItem()),
						(String) cbRelatorios.getSelectedItem());
			}
		});

		pRange.add(lbDataDe);
		pRange.add(lbDataAte);
		pRange.add(dataDe);
		pRange.add(dataAte);
		pRange.add(lbVara);
		pRange.add(tfVara);
		pRange.add(lbUsuario);
		pRange.add(tfUsuario);
		pRange.add(lbMensagem);
		pRange.add(tfMensagem);
		pRange.add(lbRelatorios);
		pRange.add(cbRelatorios);
		pRange.add(btGerar);

		this.setLayout(null);
		this.add(pRange);
	}

	@SuppressWarnings("unchecked")
	private Vector gerarRelatorio(String dataDe, String dataAte, String vara, String usuario, String mensagem,
			String relatorio) {
		Vector ret = null;

		if (dataDe.equals("  -  -    ")) {
			JOptionPane.showMessageDialog(null, "Informe a data inicial.", "Atenção", JOptionPane.WARNING_MESSAGE);
			return null;
		}

		if (dataAte.equals("  -  -    ")) {
			JOptionPane.showMessageDialog(null, "Informe a data final.", "Atenção", JOptionPane.WARNING_MESSAGE);
			return null;
		}

		GravaLog loginfo = new GravaLog("INFO");
		loginfo.grava("fidelis.view.PanelRelatorios.gerarRelatorio()", usuarioLogado,
				"Solicitou o relatório: " + relatorio + ". Período " + dataDe + " até " + dataAte);

		dataDe += " 00:00:00";
		dataAte += " 23:59:59";
		String sql = "SELECT * FROM vc_log WHERE to_date(data, 'DD-MM-YYYY HH24:MI:SS') >= to_date('" + dataDe
				+ "', 'DD-MM-YYYY HH24:MI:SS') and to_date(data, 'DD-MM-YYYY HH24:MI:SS') <= to_date('" + dataAte
				+ "', 'DD-MM-YYYY HH24:MI:SS')";

		if (!vara.equals("")) {
			sql += " AND vara like '%" + vara + "%'";
		}

		if (!usuario.equals("")) {
			sql += " AND usuario like '%" + usuario + "%'";
		}

		if (relatorio.equals("Audiências Concluídas")) {
			sql += " AND mensagem LIKE '001-%'";
		}

		if (relatorio.equals("Audiências Transmitidas")) {
			sql += " AND mensagem LIKE '003-%'";
		}

		if (relatorio.equals("Registro Log")) {
			sql += " AND mensagem LIKE '%" + mensagem + "%'";
		}

		BancodeDados db = new BancodeDados();
		Connection con = db.getConexao();
		ret = new Vector();

		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				String[] strRet = new String[6];
				strRet[0] = rs.getString("data");
				strRet[1] = rs.getString("classe");
				strRet[2] = rs.getString("usuario");
				strRet[3] = rs.getString("mensagem");
				strRet[4] = rs.getString("tipo");
				strRet[5] = rs.getString("vara");
				ret.addElement(strRet);
			}
			rs.close();
			st.close();

			return ret;

		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.view.PanelRelatorios.gerarRelatorio()", e);
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean gravaPDF(Vector vtRet, String nomeRelatorio) {
		String pdfFile = null;

		if (vtRet == null) {
			return false;
		}

		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Abrir");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int res = fc.showOpenDialog(null);

		if (res == JFileChooser.APPROVE_OPTION) {
			File diretorio = fc.getSelectedFile();
			pdfFile = diretorio.getPath() + "/Relatorio de " + nomeRelatorio + ".pdf";
		}

		try {
			com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A4);
			PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

			document.open();

			Configuracoes cfg = new Configuracoes();
			String strCabFile = cfg.getConfigParm("cabecalho");
			File fCab = new File(strCabFile);

			if (fCab.exists()) {
				Image iHdr = Image.getInstance(strCabFile);
				iHdr.scalePercent(54);
				iHdr.setAbsolutePosition(30, 780);
				document.add(iHdr);
			}

			document.add(new Paragraph(" ", new Font(Font.HELVETICA, 10, Font.BOLD)));
			document.add(new Paragraph(" ", new Font(Font.HELVETICA, 10, Font.BOLD)));
			document.add(new Paragraph("Relatório de " + nomeRelatorio, new Font(Font.HELVETICA, 18, Font.BOLD)));
			document.add(new Paragraph(" ", new Font(Font.HELVETICA, 10, Font.BOLD)));

			PdfPTable table = new PdfPTable(4);
			// table.setHorizontalAlignment(1);
			table.setWidthPercentage(100);
			table.addCell(new Phrase("Data", new Font(Font.HELVETICA, 10, Font.BOLD)));
			table.addCell(new Phrase("Vara", new Font(Font.HELVETICA, 10, Font.BOLD)));
			table.addCell(new Phrase("Usuário", new Font(Font.HELVETICA, 10, Font.BOLD)));
			table.addCell(new Phrase("Mensagem", new Font(Font.HELVETICA, 10, Font.BOLD)));

			for (int i = 0; i < vtRet.size(); i++) {
				String[] strRet = (String[]) vtRet.elementAt(i);
				table.addCell(new Phrase(strRet[0], new Font(Font.HELVETICA, 8, Font.NORMAL)));
				table.addCell(new Phrase(strRet[5], new Font(Font.HELVETICA, 8, Font.NORMAL)));
				table.addCell(new Phrase(strRet[2], new Font(Font.HELVETICA, 8, Font.NORMAL)));
				table.addCell(new Phrase(strRet[3], new Font(Font.HELVETICA, 8, Font.NORMAL)));
			}

			document.add(table);

			String strRodFile = cfg.getConfigParm("rodape");
			File fRod = new File(strRodFile);

			if (fRod.exists()) {
				Image iRod = Image.getInstance(strRodFile);
				iRod.scalePercent(53);
				iRod.setAbsolutePosition(30, 30);
				document.add(iRod);
			}

			document.close();

		} catch (DocumentException de) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.view.PanelRelatorios.gravaPDF()", de);
			de.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Ocorreu um erro ao gravar o arquivo PDF.\nContacte o Administrador para análise da LOG.",
					"Atenção", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (FileNotFoundException fnf) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.view.PanelRelatorios.gravaPDF()", fnf);
			fnf.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Ocorreu um erro ao gravar o arquivo PDF.\nContacte o Administrador para análise da LOG.",
					"Atenção", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (Exception e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.view.PanelRelatorios.gravaPDF()", e);
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Ocorreu um erro ao gravar o arquivo PDF.\nContacte o Administrador para análise da LOG.",
					"Atenção", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		JOptionPane.showMessageDialog(null, "Relatório Gerado com SUCESSO.", "Atenção",
				JOptionPane.INFORMATION_MESSAGE);
		return true;
	}

}
