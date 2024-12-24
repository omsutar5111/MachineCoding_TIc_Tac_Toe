package models;

import exception.BotCountExceededException;
import exception.InvalidGameStateException;
import strategies.checkForWin.OrderOneWinningStrategy;
import strategies.checkForWin.PlayerWonStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private  Board board;
    private List<Player> players;
    private  GameStatus gameStatus;
    private  int currentPlayerIndex;
    private List<Move>moves;
    private PlayerWonStrategy winningStategy;

    private  Game(Board board, List<Player>players, GameStatus gameStatus, int currentPlayerIndex, List<Move>moves,PlayerWonStrategy winningStategy,int undoLimitedPerGame){
        this.board=board;
        this.gameStatus=gameStatus;
        this.currentPlayerIndex=currentPlayerIndex;
        this.moves=moves;
        this.players=players;
        this.winningStategy=winningStategy;
        for(Player player:players){
            if(player instanceof HumanPlayer){
                HumanPlayer humanPlayer=(HumanPlayer) player;
                humanPlayer.setPendingUndoCount(undoLimitedPerGame);
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public  void makeMove(){
        Player player=this.players.get(currentPlayerIndex);
        Pair<Integer,Integer>rowCol=player.makeMove(board);
        while(!this.board.checkIfCellIsUnoccupied(rowCol.getKey(), rowCol.getValue())){
            if(player instanceof  HumanPlayer){
                System.out.println("Please make a move on a different cell");
            }
            rowCol=player.makeMove(this.board);
        }
        this.board.setPlayer(rowCol.getKey(), rowCol.getValue(), player);
        Cell cell=this.board.getCell(rowCol.getKey(),rowCol.getValue());
        Move move=new Move(player,cell);
        this.moves.add(move);

        if(winningStategy.checkForWin(this.board, cell)){
            this.gameStatus = GameStatus.ENDED;
            return;
        } else if(checkForDraw()){
            this.gameStatus = GameStatus.DRAWN;
            return;
        }

        this.currentPlayerIndex = (this.currentPlayerIndex + 1) % (this.board.getSize() - 1);
    }



    public static GameBuilder getBuilder(){
        return new GameBuilder();
    }

    public  static class GameBuilder{
        private Board board;
        private List<Player> players;
        private GameStatus gameStatus;
        private int currentPlayerIdx;
        private List<Move> moves;
        private int undoLimitPerPlayer;




        public GameBuilder setPlayer(List<Player> players){
            this.players = players;
            int n = players.size();
            this.board = new Board(n+1);
            return this;
        }

        public GameBuilder setUndoLimit(int undoLimit){
            this.undoLimitPerPlayer = undoLimit;
            return this;
        }
        public Game build() throws BotCountExceededException {
            int botCount = 0;
            for(Player p: players){
                if(p instanceof BotPlayer){
                    botCount++;
                }
                if(botCount > 1){
                    throw new BotCountExceededException("Found more than 1 bots, maximum only 1 bot is allowed");
                }
            }
            return new Game(this.board, this.players, GameStatus.IN_PROGRESS, 0, new ArrayList<>(), new OrderOneWinningStrategy(board.getSize()), this.undoLimitPerPlayer);
        }

    }

    public void printBoard(){
        this.board.printBoard();
    }

    private boolean checkForDraw(){
        int n = this.board.getSize();
        return n * n == this.moves.size();
    }

    public Player getCurrentPlayer(){
        return this.players.get(currentPlayerIndex);
    }


    public void undo(){
        int prevPlayerIdx = currentPlayerIndex - 1;
        if(prevPlayerIdx < 0) prevPlayerIdx = players.size() - 1;
        Player player = this.players.get(prevPlayerIdx);
        if(player instanceof HumanPlayer){
            Scanner scanner = new Scanner(System.in);
            HumanPlayer humanPlayer = (HumanPlayer) player;
            if(humanPlayer.getPendingUndoCount() > 0) {
                System.out.println("Do you want to undo? (y/n)");
                String input = scanner.next();
                if (input.charAt(0) == 'y' || input.charAt(0) == 'Y') {
                    Move move = moves.remove(moves.size() - 1);
                    Cell cell = move.getCell();
                    cell.removePlayer();
                    this.currentPlayerIndex = prevPlayerIdx;
                    winningStategy.handleUndo(move);
                    System.out.println("We have successfully undoed player: " + player.getName() + "'s last move");
                    humanPlayer.decrementUndoCount();
                    if(humanPlayer.getPendingUndoCount() == 0){
                        System.out.println("This was your last undo, no more undo attempts left");
                    }
                }
            }
        } else {
            //do nothing, its a bot, bot cannot undo
        }
    }

    public void replay() throws InvalidGameStateException {
        if(gameStatus == GameStatus.IN_PROGRESS){
            throw new InvalidGameStateException("Game is still in progress");
        }
        board.resetBoard();
        int count = 1;
        for(Move move: moves){
            Cell cell = move.getCell();
            Player player = move.getPlayer();
            board.setPlayer(cell.getRow(), cell.getCol(), player);
            System.out.println("Turn #" + count++ + ", player " + player.getName() + " makes a move");
            printBoard();
        }
    }

}
