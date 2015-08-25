package agent;

import game.TileMove;

class LatinaMove	{
	TileMove move;
	int score;
	
	public LatinaMove(TileMove m, int s)	{
		move = m;
		score = s;
	}
	
	public LatinaMove(){}
	
	public int getScore()	{
		return score;
	}
}