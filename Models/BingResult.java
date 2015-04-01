package Models;

public class BingResult {
	String title;
	String description;
	String url;
	
	public void setDesciption(String description){
		this.description = description;
	}
	
	public String getDesciption(){
		return this.description;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getUrl(){
		return this.url;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public BingResult(String title, String description,String url){
		this.title = title;
		this.description = description;
		this.url = url;
	}	
}
