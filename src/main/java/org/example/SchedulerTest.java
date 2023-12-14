package org.example;

import com.aventstack.extentreports.ExtentReports;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

@Listeners(CustomListeners.class)
public class SchedulerTest {

    WebDriver driver;
    DataFormatter formatter;

    @BeforeTest
    public void satUpDriver(){
        driver = Main.driver;
        formatter = Main.formatter;
    }

    @DataProvider(name = "SchedulerData")
    public Object[][] getUserData() throws IOException {
        FileInputStream fis = new FileInputStream("C:\\Users\\vinay\\Downloads\\TestData.xlsx");
        XSSFWorkbook workbook= new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(1);
        int rowCount = sheet.getPhysicalNumberOfRows();
        XSSFRow row = sheet.getRow(0);
        int columnCount = row.getLastCellNum();
        Object[][] data = new Object[rowCount-1][columnCount];
        for(int i=0; i<rowCount-1; i++){
            row = sheet.getRow(i+1);
            for(int j=0; j<columnCount; j++){
                XSSFCell cell = row.getCell(j);
                data[i][j] = formatter.formatCellValue(cell);

            }
        }
        return data;
    }

    @Test(priority = 1, dataProvider = "SchedulerData")
    public void testSchedulerCreation(String assertName, String checkList, String frequency, String assignedToUser, String startDate, String endDate, String startTime, String endTime, String description){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
        WebElement schedulerElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@role='menu']//li[3]")));
        schedulerElement.click();

        WebElement today = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='rbc-day-bg rbc-today']")));
        Actions actions = new Actions(driver);
        actions.moveToElement(today).click().perform();

        WebElement assertInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='assetId']")));
        assertInput.click();

        WebElement assertNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'" + assertName + "')]")));
        assertNameElement.click();

        driver.findElement(By.xpath("//div[@class='ant-select-selection-overflow']")).click();
        WebElement checkListInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'" + checkList + "')]")));
        checkListInput.click();

        driver.findElement(By.xpath("//input[@id='frequency']")).click();
        WebElement frequencyInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='ant-select-item-option-content'][normalize-space()='" + frequency + "']")));
        frequencyInput.click();

        driver.findElement(By.xpath("//input[@id='userId']")).click();
        WebElement assignedToUserInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'" + assignedToUser + "')]")));
        assignedToUserInput.click();

        driver.findElement(By.xpath("//input[@id='scheduleDate']")).click();
        WebElement startDateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@class='ant-picker-content']")));
        startDateInput.click();

        driver.findElement(By.xpath("//input[@id='startTime']")).click();
        WebElement startTimeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='ant-picker-dropdown css-ru2fok ant-picker-dropdown-placement-bottomLeft ']//a[@class='ant-picker-now-btn'][normalize-space()='Now']")));
        startTimeInput.click();

        WebElement endTimeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='endTime']")));
        endTimeInput.click();
        endTimeInput.sendKeys(endTime);

        WebElement okInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='ant-picker-dropdown css-ru2fok ant-picker-dropdown-placement-bottomLeft ']//li[@class='ant-picker-ok'][normalize-space()='OK']")));
        okInput.click();

        driver.findElement(By.xpath("//textarea[@id='description']")).sendKeys(description);
        driver.findElement(By.xpath("//span[normalize-space()='Save']")).click();

        String regexPattern = "more";

        String xpathExpression = "//button[contains(normalize-space(), '" + regexPattern + "')]";

        WebElement schedulers = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@class='ant-radio-button-wrapper ant-radio-button-wrapper-checked css-ru2fok']")));

        WebElement dayButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Day']")));
        dayButton.click();

        WebElement todayScheduler = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\"rbc-events-container\"]")));

        boolean foundText = false;
        List<WebElement> todaySchedulers = driver.findElements(By.xpath("//div[@class='rbc-events-container']/div"));

        for (WebElement webElement : todaySchedulers) {
            try {
                WebElement subElement = webElement.findElement(By.xpath(".//h3[@style='margin-bottom: 1px;']"));
                if (subElement.getText().equalsIgnoreCase(assignedToUser)) {
                    foundText = true;
                    break;
                }
            } catch (NoSuchElementException e) {
                System.out.println("h3 element not found within this div");
            }
        }
        Assert.assertTrue(foundText);
    }
}
