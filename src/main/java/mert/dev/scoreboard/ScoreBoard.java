package mert.dev.scoreboard;

import java.util.List;
import java.util.Map;
import mert.dev.scoreboard.domain.Game;
import mert.dev.scoreboard.domain.Score;

public interface ScoreBoard {

    boolean startGame(final String homeTeam, final String awayTeam);

    boolean finishGame(final String homeTeam, final String awayTeam);

    boolean updateScore(final String homeTeam, final String awayTeam, final int homeTeamScore, final int awayTeamScore);

    List<Map.Entry<Game, Score>> getSummary();

}
