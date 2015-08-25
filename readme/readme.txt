README PLS - ARTIFICIAL INTELLIGENCE - SCRABBLE

	>	You are advised to only edit Agent1.java.
	
	>	You may edit other parts of the program, but please take note that
		the program to be used on the final showdown will be the most
		recent patch of the environment made by the representatives.
		
	> 	If you ever find a bug or have some suggestions that you think 
		can help the representatives improve the environment, please talk
		to your section's respective representative.
		
	>	If you ever create another file (e.g. dictionary.txt), please rename
		it so that its name will be unique. This is to avoid teams having
		the same file names, which will cause a lot of trouble. Add
		your surname as a prefix to the filename, for example.
		
	> 	Agent1.java will contain four methods:
	
			public Agent1()
				- sets team name
				--> name = "Mineski";
				
			public boolean WillExchange(BoardTile[][] board, Tile[] tiles)
				- decides whether you use your turn to exchange tiles
						or make a move.
				-- return true if you will use your turn to exchange.
				-- return false if you will use your turn to move.
				--> if(I can't spell anything) return true;
				--> if(I can spell 'cat') return false;
						
			public TileMove Move(BoardTile[][] board, Tile[] tiles)
				- makes your turn based on the algorithm you state.
				-- return new TileMove(<startX>, <startY>, <word>, <direction>);
				--> return new TileMove(5, 7, "HELLO", Direction.Across);
					----------------------------------------------
					|  |  |  |  |  |0 |  |  |  |  |  |  |  |  |  |
					----------------------------------------------
					|  |  |  |  |  |1 |  |  |  |  |  |  |  |  |  |
					----------------------------------------------
					|  |  |  |  |  |2 |  |  |  |  |  |  |  |  |  |
					----------------------------------------------
											.
											.
											.
					----------------------------------------------
					|  |  |  |  |  |6 |  |  |  |  |  |  |  |  |  |
					----------------------------------------------
					|0 |1 |2 |3 |4 | h| e| l| l| o|  |  |  |  |14|
					----------------------------------------------
					|  |  |  |  |  |8 |  |  |  |  |  |  |  |  |  |
					----------------------------------------------
											.
											.
											.
					----------------------------------------------
					|  |  |  |  |  |14|  |  |  |  |  |  |  |  |  |
					----------------------------------------------
											
				>> tip:
					->	Make sure that your word is a valid word in the
						WordNet dictionary.
					-> 	Make sure your positioning is corrent.
					->	Make sure your direction is correct.
					
				>> faq:
				
					-> 	Suppose you have a 'spar' word in the Scrabble board.
						You wanted to spell the word 'sparing' because you
						have the letters 'i', 'n' and 'g'. Can you just state
						that your word is 'ring' and position it at the letter
						'r' of 'spar'?
						
						--> No. You should state that your word is 'sparing'
							and position it at the first letter of 'spar' with
							its corresponding direction.
				
			public String ExchangeTiles(BoardTile[][] board, Tile[] tiles)
				- decides what tiles you will return.
				-- return <string> // string contains the soon-to-be-replaced tiles.
				--> return "146"; // will replace tiles[1], tiles[4], and tiles[6].
				
						Previous tiles : 	ABCDEFG
						Current tiles:		AQCDMFI
						
				>> faq:
					
					->	Can I return "0123456"?
						--> Yes.
					
					-> 	Can I return "6543210"?
						--> Yes.
					
					->	Can I return ""?
						--> Yes.
					
					-> 	Can I return "111111"?
						--> Yes.
						
					-> 	Can I return "0b2d4f"?
						--> Yes.
								
	>	Agent1.java method parameters:
			
			a. BoardTile[][] board
			b. Tile[] tiles
			
			- BoardTile[][] board
				->	gives the state of the Scrabble Board (15 x 15).
					--> board[7][7]   is the middlemost tile.
					-->	board[0][0]   is the upper-leftmost tile.
					-->	board[14][14] is the lower-rightmost tile.
					
					::attributes::
						-> type 
							--> if(board[7][7].type == TileType.DoubleWord)
								- TileType.Normal
								- TileType.DoubleLetter
								- TileType.TripleLetter
								- TileType.DoubleWord
								- TileType.TripleWord
								
						-> tile
							--> if(board[7][7].tile == null)
							--> if(board[7][7].tile.letter == 'A')
			
			- Tile[] tiles
				->	gives the states of your agent's tiles (7 tiles).
					
					--> tiles[0] is your first tile.
					--> tiles[6] is your last tile.
					
					::attributes::
						-> letter
							--> if(tile.letter == 'A')
						-> points
							--> if(tile.points >= 2)

	>	A default algorithm is given to the agents. It's not smart enough; that's what you
		will have to fix. You will edit the contents of Agent1.java so that it will be 
		smart enough to build its own words upon the Scrabble board.
	
	>	Please take note that the rules to be followed is the one uploaded by 
		Sir Jude Mikhael Cruz at the AI2013_Project: Scrabble Facebook group.
		Please read it.
		
	> 	Also take note that the dictionary to be used in the final showdown is 
		the WordNet; which means only NOUNS, VERBS, ADJECTIVES and ADVERBS are
		accepted by the environment. Anything else (prepositions, pronouns, etc.)
		are not. Also take note that some PROPER NOUNS and ACRONYMS are accepted.
		
	>	In order to compile Agent1.java, you must open the 'Scrabble/bin' folder
		through the command prompt and compile it using this command:
		
			javac agent/Agent1.java
			
	>	In order to run the Scrabble program, you must open the 'Scrabble/bin' folder 
		through the command prompt and run the program using this command:
			
			java game/Scrabble Dictionary/temp_dictionary.txt
			
		--> Take note that the 'temp_dictionary' is a temporary dictionary. It is only a part
			of the WordNet. If you want a copy of the WordNet and have it set up into your computer, 
			approach your section's respective representative. He/She will ask you to do a series of
			instructions(including downloading the Eclipse IDE), in which after it is done, 
			you will have your WordNet dictionary set up.