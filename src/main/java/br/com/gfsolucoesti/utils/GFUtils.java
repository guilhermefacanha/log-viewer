package br.com.gfsolucoesti.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.model.file.UploadedFile;

import br.com.gfsolucoesti.entity.BaseEntity;
import br.com.gfsolucoesti.init.servlet.AppConfig;

@SuppressWarnings("rawtypes")
public class GFUtils {

    private static String[] months = { "JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV",
            "DEZ" };
    public final static String UTM_REGEX = "(\\d{1,2})([A-Z, a-z])";
    public final static String ZONE_UTM = "24M";

    public static boolean isValidList(List list) {
        return list != null && list.size() > 0;
    }

    public static boolean isNullEmpty(String text) {
        return text == null || text.equals("");
    }

    public static String formatNumber(long number, int length) {
        String res = String.valueOf(number);
        while (res.length() < length)
            res = "0" + res;
        return res;
    }

    public static String formatCurrency(double value) {
        NumberFormat f = NumberFormat.getInstance();
        f.setMaximumFractionDigits(2);
        f.setMinimumFractionDigits(2);
        return f.format(value);
    }

    public static String formatCurrency(double value, Locale locale) {
        NumberFormat moneyFormat = DecimalFormat.getCurrencyInstance(locale);
        moneyFormat.setMinimumFractionDigits(2);
        moneyFormat.setMaximumFractionDigits(2);
        return moneyFormat.format(value);
    }

    public static int tryParse(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean validEmail(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String printMap(Set<Entry<Object, Object>> entrySet) {
        StringBuffer str = new StringBuffer();
        try {
            for (Entry<Object, Object> entry : entrySet) {
                str.append(entry.getKey().toString() + ":" + entry.getValue().toString());
                str.append(System.lineSeparator());
            }
        } catch (Exception e) {
        }

        return str.toString();
    }

    /**
     * String Normalize Functions
     */
    public static String normalizeText(String text) {
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("[^\\p{ASCII}]", "");
        return text;
    }

    public static String removeSpecialCharacters(String text) {
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("[^a-zZ-Z0-9]", "");
        return text;
    }

    public static String normalizeFileName(String name) {
        name = removeSpecialCharacters(name);
        name = name.replace(" ", "_");
        return name;
    }

    /**
     * DATE FUNCTIONS
     */

    public static String getMonth(int mes) {
        return months[mes - 1];
    }

    public static String getMesExtenso(String monthYear) {
        int mes = Integer.parseInt(monthYear.split("/")[0]);
        return months[mes - 1] + "/" + monthYear.split("/")[1];
    }

    public static String getFirstDay(String monthYear) {
        return "01/" + monthYear;
    }

    public static String getFirstDayOfMonth(String monthYear) {
        String mesStr = monthYear.split("/")[0];
        String anoStr = monthYear.split("/")[1];
        Integer mes = Integer.parseInt(mesStr);
        mes = mes + 1;
        if (mes < 10)
            mesStr = "0" + mes;

        return "01/" + mesStr + "/" + anoStr;
    }

    public static Date parseStringDate(String text) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date data = null;
        try {
            data = format.parse(text);
        } catch (Exception e) {
        }
        return data;
    }

    public static Date parseStringDate(String text, String formato) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(formato);
        Date data = null;
        try {
            data = format.parse(text);
        } catch (Exception e) {
        }
        return data;
    }

