package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class ContactModal extends BasePage {

    public ContactModal(WebDriver driver) {
        super(driver);
    }

    public void fillEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recipient-email"))).sendKeys(email);
    }

    public void fillName(String name) {
        driver.findElement(By.id("recipient-name")).sendKeys(name);
    }

    public void fillMessage(String message) {
        driver.findElement(By.id("message-text")).sendKeys(message);
    }

    public void clickSendMessage() {
        driver.findElement(By.xpath("//div[@id='exampleModal']//div[@class='modal-footer']//button[contains(@class,'btn-primary')]")).click();
    }

    public void sendContactForm(String email, String name, String message) {
        fillEmail(email);
        fillName(name);
        fillMessage(message);
        clickSendMessage();
    }
}
