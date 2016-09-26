package controller;

import java.io.File;
import java.util.Vector;
import javax.swing.JOptionPane;

public class RecuperaMedia {
	private String usuarioLogado;
	
	public RecuperaMedia(String strUsuarioLogado) {
		usuarioLogado = strUsuarioLogado;		
	}
	
	@SuppressWarnings("unchecked")
	public int recupera() {
		DBDealer dealer = new DBDealer("VideocapClient", "AUDIENCIAS");		
		Vector ret = dealer.getAudienciasComErro();
		
		if (ret.size() > 0) {
			GravaLog logInfo = new GravaLog("INFO");
			GravaLog logErro = new GravaLog("ERRO");
			
			Object[] options = { "Sim", "Não" };
			String strMsg = "Foi detectado que pelo menos 1 arquivo de vídeo/áudio está corrompido devido a uma falha do sistema.\nSe este arquivo não for recuperado não poderá ser visualizado, exportado ou transmitido.\nDeseja recuperá-lo agora?";
        	int resposta = JOptionPane.showOptionDialog(null, strMsg, "Videocap - Recuperação de Audiências e Eventos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        	if (resposta == 0) {
        		for (int i=0; i < ret.size(); i++) {
        			String campos[] = (String[]) ret.elementAt(i);
        			String strBase = System.getProperty("user.dir") + "\\videos\\" + Sessao.fileName(campos[0], campos[1], campos[2]);
        			File f = new File(strBase + ".asf");
        			if (!f.exists()) {
        				logInfo.grava("fidelis.controller.RecuperaMedia()", usuarioLogado, "Arquivo " + strBase + ".asf não existe no sistema de arquivos.");
        				JOptionPane.showMessageDialog(null, "Arquivo " + strBase + ".asf não existe no sistema de arquivos.", "Atenção", JOptionPane.ERROR_MESSAGE);
        			}
        			else {
        				f.renameTo(new File(strBase + ".rec"));
        				
        				try {
        					
        					//String strCmd = System.getProperty("user.dir") + "/lib/recupera.bat \"" + strBase + ".rec\" " + "\"" + strBase + ".asf\"";
        					String strCmd = "cmd /c " + System.getProperty("user.dir") + "/lib/asfbin.exe -i \"" + strBase + ".rec\" -o " + "\"" + strBase + ".asf\" -q";
        					//Process p = Runtime.getRuntime().exec("cmd /c " + strCmd);        					
        					//p.waitFor();
        					Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + strCmd);
        					//Executa exe = new Executa(strCmd);
        					//exe.start();
        					//System.out.println("antes join");
        					//exe.join(0);
        					//System.out.println("depois join");
        					
        					long tamanho = 0;
        					long tamanhoAnterior = 0;
        					int stuck = 0;   
        					System.out.println("fidelis.RecuperaMedia(): Aguardando a recuperacao do arquivo " + strBase + ".asf");
        					logInfo.grava("fidelis.controller.RecuperaMedia()", usuarioLogado, "Aguardando a recuperacao do arquivo " + strBase + ".asf");
        					File fAsf = new File(strBase + ".asf");
        					while (!fAsf.exists()) {
        						try { Thread.sleep(100); } catch (InterruptedException e) { }
        					}
        					
        					while (stuck < 3) {
        						tamanho = fAsf.length();
        						if (tamanho != tamanhoAnterior)
        							tamanhoAnterior = tamanho;
        						else
        							stuck++;
        						try { Thread.sleep(100); } catch (InterruptedException e) { }
        					}        					
        					File fDel = new File(strBase + ".rec");
        					fDel.delete();
        					
        					//Runtime.getRuntime().exec("cmd /c " + strCmd);
        					
        					logInfo.grava("fidelis.controller.RecuperaMedia()", usuarioLogado, "Gerando HASH do arquivo " + strBase + ".asf");
        					
        					GeraHash gh = new GeraHash();
        					String strHash = gh.getHashFromFile(strBase + ".asf");
        					
        					DBDealer dealerHash = new DBDealer(usuarioLogado, "AUDIENCIAS", campos[0], campos[1], campos[2]);
        					dealerHash.gravarHash(strHash);
        					dealerHash.mudaStatus("-3");
							
						} catch (Exception e) {
							logErro.gravaExcep("fidelis.controller.RecuperaMedia()", e);
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, "Erro ao executar o utilitário de recuperação de media.\nVerifique a Log para mais detalhes.", "Atenção", JOptionPane.ERROR_MESSAGE);
						}
        			}
        		}
        		System.out.println("fidelis.RecuperaMedia(): Recuperacao das medias concluidas!");
        		return 1;
        	}
        	else
        		return -1; // Uusario nao quer recuperar
		}
		else
			return 0; // Nao existem arquivos para recuperar
	}
}
