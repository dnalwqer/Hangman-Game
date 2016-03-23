
public class Game {
	public String gameStatus;
	public int remain;
	public String token;
	public String state;
	
	public String toString() {
		return "status: " + gameStatus + ", token: " + token + ", remaining_guesses: " + remain + ", state: " + state;
	}
}