    public static String getStringDate(Date data) {
        if (data != null) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return formatter.format(data).toString();
        }
        return "";
    }

    public static String getStringDate(Date data, String formato) {
        DateFormat formatter = new SimpleDateFormat(formato);
        return formatter.format(data).toString();
    }

    public static String formataDataPadrao(String data) {
        String[] tmp = data.split("-");

        data = tmp[2] + "/" + tmp[1] + "/" + tmp[0];

        return data;
    }

    /**
     * BaseEntity Object Functions
     */
    public static <T extends BaseEntity> boolean isValidObject(T obj) {
        if (obj != null && obj.getId() > 0)
            return true;
        else
            return false;
    }

    public static <T extends BaseEntity> BaseEntity toNullObject(T obj) {
        if (obj != null && obj.getId() == 0)
            obj = null;
        return obj;
    }

    /**
     * Context Functions
     */
    public static String getContext() {
        String http = FacesContext.getCurrentInstance().getExternalContext().getRequestScheme() + "://";
        String server = FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
        String port = ":" + FacesContext.getCurrentInstance().getExternalContext().getRequestServerPort();
        String app = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        return http + server + port + app;
    }

    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        System.out.println(ip);
        return ip;
    }

    /**
     * Password Functions
     */
    public static String generateRandomNumberPassword(int qtd) {
        String str = "";
        for (int i = 0; i < qtd; i++) {
            str += new Random().nextInt(10);
        }
        return str;
    }

    public static String generateRandomTextPassword(int qtd) {
        byte[] array = new byte[256];
        new Random().nextBytes(array);
        String randomString = new String(array, Charset.forName("UTF-8"));
        StringBuffer r = new StringBuffer();
        for (int k = 0; k < randomString.length(); k++) {
            char ch = randomString.charAt(k);
            if (((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) && (qtd > 0)) {
                r.append(ch);
                qtd--;
            }
        }
        return r.toString();
    }

    /**
     * JSF Functions
     */
    public static void addMsgWarn(String msg) {
        try {
            if (GFUtils.isValidList(FacesContext.getCurrentInstance().getMessageList()))
                FacesContext.getCurrentInstance().getMessageList().clear();

        } catch (Exception e) {
        }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "ERRO: " + msg, ""));
    }

    public static void addMsg(String msg, boolean erro) {
        try {
            if (GFUtils.isValidList(FacesContext.getCurrentInstance().getMessageList()))
                FacesContext.getCurrentInstance().getMessageList().clear();

        } catch (Exception e) {
        }

        if (erro) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO: " + msg, ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO: " + msg, ""));
        }
    }

    public static void addMsg(String msg, boolean erro, boolean append) {
        if (!append && GFUtils.isValidList(FacesContext.getCurrentInstance().getMessageList()))
            FacesContext.getCurrentInstance().getMessageList().clear();

        if (erro) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO: " + msg, ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO: " + msg, ""));
        }
    }

    public static void clearMsg() {
        FacesContext.getCurrentInstance().getMessageList().clear();
    }

    public static Object getSessionObject(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(name);
    }

    public static void setSessionObject(String name, Object valor) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(name, valor);
    }

    public static void clearSession(String name) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(name);
    }

    public static boolean verificarFlash(String key) {
        try {
            return FacesContext.getCurrentInstance().getExternalContext().getFlash().containsKey(key);
        } catch (Exception e) {
            return false;
        }
    }

    public static Object getFlash(String key) {
        if (FacesContext.getCurrentInstance().getExternalContext().getFlash().containsKey(key)) {
            Object res = FacesContext.getCurrentInstance().getExternalContext().getFlash().get(key);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().keep(key);
            return res;
        } else
            return null;
    }

    public static void keepMsgs() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public static void clearFlash() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().clear();
    }

    public static String getCurrentPage() {
        String paginaAtual = "";

        try {
            paginaAtual = FacesContext.getCurrentInstance().getViewRoot().getViewId();
            paginaAtual = paginaAtual.substring(1, paginaAtual.length());
            paginaAtual = paginaAtual.replace(".xhtml", "");
        } catch (Exception e) {
        }
        return paginaAtual;
    }

    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public static void goToPage(String page) {
        Application app = FacesContext.getCurrentInstance().getApplication();
        app.getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, page);
    }

    public static void goTo(String page, String param) {
        try {
            String app = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            page = app + page + "." + AppConfig.getUrlExtension();
            if (!GFUtils.isNullEmpty(param))
                page += "?" + param;
            FacesContext.getCurrentInstance().getExternalContext().redirect(page);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            GFUtils.addMsg(e.getMessage(), true);
        }
    }

    public static void goTo(String page) {
        goTo(page, null);
    }

    public static void reloadPage() {
        String currentPage = getCurrentPage();
        GFUtils.goTo("/" + currentPage);
    }

    public static String getPageRedirect(String page) {
        return "/" + page + "?faces-redirect=true";
    }

    /**
     * Hash Functions
     */

    public static String encodeBase64(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    public static String encodeBase64(byte[] bytes) {
        return encodeBase64(new String(bytes));
    }

    public static String decodeBase64(String text) {
        return new String(Base64.getDecoder().decode(text));
    }

    public static String decodeBase64(byte[] bytes) {
        return decodeBase64(new String(bytes));
    }

    public static String getHash(String text) throws RuntimeException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());
            byte[] hash = md.digest();

            StringBuilder s = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                int parteAlta = ((hash[i] >> 4) & 0xf) << 4;
                int parteBaixa = hash[i] & 0xf;
                if (parteAlta == 0)
                    s.append('0');
                s.append(Integer.toHexString(parteAlta | parteBaixa));
            }
            return s.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Db Functions
     */
    public static boolean isConstraintViolationException(PersistenceException e) {
        Throwable t = e.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException) {
            return true;
        }
        return false;
    }

    /**
     * Internationalization Functions
     */
    public static String getMsg(String key) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
            return bundle.getString(key);
        } catch (Exception e) {
            return "Msg not found!";
        }
    }

    /**
     * File Functions
     */
    public static String getFileContentType(String name) {
        try {
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            return fileNameMap.getContentTypeFor(name);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getFileExtension(String name) {
        try {
            int lastIndex = name.lastIndexOf(".");
            return name.substring(lastIndex, name.length());
        } catch (Exception e) {
            return "";
        }
    }

    public static String removeFileExtension(String name) {
        try {
            int lastIndex = name.lastIndexOf(".");
            return name.substring(0, lastIndex);
        } catch (Exception e) {
            return "";
        }
    }

    public static byte[] readFile(String file) {
        return readFile(new File(file));
    }

    public static byte[] readFile(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            return null;
        }
    }

    public static String getFileMd5(String filepath) throws IOException {
        try {
            InputStream is = Files.newInputStream(Paths.get(filepath));
            return DigestUtils.md5Hex(is);
        } catch (Exception e) {
            return null;
        }
    }

    public static void copyFile(File fonte, File destino, boolean apagarFonte) throws IOException {
        verifyFolder(destino.getParentFile());

        InputStream in = new FileInputStream(fonte);
        OutputStream out = new FileOutputStream(destino);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();

        try {
            if (apagarFonte)
                fonte.delete();
        } catch (Exception e) {
        }
    }

    public static void copyFile(String fonte, String destino) throws IOException {
        File fFonte = new File(fonte);
        File fDestino = new File(destino);

        verifyFolder(fDestino.getParentFile());

        InputStream in = new FileInputStream(fFonte);
        OutputStream out = new FileOutputStream(fDestino);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static void writeFile(byte[] conteudo, String caminho) throws IOException {
        verifyFolder(new File(caminho).getParentFile());
        OutputStream out = new FileOutputStream(caminho);
        out.write(conteudo);
        out.close();
    }

    public static void verifyFolder(File dir) {
        if (!dir.isDirectory())
            dir.mkdirs();
    }

    public static void verifyFolder(String str) {
        File dir = new File(str);

        if (!dir.isDirectory())
            dir.mkdirs();
    }

    public static String writeTempFile(UploadedFile file) throws IOException {
        String nameFile = GFUtils.normalizeText(file.getFileName());
        String extensaoArquivo = GFUtils.getFileExtension(nameFile);
        nameFile = nameFile.replace(extensaoArquivo, "");
        nameFile = nameFile + GFUtils.getStringDate(new Date(), "_ddMMyyyyHHmmss") + extensaoArquivo;
        String tempFilePath = AppConfig.getTmpFolder() + File.separator + nameFile;
        File f = new File(tempFilePath);
        verifyFolder(f.getParentFile());
        GFUtils.writeFile(file.getContent(), tempFilePath);

        return nameFile;
    }

    public static String writeTempFile(String content) throws IOException {
        String tempFilePath = AppConfig.getTmpFolder() + File.separator + UUID.randomUUID().toString() + ".txt";
        File f = new File(tempFilePath);
        verifyFolder(f.getParentFile());
        GFUtils.writeFile(content.getBytes(), tempFilePath);

        return tempFilePath;
    }

    public static String getTempFileLocation(String extension) {
        return AppConfig.getTmpFolder() + File.separator + UUID.randomUUID().toString() + "." + extension;
    }

    public static String writeFileTempOriginalName(UploadedFile file) throws IOException {
        String nameFile = GFUtils.normalizeText(file.getFileName());
        String tempFilePath = AppConfig.getTmpFolder() + File.separator + nameFile;
        File f = new File(tempFilePath);
        verifyFolder(f.getParentFile());
        GFUtils.writeFile(file.getContent(), tempFilePath);

        return nameFile;
    }

    public static void createZipFile(ZipOutputStream zos, String nome, long tamanho, String caminhoCompleto)
            throws IOException {
        try {
            ZipEntry entry = new ZipEntry(nome);
            entry.setSize(tamanho);

            File fileToZip = new File(caminhoCompleto);
            FileInputStream fis = new FileInputStream(fileToZip);

            zos.putNextEntry(entry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }
            fis.close();

            zos.closeEntry();
        } catch (ZipException ex) {
        }
    }

    // Metodo auxiliar para utilizacao com stream() para filtrar distinct by keys
    public static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
        final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

        return t -> {
            final List<?> keys = Arrays.stream(keyExtractors)
                    .map(ke -> ke.apply(t))
                    .collect(Collectors.toList());

            return seen.putIfAbsent(keys, Boolean.TRUE) == null;
        };
    }

    // Metodo auxiliar para arredondar Double
    public static Double roundDouble(Double val) {
        return new BigDecimal(val.toString()).setScale(2, RoundingMode.CEILING).doubleValue();
    }

    // Metodo auxiliar para converter String percentual para double
    public static Double convertFromPercentageToDouble(String percentage) {
        percentage = percentage.replace("%", "");
        Double dbl = Double.valueOf(percentage);
        return dbl / 100;
    }

}
