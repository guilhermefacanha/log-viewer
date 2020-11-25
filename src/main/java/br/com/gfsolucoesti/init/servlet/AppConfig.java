package br.com.gfsolucoesti.init.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import br.com.gfsolucoesti.utils.GFUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppConfig {

    private AppConfig() throws InstantiationException {
        throw new InstantiationException("Singleton Class");
    }

    private static final String propertiesFile = "/system.properties";
    private static Properties properties = new Properties();

    @Getter
    private static String urlExtension = "jsf";
    @Getter
    private static String tmpFolder = System.getProperty("java.io.tmpdir", "/tmp");
    @Getter
    private static String fsFolder;
    @Getter
    private static String privateKey;
    @Getter
    private static String publicKey;
    @Getter
    private static String OS = System.getProperty("os.name").toLowerCase();

    public static void initParams() {
        InputStream in = AppConfig.class.getResourceAsStream(propertiesFile);
        try {
            log.debug("======================================================================");
            log.debug(" CONFIG SYSTEM PROPERTIES");
            log.debug("======================================================================");
            log.debug("==== Reading system properties at: {} ====", propertiesFile);
            properties.load(in);
            log.debug("==== System properties: ====");
            log.debug("{}", GFUtils.printMap(properties.entrySet()));

            log.debug("==== Load operating system specification... ====");
            log.debug("==== Operating system: {} ====", OS);

            fsFolder = properties.getProperty("fs_location", "/opt/license-server/files/").trim();

            verifyFolders(fsFolder);
            log.debug("==== System Configuration Finished ====");
        } catch (IOException e) {
            throw new RuntimeException("Error reading system properties", e);
        }

    }

    /**
     * VERIFY / CREATE FOLDERS FOR THE SYSTEM
     */
    private static void verifyFolders(String... folders) {
        log.debug("==== System Folders Verification ====");
        try {
            for (String f : folders) {
                log.debug("==== Verifying folder {}... ====", f);
                GFUtils.verifyFolder(f);
                if (!new File(f).isDirectory())
                    throw new IOException(
                            "Unable to load system folder " + f + " . Please check OS administrator permisssions!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error verifying system folders: ", e);
        }
        log.debug("==== System Folders Verification Finished ====");
    }

}
