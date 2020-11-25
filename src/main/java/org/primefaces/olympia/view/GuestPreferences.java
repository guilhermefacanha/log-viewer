package org.primefaces.olympia.view;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;

import br.com.gfsolucoesti.internationalization.MessageProvider;
import br.com.gfsolucoesti.utils.CookiesUtils;
import br.com.gfsolucoesti.utils.GFUtils;
import br.com.gfsolucoesti.utils.ManifestReader;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Named
@SessionScoped
public class GuestPreferences implements Serializable {

    private static final long serialVersionUID = -5522718779043040848L;

    @Getter
    private String menuMode = "layout-slim";

    @Getter
    private String theme = "bluegrey-teal";

    @Getter
    private String menuColor = "layout-menu-light";

    @Getter
    private String topBarColor = "layout-topbar-bluegrey";

    @Getter
    @Setter
    private String logo = "logo-olympia-white";

    @Getter
    @Setter
    private String project;
    @Getter
    @Setter
    private String version;
    @Getter
    @Setter
    private String date;
    @Getter
    @Setter
    private String build;

    private final String LOCALE_COOKIE = "default.locale";
    private Locale locale = null;

    @Getter
    @Setter
    private String localeStr;

    @Inject
    private MessageProvider messages;

    @PostConstruct
    public void init() {
        try {
            date = ManifestReader.read("project-date");
            project = ManifestReader.read("project-name");
            version = ManifestReader.read("project-version");
            build = ManifestReader.read("build-number");
        } catch (Exception e) {
        }
    }

    public void setMenuMode(String menuMode) {
        this.menuMode = menuMode;
        GFUtils.reloadPage();
    }

    public void setMenuColor(String menuColor) {
        this.menuColor = menuColor;
        GFUtils.reloadPage();
    }

    public void setTheme(String theme) {
        this.theme = theme;
        GFUtils.reloadPage();
    }

    public void setTopBarColor(String topBarColor, String logo) {
        this.topBarColor = topBarColor;
        this.logo = logo;
        GFUtils.reloadPage();
    }

    public void changeLocaleStr() {
        setLocale(new Locale(localeStr), true);
    }

    public Locale getLocale() {
        if (locale == null) {
            log.debug("Configuring locale for application...");
            Cookie cookie = CookiesUtils.getCookie(LOCALE_COOKIE);
            if (cookie != null) {
                setLocale(new Locale(cookie.getValue()), false);
            } else {
                Locale l = FacesContext.getCurrentInstance().getViewRoot().getLocale();
                setLocale(l, false);
            }
        }
        return locale;
    }

    public void setLocale(Locale locale) {
        setLocale(locale, true);
    }

    public void setLocale(Locale locale, boolean redirect) {
        this.locale = locale;
        this.localeStr = locale.toString();
        changeLocale();

        if (redirect) {
            GFUtils.reloadPage();
        }
    }

    private void changeLocale() {
        log.debug("storing locale in cooke with key {}", LOCALE_COOKIE);
        CookiesUtils.addCookie(LOCALE_COOKIE, locale.toString(), -1);
        log.debug("set default locale with {}", this.locale);
        Locale.setDefault(this.locale);
    }

    public String getMsg(String key) {
        return messages.get(key);
    }

}
