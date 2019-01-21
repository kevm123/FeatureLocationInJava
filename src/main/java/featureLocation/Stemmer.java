package featureLocation;

public class Stemmer {

	private String word;
	private String output;
	private int wLength;

	public String stem(String input) {
		word = input;
		wLength = word.length();
		if (wLength < 2)
			return word;
		else {
			output = word;
			checkEndings();
			word = output;
			wLength = word.length();
			removeEnding();
			word = output;
			wLength = word.length();
			if(wLength>2)
				checkDouble();

			return output;
		}
	}

	private void checkEndings() {
		if (word.charAt(wLength - 1) == 's') {
			if (end("iveness"))
				output = word.substring(0, wLength - 5);
			else if (end("fulness"))
				output = word.substring(0, wLength - 4);
			else if (end("ousness"))
				output = word.substring(0, wLength - 4);
			else if (end("ness"))
				output = word.substring(0, wLength - 4);
			else if (end("sses"))
				output = word.substring(0, wLength - 4);
			else if (end("ies"))
				output = word.substring(0, wLength - 3);
			else if (end("ss"))
				output = word;
			else if (end("s"))
				output = word.substring(0, wLength - 1);
		}

		// step 2
		else if (end("eed")) {
			if (canStem(3))
				output = word.substring(0, wLength - 1);
			else
				output = word;
		}

		else if (end("ed")) {
			if (canStem(2)) {
				output = word.substring(0, wLength - 2);
			} else
				output = word;
		}

		else if (end("ing")) {
			if (canStem(3)) {
				output = word.substring(0, wLength - 3);
				word = output;
			} else
				output = word;
		}

		// step 3
		else if (word.charAt(wLength - 2) == 'a') {
			if (end("ational"))
				output = word.substring(0, wLength - 5);
			else if (end("tional"))
				output = word.substring(0, wLength - 2);
			else if (end("al"))
				output = word.substring(0, wLength - 2);
		}

		else if (word.charAt(wLength - 2) == 'c') {
			if (end("ency"))
				output = word.substring(0, wLength - 2);
			else if (end("ancy"))
				output = word.substring(0, wLength - 2); // hesitancy ->
															// hesitan(t)
		}

		else if (word.charAt(wLength - 2) == 'e') {
			if (end("izer"))
				output = word.substring(0, wLength - 2);
		}

		else if (word.charAt(wLength - 2) == 'l') {
			if (end("ably"))
				output = word.substring(0, wLength - 4);
			else if (end("ally"))
				output = word.substring(0, wLength - 2);
			else if (end("ently"))
				output = word.substring(0, wLength - 2);
			else if (end("ely"))
				output = word.substring(0, wLength - 2);
			else if (end("ible"))
				output = word.substring(0, wLength - 4);
			else if (end("able"))
				output = word.substring(0, wLength - 4);
			else if (end("ously"))
				output = word.substring(0, wLength - 2);
		}

		else if (word.charAt(wLength - 2) == 'o') {
			if (end("ization"))
				output = word.substring(0, wLength - 5);
			else if (end("tion"))
				output = word.substring(0, wLength - 3);
			else if (end("ator"))
				output = word.substring(0, wLength - 2);
		}

		else if (word.charAt(wLength - 2) == 's') {
			if (end("alism"))
				output = word.substring(0, wLength - 3);
		}

		else if (word.charAt(wLength - 2) == 't') {
			if (end("ality"))
				output = word.substring(0, wLength - 3);
			else if (end("ivity"))
				output = word.substring(0, wLength - 3);
			else if (end("ate"))
				output = word.substring(0, wLength - 3);
			else if (end("bility"))
				output = word.substring(0, wLength - 5);
		}

		// step 4
		else if (word.charAt(wLength - 1) == 'e') {
			if (end("icate"))
				output = word.substring(0, wLength - 3);
			else if (end("ative"))
				output = word.substring(0, wLength - 3);
			else if (end("alize"))
				output = word.substring(0, wLength - 3);
		}

		else if (word.charAt(wLength - 1) == 'i') {
			if (end("icity"))
				output = word.substring(0, wLength - 3);
		}

		else if (word.charAt(wLength - 1) == 'l') {
			if (end("ical"))
				output = word.substring(0, wLength - 2);
			else if (end("ful"))
				output = word.substring(0, wLength - 3);
		}

	}

	private boolean end(String s) {
		int length = s.length();
		if (word.length() > length) {
			for (int i = 0; i < length; i++) {
				if ((word.charAt(word.length() - (length - i))) != s.charAt(i)) {
					return false;
				}
			}
			return true;
		} else
			return false;
	}

	private boolean canStem(int i) {
		boolean value = word.substring(0, wLength - i).matches("(.*[aeiou].*)");
		return value;
	}

	private void removeEnding() {
		if (end("y"))
			output = output.substring(0, wLength - 1);
		else if (end("a"))
			output = output.substring(0, wLength - 1);
		else if (end("er"))
			output = output.substring(0, wLength - 2);
		else if (end("e"))
			output = output.substring(0, wLength - 1);
		else if (end("i"))
			output = output.substring(0, wLength - 1);
		else if (end("o"))
			output = output.substring(0, wLength - 1);
		else if (end("u"))
			output = output.substring(0, wLength - 1);
	}

	private void checkDouble() {
		if (output.charAt(output.length() - 1) == output.charAt(output.length() - 2)
		/*
		 * && (!end("ll") && !end("ss") && !end("zz"))
		 */
		)
			output = output.substring(0, output.length() - 1);
	}

}
