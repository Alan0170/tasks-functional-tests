package br.ce.wcaquino.tasks.prod;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class HealthCheckIT {

	@Test
	public void healthChek () throws MalformedURLException {
		String ip = System.getenv("APP_IP");
	    if (ip == null || ip.isEmpty()) {
	        throw new IllegalStateException("The APP_IP environment variable has not been set!");
	    }
		System.out.println("http://" + ip + ":8002/tasks/");
		ChromeOptions chromeOptions = new ChromeOptions();
		WebDriver driver = new RemoteWebDriver(new URL("http://" + ip + ":4444"), chromeOptions);
		try {
			driver.navigate().to("http://" + ip + ":9999/tasks/");
			driver.manage().timeouts().implicitlyWait(6, TimeUnit.SECONDS);
			
			String buildVersion = System.getenv("BUILD_NUMBER");
			if (buildVersion == null || buildVersion.isEmpty()) {
				throw new IllegalStateException("The BUILD_VERSION environment variable has not been set!");
			}
		    String version = driver.findElement(By.id("version")).getText();
		    Assert.assertEquals("build_"+buildVersion, version);
		} finally {
		    driver.quit();
		}
	}
}
