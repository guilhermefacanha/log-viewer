package br.com.gfsolucoesti.exception;

/**
 * Classe de Excecao Customizada da aplicacao
 * 
 * @author gfsolucoesti
 */
public class SystemBusinessException extends RuntimeException {

    private static final long serialVersionUID = 6446985914692633765L;

    public SystemBusinessException(Exception e) {
        super(e);
    }

    public SystemBusinessException(String string) {
        super(string);
    }
    
}
