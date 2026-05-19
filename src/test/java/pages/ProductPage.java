package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class ProductPage extends BasePage {

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public String getProductTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector(".name"))).getText();
    }

    public String getProductPrice() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector(".price-container"))).getText();
    }

    public void addToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".btn-success"))).click();
    }
}
