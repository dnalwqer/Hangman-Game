import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;


public class GetInfo {
	
	/*
	 * get the response
	 * @parm htmlurl the url string
	 * @parm s the argument e.g. &token=TOKEN&guess=GUESS
	 * @return the response representd by json string
	 */
	public static String getContent(String htmlurl, String s) {
		URL url;
		String tmp;
		StringBuffer sb = new StringBuffer();
		try {
			url = new URL(htmlurl + s);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			while ((tmp = in.readLine()) != null) {
				sb.append(tmp);
			}
			in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.err.println(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e);
		}
		return sb.toString();
	}
	
	/*
	 * start a game
	 * @parm htmlurl the url string
	 * @parm s the argument e.g. &token=TOKEN&guess=GUESS
	 * @return the Game object
	 */
	public static Game playGame(String htmlurl, String s) {
		String content = getContent(htmlurl, s);
		
		Game game = new Game();
		try {
			JSONObject json = new JSONObject(content);
			game.gameStatus = json.getString("status");
			game.token = json.getString("token");
			game.remain = json.getInt("remaining_guesses");
			game.state = json.getString("state");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return game;
	}
	
	//start the game
	public static Game startGame() {
		return playGame("http://gallows.hulu.com/play?code=weiqiang.liu.gr@dartmouth.edu", "");
	}
	
	//guess a character
	public static Game guess(Game game, char c) {
		return playGame("http://gallows.hulu.com/play?code=weiqiang.liu.gr@dartmouth.edu", "&token="+game.token+"&guess="+c);
	}
}
