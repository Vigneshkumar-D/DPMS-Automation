package org.example;

import com.aventstack.extentreports.ExtentReports;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.List;

@Listeners(CustomListeners.class)
public class ReportTest {
    WebDriver driver;
    String woNum;

    @BeforeTest
    public void satUpDriver(){
        driver = Main.driver;
        woNum = DashBoardTest.woNum;
    }

    @Test(priority = 1)
    public void testReports(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement schedulerElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@role='menu']//li[6]")));
        schedulerElement.click();
        boolean hasNextPage = true;
        boolean foundText = false;

        while (hasNextPage) {
            List<WebElement> tableData =  driver.findElements(By.xpath("//tbody//tr"));
            System.out.println(tableData.size());
            for (int i = 0; i < tableData.size(); i++) {
                WebElement webElement = tableData.get(i);
                WebElement woNumElement;

                try {
                    woNumElement = webElement.findElement(By.xpath(".//td[2]"));
                } catch (StaleElementReferenceException e) {
                    System.out.println("Stale element reference encountered");
                    try {
                        Thread.sleep(3000);
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }
                    tableData = driver.findElements(By.xpath("//tbody//tr"));
                    webElement = tableData.get(i);
                    woNumElement = webElement.findElement(By.xpath(".//td[2]"));
                }

                boolean isValidWoNum = woNumElement.getText().equalsIgnoreCase(woNum);

                if (isValidWoNum) {
                    foundText = true;
                    break;
                }
            }
            hasNextPage = false;
        }
        Assert.assertTrue(foundText);
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
