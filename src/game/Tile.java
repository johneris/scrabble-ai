package game;

import javax.swing.*;

public class Tile
{
	public char letter;
	public int points;
	public ImageIcon img;
	
	public Tile(char l, int p, String url)
	{
		letter = l;
		points = p;
		img = new ImageIcon(url);
	}
	public Tile(Tile tile)
	{
		letter = tile.letter;
		points = tile.points;
		img = tile.img;
	}
	public Tile(){}
}