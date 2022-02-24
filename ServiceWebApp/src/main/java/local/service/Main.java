package local.service;

import java.util.Optional;

import org.apache.catalina.startup.Tomcat;

public class Main {

	public static final Optional<String> port = Optional.ofNullable(System.getenv("PORT"));
	public static void main(String[] args) throws Exception {
		String contextPath = "/";
        String appBase = ".";
        Tomcat tomcat = new Tomcat(); 
        
        tomcat.setPort(8291);
        tomcat.getHost().setAppBase(appBase);
        tomcat.addWebapp(contextPath, appBase);
        tomcat.start();
        tomcat.getServer().await();
	}
}
