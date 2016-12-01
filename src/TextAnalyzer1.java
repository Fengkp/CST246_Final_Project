import java.util.List;

public class TextAnalyzer1 extends Helper {

    public TextAnalyzer1(String text) {
        super(text);
    }

    @Override
    public long[] getCounts() {
        return new long[0];
    }

    @Override
    public long getNumberOfWords() {
        return super.getTokens("([^\\s]+)[a-zA-Z\\d]").size();
    }

    @Override
    public long getNumberOfSentences() {
        List<String> sentences = super.getTokens("[!.?]");

        return sentences.size();

    }

    @Override
    public long getNumberOfSyllables() {
        int counter = 0;
        List<String> words = super.getTokens("([^\\s]+)[a-zA-Z\\d]");

        for (int i = 0; i < words.size(); i++) {
            counter += super.countSyllables(words.get(i));
        }
        return counter;
    }

}
