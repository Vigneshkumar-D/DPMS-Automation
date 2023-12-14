package org.example;

import com.aventstack.extentreports.ExtentReports;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class ResolutionWorkOrderTest {
    WebDriver driver;
    ExtentReports extent;
    String execNum;
    @BeforeTest
    public void satUpDriver(){
        driver = Main.driver;
        extent = Main.extent;
        execNum = ChecklistExecutionTest.execNum;
    }

    @Test(priority = 1)
    public void testResolutionWorkOrder(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement schedulerElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@role='menu']//li[5]")));
        schedulerElement.click();

        try {
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }

        boolean hasNextPage = true;
        boolean foundText = false;
        while (hasNextPage) {

            List<WebElement> tableData =  driver.findElements(By.xpath("//tbody//tr"));
            System.out.println(tableData.size());

            for (int i = 0; i < tableData.size(); i++) {
                WebElement webElement = tableData.get(i);
                WebElement openElement;
                WebElement execNumElement;

                try {
                    execNumElement = webElement.findElement(By.xpath(".//td[2]"));
                    openElement = driver.findElement(By.xpath("//span[contains(text(),'Open')]"));
                } catch (StaleElementReferenceException e) {
                    System.out.println("Stale element reference encountered");
                    try {
                        Thread.sleep(3000);
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }
                    tableData = driver.findElements(By.xpath("//tbody//tr"));
                    openElement = driver.findElement(By.xpath("//span[contains(text(),'Open')]"));
                    webElement = tableData.get(i);
                    execNumElement = webElement.findElement(By.xpath(".//td[2]"));
                }

                boolean isValidExecNum = execNumElement.getText().equalsIgnoreCase(execNum);

                if (isValidExecNum) {
                    System.out.println("execNumElement:" + execNumElement.getText());
                    foundText = true;
                    openElement.click();
                    break;
                }
            }
//            try {
//                WebElement nextPageButton = driver.findElement(By.xpath("(//button[@class=\"ant-pagination-item-link\"])[2]"));
//                if (nextPageButton.isEnabled()) {
//                    nextPageButton.click(); // Click the next page button
//
//                } else {
//                    hasNextPage = false; // Set hasNextPage to false if there's no next page
//                }
//            } catch (NoSuchElementException e) {
//                System.out.println("No next page button found. Exiting loop.");
//                hasNextPage = false; // Exit loop if there's no next page button
//            }
            hasNextPage = false;
        }

        driver.findElement(By.xpath("//input[@id='assignedToId']")).click();
        WebElement assignedToId= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'benny')]")));
        assignedToId.click();

        driver.findElement(By.xpath("//input[@id='priority']")).click();
        WebElement priority= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'High')]")));
        priority.click();

        driver.findElement(By.xpath("//input[@id='dueDate']")).click();
        WebElement dueDate= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='ant-picker-today-btn']")));
        dueDate.click();

        driver.findElement(By.xpath("//span[normalize-space()='Next']")).click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        driver.findElement(By.xpath("//textarea[@id='rca']")).sendKeys("Testing");
        driver.findElement(By.xpath("//textarea[@id='ca']")).sendKeys("Testing");
        driver.findElement(By.xpath("//textarea[@id='pa']")).sendKeys("Testing");
        driver.findElement(By.xpath("//span[normalize-space()='Send For Approval']")).click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        driver.findElement(By.xpath("//span[normalize-space()='Approve']")).click();

        WebElement goToList =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Go To List']")));
        goToList.click();

    }

}
