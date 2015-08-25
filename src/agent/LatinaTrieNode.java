package agent;
import java.io.Serializable;

public class LatinaTrieNode implements Serializable {

	private static final long serialVersionUID = 1L;
	private char c;
    private LatinaTrieNode firstChild;
    private LatinaTrieNode sibling;
    private boolean terminalState;
    private int indexInTrie;
    private int numChildren;

    public LatinaTrieNode() {
        c = ' ';
        firstChild = null;
        sibling = null;
        terminalState = false;
        numChildren = 0;
    }

    public void setChar(char c) {
        this.c = c;
    }

    public char getChar() {
        return c;
    }

    public LatinaTrieNode getFirstChild() {
        return firstChild;
    }

    public LatinaTrieNode getSibling() {
        return sibling;
    }

    public void setSibling(LatinaTrieNode node) {
        sibling = node;
    }

    public void setTerminalState() {
        terminalState = true;
    }

    public boolean isTerminalState() {
        return terminalState;
    }

    public void setIndexInTrie(int index) {
        indexInTrie = index;
    }

    public int getIndexInTrie() {
        return indexInTrie;
    }

    public int getNumChildren() {
        return numChildren;
    }

    public boolean hasChild(char c) {
        LatinaTrieNode node = firstChild;
        while (node != null && node.getChar() < c) node = node.getSibling();
        return (node != null && node.getChar() == c);
    }

    public LatinaTrieNode getChild(char c) {
        LatinaTrieNode node = firstChild;
        while (node != null && node.getChar() < c) node = node.getSibling();
        return (node == null || node.getChar() > c ? null : node);
    }

    public void addChild(char c, LatinaTrieNode node) {
        node.setChar(c);
        if (firstChild == null) firstChild = node;
        else if (firstChild.getChar() > c) {
            node.setSibling(firstChild);
            firstChild = node;
        } else {
            LatinaTrieNode child = firstChild;
            while (child.getChar() != c &&
                    child.getSibling() != null &&
                    child.getSibling().getChar() < c)
                child = child.getSibling();
            if (child.getChar() == c) {
                return;
            } else if (child.getSibling() == null) {
                child.setSibling(node);
            } else {
                node.setSibling(child.getSibling());
                child.setSibling(node);
            }
        }
        numChildren++;
    }

}