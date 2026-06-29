import org.example.board.BoardState;
import org.testng.annotations.Test;

import java.awt.Color;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class GO_CORE_01 {

    @Test
    void GO_CORE_01_PlaceStoneSuccess(){

        BoardState board=new BoardState();

        boolean result=
                board.placeStone(
                        5,
                        5,
                        Color.BLACK
                );

        assertTrue(result);

        assertNotNull(
                board.getGrid()[5][5]
        );

    }

}
