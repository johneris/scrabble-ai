package game;

import javax.swing.*;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.Scanner;

import agent.*;

public class Scrabble extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private BoardTile[][] tiles;
	private ScrabbleTiles letters;
	
	Player[] players;
	
	JPanel board;
	JPanel control;
	
	JLabel[] playerInfo;
	JPanel[] playerTiles;
	
	boolean first;
	
	boolean usesWordNet;
	String dictionary;
	
	final static boolean success = true;
	
	static ArrayList<String> words;

	//private static Scanner sc;
	
	public static boolean isDebug;
	
	int passes = 0;
	
	public Scrabble(String dictionary, boolean isDebug) throws HeadlessException, IOException
	{
		super("Scrabble AI 2013");
		
		if(dictionary.equals(""))
		{
			usesWordNet = true;
			initializeWordNet();
		}
		else
		{
			this.dictionary = dictionary;
			words = new ArrayList<String>();
			try
			{				
				BufferedReader br = new BufferedReader(new FileReader(new File(dictionary)));
				String[] data;
				String line;
				
				while((line = br.readLine()) != null)
				{
					data = line.split(" ");
					
					for(String datum : data)
					{
						words.add(datum);
					}
				}
				
				br.close();
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				return;
			}
		}
		
		Scrabble.isDebug = isDebug;
		
		initialize_tiles();
		initialize_players();
		
		setSize(750, 500);
		setVisible(true);
		setResizable(false);
		
		try
		{
			gameStart();
		}
		catch(Exception e)
		{
		}
		printResults();
	}
	
	void initialize_tiles()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		board = new JPanel();
		control = new JPanel();
		
		board.setLayout(new GridLayout(15, 15));
		control.setLayout(new GridLayout(8, 1));
		
		add(board, BorderLayout.CENTER);
		add(control, BorderLayout.EAST);
		
		tiles = new BoardTile[15][15];		
		letters = new ScrabbleTiles();
		
		for(int j = 0; j < 15; j++)
			for(int i = 0; i < 15; i++)
			{
				tiles[i][j] = new BoardTile(TileType.Normal);
				board.add(tiles[i][j]);
			}
				
		DoubleLetterTiles(TileType.DoubleLetter);
		TripleLetterTiles(TileType.TripleLetter);
		DoubleWordTiles(TileType.DoubleWord);
		TripleWordTiles(TileType.TripleWord);
		StarTile(TileType.Star);
		
	}
	
	void initialize_players()
	{
		players = new Player[]
		{
			new Player(new Agent1()),
			new Player(new Agent2())
			//new Player(new Agent3()),
			//new Player(new Agent4())
		};
		
		players = (Player[])randomize(players);
		
		System.out.println(players[0].agent.name);
		System.out.println(players[1].agent.name);
		//System.out.println(players[2].agent.name);
		//System.out.println(players[3].agent.name);
		System.out.println();
		
		playerInfo 	= new JLabel[players.length];
		playerTiles = new JPanel[players.length];
		
		for(int i = 0; i < players.length; i++)
		{
			playerInfo[i] = new JLabel(players[i].agent.name + "      Score: " + players[i].score);
			playerTiles[i] = new JPanel();
		}
		for(Player player : players)
		{
			for(int i = 0; i < 7; i++)
			{
				player.tiles[i] = letters.getTile();
			}
		}
		for(int i = 0; i < players.length; i++)
		{
			control.add(playerInfo[i]);
			control.add(playerTiles[i]);
			playerTiles[i].setLayout(new GridLayout(1, 7));
			for(Tile tile : players[i].tiles)
			{
				playerTiles[i].add(new JLabel(tile.img));
			}
		}
	}
	
	void initializeWordNet()
	{
		try 
		{
			JWNL.initialize(new FileInputStream("file_properties.xml"));	
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		}
	}
	
	void gameStart() throws HeadlessException, IOException
	{
		first = true;
		
		while(!gameOver())
		{
			for(Player player : players)
			{
				try
				{
					int tries;
					for(tries = 0; tries < 3; tries++)
					{
						if(playerMove(player) == success)
							break;
					}
					
					if(tries == 3)
					{
						if(isDebug)
							JOptionPane.showMessageDialog(null,
								player.agent.name + " made 3 consecutive mistakes!\n" +
								player.agent.name + " loses his/her turn.");
						else
							System.out.println(
								player.agent.name + " made 3 consecutive mistakes!\n" +
								player.agent.name + " loses his/her turn.");
					}						
				}
				catch(Exception e)
				{
					System.out.println("Error at " + player.agent.name + "'s move. ");
					passes++;
					e.printStackTrace();
				}
			}
		}
	}
	
	boolean playerMove(Player player) throws HeadlessException, IOException
	{
		Tile[] tempTiles = new Tile[7];
		BoardTile[][] tempBoard = new BoardTile[15][15];
		
		for(int i = 0; i < tempTiles.length; i++)
		{
			tempTiles[i] = new Tile(player.tiles[i]);
		}
		for(int j = 0; j < 15; j++)
		{
			for(int i = 0; i < 15; i++)
			{
				tempBoard[i][j] = new BoardTile(tiles[i][j]);
			}
		}
		
		if(player.agent.WillExchange(tempBoard, tempTiles))
		{
			System.out.println(player.agent.name + " returns some tiles!");
			System.out.print("Previous hand:\t");
			
			for(Tile tile : player.tiles)
				System.out.print(tile.letter);
				
			System.out.println();
				
			String exchange = player.agent.ExchangeTiles(tempBoard, tempTiles);
			Tile[] temp = new Tile[7];
			
			for(int i = 0; i < exchange.length(); i++)
			{
				try
				{
					int x = Integer.parseInt(exchange.charAt(i)+"");
					
					if(temp[x] == null)
					{
						temp[x] = player.tiles[x];
						player.tiles[x] = letters.getTile();
					}
				}
				catch(Exception e) {}
			}
			for(int i = 0; i < 7; i++)
			{
				if(temp[i] != null)
				{
					letters.addTile(temp[i]);
				}
			}
			
			System.out.print("Current hand:\t");
			
			for(Tile tile : player.tiles)
				System.out.print(tile.letter);
			
			System.out.println();
			System.out.println();
			
			update();
		}
		else
		{
			System.out.print(player.agent.name + "'s turn:");
			long startTime = System.currentTimeMillis();
			TileMove move = player.agent.Move(tempBoard, tempTiles);
			long endTime = System.currentTimeMillis();
			System.out.println(((double)(endTime-startTime)/1000) + " seconds.");
			if(endTime - startTime > 90000)
			{
				if(isDebug)
					JOptionPane.showMessageDialog(null, "It took you "
							+ "over 90 seconds to move.\nYour move "
							+ "will be voided.");
				else
					System.out.println("It took you "
							+ "over 90 seconds to move.\nYour move "
							+ "will be voided.");
				passes++;
				return true;
			}
			if(move == null)
			{
				passes++;
				System.out.println(player.agent.name + " passed.");
			}
			else if(valid(player, move))
			{								
				first = false;
				
				int x = move.x;
				int y = move.y;
				
				int score = 0;
				int wordMultiplier = 1;
				
				for(int i = 0; i < move.word.length(); i++)
				{
					if(tiles[x][y].tile == null)
					{
						tiles[x][y].tile = player.find(move.word.charAt(i));
						System.out.println(player.agent.name + " put the letter " + 
								tiles[x][y].tile.letter + " at " + x + "," + y);
						
						int x2 = x;
						int y2 = y;
						
						int score2 = 0;
						int wordMultiplier2 = 1;
						String word = "";
						switch(move.dir)
						{
							case Downward:
								if((x-1 >= 0 && tiles[x-1][y].tile != null) || 
									(x+1 <= 14 && tiles[x+1][y].tile != null))
								{	
									while(x2-1 >= 0 && tiles[x2-1][y2].tile != null)
										x2--;
									
									while(x2 <= 14 && tiles[x2][y2].tile != null)
									{													
										word += tiles[x2][y2].tile.letter;
										score2 += tiles[x2][y2].tile.points * tiles[x2][y2].letterMultiplier();
										wordMultiplier2 *= tiles[x2][y2].wordMultiplier();
										x2++;
									}
								}
							break;
							case Across:
								if((y-1 >= 0 && tiles[x][y-1].tile != null) || 
									(y+1 <= 14 && tiles[x][y+1].tile != null))
								{	
									while(y2-1 >= 0 && tiles[x2][y2-1].tile != null)
										y2--;
									
									while(y2 <= 14 && tiles[x2][y2].tile != null)
									{
										word += tiles[x2][y2].tile.letter;
										score2 += tiles[x2][y2].tile.points * tiles[x2][y2].letterMultiplier();
										wordMultiplier2 *= tiles[x2][y2].wordMultiplier();
										y2++;
									}
								}
							break;
						}
													
						score2 *= wordMultiplier2;
						player.score += score2;
						
						if(!word.equals(""))
						{
							System.out.println("\n" + player.agent.name + " scored " +
									score2 + " points by spelling " + word + "\n");
						}
					}
					
					score += tiles[x][y].tile.points * tiles[x][y].letterMultiplier();
					wordMultiplier *= tiles[x][y].wordMultiplier();
					
					tiles[x][y].type = TileType.Normal;
					
					switch(move.dir)
					{
						case Downward: 	y++; break;
						case Across: 	x++; break;
					}
				}
				
				score *= wordMultiplier;
				player.score += score;
				
				System.out.println("\n" + player.agent.name + " scored " +
								score + " points by spelling " + move.word + "\n");
				
				int used = 0;
				for(int i = 0; i < player.tiles.length; i++)
				{
					if(player.tiles[i] == null)
					{
						used++;
						player.tiles[i] = letters.getTile();
					}
				}
				if(used == 7)
				{
					System.out.println("BINGO! " + player.agent.name + " used all his/her tiles! +50 points.");
					player.score += 50;
				}
				passes = 0;
				update();
			}
			else
			{
				if(isDebug)
					JOptionPane.showMessageDialog(null, "Invalid! " + 
						player.agent.name + " tried to spell " + move.word +
						" at " + move.x + ", " + move.y + " " + move.dir.toString());
				else
					System.out.println("Invalid! " + 
						player.agent.name + " tried to spell " + move.word +
						" at " + move.x + ", " + move.y + " " + move.dir.toString());
				return false;
			}
		}
		return true;
	}	
	boolean valid(Player player, TileMove move) throws IOException
	{
		if(!wordExists(move.word))
			return false;
		
		int x = move.x;
		int y = move.y;
		
		switch(move.dir)
		{
			case Downward:
				if(y + move.word.length()-1 > 14 || y < 0)
					return false;
				if(y + move.word.length() <= 14 && tiles[x][y + move.word.length()].tile != null)
					return false;
				if(y - 1 >= 0 && tiles[x][y - 1].tile != null)
					return false;
				break;
			case Across:
				if(x + move.word.length()-1 > 14 || x < 0)
					return false;
				if(x + move.word.length() <= 14 && tiles[x + move.word.length()][y].tile != null)
					return false;
				if(x - 1 >= 0 && tiles[x - 1][y].tile != null)
					return false;
				break;
		}
		
		ArrayList<Tile> list = new ArrayList<Tile>();
		
		boolean star = false;
		int existing = 0;
		for(int i = 0; i < move.word.length(); i++)
		{
			if(tiles[x][y].tile == null)
			{
				Tile tile = player.find(move.word.charAt(i));
				
				if(tile != null)
				{
					list.add(tile);
					
					int x2, y2;
					switch(move.dir)
					{
						case Downward:

							x2 = x;
							y2 = y; 

							if((x-1 >= 0 && tiles[x-1][y].tile != null) || 
								(x+1 <= 14 && tiles[x+1][y].tile != null))
							{	
								existing++;
								
								while(x2-1 >= 0 && tiles[x2-1][y2].tile != null)
									x2--;
									
								String word = "";
								
								while((x == x2 && y == y2) || (x2 <= 14 && tiles[x2][y2].tile != null))
								{
									if(x == x2 && y == y2)
										word += tile.letter;
									else
										word += tiles[x2][y2].tile.letter;
									x2++;
								}
								
								if(!wordExists(word))
								{										
									for(int j = 0; j < player.tiles.length; j++)
									{
										if(player.tiles[j] == null)
										{
											player.tiles[j] = list.get(0);
											list.remove(0);
										}
									}
									return false;
								}
							}
						break;
						case Across:
							
							x2 = x;
							y2 = y; 
							
							if((y-1 >= 0 && tiles[x][y-1].tile != null) || 
								(y+1 <= 14 && tiles[x][y+1].tile != null))
							{				
								existing++;				
								while(y2-1 >= 0 && tiles[x2][y2-1].tile != null)
									y2--;
									
								String word = "";
								
								while((x == x2 && y == y2) || y2+1 <= 14 && tiles[x2][y2].tile != null)
								{
									if(x == x2 && y == y2)
										word += tile.letter;
									else
										word += tiles[x2][y2].tile.letter;
									y2++;
								}
								
								if(!wordExists(word))
								{										
									for(int j = 0; j < player.tiles.length; j++)
									{
										if(player.tiles[j] == null)
										{
											player.tiles[j] = list.get(0);
											list.remove(0);
										}
									}
									return false;
								}
							}
						break;
					}
					if(tiles[x][y].type == TileType.Star)
						star = true;
				}
				else
				{
					for(int j = 0; j < player.tiles.length; j++)
					{
						if(player.tiles[j] == null)
						{
							player.tiles[j] = list.get(0);
							list.remove(0);
						}
					}
					return false;
				}
			}
			else if(tiles[x][y].tile.letter != move.word.charAt(i))
			{
				for(int j = 0; j < player.tiles.length; j++)
				{
					if(player.tiles[j] == null)
					{
						player.tiles[j] = list.get(0);
						list.remove(0);
					}
				}
				return false;
			}
			else
			{
				existing++;
			}
		
			switch(move.dir)
			{
				case Downward: 	y++; break;
				case Across: 	x++; break;
			}
		}
		int length = list.size();
		
		for(int j = 0; j < player.tiles.length; j++)
		{
			if(player.tiles[j] == null && list.size() > 0)
			{
				player.tiles[j] = list.get(0);
				list.remove(0);
			}
		}
		
		if(length == 0)
			return false;
		if(first && !star)
			return false;
		if(!first && existing == 0)
			return false;
		
		return true;
	}

	void update()
	{
		for(int i = 0; i < players.length; i++)
		{
			playerInfo[i].setText(players[i].agent.name + "      Score: " + players[i].score);
			playerTiles[i].removeAll();
			for(Tile tile : players[i].tiles)
			{
				if(playerTiles[i] != null)
					playerTiles[i].add(new JLabel(tile.img));
			}
		}
		
		invalidate();
		validate();
		repaint();
	}
	
	boolean gameOver()
	{
		if(passes > 40)
			return true;
		return false;
	}
	
	void printResults()
	{
		ArrayList<Player> noTiles = new ArrayList<Player>();
		int totalMinuses = 0;
		for(Player player : players)
		{
			int minus = 0;
			for(Tile tile : player.tiles)
				if(tile != null)
					minus += tile.points;
			player.score -= minus;
			totalMinuses += minus;
		}
		
		for(Player player : noTiles)
			player.score += totalMinuses;
		
		update();
		
		for(Player player : players)
		{
			System.out.println(String.format("%-32s%-4d", player.agent.name, player.score));
		}
	}
	void DoubleLetterTiles(TileType type)
	{
		MirrorTiles(0, 3, type);
		MirrorTiles(0, 11, type);
		
		MirrorTiles(2, 6, type);
		MirrorTiles(3, 7, type);
		MirrorTiles(2, 8, type);
		
		MirrorTiles(6, 6, type);
	}
	void TripleLetterTiles(TileType type)
	{
		MirrorTiles(1, 5, type);
		MirrorTiles(1, 9, type);
		MirrorTiles(5, 9, type);
	}
	void DoubleWordTiles(TileType type)
	{
		MirrorTiles(1, 1, type);
		MirrorTiles(2, 2, type);
		MirrorTiles(3, 3, type);
		MirrorTiles(4, 4, type);
	}
	void TripleWordTiles(TileType type)
	{
		MirrorTiles(0, 0, type);
		MirrorTiles(0, 7, type);
	}
	void StarTile(TileType type)
	{
		tiles[7][7].type = type;
	}
	void MirrorTiles(int x, int y, TileType type)
	{
		tiles[x][y].type 		= type;
		tiles[y][14-x].type 	= type;
		tiles[14-x][14-y].type 	= type;
		tiles[14-y][x].type 	= type;
	}
	Object[] randomize(Object[] array)
	{
		int length = array.length;
		for(int j = 0; j < length; j++)
		{
			for(int i = 0; i < length; i++)
			{
				if((Math.random()*3) > 1)
				{
					Object temp = array[i];
					array[i] = array[(i+1)%length];
					array[(i+1)%length] = temp;
				}
			}
		}
		return array;
	}
	public static boolean vowel(char a)
	{
		a = Character.toUpperCase(a);
		if(Character.isLetter(a))
			switch(a)
			{
			case 'A':
			case 'E':
			case 'I':
			case 'O':
			case 'U': return true;
			}
		return false;
	}
	public static boolean wordExists(String word) throws IOException
	{
		if(word.length() == 1) return false;
		word = word.toLowerCase();
		
		if(word.length() <= 3)
		{
			boolean accept = false;
			for(int i = 0; i < word.length(); i++)
				if(vowel(word.charAt(i)))
				{
					accept = true;
					break;
				}
			if(!accept) return false;
		}
		
		try
		{
			IndexWord indexWord = null;
			
			if(indexWord == null)
				indexWord = Dictionary.getInstance().lookupIndexWord(POS.VERB, word);
			
			if(indexWord == null)
				indexWord = Dictionary.getInstance().lookupIndexWord(POS.ADJECTIVE, word);
			
			if(indexWord == null)
				indexWord = Dictionary.getInstance().lookupIndexWord(POS.ADVERB, word);
			
			if(indexWord == null)
				indexWord = Dictionary.getInstance().lookupIndexWord(POS.NOUN, word);
			
			if(indexWord != null)
			{
				for(Synset synset : indexWord.getSenses())
				{
					for(Word w:synset.getWords())
					{
						if(w.getLemma().equalsIgnoreCase(word))
						{
							if(Character.isUpperCase(w.getLemma().charAt(0)))
								return false;
							else return true;
						}
						else if((w.getLemma()+'s').equalsIgnoreCase(word))
						{
							if(word.length() == 2) return false;
							if(Character.isUpperCase(w.getLemma().charAt(0)))
								return false;
							else return true;
						}
						else try
						{
							Integer.parseInt(w.getLemma());
							return false;
						}
						catch(Exception e) {}

						for(char c : w.getLemma().toCharArray())
							if(c == '_')
								return false;
					}
				}
				return true;
			}
			else
				return false;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		}
		return false;
	}	
	
	public static void main(String[] args) throws HeadlessException, IOException
	{
		String dictionary = "";
		
		/*sc = new Scanner(System.in);
		String input;
		
		System.out.print("Run in debug mode? [Y/N] ");
		
		do
		{
			input = sc.nextLine();
		}
		while(!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("N"));*/
			
		if(args.length > 0) 
			dictionary = args[0];
			
		new Scrabble(dictionary, false);
	}
}