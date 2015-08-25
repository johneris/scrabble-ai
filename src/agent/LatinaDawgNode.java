package agent;
import java.io.Serializable;
import java.util.Arrays;

class LatinaDawgNode implements Serializable {

    private static final long serialVersionUID = 1L;
	private char[] letters;
    private LatinaDawgNode[] children;
    private boolean terminalState;
    private int numChildren;
    private int indexInDawg;
    
    public LatinaDawgNode(int maxChildren) {
        letters = new char[maxChildren];
        children = new LatinaDawgNode[maxChildren];
        terminalState = false;
        numChildren = 0;
    }

    public void addChild(char c, LatinaDawgNode node) {
        letters[numChildren] = c;
        children[numChildren] = node;
        numChildren++;
    }

    public void setTerminalState() {
        terminalState = true;
    }

    public boolean isTerminalState() {
        return terminalState;
    }

    public int getNumChildren() {
        return numChildren;
    }

    public int getIndexOfChild(char c) {
        return Arrays.binarySearch(letters, c);
    }

    public LatinaDawgNode getChild(int index) {
        return (index < 0 || index >= numChildren ? null : children[index]);
    }

    public char getChar(int index) {
        return (index < 0 || index >= numChildren ? ' ' : letters[index]);
    }

    public LatinaDawgNode getChild(char c) {
        int index = getIndexOfChild(c);
        return (index < 0 ? null : children[index]);
    }

    public void setIndexInDawg(int index) {
        indexInDawg = index;
    }

    public int getIndexInDawg() {
        return indexInDawg;
    }

    public char[] getEdges()	{
    	return letters;
    }
    
    public boolean hasEdge(char c)	{
    	String str = new String();
    	for(char ch : letters)	str += ch;
    	return str.contains(c + "");
    }
}
