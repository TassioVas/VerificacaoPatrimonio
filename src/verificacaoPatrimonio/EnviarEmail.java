package verificacaoPatrimonio;

import java.math.BigDecimal;
import java.sql.Timestamp;

import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.bmp.PersistentLocalEntity;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.vo.EntityVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class EnviarEmail {

	Timestamp agora = new Timestamp(System.currentTimeMillis());

	String msg;
	String corpoEmail;

	BigDecimal codFila = null;

	public void enviaEmail(int codGrupo, BigDecimal usuarioLogado, BigDecimal nuNota) {

		JdbcWrapper jdbc = JapeFactory.getEntityFacade().getJdbcWrapper();
		NativeSql nativeSql = new NativeSql(jdbc);

		EntityFacade dwfEntityFacade = EntityFacadeFactory.getDWFFacade();
		jdbc = dwfEntityFacade.getJdbcWrapper();
		JapeSession.SessionHandle hnd = JapeSession.open();

		if (codGrupo == 6) {
			corpoEmail = " <h2>CONTABILIDADE</h2>\r\n"
					+ "	<p><span style=\"font-size:14px\"><b>A NOTA DE NÚMERO ÚNICO DE ORIGEM :</b> "
					+ "	 <span style=\"font-size:16px\"><span style=\"color:#FF0000\"><b>" + nuNota
					+ "</span></span></span></b><span style=\"font-size:14px\">\r\n"
					+ "	 <b>FOI ENVIADO PELA AUDITORIA CONFORME SOLICITADO, PARA VERIFICAÇÃO DE PATRIMÔNIO. </b></span><span style=\"font-size:16px\">\r\n"
					+ "	 <p><span style=\"font-size:14px\"><b>APOS OS AJUSTES DEVERÁ SER ENVIADA DE VOLTA PARA A CONTROLADORIA ATRAVES DO \r\n"
					+ "		BOTÃO DE AÇÃO, CLICANDO NO \"RAIO\" SELECIONANDO A OPÇÃO <span style=\"color:#FF0000\"><b></b>\"nome do botão aqui\"</span></b>, .</b></span><br/>\r\n"
					+ "		 <p><span style=\"font-size:14px\"><b>NÃO RESPONDA ESTE E-MAIL!<br></b></span><span style=\"color:#696969\"><span style=\"font-size:12px\">\r\n"
					+ "	\r\n" + "		<p><strong>ATENCIOSAMENTE</strong></p>\r\n" + " ";
		} else {
			corpoEmail = "<h2>AUDITORIA</h2>\r\n"
					+ "	<p><span style=\"font-size:14px\"><b>A NOTA DO NÚMERO ÚNICO DE ORIGEM :</b> "
					+ "	 <span style=\"font-size:16px\"><span style=\"color:#FF0000\"><b>" + nuNota
					+ "</span></span></span></b><span style=\"font-size:14px\">\r\n"
					+ "	 <b>APOS VERIFICAÇÃO DE PATRIMÔNIO DA CONTABILIDADE, FOI DEVOLVIDA. </b></span><span style=\"font-size:16px\">\r\n"
					+ "	\r\n"
					+ "		 <p><span style=\"font-size:14px\"><b>NÃO RESPONDA ESTE E-MAIL!<br></b></span><span style=\"color:#696969\"><span style=\"font-size:12px\">\r\n"
					+ "	\r\n" + "		<p><strong>ATENCIOSAMENTE</strong></p>";
		}
		char[] corpoEmailchar = corpoEmail.toCharArray();

		try {

			DynamicVO filaMensagemVO = (DynamicVO) dwfEntityFacade.getDefaultValueObjectInstance("MSDFilaMensagem");
			filaMensagemVO.setProperty("CODMSG", null);
			filaMensagemVO.setProperty("DTENTRADA", agora);
			filaMensagemVO.setProperty("STATUS", "Pendente");
			filaMensagemVO.setProperty("CODCON", new BigDecimal(0));
			filaMensagemVO.setProperty("TENTENVIO", new BigDecimal(0));
			filaMensagemVO.setProperty("MENSAGEM", corpoEmailchar);
			filaMensagemVO.setProperty("TIPOENVIO", "E");
			filaMensagemVO.setProperty("MAXTENTENVIO", new BigDecimal(3));
			filaMensagemVO.setProperty("ASSUNTO", "Verificação De Patrimonio");
			if (codGrupo == 6) {
				filaMensagemVO.setProperty("EMAIL", "contabilidade@faepu.org.br");
				// filaMensagemVO.setProperty("EMAIL", "tassio@faepu.org.br");
			} else {
				filaMensagemVO.setProperty("EMAIL", "auditoria@faepu.org.br");
				// filaMensagemVO.setProperty("EMAIL", "t.santos.vasconcelos@gmail.com");
			}
			// filaMensagemVO.setProperty("CODSMTP", new BigDecimal(2));
			filaMensagemVO.setProperty("CODUSUREMET", usuarioLogado);

			PersistentLocalEntity createFilaMensagem = dwfEntityFacade.createEntity("MSDFilaMensagem",
					(EntityVO) filaMensagemVO);
			filaMensagemVO = (DynamicVO) createFilaMensagem.getValueObject();
			codFila = filaMensagemVO.asBigDecimal("CODFILA");

		} catch (Exception e) {
			e.printStackTrace();
			msg = "Erro na inclusao do item " + e.getMessage();
			System.out.println(msg);
		}

		JapeSession.close(hnd);
	}

}
