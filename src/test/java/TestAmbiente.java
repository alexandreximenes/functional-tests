import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class TestAmbiente {

    public static final int TIME = 500;
//    ChromeOptions options;
    WebDriver driver;

    @Before
    public void setUp(){
//        String property = System.setProperty("webdriver.chrome.driver", "caminho/pasta/chromedriver");
//        System.setProperty("webdriver.chrome.whitelistedIps", "");

//        options = new ChromeOptions();
//        options.addArguments("--allowed-ips", "--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors");

    }

    public WebDriver getWebDriver(){
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.navigate().to("http://localhost:8001/tasks/");

        return driver;
    }

    public WebElement findByIdElement(String id) throws InterruptedException {
        Thread.sleep(TIME);
        return driver.findElement(By.id(id));
    }

    @Test
    public void deveSalvarTarefaComSucesso() throws InterruptedException {

        driver = getWebDriver();

        findByIdElement("addTodo").click();
        findByIdElement("task").sendKeys("Teste via selenium");
        findByIdElement("dueDate").sendKeys("10/10/2030");
        findByIdElement("saveButton").click();
        String urlSave = "http://localhost:8001/tasks/save";

        Assert.assertEquals("Success!", findByIdElement("message").getText());
        Assert.assertEquals(urlSave, driver.getCurrentUrl());

        Thread.sleep(5_000);
        driver.quit();
    }

    @Test
    public void naoDeveSalvarTarefaComSucesso() throws InterruptedException {

        driver = getWebDriver();

        try{
            findByIdElement("addTodo").click();
            findByIdElement("task").sendKeys("Teste via selenium");
            findByIdElement("dueDate").sendKeys("10/10/2020");
            findByIdElement("saveButton").click();

            Assert.assertEquals("Due date must not be in past", findByIdElement("message").getText());

            Thread.sleep(2_000);
            driver.navigate().back();
            Thread.sleep(2_000);
        }finally {
            driver.quit();
        }
    }

    @Test
    public void naoDeveSalvarTarefaSemData() throws InterruptedException {

        driver = getWebDriver();

        try{
            findByIdElement("addTodo").click();
            findByIdElement("task").sendKeys("Teste via selenium");
            findByIdElement("dueDate").sendKeys("");
            findByIdElement("saveButton").click();

            Assert.assertEquals("Fill the due date", findByIdElement("message").getText());

            Thread.sleep(2_000);
            driver.navigate().back();
            Thread.sleep(2_000);
        }finally {
            driver.quit();
        }
    }

    @Test
    public void naoDeveSalvarTarefaSemDescricao() throws InterruptedException {

        driver = getWebDriver();

        try{
            findByIdElement("addTodo").click();
            findByIdElement("task").sendKeys("");
            findByIdElement("dueDate").sendKeys("10/10/2020");
            findByIdElement("saveButton").click();

            Assert.assertEquals("Fill the task description", findByIdElement("message").getText());

            Thread.sleep(2_000);
            driver.navigate().back();
            Thread.sleep(2_000);
        }finally {
            driver.quit();
        }
    }

}
