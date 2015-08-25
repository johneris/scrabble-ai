package game;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class BoardTile extends JLabel
{
	private static final long serialVersionUID = 1L;
	
	public TileType type;
	public Tile tile;
	
	public BoardTile(TileType type)
	{
		super();
		this.type = type;
		this.tile = null;
	}
	public BoardTile(BoardTile btile)
	{
		super();
		type = btile.type;
		tile = btile.tile;
	}
	public Icon getIcon()
	{
		if(tile != null)
		{
			return tile.img;
		}
			
		switch(type)
		{
			case Normal:
				return new ImageIcon("Tile Images/norm_tile.png");
			case DoubleLetter:
				return new ImageIcon("Tile Images/dblt_tile.png");
			case TripleLetter:
				return new ImageIcon("Tile Images/trlt_tile.png");
			case DoubleWord:
				return new ImageIcon("Tile Images/dbwd_tile.png");
			case TripleWord:
				return new ImageIcon("Tile Images/trwd_tile.png");
			case Star:
				return new ImageIcon("Tile Images/star_tile.png");
			default:
				return null;
		}
	}
	public int letterMultiplier()
	{
		switch(type)
		{
			case DoubleLetter:
				return 2;
			case TripleLetter:
				return 3;
			default:
				return 1;
		}
	}
	public int wordMultiplier()
	{
		switch(type)
		{
			case Star:
			case DoubleWord:
				return 2;
			case TripleWord:
				return 3;
			default:
				return 1;
		}
	}
}