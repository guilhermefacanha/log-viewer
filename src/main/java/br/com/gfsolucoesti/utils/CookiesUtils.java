package br.com.gfsolucoesti.utils;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class CookiesUtils {

	private CookiesUtils() {
	}

	public static void addCookie(String key, String value, Integer age) {
		Cookie cookie = new Cookie(key, value);
		if (age == null || age.equals(0)) {
			cookie.setMaxAge(Integer.MAX_VALUE);
		} else {
			cookie.setMaxAge(age);
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		response.addCookie(cookie);
	}

	public static void removeCookie(String key) {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(key)) {
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
	}

	public static Cookie getCookie(String key) {
		// Recuperando colecao de cookies
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(key)) {
					return cookie;
				}
			}
		}

		return null;
	}
}