package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import controller.DBDealer;
import controller.GeraHash;
import controller.GravaLog;
import model.Usuario;

@SuppressWarnings("serial")
public class PanelGridManutencao extends JPanel {
	
	private String usuarioLogado = null;
	private JPopupMenu popup = null;
	private DefaultTableModel tablemodel = null;
	private JTable tbDados = null;
	TableCellRenderer passwordRenderer = new PasswordRenderer();
	private Usuario user;
	@SuppressWarnings("unchecked")
	private Vector nome_usuarios;
	
	@SuppressWarnings("unchecked")
	public PanelGridManutencao(String strUsuarioLogado) {
		usuarioLogado = strUsuarioLogado;		
		
		JPanel pCentro = new JPanel();	
		
		tablemodel = new DefaultTableModel(); 
		tablemodel.setColumnIdentifiers(new String[] {"Nome", "Senha", "Perfil"});
		
		tbDados = new JTable(tablemodel)
		{
			public TableCellRenderer getCellRenderer(int row, int column)
			{
				if (column == 1)
					return passwordRenderer;
				else
					return super.getCellRenderer(row, column);
			}			
		};
		tbDados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbDados.setModel(tablemodel);
		tbDados.setRowHeight(20);		
		
		final JPasswordField pfSenha = new JPasswordField();
		TableCellEditor editaSenha = new DefaultCellEditor(pfSenha);
		
		DBDealer dealer = new DBDealer(usuarioLogado, "PERFIL");
		Vector vetPerfil = new Vector();		
		vetPerfil = dealer.listarTudo();
		
		Vector vetPerfilMix = new Vector();		
		for (int i = 0; i < vetPerfil.size(); i++) {
			String [] strPerfil = (String[]) vetPerfil.elementAt(i);
			vetPerfilMix.addElement(strPerfil[0] + "-" + strPerfil[1]);		
		}
		
		final JComboBox cbPerfil = new JComboBox(vetPerfilMix);
		TableCellEditor editaPerfil = new DefaultCellEditor(cbPerfil);		
		
		javax.swing.table.TableColumnModel colmod = tbDados.getColumnModel();
		colmod.getColumn(1).setCellEditor(editaSenha);
		colmod.getColumn(2).setCellEditor(editaPerfil);
		
        JScrollPane sp = new JScrollPane(tbDados);        
        pCentro.add(sp);	
        
        user = new Usuario();
        Vector vtRetorno = user.listar();
        
        nome_usuarios = new Vector();
		String [] strRetorno = new String[2];
		for (int i = 0; i < vtRetorno.size(); i++) {
			strRetorno = (String[]) vtRetorno.elementAt(i);			
	        tablemodel.addRow(new String[] {null, null});
			tbDados.setValueAt(strRetorno[0], i, 0);
			tbDados.setValueAt(strRetorno[1], i, 1);
			tbDados.setValueAt(strRetorno[2], i, 2);
			nome_usuarios.addElement((String)strRetorno[0]);
		}
        
        popup = new JPopupMenu();
		ActionListener menuListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				if (event.getActionCommand().toString().equals("Incluir")) {
			        tablemodel.addRow(new String[] {null, null, null});
					tbDados.setValueAt("usuario" + tablemodel.getRowCount(), tablemodel.getRowCount()-1, 0);
					tbDados.setValueAt("usuario", tablemodel.getRowCount()-1, 1);
					tbDados.setValueAt(cbPerfil.getItemAt(0), tablemodel.getRowCount()-1, 2);
					user = new Usuario();
					if (user.incluir("usuario" + tablemodel.getRowCount(), "usuario", Integer.parseInt(cbPerfil.getItemAt(0).toString().substring(0,1)))) {
						nome_usuarios.addElement((String) "usuario" + tablemodel.getRowCount());
						GravaLog log = new GravaLog("INFO");
						log.grava("fidelis.view.PanelGridManutencao.incluir()", usuarioLogado, "Inclusão do usuário: " + "usuario" + tablemodel.getRowCount() + " realizada com sucesso.");
					}
					else {
						GravaLog log = new GravaLog("INFO");
						log.grava("fidelis.view.PanelGridManutencao.incluir()", usuarioLogado, "Erro ao incluir usuário: " + "usuario" + tablemodel.getRowCount());
						tablemodel.removeRow(tablemodel.getRowCount()-1);
						JOptionPane.showMessageDialog(null, "Erro ao incluir usuário. Verifique a existencia de outro usuário de nome: usuario" + (tablemodel.getRowCount()+1), "fidelis.view.PanelGridManutencao()", JOptionPane.ERROR_MESSAGE);
					}
				}
				
				if (event.getActionCommand().toString().equals("Excluir")) {
					user = new Usuario();
					String userName = (String) tbDados.getValueAt(tbDados.getSelectedRow(), 0);
					if (user.excluir((String) tbDados.getValueAt(tbDados.getSelectedRow(), 0))) {
						nome_usuarios.removeElementAt(tbDados.getSelectedRow());
						tablemodel.removeRow(tbDados.getSelectedRow());
						GravaLog log = new GravaLog("INFO");
						log.grava("fidelis.view.PanelGridManutencao.excluir()", usuarioLogado, "Exclusão do usuário: " + userName + " realizada com sucesso.");
					}
					else {
						GravaLog log = new GravaLog("INFO");
						log.grava("fidelis.view.PanelGridManutencao.excluir()", usuarioLogado, "Erro ao excluir usuário: " + userName);
						JOptionPane.showMessageDialog(null, "Erro ao excluir usuário [" + userName + "].", "fidelis.view.PanelGridManutencao()", JOptionPane.ERROR_MESSAGE);
					}
				}
				
				if (event.getActionCommand().toString().equals("Atualizar")) {					
					// Verifica se a senha eh nula.
					if (((String)tbDados.getValueAt(tbDados.getSelectedRow(), 1)).equals("")) {
						JOptionPane.showMessageDialog(null, "O campo SENHA deve ser preenchido!", "fidelis.view.PanelGridManutencao()", JOptionPane.WARNING_MESSAGE);
					}
					else {
						boolean flagOk[] = new boolean[3];
						user = new Usuario();
						GravaLog log = new GravaLog("INFO");
						
						String strChave = (String) nome_usuarios.elementAt(tbDados.getSelectedRow());
						flagOk[0] = user.alterar(1, strChave, (String) tbDados.getValueAt(tbDados.getSelectedRow(), 0));						
						log.grava("fidelis.view.PanelGridManutencao.excluir()", usuarioLogado, "Alteração da chave do usuário: [" + strChave + "] -> [" + (String) tbDados.getValueAt(tbDados.getSelectedRow(), 0) + "]");
						nome_usuarios.setElementAt((String) tbDados.getValueAt(tbDados.getSelectedRow(), 0), tbDados.getSelectedRow());
						
						// verifica se a senha estah criptografada no formato hash, se estiver, a senha
						// nao eh alterada.
						flagOk[1] = true;
						strChave = (String) nome_usuarios.elementAt(tbDados.getSelectedRow());
						if (((String)tbDados.getValueAt(tbDados.getSelectedRow(), 1)).length() < 40) {
							user = new Usuario();
							GeraHash gh = new GeraHash();
							String senhaCript = gh.getHashFromString((String) tbDados.getValueAt(tbDados.getSelectedRow(), 1));
							flagOk[1] = user.alterar(2, strChave, senhaCript);
							log.grava("fidelis.view.PanelGridManutencao.excluir()", usuarioLogado, "Alteração da senha do usuário: [" + strChave + "]");
						}
						
						user = new Usuario();
						strChave = (String) nome_usuarios.elementAt(tbDados.getSelectedRow());
						flagOk[2] = user.alterar(3, strChave, ((String)tbDados.getValueAt(tbDados.getSelectedRow(), 2)).substring(0,1));
						log.grava("fidelis.view.PanelGridManutencao.excluir()", usuarioLogado, "Alteração do perfil do usuário: [" + strChave + "]");
						
						if (flagOk[0] && flagOk[1] && flagOk[2]) {							
							JOptionPane.showMessageDialog(null, "Dados do usuário [" + strChave + "] foram alterados com sucesso!", "fidelis.view.PanelGridManutencao()", JOptionPane.INFORMATION_MESSAGE);
						}
						else {
							JOptionPane.showMessageDialog(null, "Erro ao atualizar dados do usuário [" + strChave + "]. Favor verificar log de erro!", "fidelis.view.PanelGridManutencao()", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		};

		JMenuItem item = new JMenuItem();
		popup.add(item = new JMenuItem("Incluir"));
	    item.addActionListener(menuListener);
	    popup.add(item = new JMenuItem("Excluir"));
	    item.addActionListener(menuListener);
	    popup.addSeparator();
	    popup.add(item = new JMenuItem("Atualizar"));
	    item.addActionListener(menuListener);
	    
	    
	    tbDados.addMouseListener(new MouseAdapter() {
	    	public void mousePressed(MouseEvent e) {
	    		checkPopup(e);
		    }
		    public void mouseClicked(MouseEvent e) {
		    	checkPopup(e);
		    }
		    public void mouseReleased(MouseEvent e) {
		    	checkPopup(e);
		    }
		    private void checkPopup(MouseEvent e) {
		    	if (e.isPopupTrigger()) {
		    		popup.show(e.getComponent(), e.getX(), e.getY());
		    	}
		    }
	    });

	    sp.addMouseListener(new MouseAdapter() {
	    	public void mousePressed(MouseEvent e) {
	    		checkPopup(e);
		    }
		    public void mouseClicked(MouseEvent e) {
		    	checkPopup(e);
		    }
		    public void mouseReleased(MouseEvent e) {
		    	checkPopup(e);
		    }
		    private void checkPopup(MouseEvent e) {
		    	if (e.isPopupTrigger()) {
		    		popup.show(e.getComponent(), e.getX(), e.getY());
		    	}
		    }
	    });
		
		this.setLayout(new BorderLayout());
		this.add(pCentro, BorderLayout.CENTER);		
	}

	class PasswordRenderer extends DefaultTableCellRenderer	{
		protected void setValue(Object value)
		{
			setText( "********" );
		}
	}
}
