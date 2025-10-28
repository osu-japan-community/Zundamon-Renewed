package mames1.community.japan.osu.object;

import lombok.Getter;

import java.util.List;

@Getter
public class BannedWord {

    List<String> banWords;

    public BannedWord() {

       loadBanWords();
    }

    private void loadBanWords() {

        MySQL mySQL = new MySQL();
        banWords = mySQL.getBanwords();
    }
}
