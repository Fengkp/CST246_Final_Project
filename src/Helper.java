
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Helper {

    private String text;

    public Helper(String text) {
        this.text = text;
    }

    public List<String> getTokens(String pattern) {
        ArrayList<String> tokens = new ArrayList<>();
        Pattern tokenSplitter = Pattern.compile(pattern);
        Matcher matcher = tokenSplitter.matcher(text);

        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
    }

    public int countSyllables(String word) {
        int num = 0;
        String pattern = "[aeiouyAEIOUY]+";
        Pattern tokenSplitter = Pattern.compile(pattern);
        Matcher matcher = tokenSplitter.matcher(word);

        while (matcher.find())
            ++num;
		if (word.charAt(word.length() - 1) == 'e')
			num--;
        return num;
    }

    public abstract long[] getCounts();

    public abstract long getNumberOfWords();

    public abstract long getNumberOfSentences();

    public abstract long getNumberOfSyllables();

    public String getText() {
        return text;
    }

    public double getFleschScore() {
        double score = 206.835 - (1.015 * ((double)getNumberOfWords() / (double)getNumberOfSentences()))
                - (84.6 * (double)getNumberOfSyllables() / (double)getNumberOfWords());

        return score;
    }
}
