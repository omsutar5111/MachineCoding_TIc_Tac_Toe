package models;

public class BotPlayer extends Player{
    private  String name;
    private  Symbol symbol;
    private  BotLevel botLevel;
    public BotPlayer(String name,Symbol symbol,BotLevel botLevel){
        super(name,symbol);
      this.botLevel=botLevel;
    }

    @Override
    public Pair<Integer, Integer> makeMove(Board board) {
        return null;
    }
}
