package strategies.botPlaying;

import models.Board;
import models.Cell;
import models.CellStatus;
import models.Pair;

import java.util.List;

public class EasyBotPlayingStrategy implements botPlayingStrategy {
    @Override
    public Pair<Integer, Integer> makeMove(Board board) {
        for(List<Cell>row:board.getCells()){
            for(Cell cell:row){
                if(cell.getCellStatus().equals(CellStatus.UNOCCUPIED)){
                    return new Pair<>(cell.getRow(), cell.getCol());
                }
            }
        }
        return null;
    }
}
