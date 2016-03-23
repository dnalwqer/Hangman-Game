
public class HangmanGame {
	public static void main(String []args) {
		int successful = 0;
		int total = 0;
		while (true) {
			Game game = GetInfo.startGame();
			Hangman hangman = new Hangman();
			total++;
			System.out.println("********Game "+ total + "********");
			//when the status of the game is alive, stay in the loop
			while (game.gameStatus.equals("ALIVE")) {
				char guess = hangman.makeGuess(game);
				
				//make a guess using the guess char
				Game g = GetInfo.guess(game, guess);
				if (g.gameStatus.equals("ALIVE")) {
					System.out.println(game.state);
					//the state does not change, so the guess is wrong
					if (g.state.equals(game.state)) {
						hangman.changeList(guess, false);
						System.out.println("Your guess " + guess + " is wrong. The remaining guesses is " + g.remain);
					}
					//the state changes, the guess is correct
					else {
						hangman.changeList(guess, true);
						System.out.println("Your guess " + guess + " is true.");
					}	
				}
				//game over
				else if (g.gameStatus.equals("DEAD")) {
					System.out.println(game.state);
					System.out.println("Your guess " + guess + " is wrong. The remaining guesses is " + g.remain);
					System.out.println(g.state);
					System.out.println("Unsuccessful!");
				}
				//game success
				else if (g.gameStatus.equals("FREE")) {
					System.out.println(game.state);
					System.out.println("Your guess " + guess + " is right.");
					System.out.println(g.state);
					System.out.println("Successful");
					successful++;
				}		
				game = g;
			}
			//record the accurancy rate
			System.out.println("Accurancy rate: " + successful * 1.0 / total);
			System.out.println();
		}
	}
}
