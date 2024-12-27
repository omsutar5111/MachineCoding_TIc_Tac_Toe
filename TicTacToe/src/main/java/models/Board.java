package models;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<List<Cell>> cells;

    public List<List<Cell>> getCells() {
        return cells;
    }

    public void setCells(List<List<Cell>> cells) {
        this.cells = cells;
    }

    public Board(int n){
        this.cells=new ArrayList<>();
        for(int i=0;i<n;i++){
            List<Cell>row=new ArrayList<>();
            for(int j=0;j<n;j++){
                row.add(new Cell(i,j,CellStatus.UNOCCUPIED));
            }
            this.cells.add(row);
        }
    }
    public  void printBoard(){
        int n=cells.size();
        for(int i=0;i<n;i++){
            List<Cell>row=this.cells.get(i);
            for (int j = 0; j < n; j++) {
                Cell cell=row.get(j);
                cell.printCell();
            }
            System.out.println();
        }
    }
    public Cell getCell(int row, int col) {
        return this.cells.get(row).get(col);
    }
    public void resetBoard(){
        for (List<Cell> row : cells) {
            for (Cell cell : row) {
                cell.removePlayer();
            }
        }
    }
    public boolean checkIfCellIsUnoccupied(int row, int col){
        Cell cell = this.cells.get(row).get(col);
        return cell.isUnoccupied();
    }

    public void setPlayer(int row, int col, Player player){
        Cell cell = this.cells.get(row).get(col);
        cell.setPlayer(player);
    }
    public int getSize(){
        return this.cells.size();
    }
}
