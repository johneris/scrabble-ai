package game;

import agent.Agent;

public class Player
{
	Agent agent;
	int score;
	Tile[] tiles;
	
	public Player(Agent agent)
	{
		this.agent = agent;
		score = 0;
		tiles = new Tile[7];
	}
	public Tile find(char a)
	{
		for(int i = 0; i < tiles.length; i++)
		{
			if(tiles[i] != null)
			{
				if(tiles[i].letter == a)
				{
					Tile tile = tiles[i];
					tiles[i] = null;
					return tile;
				}
			}
		}
		
		for(int i = 0; i < tiles.length; i++)
		{
			if(tiles[i] != null)
			{
				if(tiles[i].letter == ' ')
				{
					Tile tile = tiles[i];
					tiles[i] = null;
					tile.letter = a;
					System.out.println("\n\n\n\n\n" + tile.letter + "\n\n\n\n");
					return tile;
				}
			}
		}
		
		System.out.println(agent.name + " cannot find letter " + a + "!");
		return null;
	}
}