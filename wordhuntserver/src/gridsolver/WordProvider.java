package gridsolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;

public class WordProvider {

	private final static String path = "src" + File.separator + "gridsolver"
			+ File.separator + "dboptitest" + File.separator;
	private static WordProvider instance;

	public static WordProvider getInstance() {
		if (instance == null) {
			instance = new WordProvider();
		}
		return instance;
	}

	private WordProvider() {
		load(false);
	}

	private TreeSet<String> strings;

	private void load(boolean b) {
		strings = new TreeSet<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					path + "motsByLine.txt")));
			String line;
			while ((line = reader.readLine()) != null) {
				strings.add(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isWord(String word) {
		return strings.contains(word);
	}

	public boolean continueWithSubstring(String substring) {
		if (substring.length() < 2) {
			return true;
		}
		String high = strings.higher(substring);
		if (high == null) {
			return false;
		}
		return high.startsWith(substring);
	}
}