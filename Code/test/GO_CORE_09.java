import org.example.board.BoardState;
import org.testng.annotations.Test;

import java.awt.*;

import static org.testng.Assert.assertTrue;

public class GO_CORE_09 {

    @Test
    void GO_CORE_09_DoublePassEndGame() {

        BoardState board = new BoardState();

        /*
            Hai người đã đánh vài nước.
        */

        board.placeStone(3,3,Color.BLACK);
        board.placeStone(16,16,Color.WHITE);

        /*
            Đen Pass

            Trắng Pass

            Game kết thúc
         */

        board.pass();

        board.pass();

        assertTrue(board.isGameOver());

    }
}
