package game;

import java.util.*;

public class ScrabbleTiles
{
    public ArrayList<Tile> tiles;
    
	public ScrabbleTiles()
	{
		initialize();
	}
	
    void initialize()	
	{
		String url = "";
		tiles = new ArrayList<Tile>();
		
		for(int i = 1; i <= 2; i++)
		{
			tiles.add(new Tile(' ', 0, "Tile Images/blank_tile.png"));
		}
		
		for(int ascii = 65; ascii <= 90; ascii++)
		{
			char ch = (char) ascii;
			int ct = 0;
			int pt = 0;
			
			switch(ch)
			{
				case 'A':
					ct = 9;
					pt = 1;
					url = "Tile Images/a_tile.png";
					break;
				case 'B':
					ct = 2;
					pt = 3;
					url = "Tile Images/b_tile.png";
					break;
				case 'C':
					ct = 2;
					pt = 3;
					url = "Tile Images/c_tile.png";
					break;
				case 'D':
					ct = 4;
					pt = 2;
					url = "Tile Images/d_tile.png";
					break;
				case 'E':
					ct = 12;
					pt = 1;
					url = "Tile Images/e_tile.png";
					break;
				case 'F':
					ct = 2;
					pt = 4;
					url = "Tile Images/f_tile.png";
					break;
				case 'G':
					ct = 3;
					pt = 2;
					url = "Tile Images/g_tile.png";
					break;
				case 'H':
					ct = 2;
					pt = 4;
					url = "Tile Images/h_tile.png";
					break;
				case 'I':
					ct = 9;
					pt = 1;
					url = "Tile Images/i_tile.png";
					break;
				case 'J':
					ct = 1;
					pt = 8;
					url = "Tile Images/j_tile.png";
					break;
				case 'K':
					ct = 1;
					pt = 5;
					url = "Tile Images/k_tile.png";
					break;
				case 'L':
					ct = 4;
					pt = 1;
					url = "Tile Images/l_tile.png";
					break;
				case 'M':
					ct = 2;
					pt = 3;
					url = "Tile Images/m_tile.png";
					break;
				case 'N':
					ct = 6;
					pt = 1;
					url = "Tile Images/n_tile.png";
					break;
				case 'O':
					ct = 8;
					pt = 1;
					url = "Tile Images/o_tile.png";
					break;
				case 'P':
					ct = 2;
					pt = 3;
					url = "Tile Images/p_tile.png";
					break;
				case 'Q':
					ct = 1;
					pt = 10;
					url = "Tile Images/q_tile.png";
					break;
				case 'R':
					ct = 6;
					pt = 1;
					url = "Tile Images/r_tile.png";
					break;
				case 'S':
					ct = 4;
					pt = 1;
					url = "Tile Images/s_tile.png";
					break;
				case 'T':
					ct = 6;
					pt = 1;
					url = "Tile Images/t_tile.png";
					break;
				case 'U':
					ct = 4;
					pt = 1;
					url = "Tile Images/u_tile.png";
					break;
				case 'V':
					ct = 2;
					pt = 4;
					url = "Tile Images/v_tile.png";
					break;
				case 'W':
					ct = 2;
					pt = 4;
					url = "Tile Images/w_tile.png";
					break;
				case 'X':
					ct = 1;
					pt = 8;
					url = "Tile Images/x_tile.png";
					break;
				case 'Y':
					ct = 2;
					pt = 4;
					url = "Tile Images/y_tile.png";
					break;
				case 'Z':
					ct = 1;
					pt = 10;
					url = "Tile Images/z_tile.png";
					break;
			}
			for(int i = 1; i <= ct; i++)
			{
				tiles.add(new Tile(ch, pt, url));
			}
		}
		tiles = shuffle(tiles);
	}
	
    private Tile getTile(ArrayList<Tile> t, int i)
    {
        Tile temp = new Tile();
        temp = t.get(i);
        t.remove(i);
        return temp;
    }
	
	public Tile getTile()
    {
		if(tiles.size() == 0) return null;
        Tile temp = new Tile();
        temp = tiles.get(0);
        tiles.remove(0);
        return temp;
    }
	
	private ArrayList<Tile> shuffle(ArrayList<Tile> t)
	{
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		int length = t.size();
		Random rand = new Random();
		for(int i = 1; i <= length; i++)
		{
			Tile temp = getTile(t, rand.nextInt(t.size()));
			tiles.add(temp);
		}
		return tiles;
	}
	
	public void addTile(Tile t)
	{
		tiles.add(t);
		tiles = shuffle(tiles);
	}
	
	public boolean isEmpty()
	{
		return tiles.isEmpty();
	}
	
	public void print()
	{
		for( Tile t : tiles )
			System.out.println(t.letter + " " + t.points);
	}
	
}