package agent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import game.*;

public class Agent
{	 
	public String name;
	private BufferedReader br;
	
	public boolean WillExchange(BoardTile[][] board, Tile[] tiles)
	{
		if((int)(Math.random() * 2) == 1) return true;
		else return false;
	}
	 
	public TileMove Move(BoardTile[][] board, Tile[] tiles) throws IOException
	{	
		String word = "";
		
		for(int i = 0; i < 7; i++)
		{
			if(Math.random()*2 > 1)
			{
				if(tiles[i].letter == ' ') // if tile is blank tile,
					word += (char)((int)(65 + Math.random()*26)); // generate a letter form A to Z.
				else
					word += tiles[i].letter;
			}
		}	
			
		if(board[7][7].tile == null) // means if you are the first to move.
		{
			Direction dir;
			switch((int)(Math.random()*2))
			{
				case 0:
					dir = Direction.Downward;
				break;
				default:
					dir = Direction.Across;
				break;
			}
			int x = 7;
			int y = 7;
			
			for(int i = 0; i < word.length()/2; i++)
			{
				switch(dir)
				{
					case Downward: 	y--; break;
					case Across:	x--; break;
				}
			}
			return new TileMove(x, y, word, dir);
		}
		else // if you are not the first to move.
		{
			for(int j = 0; j < 15; j++)
			{
				for(int i = 0; i < 15; i++)
				{	
					if(board[i][j].tile != null)
					{
						for(int k = 0; k < word.length(); k++)
						{
							if(word.charAt(k) == board[i][j].tile.letter)
							{
								if((i-1 >= 0 && board[i-1][j].tile == null) && 
									(i+1 <= 14 && board[i+1][j].tile == null))
								{
									return new TileMove(i-k, j, word, Direction.Across);
								}
								if((j-1 >= 0 && board[i][j-1].tile == null) && 
									(j+1 <= 14 && board[i][j+1].tile == null))
								{
									return new TileMove(i, j-k, word, Direction.Downward);
								}
							}
						}
					}
				}
			}			
			return null; // to pass.
		}
	}
	
	public String ExchangeTiles(BoardTile[][] board, Tile[] tiles)
	{
		String exchange = "";
		
		for(int i = 0; i < 7; i++)
			if(Math.random()*2 > 1)
				exchange += i;
		
		return exchange;
	}
	
	boolean existing(String word)
	{
		try
		{
			br = new BufferedReader(new FileReader(new File("Dictionary\\temp_dictionary.txt")));
			String[] data;
			String line;
			
			while((line = br.readLine()) != null)
			{
				data = line.split(" ");
				for(String datum : data)
				{
					if(word.equals(datum))
					{
						return true;
					}
				}
			}
			
			return false;
		}
		catch(FileNotFoundException e) 
		{
			System.out.println(e.getCause());
		}
		catch(IOException e)
		{
			System.out.println(e.getCause());
		}
		return true;
	}
}