package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class SignUpPage extends BasePage {

    public SignUpPage(WebDriver driver) {
        super(driver);
    }

    public void signUp(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-username"))).sendKeys(username);
        driver.findElement(By.id("sign-password")).sendKeys(password);
        driver.findElement(By.xpath("//div[@id='signInModal']//div[@class='modal-footer']//button[contains(@class,'btn-primary')]")).click();
    }
}
