package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void logIn(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername"))).sendKeys(username);
        driver.findElement(By.id("loginpassword")).sendKeys(password);
        driver.findElement(By.xpath("//div[@id='logInModal']//div[@class='modal-footer']//button[contains(@class,'btn-primary')]")).click();
    }
}
