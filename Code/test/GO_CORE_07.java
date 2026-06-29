import org.example.board.BoardState;
import org.testng.annotations.Test;

import java.awt.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class GO_CORE_07 {

    @Test
    void GO_CORE_07_KoRule() {

        BoardState board = new BoardState();

        /*
            Ví dụ trạng thái Ko

              B W
            W . B
              B

            Đen vừa ăn quân.

            Trắng ăn lại ngay
            => phải bị từ chối.
         */

        board.placeStone(8,9,Color.BLACK);
        board.placeStone(8,10,Color.WHITE);

        board.placeStone(9,8,Color.WHITE);
        board.placeStone(9,10,Color.BLACK);

        board.placeStone(10,9,Color.BLACK);

        // Đen vừa ăn quân ở đây

        boolean result =
                board.placeStone(9,9,Color.WHITE);

        assertFalse(result);

    }
}
