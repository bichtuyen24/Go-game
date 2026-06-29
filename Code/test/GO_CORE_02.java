
import org.example.board.BoardState;
import org.junit.jupiter.api.Test;
import org.testng.asserts.IAssert;

import static org.testng.Assert.assertFalse;

import java.awt.Color;
import java.awt.*;

public class GO_CORE_02 {
    @Test
    void GO_CORE_02() {

        BoardState board=new BoardState();
        board.placeStone(5, 5, Color.BLACK);
        boolean result=
            board.placeStone(
                    5,
                    5,
                    Color.WHITE
            );
    assertFalse(result);

}
}
