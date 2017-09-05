import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class App {

	/*
	 * public static void main(String[] args) { // vrniVpisane(); //
	 * vpisiMe("uporabnik"); // System.out.println(preberi2("uporabnik")); //
	 * izpisiMe("uporabnik"); // System.out.println(preberi("Urša16180")); //
	 * poslji("Urša16180", "A kdo ve kaj delamo?"); // izpisiMe("Urša16180");
	 * 
	 * // Date datum = new Date(); // Sporocilo poskus2 = new Sporocilo (false,
	 * "miki","pluto","blah", datum); // Sporocilo poskus1 = new Sporocilo (false,
	 * "miki","pluto","buu", datum); // List<Sporocilo> seznamcic= new
	 * ArrayList<Sporocilo>(); // seznamcic.add(poskus1); // seznamcic.add(poskus2);
	 * // try { String hello =
	 * Request.Get("http://chitchat.andrej.com").execute().returnContent().asString(
	 * ); System.out.println(hello); } catch (IOException e) { e.printStackTrace();
	 * }
	 * 
	 * }
	 */

	public static List<Uporabnik> vrniVpisane() throws ClientProtocolException, IOException {
		String vpisani = Request.Get("http://chitchat.andrej.com/users").execute().returnContent().asString();

		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new ISO8601DateFormat()); // Vsi časi na serverju v takem formatu
		List<Uporabnik> seznamUporabnikov = mapper.readValue(vpisani, new TypeReference<List<Uporabnik>>() {
		});
		return seznamUporabnikov;

	}

	public static void vpisiMe(String mojeIme) {
		try {
			URI uri = new URIBuilder("http://chitchat.andrej.com/users").addParameter("username", mojeIme).build();
			String responseBody = Request.Post(uri).execute().returnContent().asString();
			System.out.println(responseBody);
			ChitChat.chatFrame.setMojStatus(true);
		} catch (URISyntaxException | IOException e) {
			System.out.println("Oseba s tem vzdevkom je že vpisana.");
			e.printStackTrace();
		}

	}

	public static void izpisiMe(String mojeIme) {
		try {
			URI uri = new URIBuilder("http://chitchat.andrej.com/users").addParameter("username", mojeIme).build();

			String responseBody = Request.Delete(uri).execute().returnContent().asString();
			System.out.println(responseBody);
			ChitChat.chatFrame.setMojStatus(false);

		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<Sporocilo> preberi(String mojeIme)
			throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/messages").addParameter("username", mojeIme).build();
		String responseBody = Request.Get(uri).execute().returnContent().asString();
		// System.out.print(responseBody);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new ISO8601DateFormat()); // Vsi časi na serverju v takem formatu
		List<Sporocilo> seznamSporocil = mapper.readValue(responseBody, new TypeReference<List<Sporocilo>>() {
		});
		//System.out.println(seznamSporocil.size());
		return seznamSporocil;

	}

	public static void poslji(String mojeIme, String sporocilo) {
		try {
			URI uri = new URIBuilder("http://chitchat.andrej.com/messages").addParameter("username", mojeIme).build();
			String message = "{ \"global\" : true, \"text\" : \" " + sporocilo + "\"  }";

			String responseBody = Request.Post(uri).bodyString(message, ContentType.APPLICATION_JSON).execute()
					.returnContent().asString();

			System.out.println(responseBody);
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void posljiZasebno(String jaz, String dopisovalec, String text) {
		try {
			URI uri = new URIBuilder("http://chitchat.andrej.com/messages").addParameter("username", jaz).build();
			String message = "{ \"global\" : false, \"recipient\" : \"" + dopisovalec + "\", \"text\" : \" " + text
					+ "\"  }";

			String responseBody = Request.Post(uri).bodyString(message, ContentType.APPLICATION_JSON).execute()
					.returnContent().asString();
			//System.out.println("A pride sm?");

			System.out.println(responseBody);
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * public static String preberi2(String mojeIme) { URI uri; try { uri = new
	 * URIBuilder("http://chitchat.andrej.com/messages").addParameter("username",
	 * "miki").build();
	 * 
	 * String responseBody = Request.Get(uri).execute().returnContent().asString();
	 * return (responseBody); } catch (URISyntaxException e) {
	 * 
	 * e.printStackTrace(); return new String(); } catch (ClientProtocolException e)
	 * { // TODO Auto-generated catch block e.printStackTrace(); return new
	 * String(); } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); return new String(); }
	 * 
	 * }
	 */
}
