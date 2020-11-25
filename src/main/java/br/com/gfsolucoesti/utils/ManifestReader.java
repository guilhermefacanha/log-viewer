package br.com.gfsolucoesti.utils;

import java.io.InputStream;
import java.util.Map.Entry;
import java.util.jar.Manifest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ManifestReader {

	private ManifestReader() {
	};

	private static Manifest manifest;

	public static void init(InputStream in) {
		try {
			ManifestReader.manifest = new Manifest(in);
			if (manifest != null) {
				log.info("==== Manifest Entries loaded ====");
				log.debug("==== Manifest entries: ====");
				for (Entry<Object, Object> entry : manifest.getMainAttributes().entrySet()) {
					log.debug("==== " + entry.getKey() + ":" + entry.getValue() + " ====");
				}
			}
		} catch (Exception e) {
			log.error("Error reading manifest file: ", e);
		}
	}

	public static String read(String prop) {
		try {
			return manifest.getMainAttributes().getValue(prop);
		} catch (Exception e) {
			return "";
		}
	}
}
