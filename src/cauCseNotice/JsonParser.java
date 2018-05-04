package cauCseNotice;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

	public static String getSenderAddress() throws JSONException, IOException {
		byte[] jsonData = Files.readAllBytes(Paths.get("config/senderAddress.json"));
		String jsonString = new String(jsonData, StandardCharsets.UTF_8);
		JSONObject object = new JSONObject(jsonString);
		return object.getString("address");
	}
	public static String getSenderPassword() throws JSONException, IOException {
		byte[] jsonData = Files.readAllBytes(Paths.get("config/senderAddress.json"));
		String jsonString = new String(jsonData, StandardCharsets.UTF_8);
		JSONObject object = new JSONObject(jsonString);
		return object.getString("password");
	}

	public static ArrayList<Post> getPosts() throws JSONException, IOException {
		byte[] jsonData = Files.readAllBytes(Paths.get("data/data.json"));
		String jsonString = new String(jsonData, StandardCharsets.UTF_8);
		JSONObject object = new JSONObject(jsonString);

		JSONArray ict = object.getJSONArray("ict");
		JSONArray cse = object.getJSONArray("cse");
		JSONArray sw = object.getJSONArray("sw");

		ArrayList<Post> ictPosts = jsonArrayToPosts(ict);
		ArrayList<Post> csePosts = jsonArrayToPosts(cse);
		ArrayList<Post> swPosts = jsonArrayToPosts(sw);

		ArrayList<Post> posts = combinePost(ictPosts, csePosts, swPosts);

		return posts;
	}

	private static ArrayList<Post> combinePost(ArrayList<Post> ictPosts, ArrayList<Post> csePosts,
			ArrayList<Post> swPosts) {
		ArrayList<Post> result = new ArrayList<Post>();
		for(Post post : ictPosts) {
			post.setType(Post.ICT_TYPE_NAME);
			result.add(post);
		}
		for(Post post : csePosts) {
			post.setType(Post.CSE_TYPE_NAME);
			result.add(post);
		}
		for(Post post : swPosts) {
			post.setType(Post.SW_TYPE_NAME);
			result.add(post);
		}
		return result;
	}

	private static ArrayList<Post> jsonArrayToPosts(JSONArray jsonArray) throws JSONException {
		ArrayList<Post> result = new ArrayList<Post>();
		for(int i=0;i<jsonArray.length();i++) {
			result.add(new Post((JSONObject) jsonArray.get(i)));
		}
		return result;
	}
}
