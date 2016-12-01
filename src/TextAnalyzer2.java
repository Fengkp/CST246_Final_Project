import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextAnalyzer2 extends Helper {

    public TextAnalyzer2(String text) {
        super(text);
    }

    @Override
    public long[] getCounts() {
        long wordCount = 0;
        long sentencecount = 0;
        long syllableCount = 0;
        long[] countList = new long[3];

        Pattern wordCountPattern = Pattern.compile("([^\\s]+)[a-zA-Z\\d]");
        Pattern sentenceCountPattern = Pattern.compile("[!.?]");
        Pattern syllableCountPattern = Pattern.compile("[aeiouyAEIOUY]+");

        String text = super.getText();
        Matcher wordMatcher = wordCountPattern.matcher(text);
        Matcher sentenceMatcher = sentenceCountPattern.matcher(super.getText());

        String word = "";
        String syllables = "";
        boolean wordFound = true;
        boolean sentenceFound = true;


        while (wordFound == true || sentenceFound == true) {
            if (wordMatcher.find()) {
                word = wordMatcher.group();
                wordCount++;
                Matcher syllableMatcher = syllableCountPattern.matcher(word);
                if (syllableMatcher.find()) {
                    syllables = syllableMatcher.group();
                    syllableCount += syllables.length();
                }
                if (word.charAt(word.length() - 1) == 'e')
                    syllableCount--;
            }
            else wordFound = false;

            if (sentenceMatcher.find())
                sentencecount++;
            else sentenceFound = false;
        }

        countList[0] = wordCount;
        countList[1] = sentencecount;
        countList[2] = syllableCount;
        return countList;

    }

    @Override
    public long getNumberOfWords() {
        return 0;
    }

    @Override
    public long getNumberOfSentences() {
        return 0;
    }

    @Override
    public long getNumberOfSyllables() {return 0; }
}
