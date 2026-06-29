import org.example.board.BoardState;
import org.testng.annotations.Test;

import java.awt.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class GO_CORE_10 {

    @Test
    void GO_CORE_10_FinalScore() {

        BoardState board = new BoardState();

        /*
            Đen chiếm nhiều lãnh thổ hơn.
        */

        board.placeStone(3,3,Color.BLACK);
        board.placeStone(3,4,Color.BLACK);
        board.placeStone(4,3,Color.BLACK);

        board.placeStone(15,15,Color.WHITE);

        board.pass();
        board.pass();

        String winner =
                board.getWinner();

        assertEquals(winner,"BLACK");

    }
}
