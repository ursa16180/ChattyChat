import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class KomunikacijaServer {

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
			System.out.println(responseBody); // Sporoči, če uspešno vpisan
			ChattyChat.chatFrame.setMojStatus(true);
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
			ChattyChat.chatFrame.setMojStatus(false);
		} catch (URISyntaxException | IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<Sporocilo> sprejemSporocil(String mojeIme)
			throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/messages").addParameter("username", mojeIme).build();
		String responseBody = Request.Get(uri).execute().returnContent().asString();

		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new ISO8601DateFormat()); // Vsi časi na serverju v takem formatu
		List<Sporocilo> seznamSporocil = mapper.readValue(responseBody, new TypeReference<List<Sporocilo>>() {
		});
		return seznamSporocil;
	}

	public static void posljiJavno(String jaz, String sporocilo) {
		try {
			URI uri = new URIBuilder("http://chitchat.andrej.com/messages").addParameter("username", jaz).build();
			String message = "{ \"global\" : true, \"text\" : \" " + sporocilo + "\"  }";
			String responseBody = Request.Post(uri).bodyString(message, ContentType.APPLICATION_JSON).execute()
					.returnContent().asString();
			System.out.println(responseBody);
		} catch (URISyntaxException | IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void posljiZasebno(String jaz, String dopisovalec, String sporocilo) {
		try {
			URI uri = new URIBuilder("http://chitchat.andrej.com/messages").addParameter("username", jaz).build();
			String message = "{ \"global\" : false, \"recipient\" : \"" + dopisovalec + "\", \"text\" : \" " + sporocilo
					+ "\"  }";
			String responseBody = Request.Post(uri).bodyString(message, ContentType.APPLICATION_JSON).execute()
					.returnContent().asString();
			System.out.println(responseBody);
		} catch (URISyntaxException | IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}
}
