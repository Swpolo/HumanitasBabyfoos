package a.paul.humanitasbabyfoos;

public class PlayerRating {

    private PlayerRating() {

    }


    /**
     * based from https://math.stackexchange.com/a/838951
     * @param scoreRed
     * @param scoreBlue
     * @param attackRed
     * @param defenseRed
     * @param attackBlue
     * @param defenseBlue
     */
    public static void computeRating(int scoreRed, int scoreBlue,
                              Player attackRed, Player defenseRed,
                              Player attackBlue, Player defenseBlue) {

        double rRed = attackRed.score + defenseRed.score;
        rRed /= 2;

        double rBlue = attackBlue.score + defenseBlue.score;
        rBlue /= 2;

        double sScore = 10L / (double)(scoreRed + scoreBlue);
        double sRed;
        double sBlue;
        if (scoreRed == 10) {
            sRed = sScore;
            sBlue = 1 - sScore;
        } else {
            sRed = 1 - sScore;
            sBlue = sScore;
        }

        double eRed = 1 + Math.pow(10L, (rBlue - rRed)/500L);
        eRed = 1 / eRed;
        double eBlue = 1 + Math.pow(10L, (rRed - rBlue)/500L);
        eBlue = 1 / eBlue;

        attackRed.score += 100 * (sRed - eRed);
        defenseRed.score += 100 * (sRed - eRed);
        attackBlue.score += 100 * (sBlue - eBlue);
        defenseBlue.score += 100 * (sBlue - eBlue);
    }
}
