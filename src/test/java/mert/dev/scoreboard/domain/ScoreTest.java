package mert.dev.scoreboard.domain;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScoreTest {

    private Score score;

    @Test
    void defaultConstructor() {
        // when
        score = new Score();

        // then
        assertThat(score.homeTeamScore()).isZero();
        assertThat(score.awayTeamScore()).isZero();
        assertThat(score.updatedAt()).isNotZero();
    }

    @Test
    void parameterizedConstructor() {
        // given
        int firstScore = 3;
        int secondScore = 2;

        // when
        score = new Score(firstScore, secondScore);

        // then
        assertThat(score.homeTeamScore()).isEqualTo(firstScore);
        assertThat(score.awayTeamScore()).isEqualTo(secondScore);
        assertThat(score.updatedAt()).isNotZero();
    }

    @Test
    void getHomeTeamScore() {
        // given
        int homeTeamScore = 3;

        // when
        score = new Score(homeTeamScore, 0);

        // when
        assertThat(score.homeTeamScore()).isEqualTo(homeTeamScore);
    }

    @Test
    void getAwayTeamScore() {
        // given
        int awayTeamScore = 5;

        // when
        score = new Score(0, awayTeamScore);

        // then
        assertThat(score.awayTeamScore()).isEqualTo(awayTeamScore);
    }

    @Test
    void totalScore() {
        // given
        int homeTeamScore = 3;
        int awayTeamScore = 2;
        int expectedScore = homeTeamScore + awayTeamScore;

        // when
        score = new Score(homeTeamScore, awayTeamScore);

        // then
        assertThat(score.totalScore()).isEqualTo(expectedScore);
    }

    @Test
    void updatedAt() {
        // when
        score = new Score();

        // then
        assertThat(score.updatedAt()).isLessThan(LocalDateTime.now().plusMinutes(10).toEpochSecond(ZoneOffset.UTC));

    }

}