package mert.dev.scoreboard.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameTest {

    private Game game;

    @Test
    void parameterizedConstructor() {
        // given
        String firstTeamName = "Turkey";
        String secondTeamName = "Estonia";

        // when
        game = new Game(firstTeamName, secondTeamName);

        // then
        assertThat(game.homeTeam()).isEqualTo(firstTeamName);
        assertThat(game.awayTeam()).isEqualTo(secondTeamName);
    }

    @Test
    void getHomeTeamName() {
        // given
        String homeTeamName = "Estonia";

        // when
        game = new Game(homeTeamName, "randomTeam");

        // then
        assertThat(game.homeTeam()).isEqualTo(homeTeamName);
    }

    @Test
    void getAwayTeamName() {
        // given
        String awayTeamName = "Estonia";

        // when
        game = new Game("randomTeam", awayTeamName);

        // when
        assertThat(game.awayTeam()).isEqualTo(awayTeamName);
    }

    @Test
    void equals() {
        // given
        String homeTeamName = "Turkey";
        String awayTeamName = "Estonia";
        Game newGame = new Game(homeTeamName, awayTeamName);

        // when
        game = new Game(homeTeamName, awayTeamName);

        // then
        assertThat(game).isEqualTo(newGame);
    }

    @Test
    void notEqual() {
        // given
        String homeTeamName = "Turkey";
        String awayTeamName = "Estonia";
        Game newGame = new Game(awayTeamName, homeTeamName);

        // when
        game = new Game(homeTeamName, awayTeamName);

        // then
        assertThat(game).isNotEqualTo(newGame);
    }

}