package log.test;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogTest {

	@Test
	public void testLog() {
		log.trace("This is a trace message!");
		log.debug("This is a debug message!");
		log.info("This is a info message!");
		log.warn("This is a warn message!");
		log.error("This is a error message!");
	}
}
