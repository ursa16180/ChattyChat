import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;

public class App {

	public static void main(String[] args) {
		//vrniVpisane();
		//vpisiMe("Urša16180");
		//preberi("Urša16180");
		//poslji("Urša16180", "A kdo ve kaj delamo?");
		//izpisiMe("Urša16180");
	        try {
	            String hello = Request.Get("http://chitchat.andrej.com")
	                                  .execute()
	                                  .returnContent().asString();
	            System.out.println(hello);
	        } catch (IOException e) {
	            e.printStackTrace();
	}

	}

	public static void vrniVpisane(){
		try {
			String vpisani = Request.Get("http://chitchat.andrej.com/users").execute().returnContent().asString();
			System.out.println(vpisani);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void vpisiMe(String mojeIme){
		try {
			URI uri = new URIBuilder("http://chitchat.andrej.com/users").addParameter("username", mojeIme).build();
			String responseBody = Request.Post(uri).execute().returnContent().asString();
			System.out.println(responseBody);
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void izpisiMe(String mojeIme){
		try {
			URI uri = new URIBuilder("http://chitchat.andrej.com/users")
			          .addParameter("username", mojeIme)
			          .build();
			
			String responseBody = Request.Delete(uri)
                    .execute()
                    .returnContent()
                    .asString();
			System.out.println(responseBody);
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		
	public static void preberi(String mojeIme){
			try {
				URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
				          .addParameter("username", mojeIme)
				          .build();
				
				  String responseBody = Request.Get(uri)
                          .execute()
                          .returnContent()
                          .asString();
				  System.out.println(responseBody);
				  
			} catch (URISyntaxException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			}
	public static void poslji(String mojeIme, String sporocilo){
		try {
			URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
			          .addParameter("username", mojeIme)
			          .build();
			  String message = "{ \"global\" : true, \"text\" : \" "+ sporocilo +"\"  }";

			  String responseBody = Request.Post(uri)
			          .bodyString(message, ContentType.APPLICATION_JSON)
			          .execute()
			          .returnContent()
			          .asString();

			  System.out.println(responseBody);
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
		  
}
