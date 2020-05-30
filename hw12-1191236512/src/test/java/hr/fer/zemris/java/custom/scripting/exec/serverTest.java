package hr.fer.zemris.java.custom.scripting.exec;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.webserver.SmartHttpServer;

public class serverTest {
	@Test
	public void test() {
		SmartHttpServer server = new SmartHttpServer("./config/server.properties");
	}
}
