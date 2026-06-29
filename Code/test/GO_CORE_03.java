import org.example.board.BoardState;
import org.testng.annotations.Test;

import java.awt.*;

import static org.testng.Assert.*;

public class GO_CORE_03 {

    @Test
    void GO_CORE_03_CaptureOneStone() {

        BoardState board = new BoardState();

        board.placeStone(8, 9, Color.BLACK);
        board.placeStone(9, 8, Color.BLACK);
        board.placeStone(10, 9, Color.BLACK);

        board.placeStone(9, 9, Color.WHITE);

        board.placeStone(9, 10, Color.BLACK);

        assertNull(board.getGrid()[9][9]);

}}
