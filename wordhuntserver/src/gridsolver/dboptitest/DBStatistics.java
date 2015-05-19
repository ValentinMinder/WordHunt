package gridsolver.dboptitest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * Test class for DB contents (frequencies, nb of letters).
 */
public class DBStatistics {

	private final static String path = "src" + File.separator + "gridsolver"
			+ File.separator + "dboptitest" + File.separator;

	private static TreeMap<String, TreeMap<String, Integer>> enclosing;
	private static TreeMap<String, Integer> starts1;
	private static TreeMap<String, Integer> containing1;
	private static TreeMap<String, Integer> composing1;
	private static TreeMap<String, Integer> ends1;
	private static TreeMap<String, Integer> starts2;
	private static TreeMap<String, Integer> containing2;
	private static TreeMap<String, Integer> composing2;
	private static TreeMap<String, Integer> ends2;
	private static TreeMap<String, Integer> starts3;
	private static TreeMap<String, Integer> containing3;
	private static TreeMap<String, Integer> composing3;
	private static TreeMap<String, Integer> ends3;

	private static TreeMap<String, Integer> lengthOfWords;

	/**
	 * @param args
	 * @throws SQLException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws SQLException,
			FileNotFoundException, IOException {
//		testWithDB();
		testWithFile();
	}

	public static void initStructures() {
		enclosing = new TreeMap<>();
		starts1 = new TreeMap<>();
		containing1 = new TreeMap<>();
		composing1 = new TreeMap<>();
		ends1 = new TreeMap<>();
		starts2 = new TreeMap<>();
		containing2 = new TreeMap<>();
		composing2 = new TreeMap<>();
		ends2 = new TreeMap<>();
		starts3 = new TreeMap<>();
		containing3 = new TreeMap<>();
		composing3 = new TreeMap<>();
		ends3 = new TreeMap<>();

		// Initialization to 0 for ALL keys of 1 to 3 ASCII A-Z letters
		// insertion in reverse order for better balanced TreeMaps
		// (AAA, BAA, CAA, ... ZAA, ABA, BBA, CBA,...)
		String current = "";
		for (int i = 'A'; i <= 'Z'; i++) {
			current = "" + (char) i + "";
			starts1.put(current, 0);
			ends1.put(current, 0);
			containing1.put(current, 0);
			composing1.put(current, 0);
			// System.out.println(current);
			for (int j = 'A'; j <= 'Z'; j++) {
				current = "" + (char) j + "" + (char) i + "";
				starts2.put(current, 0);
				ends2.put(current, 0);
				containing2.put(current, 0);
				composing2.put(current, 0);
				// System.out.println(current);
				for (int k = 'A'; k <= 'Z'; k++) {
					current = "" + (char) k + "" + (char) j + "" + (char) i
							+ "";
					starts3.put(current, 0);
					ends3.put(current, 0);
					containing3.put(current, 0);
					composing3.put(current, 0);
					// System.out.println(current);
				}
			}
		}

		enclosing.put("LENGTH 1 - 1-starts", starts1);
		enclosing.put("LENGTH 1 - 2-ends", ends1);
		enclosing.put("LENGTH 1 - 3-contains", containing1);
		enclosing.put("LENGTH 2 - 1-starts", starts2);
		enclosing.put("LENGTH 2 - 2-ends", ends2);
		enclosing.put("LENGTH 2 - 3-contains", containing2);
		enclosing.put("LENGTH 3 - 1-starts", starts3);
		enclosing.put("LENGTH 3 - 2-ends", ends3);
		enclosing.put("LENGTH 3 - 3-contains", containing3);

		enclosing.put("LENGTH 1 - 4-composed", composing1);
		enclosing.put("LENGTH 2 - 4-composed", composing2);
		enclosing.put("LENGTH 3 - 4-composed", composing3);

		lengthOfWords = new TreeMap<>();

		for (int i = 0; i < 27; i++) {
			lengthOfWords.put(i + "", 0);
		}

		enclosing.put("LENGTH 0 - length of words", lengthOfWords);
	}

	private static void printStructures(PrintStream stream) {
		Iterator<Entry<String, TreeMap<String, Integer>>> iter = enclosing
				.entrySet().iterator();
		Entry<String, TreeMap<String, Integer>> entry;
		while (iter.hasNext()) {
			entry = iter.next();
			stream.println(entry.getKey());
			print(entry.getValue().entrySet().iterator(), stream);
		}
	}

	private static void testWithFile() throws FileNotFoundException,
			IOException {

		initStructures();

		BufferedReader reader = new BufferedReader(new FileReader(new File(path
				+ "motsByLine.txt")));

		// double
		boolean detectDoubles = false;
		int nbDoublons = 0;
		Set<String> strings = new HashSet<>();
		TreeMap<String, Integer> doub = new TreeMap<String, Integer>();

		// read line by line
		String a, b, c;
		int length;
		int size = 0;
		String line = "";
		label: while ((line = reader.readLine()) != null) {
			size++;

			// doubles
			if (detectDoubles) {
				if (doub.containsKey(line)) {
					doub.put(line, doub.get(line) + 1);
				} else {
					doub.put(line, 1);
				}

				if (strings.contains(line)) {
					// System.err.println(line);
					nbDoublons++;
					continue label;
				}
				strings.add(line);
			}

			// line length
			length = line.length();
			lengthOfWords.put(length + "", lengthOfWords.get(length + "") + 1);
			if (length < 3) {
				System.err.println("Length < 3: " + line);
				continue label;
			}

			// invalid chars detection and replacement
			for (int i = 0; i < line.length(); i++) {
				char charAt = line.charAt(i);
				if (charAt < 'A' || charAt > 'Z') {
					System.out.println("Invalid char: '" + charAt
							+ "' in word: " + line);
					char next = '\0';
					switch (charAt) {
					case 'Ë':
						next = 'E';
						break;
					case 'Ü':
						next = 'U';
						break;
					case 'Ö':
						next = 'O';
						break;
					case 'À':
					case 'Ä':
						next = 'A';
						break;
					default:
						System.err.println("Invalid char: '" + charAt
								+ "' in word: " + line);
					}
					line = line.replace(charAt, next);
				}
			}

			// starts of words
			a = line.substring(0, 1);
			b = line.substring(0, 2);
			c = line.substring(0, 3);
			starts1.put(a, starts1.get(a) + 1);
			starts2.put(b, starts2.get(b) + 1);
			starts3.put(c, starts3.get(c) + 1);

			// ends of words
			a = line.substring(length - 1);
			b = line.substring(length - 2);
			c = line.substring(length - 3);

			ends1.put(a, ends1.get(a) + 1);
			ends2.put(b, ends2.get(b) + 1);
			ends3.put(c, ends3.get(c) + 1);
			Set<String> set = new HashSet<String>();

			// middle of words (INCLUDING BEGIN & END)
			for (int i = 0; i <= length - 3; i++) {
				a = line.substring(i, i + 1);
				b = line.substring(i, i + 2);
				c = line.substring(i, i + 3);

				if (!set.contains(a)) {
					containing1.put(a, containing1.get(a) + 1);
				}
				if (!set.contains(b)) {
					containing2.put(b, containing2.get(b) + 1);
				}
				if (!set.contains(c)) {
					containing3.put(c, containing3.get(c) + 1);
				}

				composing1.put(a, composing1.get(a) + 1);
				composing2.put(b, composing2.get(b) + 1);
				composing3.put(c, composing3.get(c) + 1);
				set.add(a);
				set.add(b);
				set.add(c);
			}
			// three cases missing in for loop
			a = line.substring(length - 2, length - 1);
			composing1.put(a, composing1.get(a) + 1);
			if (!set.contains(a)) {
				containing1.put(a, containing1.get(a) + 1);
			}
			set.add(a);
			a = line.substring(length - 1, length);
			composing1.put(a, composing1.get(a) + 1);
			if (!set.contains(a)) {
				containing1.put(a, containing1.get(a) + 1);
			}
			set.add(a);
			b = line.substring(length - 2, length);
			composing2.put(b, composing2.get(b) + 1);
			if (!set.contains(a)) {
				containing2.put(b, containing2.get(b) + 1);
			}
			set.add(b);

		}
		System.out.println("size is: " + size);

		boolean printDoublons = false;
		if (printDoublons) {
			System.err.println("All doublons are: " + nbDoublons);
			Iterator<Entry<String, Integer>> iter = doub.entrySet().iterator();
			nbDoublons = 0;
			while (iter.hasNext()) {
				Entry<String, Integer> entry = iter.next();
				if (entry.getValue() >= 2) {
					// System.err.println(entry.getKey());
					nbDoublons++;
					// warning this may OVERSIZE the console !
					// if (doublons == 5000) {
					// break;
					// }
				}
			}
			System.err.println("Doublons are: " + nbDoublons);
		}

		PrintStream writer = new PrintStream(new File(path + "result.txt"));
		// printStructures(System.out);
		printStructures(writer);
		reader.close();

	}

	private static void testWithDB() throws SQLException {
		Connection connect = DriverManager.getConnection(
				"jdbc:mysql://localhost/wordhunt", "root", "root");
		Statement query;
		ResultSet rs;
		query = connect.createStatement();

		rs = query.executeQuery("SELECT COUNT(*) FROM Mot");
		while (rs.next()) {
			System.out.println(rs.getInt("COUNT(*)"));
			// System.out.println(rs.getString("config"));
		}

		PreparedStatement start = connect
				.prepareStatement("SELECT COUNT(*) FROM Mot WHERE mot LIKE (?)");

		initStructures();

		long time = System.currentTimeMillis();
		String current = "";
		for (int i = 'A'; i <= 'Z'; i++) {
			current = "" + (char) i + "";
			execute(rs, start, current, starts1, ends1, containing1);
			for (int j = 'A'; false && j <= 'Z'; j++) {
				current = "" + (char) i + "" + (char) j + "";
				execute(rs, start, current, starts2, ends2, containing2);
				for (int k = 'A'; false && k <= 'Z'; k++) {
					current = "" + (char) i + "" + (char) j + "" + (char) k;
					execute(rs, start, current, starts3, ends3, containing3);
				}
			}
		}

		time = System.currentTimeMillis() - time;
		System.out.println("Time used in ms: " + time);

		printStructures(System.out);
	}

	private static void print(Iterator<Entry<String, Integer>> it,
			PrintStream stream) {
		Entry<String, Integer> entry;
		while (it.hasNext()) {
			entry = it.next();
			stream.println(entry.getKey() + "\t" + entry.getValue());
		}
	}

	private static void execute(ResultSet rs, PreparedStatement start,
			String current, TreeMap<String, Integer> starts,
			TreeMap<String, Integer> ends, TreeMap<String, Integer> anywhere)
			throws SQLException {

		// detecting start of word
		start.setString(1, current + "%");
		rs = start.executeQuery();

		while (rs.next()) {
			starts.put(current, rs.getInt("COUNT(*)"));
		}

		// detecting end of word
		start.setString(1, "%" + current);
		rs = start.executeQuery();

		while (rs.next()) {
			ends.put(current, rs.getInt("COUNT(*)"));
		}

		// detecting middle of word
		start.setString(1, "%" + current + "%");
		rs = start.executeQuery();

		while (rs.next()) {
			anywhere.put(current, rs.getInt("COUNT(*)"));
		}

	}

}
