package mert.dev.scoreboard.domain;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Score {

    private final int homeTeamScore;
    private final int awayTeamScore;
    private final long updatedAt;

    public Score() {
        this.homeTeamScore = 0;
        this.awayTeamScore = 0;
        this.updatedAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public Score(int homeTeamScore, int awayTeamScore) {
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
        this.updatedAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public int homeTeamScore() {
        return homeTeamScore;
    }

    public int awayTeamScore() {
        return awayTeamScore;
    }

    public int totalScore() {
        return homeTeamScore + awayTeamScore;
    }

    public long updatedAt() {
        return updatedAt;
    }
}
