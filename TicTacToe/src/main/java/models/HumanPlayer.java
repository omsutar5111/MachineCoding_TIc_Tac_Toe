package models;

import java.util.Scanner;

public class HumanPlayer extends Player{
    private int pendingUndoCount;
    public HumanPlayer(String name,Symbol symbol){
        super(name, symbol);
    }
    @Override
    public Pair<Integer, Integer> makeMove(Board board) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Its " + this.getName() + "'s turn. Enter row and col");
        int row = scanner.nextInt();
        int col = scanner.nextInt();
        return new Pair<>(row, col);
    }

    public int getPendingUndoCount() {
        return pendingUndoCount;
    }

    public void setPendingUndoCount(int pendingUndoCount) {
        this.pendingUndoCount = pendingUndoCount;
    }

    public void decrementUndoCount() {
        this.pendingUndoCount--;
    }
}
