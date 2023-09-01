package verificacaoPatrimonio;

import java.math.BigDecimal;

import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.wrapper.JapeFactory;

public class AlteraCampo {

	String msg;
	boolean update;
	
	public void alteraCampoControladoria(BigDecimal nuNota) {
		JdbcWrapper JDBC = JapeFactory.getEntityFacade().getJdbcWrapper();
		NativeSql nativeSql = new NativeSql(JDBC);
		JapeSession.SessionHandle hnd = JapeSession.open();

		try {
			
			update = nativeSql.executeUpdate("UPDATE TGFCAB SET AD_VERPATR = 'P', AD_ENVIPATR = 1 WHERE NUNOTA = " + nuNota);
			
			//AD_ENVIPATR, AD_VERPATR
			//AD_VERPATR lista de opções
			//AD_ENVPATRIMONIO inteiro

		} catch (Exception e) {
			e.printStackTrace();
			msg = "Erro na inclusao do item " + e.getMessage();
			System.out.println(msg);
		}

		JapeSession.close(hnd);
	}

	public void alteraCampoContabilidade(BigDecimal nuNota) {
		
		JdbcWrapper JDBC = JapeFactory.getEntityFacade().getJdbcWrapper();
		NativeSql nativeSql = new NativeSql(JDBC);
		JapeSession.SessionHandle hnd = JapeSession.open();

		try {
			
			 update = nativeSql.executeUpdate("UPDATE TGFCAB SET AD_VERPATR = 'A', AD_ENVIPATR = 2 WHERE NUNOTA = " + nuNota);
			

		} catch (Exception e) {
			e.printStackTrace();
			msg = "Erro na inclusao do item " + e.getMessage();
			System.out.println(msg);
		}

		JapeSession.close(hnd);
	}
}
