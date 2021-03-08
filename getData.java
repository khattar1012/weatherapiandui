package getRequest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class getData {
	
	
	public int gettempfromAPI()
	{
		Response resp= RestAssured.get("http://api.openweathermap.org/data/2.5/weather?q=Delhi&appid=713b554e934d1047a99b9fa9d006e403");
		
		int code=resp.getStatusCode();
		
		System.out.println("Status code is"+code);
		
		Assert.assertEquals(code, 200);
	
		String data=resp.asString();
		
		System.out.println("Data is"+data);
		
		JsonPath json=new JsonPath(data);
		
		float temp= json.getFloat("main.temp");
		
		int temperature=(int)temp;

		System.out.println("Temperture of Delhi fetched from API "+ temperature);
		return temperature;
		
	}
	
	
	public int gettempfromUI() throws InterruptedException
	{
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Lenovo\\Downloads\\chromedriver_win32\\chromedriver.exe");
		WebDriver driver=new ChromeDriver();
		driver.get("https://weather.com/");
		driver.manage().window().maximize();
		WebDriverWait wait = new WebDriverWait(driver, 20);
		WebElement element = wait.until(
		ExpectedConditions.elementToBeClickable(By.xpath("//div[@role='combobox']/input")));
		element.click();
		driver.findElement(By.xpath("//div[@role='combobox']/input")).sendKeys("Delhi");
		Actions action=new Actions(driver);
		action.moveToElement(driver.findElement(By.xpath("//input[@id='LocationSearch_input']"))).build().perform();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id='LocationSearch_listbox']/*[contains(text(),'Delhi')][1]")).click();
		String text = driver.findElement(By.xpath("//div[@class='CurrentConditions--primary--3xWnK']/*[1]")).getText();
		String temperature= text.substring(0, text.length()-1);
		
		int temp=Integer.parseInt(temperature);
		System.out.println("Temperture of Delhi fetched from UI "+ temperature);
		return temp;
		
	}
	 @Test
	 public void compare() throws InterruptedException 
	 {
		 if(gettempfromAPI()==gettempfromUI()) 
		 {
			 System.out.println("Success:temperature fetched from API and UI is same");
		 }
		 else 
		 {
			 System.out.println("Failure:temperature fetched from API and UI is not same");
		 }
			 
	 }
	
}
