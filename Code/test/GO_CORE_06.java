import org.example.board.BoardState;
import org.testng.annotations.Test;

import java.awt.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class GO_CORE_06 {

    @Test
    void GO_CORE_06_SuicideButCapture() {

        BoardState board = new BoardState();

        /*
              B
           B  W  B
              .

        Đen đi xuống dưới để bắt quân trắng.
        */

        board.placeStone(8,9,Color.BLACK);
        board.placeStone(9,8,Color.BLACK);
        board.placeStone(9,10,Color.BLACK);

        board.placeStone(9,9,Color.WHITE);

        boolean result =
                board.placeStone(10,9,Color.BLACK);

        assertTrue(result);

    }
}
