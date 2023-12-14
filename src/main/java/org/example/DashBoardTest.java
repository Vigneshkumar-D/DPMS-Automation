package org.example;

import com.aventstack.extentreports.ExtentReports;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

@Listeners(CustomListeners.class)
public class DashBoardTest {

    WebDriver driver;
    String execNum;
    static String woNum;

    @BeforeTest
    public void satUpDriver(){
        driver = Main.driver;
        execNum = ChecklistExecutionTest.execNum;
    }

    @Test(priority = 1)
    public void testDashBoardWORStatus(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement resolutionWorkOrder = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@role='menu']//li[1]")));
        resolutionWorkOrder.click();

        driver.findElement(By.xpath("//span[normalize-space()='Completed']")).click();

        boolean hasNextPage = true;
        boolean foundText = false;
        while (hasNextPage) {

            List<WebElement> tableData =  driver.findElements(By.xpath("//tbody//tr"));
            System.out.println(tableData.size());

            for (int i = 0; i < tableData.size(); i++) {
                WebElement webElement = tableData.get(i);
                WebElement statusElement;
                WebElement execNumElement;

                try {
                    statusElement = webElement.findElement(By.xpath(".//td[9]"));
                    execNumElement = webElement.findElement(By.xpath(".//td[2]"));
                    woNum = webElement.findElement(By.xpath(".//td[3]")).getText();

                } catch (StaleElementReferenceException e) {
                    System.out.println("Stale element reference encountered");
                    try {
                        Thread.sleep(3000);
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }
                    tableData = driver.findElements(By.xpath("//tbody//tr"));
                    webElement = tableData.get(i);
                    statusElement = webElement.findElement(By.xpath(".//td[9]"));
                    execNumElement = webElement.findElement(By.xpath(".//td[2]"));
                    woNum = webElement.findElement(By.xpath(".//td[3]")).getText();
                }

                boolean isValidStatus = statusElement.getText().equalsIgnoreCase("Completed");
                boolean isValidExecNum = execNumElement.getText().equalsIgnoreCase(execNum);

                if (isValidStatus && isValidExecNum) {
                    foundText = true;
                    break;
                }
            }
            hasNextPage = false;
        }
        Assert.assertTrue(foundText);
    }
}
