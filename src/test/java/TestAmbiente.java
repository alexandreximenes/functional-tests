import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class TestAmbiente {

    public static final int TIME = 100;
    ChromeOptions options;
    WebDriver driver;

    @Before
    public void setUp(){
//        String property = System.setProperty("webdriver.chrome.driver", "caminho/pasta/chromedriver");
//        System.setProperty("webdriver.chrome.whitelistedIps", "");

        options = new ChromeOptions();
        options.addArguments("--allowed-ips", "--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors");

    }

    public WebDriver getWebDriver() throws MalformedURLException {
//        WebDriver driver = new ChromeDriver();
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        WebDriver driver = new RemoteWebDriver(new URL("http://172.17.0.1:4444/wd/hub"), cap);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.navigate().to("http://172.17.0.1:8001/tasks/");

        return driver;
    }

    public WebElement findByIdElement(String id) throws InterruptedException {
        Thread.sleep(TIME);
        return driver.findElement(By.id(id));
    }

    @Test
    public void deveSalvarTarefaComSucesso() throws InterruptedException, MalformedURLException {

        driver = getWebDriver();

        findByIdElement("addTodo").click();
        findByIdElement("task").sendKeys("Teste via selenium");
        findByIdElement("dueDate").sendKeys("10/10/2030");
        findByIdElement("saveButton").click();
        String urlSave = "http://172.17.0.1:8001/tasks/save";

        Assert.assertEquals("Success!", findByIdElement("message").getText());
        Assert.assertEquals(urlSave, driver.getCurrentUrl());

//        Thread.sleep(5_000);
        driver.quit();
    }

    @Test
    public void naoDeveSalvarTarefaComSucesso() throws InterruptedException, MalformedURLException {

        driver = getWebDriver();

        try{
            findByIdElement("addTodo").click();
            findByIdElement("task").sendKeys("Teste via selenium");
            findByIdElement("dueDate").sendKeys("10/10/2020");
            findByIdElement("saveButton").click();

            Assert.assertEquals("Due date must not be in past", findByIdElement("message").getText());

//            Thread.sleep(2_000);
//            driver.navigate().back();
//            Thread.sleep(2_000);
        }finally {
            driver.quit();
        }
    }

    @Test
    public void naoDeveSalvarTarefaSemData() throws InterruptedException, MalformedURLException {

        driver = getWebDriver();

        try{
            findByIdElement("addTodo").click();
            findByIdElement("task").sendKeys("Teste via selenium");
            findByIdElement("dueDate").sendKeys("");
            findByIdElement("saveButton").click();

            Assert.assertEquals("Fill the due date", findByIdElement("message").getText());

//            Thread.sleep(2_000);
//            driver.navigate().back();
//            Thread.sleep(2_000);
        }finally {
            driver.quit();
        }
    }

    @Test
    public void naoDeveSalvarTarefaSemDescricao() throws InterruptedException, MalformedURLException {

        driver = getWebDriver();

        try{
            findByIdElement("addTodo").click();
            findByIdElement("task").sendKeys("");
            findByIdElement("dueDate").sendKeys("10/10/2020");
            findByIdElement("saveButton").click();

            Assert.assertEquals("Fill the task description", findByIdElement("message").getText());

//            Thread.sleep(2_000);
//            driver.navigate().back();
//            Thread.sleep(2_000);
        }finally {
            driver.quit();
        }
    }

}
