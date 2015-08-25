package game;

public class TileMove
{
	public int x;
	public int y;
	public String word;
	public Direction dir;
	
	public TileMove(int x, int y, String word, Direction dir)
	{
		this.x = x;
		this.y = y;
		this.word = word;
		this.dir = dir;
	}
	
	public String toString()
	{
		return (x + " " + y + " " + word + " " + dir.toString());
	}
}