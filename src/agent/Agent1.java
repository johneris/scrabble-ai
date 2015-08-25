package agent;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;
import game.*;

public class Agent1 extends Agent
{
	Scanner in = new Scanner(System.in);
	static ArrayList<Tile> my_tiles = new ArrayList<Tile>();
	
	static ArrayList<LatinaMove> possible_moves = new ArrayList<LatinaMove>();
	static ArrayList<LatinaMove> candidate_moves = new ArrayList<LatinaMove>();

	ArrayList<String> valid_words = new ArrayList<String>();
	static BoardTile[][] board_copy;
	
	static char[][] constraints;
	static String[] vertical_constraint;
	static String[] horizontal_constraint;
	
	int blank_ct;
	static boolean first;
	static LatinaDictionary dawg = null;
	boolean had_passed = false;

	public Agent1()
	{
		name = "Latina";
		constraints = new char[15][15];
		
		try {
			dawg = LatinaDictionary.getDAWG("src/agent/latina_dawg.dat");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	 
	public boolean WillExchange(BoardTile[][] board, Tile[] tiles)
	{
		int count_vowel = 0;
		
		sortTiles();
		my_tiles.clear();
		blank_ct = 0;
		
		for(Tile t : tiles)
		{
			if(vowel(t.letter))	count_vowel++;
			my_tiles.add(t);
		}
		
		for(Tile t : my_tiles)
		{
			if(t.letter == ' ')	{
				blank_ct++;
				continue;
			}
		}
		
		// if first to move and has blank
		if(board[7][7].tile == null && blank_ct > 0)	return false;	
		// if no vowel is in the rack
		if(count_vowel == 0)	return true;
		// if had passed from previous turn
		if(had_passed == true)	{
			had_passed = false;
			return true;
		}
		
		return false;
	}
		 
	public TileMove Move(BoardTile[][] board, Tile[] tiles)
	{	
		TileMove move = null;
		
		blank_ct = 0;
		first = false;	
		my_tiles.clear();
		possible_moves.clear();
		candidate_moves.clear();
		
		for(Tile t : tiles)
		{
			my_tiles.add(t);
		}
		board_copy = board;
		
		String rack = new String();
		
		for(Tile t : my_tiles)
		{
			if(t.letter == ' ')	{
				blank_ct++;
				continue;
			}
			rack += ("" + t.letter);
		}
		
		
		// if first to move
		if(board[7][7].tile == null)	{
			first = true;
			
			valid_words.clear();
			leftPart("", dawg.getRoot(), 
					7, rack);
			
			for(String w : valid_words)	{
				for(int i = (7 - w.length())+1; i <= 7; i++)	{
					possible_moves.add(new LatinaMove(
							new TileMove(i, 7, w, Direction.Across),
							getScore(new TileMove(i, 7, w, Direction.Across))
							));
					sortMove();
					System.out.println("x: " + i +
							" y: " + 7 +
							" word: " + w +
							" dir: Across" + 
							" score: " + getScore(new TileMove(i, 7, w, Direction.Across)));
				}
			}
			if(!possible_moves.isEmpty())	{
				int i = 0;
				try {
					while(!wordExists(possible_moves.get(i).move.word)
						& valid(possible_moves.get(i++).move));
					return possible_moves.get(i-1).move;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// end if first to move
		
		else	{		
			//set up constraints
			vertical_constraint = new String[15];
			horizontal_constraint = new String[15];
			//get board constraints
			for(int i=0; i < 15; i++)	{
				for(int j=0; j < 15; j++)	{
					if(board_copy[i][j].tile == null)	{
						if(i != 0 && board_copy[i-1][j].tile != null)	{
							constraints[i][j] = '$';
						}
						if(i != 14 && board_copy[i+1][j].tile != null)	{
							constraints[i][j] = '$';
						}
						if(constraints[i][j] != '$')	{
							constraints[i][j] = '?';
						}
					}
					else	{
						constraints[i][j] = board_copy[i][j].tile.letter;
					}
				}
			}
			// set vertical and horizontal constraints
			for(int i=0; i < 15; i++)	{
				vertical_constraint[i] = new String(); 
				horizontal_constraint[i] = new String(); 
				for(int j=0; j < 15; j++)	{
					vertical_constraint[i] += ("" + constraints[i][j]);
					horizontal_constraint[i] += ("" + constraints[j][i]);
				}
			}
			//end set up constraints
			
			
			// get all plays vertical
			for(int i = 0; i < 15; i++)	{
				ArrayList <int[]> anchors = new ArrayList <int[]>();
				anchors = getAnchors(vertical_constraint[i]);
				System.out.println("v cons " + i + " " + vertical_constraint[i]);
				
				if(vertical_constraint[i].contains("$")
					&& !vertical_constraint[i].contains("A") && !vertical_constraint[i].contains("B")
					&& !vertical_constraint[i].contains("C") && !vertical_constraint[i].contains("D")
					&& !vertical_constraint[i].contains("E") && !vertical_constraint[i].contains("F")
					&& !vertical_constraint[i].contains("G") && !vertical_constraint[i].contains("H")
					&& !vertical_constraint[i].contains("I") && !vertical_constraint[i].contains("J")
					&& !vertical_constraint[i].contains("K") && !vertical_constraint[i].contains("L")
					&& !vertical_constraint[i].contains("M") && !vertical_constraint[i].contains("N")
					&& !vertical_constraint[i].contains("O") && !vertical_constraint[i].contains("P")
					&& !vertical_constraint[i].contains("Q") && !vertical_constraint[i].contains("R")
					&& !vertical_constraint[i].contains("S") && !vertical_constraint[i].contains("T")
					&& !vertical_constraint[i].contains("U") && !vertical_constraint[i].contains("V")
					&& !vertical_constraint[i].contains("W") && !vertical_constraint[i].contains("X")
					&& !vertical_constraint[i].contains("Y") && !vertical_constraint[i].contains("Z")
					)	{
					
					valid_words.clear();
					leftPart("", dawg.getRoot(), 
							7, rack);
					for(String w : valid_words)	{
						for(int j = 0; j < 15; j++)	{
							try {
								if(valid(new TileMove(i, j, w, Direction.Downward)))	{
									possible_moves.add(new LatinaMove(
											new TileMove(i, j, w, Direction.Downward),
											getScore(new TileMove(i, j, w, Direction.Downward))
											) );
									sortMove();
									System.out.println("x: " + i +
											" y: " + j +
											" word: " + w +
											" dir: Downward" + 
											" score: " + getScore(new TileMove(i, j, w, Direction.Downward)));
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					continue;
				}
					
				
				dawg.clearLegalWords();
				valid_words.clear();
				
				for(int [] x : anchors)	{
					System.out.println("anchor: " + x[0] + " limit: " + x[1]);
					dawg.leftPart("", dawg.getRoot(), x[1], rack, x[0], vertical_constraint[i]);
					valid_words = dawg.getLegalWords();
					for(String w : valid_words)	{
						for(int j = 0; j < 15; j++)	{
							try {
								if(valid(new TileMove(i, j, w, Direction.Downward)))	{
									possible_moves.add(new LatinaMove(
											new TileMove(i, j, w, Direction.Downward),
											getScore(new TileMove(i, j, w, Direction.Downward))
											) );
									sortMove();
									System.out.println("x: " + i +
											" y: " + j +
											" word: " + w +
											" dir: Downward" + 
											" score: " + getScore(new TileMove(i, j, w, Direction.Downward)));
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					dawg.clearLegalWords();
					valid_words.clear();
				}
				anchors.clear();
			}
			
			
			// get all plays horizontal
			for(int i = 0; i < 15; i++)	{
				ArrayList <int[]> anchors = new ArrayList <int[]>();
				anchors = getAnchors(horizontal_constraint[i]);
				System.out.println("h cons " + i + " " + horizontal_constraint[i]);
				
				if(horizontal_constraint[i].contains("$")
					&& !horizontal_constraint[i].contains("A") && !horizontal_constraint[i].contains("B")
					&& !horizontal_constraint[i].contains("C") && !horizontal_constraint[i].contains("D")
					&& !horizontal_constraint[i].contains("E") && !horizontal_constraint[i].contains("F")
					&& !horizontal_constraint[i].contains("G") && !horizontal_constraint[i].contains("H")
					&& !horizontal_constraint[i].contains("I") && !horizontal_constraint[i].contains("J")
					&& !horizontal_constraint[i].contains("K") && !horizontal_constraint[i].contains("L")
					&& !horizontal_constraint[i].contains("M") && !horizontal_constraint[i].contains("N")
					&& !horizontal_constraint[i].contains("O") && !horizontal_constraint[i].contains("P")
					&& !horizontal_constraint[i].contains("Q") && !horizontal_constraint[i].contains("R")
					&& !horizontal_constraint[i].contains("S") && !horizontal_constraint[i].contains("T")
					&& !horizontal_constraint[i].contains("U") && !horizontal_constraint[i].contains("V")
					&& !horizontal_constraint[i].contains("W") && !horizontal_constraint[i].contains("X")
					&& !horizontal_constraint[i].contains("Y") && !horizontal_constraint[i].contains("Z")
						)	{
					
						valid_words.clear();
						leftPart("", dawg.getRoot(), 
								7, rack);
						for(String w : valid_words)	{
							for(int j = 0; j < 15; j++)	{
								try {
									if(valid(new TileMove(j, i, w, Direction.Across)))	{
										possible_moves.add(new LatinaMove(
												new TileMove(j, i, w, Direction.Across),
												getScore(new TileMove(j, i, w, Direction.Across))
												) );
										sortMove();
										System.out.println("x: " + j +
												" y: " + i +
												" word: " + w +
												" dir: Across" + 
												" score: " + getScore(new TileMove(j, i, w, Direction.Across)));
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						continue;
					}
				
				dawg.clearLegalWords();
				valid_words.clear();
				
				for(int [] x : anchors)	{
					System.out.println("anchor: " + x[0] + " limit: " + x[1]);
					dawg.leftPart("", dawg.getRoot(), x[1], rack, x[0], horizontal_constraint[i]);
					valid_words = dawg.getLegalWords();
					for(String w : valid_words)	{
						for(int j = 0; j < 15; j++)	{
							try {
								if(valid(new TileMove(j, i, w, Direction.Across)))	{
									possible_moves.add(new LatinaMove(
											new TileMove(j, i, w, Direction.Across),
											getScore(new TileMove(j, i, w, Direction.Across))
											) );
									sortMove();
									System.out.println("x: " + i +
											" y: " + j +
											" word: " + w +
											" dir: Downward" + 
											" score: " + getScore(new TileMove(j, i, w, Direction.Across)));
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					dawg.clearLegalWords();
					valid_words.clear();
				}
				anchors.clear();
			}
			if(!possible_moves.isEmpty())	{
				int i = 0;
				int highest_score = possible_moves.get(i).getScore();
				try {
					for(LatinaMove m : possible_moves)	{
						if(wordExists(m.move.word)
							&& valid(m.move) 
							&& m.getScore()+2 >= highest_score )	{
							candidate_moves.add(m);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			move = possible_moves.get(0).move;
		}// end else
		
		if(move == null)	had_passed = true;
		return move;
	}
	
	
	public String ExchangeTiles(BoardTile[][] board, Tile[] tiles)
	{
		had_passed = false;
		String temp_tiles = new String();
		String exhange = new String();
		
		for(Tile t : tiles)	{
			temp_tiles += t.letter;
		}
		
		for(int i = 0; i < 3 && i < tiles.length ; i++)
			exhange += temp_tiles.indexOf(my_tiles.get(i).letter);
		
		return exhange;
	}
	
	public ArrayList <int[]> getAnchors(String constraint)	{
		ArrayList <int[]> anchors = new ArrayList <int[]>();
		int ctr = 0;
		//
		// index 0 - anchor
		// index 1 - limit
		//
		for(int i=0; i < constraint.length(); i++)	{
			while(constraint.charAt(i) == '?' || constraint.charAt(i) == '$')	{
				i++;
				ctr++;
				if(i == constraint.length())	break;
			}
			if(i == constraint.length())	break;
			anchors.add(new int[]{i-1, ctr});
			ctr = 0;
		}
		
		return anchors;
	}
	
	public void leftPart(String partial_word, LatinaDawgNode node, 
    		int limit, String rack)	{
    	if(limit > 0)	{
    		for(char edge : node.getEdges())	{
    			if(rack.contains(edge + ""))	{
    				rack = rack.replaceFirst(edge + "", "");
    				LatinaDawgNode node_ = node.getChild(edge);
    				if(node_.isTerminalState())	{
    					valid_words.add(partial_word + edge);
    				}
    				leftPart(partial_word + edge, node_, 
    			    		limit-1, rack);
    				rack += edge;
    			}
    		}
    	}
    }
	
	public static TileMove chooseMove()	{
		// will be depending on the rack
		// rack heuristic 1 - vowel and consonant balance
		// rack heuristic 2 - duplicated letters
		ArrayList<LatinaMove> filtered_dup = new ArrayList<LatinaMove>();
		ArrayList<LatinaMove> filtered_vc = new ArrayList<LatinaMove>();
		
		TileMove move = null;
		
		String duplicates = checkDuplicates();
		if(!duplicates.equals(""))	{
			for(LatinaMove m : candidate_moves)	{
				if(m.move.word.contains(duplicates))	{
					filtered_dup.add(m);
				}
			}
		}
		else	{
			filtered_dup.addAll(candidate_moves);
		}
		
		String vc = checkVowelConsonantBalance();
		if(!vc.equals(""))	{
			if(vc.equals("TOO MANY VOWEL"))	{
				for(LatinaMove m : filtered_dup)	{
					if(countVowels(m.move.word) > 0)
						filtered_vc.add(m);
				}
			}
			else if(vc.equals("TOO MANY CONSONANT"))	{
				for(LatinaMove m : filtered_dup)	{
					if(countConsonants(m.move.word) > 0)
						filtered_vc.add(m);
				}
			}
		}
		else	{
			filtered_vc.addAll(filtered_dup);
		}
		
		move = filtered_vc.get(0).move;
		return move;
	}
	
	public static String checkDuplicates()	{
		int ctr = 0;
		for(Tile t : my_tiles)	{
			for(Tile t_ : my_tiles)	{
				if(t.letter == t_.letter)	{
					ctr++;
					if(ctr >= 3)	return t.letter + "";
				}
			}
			ctr = 0;
		}
		return "";
	}
	
	public static String checkVowelConsonantBalance()	{
		if(countVowels() >= 4)	return "TOO MANY VOWEL";
		if(countConsonants() >= 4)	return "TOO MANY CONSONANT";
		return "";
	}
	
	public static int countVowels()	{
		int ctr = 0;
		for(Tile t : my_tiles)	{
			if(t.letter == 'A' ||
				t.letter == 'E' ||
				t.letter == 'I' ||
				t.letter == 'O' ||
				t.letter == 'U' )
				ctr++;
		}
		return ctr;
	}
	
	public static int countVowels(String str)	{
		int ctr = 0;
		for(int i = 0; i < str.length(); i++)	{
			if(str.charAt(i) == 'A' ||
				str.charAt(i) == 'E' ||
				str.charAt(i) == 'I' ||
				str.charAt(i) == 'O' ||
				str.charAt(i) == 'U' )
				ctr++;
		}
		return ctr;
	}
	
	public static int countConsonants()	{
		int ctr = 0;
		for(Tile t : my_tiles)	{
			if(t.letter != 'A' &&
				t.letter != 'E' &&
				t.letter != 'I' &&
				t.letter != 'O' &&
				t.letter != 'U' )
				ctr++;
		}
		return ctr;
	}
	
	public static int countConsonants(String str)	{
		int ctr = 0;
		for(int i = 0; i < str.length(); i++)	{
			if(str.charAt(i) != 'A' &&
				str.charAt(i) != 'E' &&
				str.charAt(i) != 'I' &&
				str.charAt(i) != 'O' &&
				str.charAt(i) != 'U' )
				ctr++;
		}
		return ctr;
	}
	
	public static int getLetterPoints(String tile)	{
    	tile.toUpperCase();
    	char ch = tile.charAt(0);
    	int pt = 0;
    	switch(ch)
		{
			case 'A':
				pt = 1;
				break;
			case 'B':
				pt = 3;
				break;
			case 'C':
				pt = 3;
				break;
			case 'D':
				pt = 2;
				break;
			case 'E':
				pt = 1;
				break;
			case 'F':
				pt = 4;
				break;
			case 'G':
				pt = 2;
				break;
			case 'H':
				pt = 4;
				break;
			case 'I':
				pt = 1;
				break;
			case 'J':
				pt = 8;
				break;
			case 'K':
				pt = 5;
				break;
			case 'L':
				pt = 1;
				break;
			case 'M':
				pt = 3;
				break;
			case 'N':
				pt = 1;
				break;
			case 'O':
				pt = 1;
				break;
			case 'P':
				pt = 3;
				break;
			case 'Q':
				pt = 10;
				break;
			case 'R':
				pt = 1;
				break;
			case 'S':
				pt = 1;
				break;
			case 'T':
				pt = 1;
				break;
			case 'U':
				pt = 1;
				break;
			case 'V':
				pt = 4;
				break;
			case 'W':
				pt = 4;
				break;
			case 'X':
				pt = 8;
				break;
			case 'Y':
				pt = 4;
				break;
			case 'Z':
				pt = 10;
				break;
		}
    	return pt;
    }
    
	public static int getScore(TileMove move)	{
		BoardTile[][] tiles = new BoardTile[15][15];
		
		for(int j = 0; j < 15; j++)
		{
			for(int i = 0; i < 15; i++)
			{
				tiles[i][j] = new BoardTile(board_copy[i][j]);
			}
		}
		
		int final_score = 0;
		int used = 0;
		
		first = false;
		
		int x = move.x;
		int y = move.y;
		
		int score = 0;
		int wordMultiplier = 1;
		
		for(int i = 0; i < move.word.length(); i++)
		{
			if(tiles[x][y].tile == null)
			{
				tiles[x][y].tile = find(move.word.charAt(i));
				if(tiles[x][y].tile == null)	return 0;
				
				used++;
				
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
				final_score += score2;
				
				if(!word.equals(""))
				{
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
		final_score += score;
		
		if(used == 7)
		{
			final_score += 50;
		}
		
		return final_score;
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
	
	boolean valid(TileMove move) throws IOException
	{
    	Tile[] tiles = new Tile[7];
    	
    	for(int i=0; i < 7; i++)
    	{
    		tiles[i] = new Tile(my_tiles.get(i));
    	}
    	
		if(!dawg.isWord(move.word))
			return false;
		
		int x = move.x;
		int y = move.y;
		
		switch(move.dir)
		{
			case Downward:
				if(y + move.word.length()-1 > 14 || y < 0)
					return false;
				if(y + move.word.length() <= 14 && board_copy[x][y + move.word.length()].tile != null)
					return false;
				if(y - 1 >= 0 && board_copy[x][y - 1].tile != null)
					return false;
				break;
			case Across:
				if(x + move.word.length()-1 > 14 || x < 0)
					return false;
				if(x + move.word.length() <= 14 && board_copy[x + move.word.length()][y].tile != null)
					return false;
				if(x - 1 >= 0 && board_copy[x - 1][y].tile != null)
					return false;
				break;
		}
		
		ArrayList<Tile> list = new ArrayList<Tile>();
		
		boolean star = false;
		int existing = 0;
		for(int i = 0; i < move.word.length(); i++)
		{
			if(board_copy[x][y].tile == null)
			{
				Tile tile = find(move.word.charAt(i));
				
				if(tile != null)
				{
					list.add(tile);
					
					int x2, y2;
					switch(move.dir)
					{
						case Downward:

							x2 = x;
							y2 = y; 

							if((x-1 >= 0 && board_copy[x-1][y].tile != null) || 
								(x+1 <= 14 && board_copy[x+1][y].tile != null))
							{	
								existing++;
								
								while(x2-1 >= 0 && board_copy[x2-1][y2].tile != null)
									x2--;
									
								String word = "";
								
								while((x == x2 && y == y2) || (x2 <= 14 && board_copy[x2][y2].tile != null))
								{
									if(x == x2 && y == y2)
										word += tile.letter;
									else
										word += board_copy[x2][y2].tile.letter;
									x2++;
								}
								
								if(!dawg.isWord(word))
								{										
									for(int j = 0; j < tiles.length; j++)
									{
										if(tiles[j] == null)
										{
											tiles[j] = list.get(0);
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
							
							if((y-1 >= 0 && board_copy[x][y-1].tile != null) || 
								(y+1 <= 14 && board_copy[x][y+1].tile != null))
							{				
								existing++;				
								while(y2-1 >= 0 && board_copy[x2][y2-1].tile != null)
									y2--;
									
								String word = "";
								
								while((x == x2 && y == y2) || y2+1 <= 14 && board_copy[x2][y2].tile != null)
								{
									if(x == x2 && y == y2)
										word += tile.letter;
									else
										word += board_copy[x2][y2].tile.letter;
									y2++;
								}
								
								if(!dawg.isWord(word))
								{										
									for(int j = 0; j < tiles.length; j++)
									{
										if(tiles[j] == null)
										{
											tiles[j] = list.get(0);
											list.remove(0);
										}
									}
									return false;
								}
							}
						break;
					}
					if(board_copy[x][y].type == TileType.Star)
						star = true;
				}
				else
				{
					for(int j = 0; j < tiles.length; j++)
					{
						if(tiles[j] == null)
						{
							tiles[j] = list.get(0);
							list.remove(0);
						}
					}
					return false;
				}
			}
			else if(board_copy[x][y].tile.letter != move.word.charAt(i))
			{
				for(int j = 0; j < tiles.length; j++)
				{
					if(tiles[j] == null)
					{
						tiles[j] = list.get(0);
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
		
		for(int j = 0; j < tiles.length; j++)
		{
			if(tiles[j] == null && list.size() > 0)
			{
				tiles[j] = list.get(0);
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
	
    public static Tile find(char a)
	{
    	Tile[] tiles = new Tile[7];
    	
    	for(int i=0; i < 7; i++)
    	{
    		tiles[i] = new Tile(my_tiles.get(i));
    	}
    	
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
					return tile;
				}
			}
		}
		//System.out.println("error finding tile");
		return null;
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
    
    public static void sortMove()
	{
    	int i=0, j=0; 
		LatinaMove temp = new LatinaMove();
		
		for(i=1; i<possible_moves.size(); i++)	{
			temp = possible_moves.get(i);
			j = i;
			while( (j>0) && (possible_moves.get(j-1).getScore() < temp.getScore()) )	{
				possible_moves.remove(j);
				possible_moves.add(j, possible_moves.get(j-1));
				j -= 1;
			}
			possible_moves.remove(j);
			possible_moves.add(j, temp);
		}
	}

    
    
	public static void sortTiles()
	{
		int i=0, j=0; 
		Tile temp = new Tile();
		
		for(i=1; i<my_tiles.size(); i++)	{
			temp = my_tiles.get(i);
			j = i;
			while( (j>0) && (my_tiles.get(j-1).points > temp.points) )	{
				my_tiles.remove(j);
				my_tiles.add(j, my_tiles.get(j-1));
				j -= 1;
			}
			my_tiles.remove(j);
			my_tiles.add(j, temp);
		}
	}

}