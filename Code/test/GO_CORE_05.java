import org.example.board.BoardState;
import org.testng.annotations.Test;

import java.awt.*;

import static org.testng.Assert.assertFalse;

public class GO_CORE_05 {

    @Test
    void GO_CORE_05_SuicideMove() {

        BoardState board = new BoardState();

        board.placeStone(8,9,Color.WHITE);
        board.placeStone(10,9,Color.WHITE);
        board.placeStone(9,8,Color.WHITE);
        board.placeStone(9,10,Color.WHITE);

        boolean result =
                board.placeStone(9,9,Color.BLACK);

        assertFalse(result);

    }
}
