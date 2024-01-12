package mert.dev.scoreboard;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import mert.dev.scoreboard.domain.Game;
import mert.dev.scoreboard.domain.Score;

public class ConcurrentScoreBoard implements ScoreBoard {

    private static final ConcurrentScoreBoard concurrentScoreBoard = new ConcurrentScoreBoard();
    private final Map<Game, Score> scoreBoard;

    private ConcurrentScoreBoard() {
        scoreBoard = new ConcurrentHashMap<>();
    }

    public static ConcurrentScoreBoard getInstance() {
        return concurrentScoreBoard;
    }

    @Override
    public boolean startGame(String homeTeam, String awayTeam) {
        validateTeamNames(homeTeam, awayTeam);
        Game newGame = new Game(homeTeam, awayTeam);
        Score initialScore = new Score();
        Score score = scoreBoard.putIfAbsent(newGame, initialScore);
        if (score == null) {
            return true;
        } else {
            System.out.println(STR."The game between \{homeTeam} and \{awayTeam} is already live, you cannot start new game");
            return false;
        }
    }

    @Override
    public boolean finishGame(String homeTeam, String awayTeam) {
        validateTeamNames(homeTeam, awayTeam);
        Game game = new Game(homeTeam, awayTeam);
        if (scoreBoard.remove(game) == null) {
            System.out.println(STR."There is no such live game between \{homeTeam} and \{awayTeam}");
            return false;
        }
        return true;
    }

    @Override
    public boolean updateScore(String homeTeam, String awayTeam, int homeTeamScore, int awayTeamScore) {
        validateTeamNames(homeTeam, awayTeam);
        validateScores(homeTeamScore, awayTeamScore);
        Game game = new Game(homeTeam, awayTeam);
        if (scoreBoard.containsKey(game)) {
            Score score = new Score(homeTeamScore, awayTeamScore);
            scoreBoard.replace(game, score);
            return true;
        } else {
            System.out.println(STR."There is no such live game between \{homeTeam} and \{awayTeam}");
            return false;
        }
    }

    private void validateTeamNames(String homeTeam, String awayTeam) {
        Objects.requireNonNull(homeTeam, "Please provide home team name");
        Objects.requireNonNull(awayTeam, "Please provide away team name");
    }

    private void validateScores(int homeTeamScore, int awayTeamScore) {
        if (homeTeamScore < 0) {
            throw new RuntimeException(STR."Home team score cannot be minus value: \{homeTeamScore}");
        }
        if (awayTeamScore < 0) {
            throw new RuntimeException(STR."Away team score cannot be minus value: \{awayTeamScore}");
        }
    }

    @Override
    public List<Map.Entry<Game, Score>> getSummary() {
        return scoreBoard.entrySet().stream()
                .sorted((e1, e2) -> { // first sort according to totalScore if totalScores are equal sort according to last update
                    int result = e2.getValue().totalScore() - e1.getValue().totalScore();
                    if (result == 0) {
                        return (int) (e2.getValue().updatedAt() - e1.getValue().updatedAt());
                    }
                    return result;
                })
                .toList();
    }
}
