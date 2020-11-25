package br.com.gfsolucoesti.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import br.com.gfsolucoesti.enumeration.PrimeTheme;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
public class UserController implements Serializable{

	private static final long serialVersionUID = -3770573459254222700L;
	
	@Getter @Setter
	private String theme = "bootstrap";
	
	@Getter @Setter
	private String name = "Admin";
	@Getter	@Setter
	private String type="Admin";
	
	public PrimeTheme[] getThemeList(){
		return PrimeTheme.values();
	}

}
