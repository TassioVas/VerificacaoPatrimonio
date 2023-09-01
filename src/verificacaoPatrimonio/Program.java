package verificacaoPatrimonio;

import java.math.BigDecimal;
import java.sql.ResultSet;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.modelcore.auth.AuthenticationInfo;
import br.com.sankhya.ws.ServiceContext;

public class Program implements AcaoRotinaJava {
	
	BigDecimal nuNota;
	int codGrupo;
	
	String nomeUsu;

	@Override
	public void doAction(ContextoAcao ctx) throws Exception {
		
		JdbcWrapper JDBC = JapeFactory.getEntityFacade().getJdbcWrapper();
		NativeSql nativeSql = new NativeSql(JDBC);
		JapeSession.SessionHandle hnd = JapeSession.open();
		
		Historico his = new Historico();
		AlteraCampo alcamp = new AlteraCampo();
		EnviarEmail email = new EnviarEmail();
		
		BigDecimal usuarioLogado = ((AuthenticationInfo) ServiceContext.getCurrent().getAutentication()).getUserID();
		
		
		for (int i = 0; i < (ctx.getLinhas()).length ; i++ ) {
			Registro linha = ctx.getLinhas()[i];
			nuNota = (BigDecimal) linha.getCampo("NUNOTA");
		}
		
		ResultSet codG = nativeSql.executeQuery("SELECT NOMEUSU, CODGRUPO FROM TSIUSU WHERE CODUSU = " + usuarioLogado);
		
		while (codG.next()) {
			nomeUsu = codG.getString("NOMEUSU");
			codGrupo = codG.getInt("CODGRUPO");
		}
		
		//if (codGrupo != 4 || codGrupo != 6) {
		//	ctx.setMensagemRetorno("Voce nao tem permissão para usar essa rotina, somente grupos de usuario 4 e 6 ");
		//	return;
		//}
		
		email.enviaEmail(codGrupo, usuarioLogado, nuNota);
		
		if (codGrupo == 4 ) {
			alcamp.alteraCampoContabilidade(nuNota);
		} else {
			alcamp.alteraCampoControladoria(nuNota);
		}
		
		his.insereHistorico(nomeUsu, nuNota);
		
	    ctx.setMensagemRetorno("Enviado com sucesso!"
	    		+ "");
		
		
		JapeSession.close(hnd);
		
	}

}
