/* ANUPSINGH PARDESHI cs610 3859 prp */

///<Class>
///node_3859 is class for creating nodes in the huffman tree.
///</Class>
public class node_3859 implements Comparable<node_3859>
{
	public int frequency;
	public int symbol;
	public node_3859 leftChildNode;
	public node_3859 rightChildNode;
	
	///<Paramterized constructor>
	///node_3859 creates the node according to symbol, fre, left child node and right child node.
	///</Paramterized constructor>
	public node_3859(int ch, int freq, node_3859 lChild, node_3859 rChild) 
	{
		this.symbol = ch;
		this.frequency = freq;
		this.leftChildNode = lChild;
		this.rightChildNode = rChild;
	}
	
	///<Summary>
	///compareTo is overridden method to compare two nodes.
	///</Summary>
	@Override
	public int compareTo(node_3859 nodeTobeComapared) 
	{
		return this.frequency-nodeTobeComapared.frequency;
	}
}
