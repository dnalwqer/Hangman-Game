import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Hangman {
	
	//record the correspondence between the length of a word and the word
	private Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
	//store the wrong guess characters
	private Set<Character> wrongChar = new HashSet<Character>();
	//store the right guess characters
	private Set<Character> correctChar = new HashSet<Character>();
	
	//Constructer
	//loop over the whole dictionary and establish the correspondence between the length and the word
	//store in the hashmap
	public Hangman() {
		File file = new File("./dictionary.txt");
		FileReader f;
		BufferedReader br = null;
		try {
			f = new FileReader(file);
			br = new BufferedReader(f);
			String word = null;
			while ((word = br.readLine()) != null) {
				int len = word.length();
				if (map.containsKey(len)) {
					map.get(len).add(word);
				}
				else {
					List<String> list = new ArrayList<String>();
					map.put(len, list);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * guess a character
	 * @parm game the game instance
	 * @return character to be guessed
	 */
	public char makeGuess(Game game) {
		/*
		 * Idea: Given a sentence that represents the state, we need to fill in each word in the sentence 
		 * First, we calculate the length of each word and try to find the words with the same length in the dictionary.
		 * And use the regex to match them.
		 * 
		 * For all of these words, record every letter that occurs in them, weighted
		 * by (the frequency of the word / the number of words of that length).
		 * Then we choose the largest weight character that is not in the correct list and wrong list and guess it
		 */
		String state = game.state.trim();
		//split the state into many words.
		String []res = state.split("\\s+");
		
		//record the total frequency of each character
		double []global = new double[26];
		for (int i = 0; i < res.length; i++) {
			String word_state = res[i];
			//if the word does not contain _, continue
			if (!word_state.contains("_")) continue;
			int len = word_state.length();
			//get the list corresponding to the length
			List<String> wordlist = map.get(len);
			
			StringBuilder sb = new StringBuilder();
			for (char c : wrongChar) {
				sb.append(c);
			}
			
			//Now use regex to match the state with the words in the dictionary
			String regex = null;
			if (wrongChar.size() == 0) {
				//no wrong guess
				regex = word_state.replace("_", "[A-Z]");
			}
			else {
				//have wrong guess and need to throw away them
				regex = word_state.replace("_", String.format("[A-Z&&[^%s]]", sb.toString()));
			}
			
			Pattern pattern = Pattern.compile(regex);
			//store the possible string words
			List<String> candidates = new ArrayList<String>();
			if (wordlist != null) {
				for (String item : wordlist) {
					Matcher m = pattern.matcher(item);
					//match the pattern and store in the possible list.
					if (m.find()) {
						candidates.add(item);
					}
				}	
			}
			
			//build character frequency for the current possible word list
			double []curfreq = new double[26];
			for (int j = 0; j < candidates.size(); j++) {
				String str = candidates.get(j);
				for (int k = 0; k < str.length(); k++) {
					int index = str.charAt(k) - 'A';
					//we only focus the letters.
					if (index < 0 || index >= curfreq.length) continue;
					curfreq[index]++;
				}
			}
			for (int j = 0; j < curfreq.length; j++) {
				//get the weight
				double priority = 1.5 * curfreq[j] / candidates.size();
				//accumulate the result for each word
				global[j] += priority;
			}
		}
		
		for (char c : wrongChar) {
			int index = c - 'A';
			if (index < 0 || index >= global.length) continue;
			//when the character is in the wrongChar, ignore
			global[index] = 0;		
		}
		for (char c : correctChar) {
			int index = c - 'A';
			if (index < 0 || index >= global.length) continue;
			//when the character is in the correctChar, ignore
			global[index] = 0;		
		}
		
		//find the character with the largest weight
		double maxCount = 0;
		char result = 'A';
		boolean hasMatch = false;
		for (int i = 0; i < global.length; i++) {
			if (global[i] > maxCount) {
				char c = (char)('A' + i);
				if (correctChar.contains(c) || wrongChar.contains(c)) continue;
				maxCount = global[i];
				result = c;
				hasMatch = true;
			}
		}
		
		//if no character, we will return the character with the largest frequency.
		//the list is obtained by wikipedia
		String order = "ETAONRISHDLFCMUGYPWBVKJXQZ";
		if (!hasMatch) {
			for (int i = 0; i < order.length(); i++) {
				char c = order.charAt(i);
				if (!wrongChar.contains(c) && !correctChar.contains(c)) {
					return c;
				}
			}
		}
		return result;
	}
	
	/*
	 * update the correctChar list and wrongChar list
	 * @parm guess the guess character
	 * @parm state the state of the sentence
	 */
	public void changeList(char guess, boolean state) {
		if (state) {
			correctChar.add(guess);
		}
		else {
			wrongChar.add(guess);
		}
	}
}
