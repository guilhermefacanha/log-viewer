package br.com.gfsolucoesti.business;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.com.gfsolucoesti.exception.SystemBusinessException;
import br.com.gfsolucoesti.utils.GFUtils;

/**
 * @author gfsolucoesti Classe responsavel por controlar os metodos comunicacao do sistema atraves de envio de emails
 */
public class EmailBusiness implements Serializable {

    private static final long serialVersionUID = 3858199227854362790L;

    /**
     * Classe de resource de mail session. Referencia recurso no web.xml
     */
//    @Resource(name = "mail/sisgia")
    private Session mailSession;

    public void testEmail() throws SystemBusinessException {
        try {
            Message message = createNewMessage();
            InternetAddress to[] = new InternetAddress[1];
            to[0] = new InternetAddress("guilhermefacanha@gmail.com");
            message.setRecipients(Message.RecipientType.TO, to);
            message.setSubject("[SISGIA] Email Teste");
            message.setContent("Aviso de movimentação de cláusula", "text/plain");
            Transport.send(message);
        } catch (AddressException e) {
            throw new SystemBusinessException("Erro ao enviar email: " + e.getMessage() + ". Favor contatar administrador do sistema!");
        } catch (MessagingException e) {
            throw new SystemBusinessException("Erro ao enviar email: " + e.getMessage() + ". Favor contatar administrador do sistema!");
        }

    }

    public void enviarEmailMovimentacaoClausula(List<String> destinatarios, String email) throws SystemBusinessException {
        try {
            Message message = createNewMessage();
            InternetAddress to[] = createRecipients(destinatarios);
            message.setRecipients(Message.RecipientType.TO, to);
            message.setSubject("[SISGIA] Aviso de movimentação de cláusula");
            message.setContent(email, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (AddressException e) {
            throw new SystemBusinessException("Erro ao enviar email: " + e.getMessage() + ". Favor contatar administrador do sistema!");
        } catch (MessagingException e) {
            throw new SystemBusinessException("Erro ao enviar email: " + e.getMessage() + ". Favor contatar administrador do sistema!");
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new SystemBusinessException("Erro ao enviar email: " + e.getMessage() + ". Favor contatar administrador do sistema!");
        }
    }

    public void enviarEmailSenhaRecuperada(String nome, String email, String novaSenha) throws SystemBusinessException {
        try {
            Message message = createNewMessage();
            InternetAddress to[] = createRecipients(Arrays.asList(email));
            message.setRecipients(Message.RecipientType.TO, to);
            message.setSubject("[SISGIA] Recuperação de Senha");
            message.setContent("Olá, foi solicitado uma recuperação de senha de acesso ao SISGIA. <br/>Favor acessar o link: "
                    + GFUtils.getContext() + "/recuperarsenha.jsf?cod=" + novaSenha, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (AddressException e) {
            throw new SystemBusinessException("Erro ao enviar email: " + e.getMessage() + ". Favor contatar administrador do sistema!");
        } catch (MessagingException e) {
            throw new SystemBusinessException("Erro ao enviar email: " + e.getMessage() + ". Favor contatar administrador do sistema!");
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new SystemBusinessException("Erro ao enviar email: " + e.getMessage() + ". Favor contatar administrador do sistema!");
        }
    }

    public void enviarEmailNovoUsuario(String nome, String email, String novaSenha) throws SystemBusinessException {
        try {
            Message message = createNewMessage();
            InternetAddress to[] = createRecipients(Arrays.asList(email));
            message.setRecipients(Message.RecipientType.TO, to);
            message.setSubject("[SISGIA] Recuperação de Senha");
            message.setContent(
                    "Olá, foi gerado um novo cadastro de usuário para acesso ao sistema SISGIA. <br/>Favor acessar o link para cadastrar sua senha de acesso: "
                            + GFUtils.getContext() + "/recuperarsenha.jsf?cod=" + novaSenha,
                    "text/html; charset=utf-8");
            Transport.send(message);
        } catch (AddressException e) {
            throw new SystemBusinessException("Erro ao enviar email: " + e.getMessage() + ". Favor contatar administrador do sistema!");
        } catch (MessagingException e) {
            throw new SystemBusinessException("Erro ao enviar email: " + e.getMessage() + ". Favor contatar administrador do sistema!");
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new SystemBusinessException("Erro ao enviar email: " + e.getMessage() + ". Favor contatar administrador do sistema!");
        }
    }

    /**
     * Metodo para transformar uma lista de string de emails em um array de {@link InternetAddress}
     * 
     * @param recipients:
     *            lista de string contendo os emails dos destinatarios
     * @return {@link InternetAddress}: array de destinatarios
     * @throws AddressException
     */
    private InternetAddress[] createRecipients(List<String> destinatarios) throws AddressException {
        InternetAddress[] to = new InternetAddress[destinatarios.size()];
        for (int i = 0; i < destinatarios.size(); i++) {
            to[i] = new InternetAddress(destinatarios.get(i));
        }

        return to;
    }

    /**
     * Metodo auxiliar para criar uma nova mensagem de email e setar o email from a partir da configuracao do mail session do servidor
     * 
     * @return {@link Message}: objeto de mensagem de email
     * @throws MessagingException
     * @throws AddressException
     */
    private Message createNewMessage() throws MessagingException, AddressException {
        Message message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(mailSession.getProperty("mail.smtp.from")));
        return message;
    }

    public Session getMailSession() {
        return mailSession;
    }

    public void setMailSession(Session mailSession) {
        this.mailSession = mailSession;
    }

}
