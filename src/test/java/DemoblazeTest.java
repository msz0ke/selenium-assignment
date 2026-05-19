import static org.junit.Assert.*;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;
import java.util.Properties;
import java.util.UUID;
import java.io.InputStream;
import java.io.IOException;
import pages.*;

public class DemoblazeTest {

    private WebDriver driver;
    private HomePage homePage;
    private static final Properties config = new Properties();
    private String baseUrl;
    private String testPassword;

    static {
        try (InputStream input = DemoblazeTest.class.getClassLoader().getResourceAsStream("test.properties")) {
            if (input != null) {
                config.load(input);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test.properties", e);
        }
    }

    private static String getConfig(String key, String defaultValue) {
        String sysProperty = System.getProperty(key);
        if (sysProperty != null) {
            return sysProperty;
        }
        return config.getProperty(key, defaultValue);
    }

    @Before
    public void setup() {
        baseUrl = getConfig("base.url", "https://demoblaze.com/");
        testPassword = getConfig("test.password", "TestPass123");
        String browser = getConfig("browser", "chrome");
        boolean headless = Boolean.parseBoolean(getConfig("headless", "false"));

        if (browser.equals("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            if (headless) {
                options.addArguments("--headless");
            }
            driver = new FirefoxDriver(options);
        } else {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-notifications");
            if (headless) {
                options.addArguments("--headless=new");
            }
            driver = new ChromeDriver(options);
        }

        driver.get(baseUrl);
        homePage = new HomePage(driver);
    }

    @After
    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }

    private String generateUniqueUsername() {
        return "testuser_" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Test
    public void testPageTitle() {
        assertEquals("STORE", homePage.getPageTitle());
    }

    @Test
    public void testStaticPageContent() {
        assertTrue(homePage.getBrandText().contains("PRODUCT STORE"));
        assertTrue(homePage.getFooterText().contains("We believe performance needs to be validated"));
    }

    @Test
    public void testSignUpWithValidCredentials() {
        SignUpPage signUpPage = homePage.clickSignUp();
        signUpPage.signUp(generateUniqueUsername(), testPassword);

        String alertText = homePage.acceptAlert();
        assertTrue(alertText.contains("Sign up successful"));
    }

    @Test
    public void testSignUpWithExistingUserShowsError() {
        String username = generateUniqueUsername();

        SignUpPage signUpPage = homePage.clickSignUp();
        signUpPage.signUp(username, testPassword);
        homePage.acceptAlert();

        driver.get(baseUrl);

        signUpPage = homePage.clickSignUp();
        signUpPage.signUp(username, testPassword);

        String alertText = homePage.acceptAlert();
        assertTrue(alertText.contains("This user already exist"));
    }

    @Test
    public void testLoginWithValidCredentials() {
        String username = generateUniqueUsername();

        SignUpPage signUpPage = homePage.clickSignUp();
        signUpPage.signUp(username, testPassword);
        homePage.acceptAlert();

        driver.get(baseUrl);

        LoginPage loginPage = homePage.clickLogIn();
        loginPage.logIn(username, testPassword);

        assertTrue(homePage.getLoggedInUsername().contains(username));
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        LoginPage loginPage = homePage.clickLogIn();
        loginPage.logIn("nonexistent_user_xyz", "wrongpassword");

        String alertText = homePage.acceptAlert();
        assertTrue(alertText.contains("User does not exist"));
    }

    @Test
    public void testLogout() {
        String username = generateUniqueUsername();

        SignUpPage signUpPage = homePage.clickSignUp();
        signUpPage.signUp(username, testPassword);
        homePage.acceptAlert();

        driver.get(baseUrl);

        LoginPage loginPage = homePage.clickLogIn();
        loginPage.logIn(username, testPassword);

        homePage.getLoggedInUsername();
        homePage.clickLogout();

        assertTrue(homePage.isLoginLinkVisible());
    }

    @Test
    public void testHoverOverProduct() {
        WebElement product = homePage.hoverOverFirstProduct();

        assertTrue(product.isDisplayed());
        assertNotNull(product.getDomAttribute("href"));
    }

    @Test
    public void testContactForm() {
        ContactModal contactModal = homePage.clickContact();
        contactModal.sendContactForm("test@example.com", "Test User", "This is a test message");

        String alertText = homePage.acceptAlert();
        assertTrue(alertText.contains("Thanks for the message!!"));
    }

    @Test
    public void testMultipleProductPages() {
        String[] productIds = {"1", "2", "3", "4", "5"};

        for (String id : productIds) {
            driver.get(baseUrl + "prod.html?idp_=" + id);
            ProductPage productPage = new ProductPage(driver);

            assertNotNull(productPage.getProductTitle());
            assertFalse(productPage.getProductTitle().isEmpty());
            assertNotNull(productPage.getProductPrice());
            assertFalse(productPage.getProductPrice().isEmpty());
        }
    }

    @Test
    public void testJavascriptScroll() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

        assertTrue(homePage.isFooterVisible());
    }

    @Test
    public void testBrowserHistory() {
        ProductPage productPage = homePage.clickFirstProduct();

        assertNotNull(productPage.getProductTitle());
        assertFalse(productPage.getProductTitle().isEmpty());

        driver.navigate().back();

        assertEquals("STORE", homePage.getPageTitle());
        assertTrue(homePage.getBrandText().contains("PRODUCT STORE"));
    }
}
