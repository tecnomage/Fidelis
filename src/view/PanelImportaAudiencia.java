package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.DBDealer;
import controller.GeraHash;
import controller.ImportarAudiencia;
import controller.Sessao;
import controller.XMLControl;

@SuppressWarnings("serial")
public class PanelImportaAudiencia extends JPanel {
	
	public String usuarioLogado = null;
	private JButton btImportar = null;
	private JButton btSelecionar = null;
	private PanelDadosAudiencia pDados= null;
	private XMLControl xml = null;
	
	public PanelImportaAudiencia(String strUsuarioLogado) {
		usuarioLogado = strUsuarioLogado;
		
		JPanel borda = new JPanel();
		borda.setLayout(null);
		borda.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados da Audiência: "));
		borda.setBounds(8, 8, 670, 200);
				
		btSelecionar = new JButton("Procurar");
		btSelecionar.setBounds(20, 135, 120, 25);
		
		btImportar = new JButton("Importar");
		btImportar.setBounds(150, 135, 120, 25);
		btImportar.setEnabled(false);
		
		
		btSelecionar.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {	
				selecao(event);
			}		
		});
		
		btImportar.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {	
				importar(event);
			}		
		});
		
		Sessao.numProcesso = "";
		Sessao.data = "";
		Sessao.hora = "";
		Sessao.tipo = "";
		Sessao.juiz = "";
		
		pDados = new PanelDadosAudiencia();
		pDados.setDados();
		pDados.setLocation(20, 30);
		
		borda.add(pDados);
		borda.add(btSelecionar);
		borda.add(btImportar);
		
		this.setLayout(null);
		this.add(borda);		
	}
	
	public void setNumProcesso() {
		if (Sessao.numProcesso != null) {
			btImportar.setEnabled(true);
			pDados.setDados();
		}
		return;
	}
	
	private void selecao(java.awt.event.ActionEvent evt) {
		
		JFileChooser fc = new JFileChooser();
	    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);		        
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos XML", "xml");      
	    fc.addChoosableFileFilter(filter);      
	    fc.setAcceptAllFileFilterUsed(false);  
	    
	    int res = fc.showOpenDialog(null);
	    
	    if (res == JFileChooser.APPROVE_OPTION) {        	
	    	File file = fc.getSelectedFile();
	    	
	    	xml = new XMLControl(usuarioLogado, file);
	    	xml.getDadosProcesso(file);
	    	xml.getDadosMarcacao(file);
	    	
	    	pDados.lbNumProcessoVlr.setText(xml.numProcesso);
	    	pDados.lbTipoProcessoVlr.setText(xml.tipo);
	    	pDados.lbJuizVlr.setText(xml.juiz);
	    	pDados.lbDataVlr.setText(xml.data);
	    	pDados.lbHoraVlr.setText(xml.hora);
	    	
	    	btImportar.setEnabled(true);	    	
	    }
		
	}
	
	private void importar(java.awt.event.ActionEvent evt) {
		
		ImportarAudiencia ia = new ImportarAudiencia(usuarioLogado, xml.numProcesso, xml.data, xml.hora);
		if (ia.getCodTipoAudiencia(xml.tipo) < 1) {
			JOptionPane.showMessageDialog(null, "O TIPO DE AUDIÊNCIA (" + xml.tipo + ") não está cadastrado no banco de dados desta estação.\nEfetue o cadastro deste TIPO DE AUDIÊNCIA antes de importá-la.", "fidelis.view.PanelImportaAudiencia.importar()", JOptionPane.WARNING_MESSAGE);
		}
		else {
			DBDealer dealer = new DBDealer(usuarioLogado, "AUDIENCIAS", xml.numProcesso, xml.data, xml.hora);
			if (dealer.getAudiencia() != null) {
				JOptionPane.showMessageDialog(null, "Esta AUDIÊNCIA já está cadastrada no banco de dados desta estação.\nNão é possível continuar com a importação!", "fidelis.view.PanelImportaAudiencia.importar()", JOptionPane.ERROR_MESSAGE);
			} else {
				String strVideoFile = xml.getFile().getPath().replace(".xml", ".asf");
				File videoFile = new File(strVideoFile);
				if (!videoFile.exists()) {
					JOptionPane.showMessageDialog(null, "Arquivo de vídeo (" + strVideoFile + ") NÃO encontrado.\nNão é possível continuar com a importação!", "fidelis.view.PanelImportaAudiencia.importar()", JOptionPane.ERROR_MESSAGE);
				}
				else {
					GeraHash gh = new GeraHash();
					if (!xml.hashvideo.equals(gh.getHashFromFile(strVideoFile))) {
						JOptionPane.showMessageDialog(null, "O código de segurança HASH NÃO confere com os dados do arquivo de vídeo.\nNão é possível continuar com a importação!", "fidelis.view.PanelImportaAudiencia.importar()", JOptionPane.ERROR_MESSAGE);
					}
					else {
		        		if (ia.copiaVideo(videoFile)) {	
			        		dealer.incluir(xml.tipo, xml.juiz);
			        		dealer.mudaStatus("CONCLUIDO");
			        		dealer.gravarHash(gh.getHashFromFile(System.getProperty("user.dir") + "/videos/" + Sessao.fileName(xml.numProcesso, xml.data, xml.hora) + ".asf"));
			        		
			        		DBDealer dealerMarcacao = new DBDealer(usuarioLogado, "MARCACOES", xml.numProcesso, xml.data, xml.hora);
			        		//DBDealer dealerTemas = new DBDealer(usuarioLogado, "TEMAS", xml.numProcesso);
			        		//DBDealer dealerQualif = new DBDealer(usuarioLogado, "QUALIFICACOES", xml.numProcesso);
			    	    	for (int i=0; i<xml.time.length; i++) {
			    	    		dealerMarcacao.incluir(xml.time[i], xml.tema[i], xml.subtema[i], xml.qualificacao[i], xml.depoente[i]);
			    	    		//dealerTemas.incluir(xml.tema[i], "null");
			    	    		//dealerTemas.incluir(xml.subtema[i], xml.tema[i]);
			    	    		//dealerQualif.incluir(xml.qualificacao[i], "null");
			    	    		//dealerQualif.incluir(xml.depoente[i], xml.qualificacao[i]);
			    	    	}			        		
			        		JOptionPane.showMessageDialog(null, "Audiência Importada com Sucesso!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
		        		}
		        		else
		        			JOptionPane.showMessageDialog(null, "Não foi possível copiar o arquivo de vídeo.\nNão é possível continuar com a importação!", "Atenção", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
		
	}

}
