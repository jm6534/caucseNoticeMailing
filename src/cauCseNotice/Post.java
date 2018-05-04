package cauCseNotice;

import org.json.JSONException;
import org.json.JSONObject;

public class Post {
	private String type;
	private String url;
	private String title;
	private String last_update;

	public static final String ICT_TYPE_NAME = "창의ICT공과대학";
	public static final String CSE_TYPE_NAME = "소프트웨어학부";
	public static final String SW_TYPE_NAME = "다빈치SW교육원";

	public Post(JSONObject jsonObject) throws JSONException {
		this.url = jsonObject.getString("url");
		this.title = jsonObject.getString("title");
		this.last_update = jsonObject.getString("last_update");
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLast_update() {
		return last_update;
	}
	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public static String cleanXSS(String value) {
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		value = value.replaceAll("'", "&#39;");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		value = value.replaceAll("script", "");
		return value;
	}
}
