package mert.dev.scoreboard;

import java.util.List;
import java.util.Map;
import mert.dev.scoreboard.domain.Game;
import mert.dev.scoreboard.domain.Score;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConcurrentScoreBoardTest {

    private final ScoreBoard scoreBoard = ConcurrentScoreBoard.getInstance();


    @Test
    void startGameHomeTeamNameValidation() {
        // given
        String awayTeamName = "Estonia";

        // when & then
        assertThatThrownBy(() -> scoreBoard.startGame(null, awayTeamName)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void startGameAwayTeamNameValidation() {
        // given
        String homeTeamName = "Turkey";

        // when & then
        assertThatThrownBy(() -> scoreBoard.startGame(homeTeamName, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void startGame() {
        // given
        String homeTeamName = "Turkey";
        String awayTeamName = "Estonia";

        // when
        boolean result = scoreBoard.startGame(homeTeamName, awayTeamName);

        // then
        assertThat(result).isTrue();

        // cleanup
        scoreBoard.finishGame(homeTeamName, awayTeamName);
    }

    @Test
    void startGameDuplication() {
        // given
        String homeTeamName = "Turkey";
        String awayTeamName = "Estonia";
        scoreBoard.startGame(homeTeamName, awayTeamName);

        // when
        boolean result = scoreBoard.startGame(homeTeamName, awayTeamName);

        // then
        assertThat(result).isFalse();

        // cleanup
        scoreBoard.finishGame(homeTeamName, awayTeamName);
    }

    @Test
    void finishGameHomeTeamNameValidation() {
        // given
        String awayTeamName = "Estonia";

        // when & then
        assertThatThrownBy(() -> scoreBoard.finishGame(null, awayTeamName)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void finishGameAwayTeamNameValidation() {
        // given
        String homeTeamName = "Turkey";

        // when & then
        assertThatThrownBy(() -> scoreBoard.finishGame(homeTeamName, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void finishGame() {
        // given
        String homeTeamName = "Turkey";
        String awayTeamName = "Estonia";
        scoreBoard.startGame(homeTeamName, awayTeamName);

        // when
        boolean result = scoreBoard.finishGame(homeTeamName, awayTeamName);

        // then
        assertThat(result).isTrue();

        // cleanup
        scoreBoard.finishGame(homeTeamName, awayTeamName);
    }

    @Test
    void finishGameNoGame() {
        // given
        String homeTeamName = "Turkey";
        String awayTeamName = "Estonia";

        // when
        boolean result = scoreBoard.finishGame(homeTeamName, awayTeamName);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void updateScoreHomeTeamNameValidation() {
        // given
        String awayTeamName = "Estonia";
        int homeTeamScore = 3;
        int awayTeamScore = 2;

        // when & then
        assertThatThrownBy(() -> scoreBoard.updateScore(null, awayTeamName, homeTeamScore, awayTeamScore)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void updateScoreAwayTeamNameValidation() {
        // given
        String homeTeamName = "Turkey";
        int homeTeamScore = 3;
        int awayTeamScore = 2;

        // when & then
        assertThatThrownBy(() -> scoreBoard.updateScore(homeTeamName, null, homeTeamScore, awayTeamScore)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void updateScoreHomeTeamScoreValidation() {
        // given
        String homeTeamName = "Turkey";
        String awayTeamName = "Estonia";
        int homeTeamScore = -3;
        int awayTeamScore = 2;

        // when & then
        assertThatThrownBy(() -> scoreBoard.updateScore(homeTeamName, awayTeamName, homeTeamScore, awayTeamScore)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void updateScoreAwayTeamScoreValidation() {
        // given
        String homeTeamName = "Turkey";
        String awayTeamName = "Estonia";
        int homeTeamScore = 3;
        int awayTeamScore = -2;

        // when & then
        assertThatThrownBy(() -> scoreBoard.updateScore(homeTeamName, awayTeamName, homeTeamScore, awayTeamScore)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void updateScore() {
        // given
        String homeTeamName = "Turkey";
        String awayTeamName = "Estonia";
        int homeTeamScore = 3;
        int awayTeamScore = 2;
        scoreBoard.startGame(homeTeamName, awayTeamName);

        // when
        boolean result = scoreBoard.updateScore(homeTeamName, awayTeamName, homeTeamScore, awayTeamScore);

        // then
        assertThat(result).isTrue();

        // cleanup
        scoreBoard.finishGame(homeTeamName, awayTeamName);
    }

    @Test
    void updateScoreNoGame() {
        // given
        String homeTeamName = "Turkey";
        String awayTeamName = "Estonia";
        String randomTeamName = "RandomTeam";
        int homeTeamScore = 3;
        int awayTeamScore = 2;
        scoreBoard.startGame(randomTeamName, awayTeamName);

        // when
        boolean result = scoreBoard.updateScore(homeTeamName, awayTeamName, homeTeamScore, awayTeamScore);

        // then
        assertThat(result).isFalse();

        // cleanup
        scoreBoard.finishGame(homeTeamName, awayTeamName);
    }

    @Test
    void getSummary() throws InterruptedException {
        // given
        Game game1 = new Game("Turkey", "Estonia");
        Game game2 = new Game("Germany", "Italy");
        Game game3 = new Game("USA", "Netherlands");
        scoreBoard.startGame(game1.homeTeam(), game1.awayTeam());
        scoreBoard.startGame(game2.homeTeam(), game2.awayTeam());
        scoreBoard.startGame(game3.homeTeam(), game3.awayTeam());
        scoreBoard.updateScore(game1.homeTeam(), game1.awayTeam(), 1, 1);
        Thread.sleep(1000); // just to trick LocalDateTime.now()
        scoreBoard.updateScore(game2.homeTeam(), game2.awayTeam(), 2, 0);
        scoreBoard.updateScore(game3.homeTeam(), game3.awayTeam(), 0, 1);

        // when
        List<Map.Entry<Game, Score>> summary = scoreBoard.getSummary();

        // then
        assertThat(summary).isNotNull().isNotEmpty().hasSize(3);
        Map.Entry<Game, Score> firstEntry = summary.getFirst();
        assertThat(firstEntry.getKey()).isEqualTo(game2);
        Map.Entry<Game, Score> lastEntry = summary.getLast();
        assertThat(lastEntry.getKey()).isEqualTo(game3);

        // cleanup
        scoreBoard.finishGame(game1.homeTeam(), game1.awayTeam());
        scoreBoard.finishGame(game2.homeTeam(), game2.awayTeam());
        scoreBoard.finishGame(game3.homeTeam(), game3.awayTeam());
    }

}