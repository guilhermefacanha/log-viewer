package br.com.gfsolucoesti.init.servlet;

import java.io.InputStream;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import br.com.gfsolucoesti.dao.TaskCategoryDao;
import br.com.gfsolucoesti.dao.TaskDao;
import br.com.gfsolucoesti.entity.Task;
import br.com.gfsolucoesti.entity.TaskCategory;
import br.com.gfsolucoesti.utils.ManifestReader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebServlet(loadOnStartup = 2, name = "initServlet", value = "/initServlet")
public class AppConfigServlet extends HttpServlet {

	private static final long serialVersionUID = 2151250503543054202L;
	
	@Inject
	DbInitUtil dbInit;

	@Override
	public void init() throws ServletException {
		super.init();
		log.debug("======================================================================");
		log.debug(" INITIAL CONFIGURATION FOR APPLICATION");
		log.debug("======================================================================");
		
		AppConfig.initParams();

		
		initManifestFile();

		dbInit.createDataSample();

		log.debug("======================================================================");
		log.debug(" CONFIGURATION CONTEXT INITIALIZED");
		log.debug("======================================================================");
	}

	private void initManifestFile() {
		try {
			InputStream input = getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF");
			ManifestReader.init(input);
		} catch (Exception e) {
			log.error("Error reading manifest file: " + e);
		}
	}

}
