package br.com.gfsolucoesti.internationalization;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.primefaces.olympia.view.GuestPreferences;

@RequestScoped
public class MessageProvider {

    private ResourceBundle bundle;
    private Locale locale;
    
    @Inject
    GuestPreferences preferences;

    @Produces
    @MessageBundle
    public ResourceBundle getBundle() {
        if (bundle == null || localeChanged()) {
            locale = preferences.getLocale();
            bundle = ResourceBundle.getBundle("local_messages", locale);
        }
        return bundle;
    }

    private boolean localeChanged() {
        return !preferences.getLocale().equals(locale);
    }

    public String get(String key) {

        String result = null;
        try {
            result = getBundle().getString(key);
        } catch (MissingResourceException e) {
            result = "???" + key + "??? not found";
        }
        return result;
    }

}