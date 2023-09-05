package verificacaoPatrimonio;

import java.math.BigDecimal;

import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.util.DynamicEntityNames;

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
			 
			// JapeWrapper finDAO = JapeFactory.dao(DynamicEntityNames.FINANCEIRO);
				// finDAO.prepareToUpdateByPK(nuFin).set("PROVISAO", "S").set("AD_DEVFIN", new
				// BigDecimal("1")).update();
			// JapeWrapper cieDao = JapeFactory.dao(DynamicEntityNames.CABECALHO_NOTA);
			 //cieDao.prepareToUpdateByPK(nuNota).set("AD_ENVIPATR", "2").update();
			

		} catch (Exception e) {
			e.printStackTrace();
			msg = "Erro na inclusao do item " + e.getMessage();
			System.out.println(msg);
		}

		JapeSession.close(hnd);
	}
}
