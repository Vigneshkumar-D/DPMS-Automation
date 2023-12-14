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
import java.util.NoSuchElementException;


@Listeners(CustomListeners.class)
public class ChecklistExecutionTest {
    WebDriver driver;
    static String execNum;


    @BeforeTest
    public void satUpDriver(){
        driver = Main.driver;
    }

    @Test(priority = 1)
    public void testChecklistExecution() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement schedulerElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@role='menu']//li[4]")));
        schedulerElement.click();
        Assert.assertTrue(this.validateChecklistExecution());
    }

    public Boolean validateChecklistExecution() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean hasNextPage = true;
        boolean foundText = false;
        String assignedTo = "benny";
        String description = "Testing 5";
        String assertName = "Aging Furnace 11";

        while (hasNextPage) {
            List<WebElement> tableData =  driver.findElements(By.xpath("//tbody//tr"));
            System.out.println(tableData.size());

            for (int i = 0; i < tableData.size(); i++) {
                WebElement webElement = tableData.get(i);
                WebElement assignedToElement;
                WebElement descriptionElement;
                WebElement assertNameElement;

                try {
                    descriptionElement = webElement.findElement(By.xpath(".//td[5]"));
                    assertNameElement = webElement.findElement(By.xpath(".//td[6]"));
                    assignedToElement = webElement.findElement(By.xpath(".//td[7]"));
                    execNum = webElement.findElement(By.xpath(".//td[2]")).getText();

                } catch (StaleElementReferenceException e) {
                    System.out.println("Stale element reference encountered");
                    try {
                        Thread.sleep(3000);
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }
                    tableData = driver.findElements(By.xpath("//tbody//tr"));
                    webElement = tableData.get(i);
                    descriptionElement = webElement.findElement(By.xpath(".//td[5]"));
                    assertNameElement = webElement.findElement(By.xpath(".//td[6]"));
                    assignedToElement = webElement.findElement(By.xpath(".//td[7]"));
                    execNum = webElement.findElement(By.xpath(".//td[2]")).getText();
                }
                Boolean isValidRole = assignedToElement.getText().equalsIgnoreCase(assignedTo);
                Boolean isValidDes = descriptionElement.getText().equalsIgnoreCase(description);
                Boolean isValidAssert = assertNameElement.getText().equalsIgnoreCase(assertName);

                if (isValidRole && isValidDes && isValidAssert) {
                    foundText = true;
                    break;
                }
            }

            try {
                WebElement nextPageButton = driver.findElement(By.xpath("(//button[@class=\"ant-pagination-item-link\"])[2]"));
                if (nextPageButton.isEnabled()) {
                    nextPageButton.click();
                } else {
                    hasNextPage = false;
                }
            } catch (NoSuchElementException e) {
                System.out.println("No next page button found. Exiting loop.");
                hasNextPage = false;
            }
        }
        return foundText;
    }
}
