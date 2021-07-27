package tests;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AmazonTestClass {

	public static void main(String[] args) throws InterruptedException {

		WebDriver driver;
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();

		driver.get("https://www.amazon.in/ref=nav_logo"); // url
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

		driver.findElement(By.xpath("//input[@id='twotabsearchtextbox']")).sendKeys("iPhone 11");
		driver.findElement(By.xpath("//input[@id='nav-search-submit-button']")).click();

		List<WebElement> searchResults = driver
				.findElements(By.xpath("//*[@data-component-type='s-search-result']//h2/a"));

		// printing all search result list
		for (WebElement searchResult : searchResults) {
//			System.out.println(searchResult.getText());
		}

		String parentWindow = driver.getWindowHandle();

		for (WebElement searchResult : searchResults) {
			if (searchResult.getText().equalsIgnoreCase("Apple iPhone 11 Pro (512GB) - Midnight Green")) {
				
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", searchResult);
				Thread.sleep(500); 
				searchResult.click();
				break;
			}
		}

//		Thread.sleep(5000);
		Set windowHandles = driver.getWindowHandles();

		Iterator it = windowHandles.iterator();
		String childWindow = "";
		while (it.hasNext()) {
			String next = (String) it.next();
			if (!parentWindow.equalsIgnoreCase(next)) {
				childWindow = next;
			}
		}

		driver.switchTo().window(childWindow);		
		
		String price = driver.findElement(By.xpath("//*[@id='priceblock_ourprice']")).getText();
		System.out.println("Price is -->" + price);
		
		WebElement addToCart = driver.findElement(By.xpath("//*[@id='add-to-cart-button']"));
		addToCart.click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id='attach-accessory-pane']")))).click();
		
		String msgAddedToCart = driver.findElement(By.xpath("//div[@id='attachDisplayAddBaseAlert']//h4[@class='a-alert-heading'][normalize-space()='Added to Cart']")).getText();
		System.out.println("...."+msgAddedToCart);
		
		String cartSubTotal = driver.findElement(By.xpath("//*[@id='attach-accessory-cart-subtotal']")).getText();
		System.out.println("Cart subtotal -->" + cartSubTotal);
		
		if(price.equalsIgnoreCase(cartSubTotal)) {
			System.out.println("Cart price == Mobile price");
		}else
			System.out.println("Cart price NOt equal to mobile price");
		
		driver.switchTo().window(parentWindow);
		System.out.println(driver.getTitle());
		
	}
}
