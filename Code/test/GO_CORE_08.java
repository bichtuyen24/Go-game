import org.example.board.BoardState;
import org.testng.annotations.Test;

import java.awt.*;

import static org.testng.Assert.assertTrue;

public class GO_CORE_08 {

    @Test
    void GO_CORE_08_ValidKoRecapture() {

        BoardState board = new BoardState();

        /*
            Tạo Ko

            Trắng KHÔNG ăn lại ngay.

            Trắng đi chỗ khác.

            Đen đi.

            Sau đó Trắng ăn lại.
         */

        board.placeStone(8,9,Color.BLACK);
        board.placeStone(8,10,Color.WHITE);

        board.placeStone(9,8,Color.WHITE);
        board.placeStone(9,10,Color.BLACK);

        board.placeStone(10,9,Color.BLACK);

        // Trắng đi nơi khác
        board.placeStone(15,15,Color.WHITE);

        // Đen đáp trả
        board.placeStone(16,16,Color.BLACK);

        // Trắng ăn lại Ko
        boolean result =
                board.placeStone(9,9,Color.WHITE);

        assertTrue(result);

    }

}
