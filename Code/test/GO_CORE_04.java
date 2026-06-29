import org.example.board.BoardState;
import org.testng.annotations.Test;

import java.awt.*;

import static org.testng.Assert.assertNull;

public class GO_CORE_04 {

    @Test
    void GO_CORE_04_CaptureGroup() {

        BoardState board = new BoardState();

        board.placeStone(8,9,Color.BLACK);
        board.placeStone(8,10,Color.BLACK);
        board.placeStone(8,11,Color.BLACK);

        board.placeStone(10,9,Color.BLACK);
        board.placeStone(10,10,Color.BLACK);
        board.placeStone(10,11,Color.BLACK);

        board.placeStone(9,8,Color.BLACK);
        board.placeStone(9,12,Color.BLACK);

        board.placeStone(9,9,Color.WHITE);
        board.placeStone(9,10,Color.WHITE);
        board.placeStone(9,11,Color.WHITE);

        assertNull(board.getGrid()[9][9]);
        assertNull(board.getGrid()[9][10]);
        assertNull(board.getGrid()[9][11]);

    }
}
