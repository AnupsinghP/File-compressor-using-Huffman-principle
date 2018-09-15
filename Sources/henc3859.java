/* ANUPSINGH PARDESHI */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

///<Class>
///henc3859 implements the huffman encoding to compress the file.
///</Class>
public class henc3859 
{
	node_3859[] hufArray = new node_3859[257];
	int location = 0;
	int symbols, fileLength;	
	int[] frequencyArray = new int[256];
	String[] huffmanCodes = new String[256];

	public static void main(String[] args) 
	{
		try 
		{
			//Accesing the filePath
			String filePath = args[0];
			File file = new File(filePath);
			String path = file.getAbsolutePath();
			
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
			int byt;
        
			henc3859 huff = new henc3859();
			
			//Timer to calculate the time required to compress the file.
			long start = System.currentTimeMillis();
			//Frequency reading
			while((byt = bis.read()) != -1)
			{
				huff.fileLength++;
				huff.frequencyArray[byt]++; 
			}
			
			bis.close();
			
			//Creating the huffman tree.
			huff.calculateHuffmanCode_3859();
			
			//Creating the encoded file
			huff.createFile_3859(path);
			
			//Deleting the orignal file
			Files.deleteIfExists(Paths.get(path));
			
			//Total time taken for compression.
			System.out.println("Time taken to compress: "+(System.currentTimeMillis()-start)+"ms");
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	
	///<Summary>
	///calculateHuffmanCode_3859 Calculates the huffman code.
	///<Summary>
	public void calculateHuffmanCode_3859()
	{
		//Creating min heap
		createMinHeap_3859();
        
		symbols = location;
		
		//creating huffman tree
		createHuffmanTree_3859();
		
		//Traversing huffman tree for the codes.
		assignCodes_3859(hufArray[1], "");
	}
	
	///<Summary>
	///createMinHeap_3859 creates min heap to build huffman tree.
	///<Summary>	
	public void createMinHeap_3859()
	{
		for(int i=0; i<frequencyArray.length; i++)
		{
			if(frequencyArray[i]>0)
			{
				location++;
				insert_3859(new node_3859(i, frequencyArray[i], null, null));
			}
		}
	}
	
	///<Summary>
	///createHuffmanTree_3859 creates huffman tree by joining two least freq nodes into one.
	///<Summary>	
	public void createHuffmanTree_3859()
	{
		int totalNodes = location;
	
		for(int i=1; i<totalNodes;i++)
		{
			node_3859 leftChildNode = getMinimumNode_3859();
			node_3859 rightChildNode = getMinimumNode_3859();
			node_3859 rootNode = new node_3859('\0',leftChildNode.frequency+rightChildNode.frequency, leftChildNode, rightChildNode);
			location++;
			insert_3859(rootNode);
			//System.out.println(rootNode.symbol+" ->"+rootNode.frequency);
		}
	}

	///<Summary>
	///assignCodes_3859 uses recursion to assign the code traversing the tree. 0 to left and 1 to the right.
	///<Summary>
	public void assignCodes_3859(node_3859 node, String code)
	{
		if(node.leftChildNode == null && node.rightChildNode == null)
			huffmanCodes[node.symbol] = code;
		else
		{
			if(node.leftChildNode!=null)
				assignCodes_3859(node.leftChildNode, code.concat("0"));
			
			if(node.rightChildNode!=null)
				assignCodes_3859(node.rightChildNode, code.concat("1"));
		}
	}

	///<Summary>
	///getMinimumNode_3859 return the 1st element of min heap which has the minimum freq of all the nodes.
	///<Summary>
	public node_3859 getMinimumNode_3859()
	{
		node_3859 minNode= hufArray[1];
		hufArray[1] = hufArray[location];
		hufArray[location] = null;
		location--;
		heapify_3859(1);
		
		return minNode;
	}
	
	///<Summary>
	///insert_3859 inserts the node into the tree.
	///<Summary>
	public void insert_3859(node_3859 node)
	{
		if(location< 2)
		{
			hufArray[location] = node;
			return;
		}
		else
			hufArray[location] = node;
		
		
		maintainHeap_3859(location);
	}
	
	///<Summary>
	///maintainHeap_3859 uses recursion to maintain the min heap property.
	///<Summary>
	public void maintainHeap_3859(int loc)
	{
		if(isParentAvailable_3859(loc) && hufArray[loc].frequency < hufArray[parentLocation_3859(loc)].frequency)
		{
			swap_3859(loc, (int) Math.round(Math.floor(loc/2)));
			maintainHeap_3859(loc = (int) Math.round(Math.floor(loc/2)));
		}
		else
			return;
	}

	///<Summary>
	///heapify_3859 uses recursion heapify the min heap after removing the root node.
	///<Summary>	
	public void heapify_3859(int index)
	{
		if(index<1)
			return;
		///TO-DO NULL CHECK for nodes
		else if(location < (index*2) )
		{
			return;
		}
		else
		{
			int childLoc = (index*2);
			
			if(location > ((index*2)+1) && hufArray[(index*2)+1].frequency < hufArray[index*2].frequency)
				childLoc = ((index*2)+1);
			
			if(hufArray[index].frequency > hufArray[childLoc].frequency)
			{
				swap_3859(index, childLoc);
				heapify_3859(childLoc);
			}
		}
	}
	
	///<Summary>
	///parentLocation_3859 return the parent location.
	///<Summary>
	public int parentLocation_3859(int loc)
	{
		int ploc = (int) Math.round(Math.floor(loc/2));
		return ploc;
	}

	///<Summary>
	///swap_3859 swaps the two nodes given the location of the two.
	///<Summary>
	public void swap_3859(int loc1, int loc2)
	{
		node_3859 temp = hufArray[loc1];
		hufArray[loc1] = hufArray[loc2];
		hufArray[loc2] = temp;
	}
	
	///<Summary>
	///isParentAvailable_3859 return the boolean value if the parent location is available is the array or not.
	///<Summary>
	public boolean isParentAvailable_3859(int loc)
	{
		if(location > (int) Math.round(Math.floor(loc/2)) && (int) Math.round(Math.floor(loc/2))!=0)
			return true;
		
		return false;
	}
	
	///<Summary>
	///createFile_3859 generates the compressed file base on the huffman codes for the symbols.
	///<Summary>
	public void createFile_3859(String filePath) 
	{
		String hufFilePath = filePath+".huf";
		try
		{
			BufferedOutputStream bops = new BufferedOutputStream(new FileOutputStream(hufFilePath));
			
			///HEADER INFO.
			//Total unique symbols in the file.
			bops.write(symbols);
			//4 Byte long file length.
			writeIntToFile_3859(bops, fileLength);
			//Symbol and its frequency.
			for(int i =0; i<frequencyArray.length; i++)
			{
				if(frequencyArray[i]>0)
				{
					bops.write(i);
					writeIntToFile_3859(bops, frequencyArray[i]);
				}
			}
			//Main body for the file.
			
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
			int byt;
			boolean[] bitsRepresentation = new boolean[8];
			String encodedString= "";
			
			while((byt = bis.read()) != -1)
			{
				String charToWrite = huffmanCodes[byt];
				encodedString = encodedString+charToWrite;
				//8bit
				if(encodedString.length()>=8)
				{	
					for(int i=0; i<8; i++)
					{
						if(encodedString.charAt(i) == '0') 
							bitsRepresentation[i] = false;
						else
							bitsRepresentation[i] = true;
					}
					bops.write(bitToByteConverter_3859(bitsRepresentation));
					encodedString = encodedString.substring(8);
				}
			}
			//For the remaining bits of length less than 8.
			if(encodedString.length()>0)
			{
				boolean[] bitsRep = new boolean[8];
				//appending 0s.
				String fin= ("00000000"+encodedString).substring(encodedString.length());
				
				for(int i=0; i<8; i++)
				{
					if(fin.charAt(i) == '0') 
						bitsRep[i] = false;
					else
						bitsRep[i] = true;
				}
				
				bops.write(bitToByteConverter_3859(bitsRep));
			}
			
			bis.close();
			bops.close();
		}
		catch(IOException e)
		{
			System.out.println("IO Exception: "+e);
		}
	}
	
	///<Summary>
	///bitToByteConverter_3859 converts the bits into its respective byte.
	///<Summary>
	private int bitToByteConverter_3859(boolean[] bitArray)
	{
		int data = 0;
		
		for (int i = 0; i < 8; i++)
		{    
			if (bitArray[i]) 
				data += Math.pow(2, (7-i));;
		}
		
		return data;
	}
	
	///<Summary>
	///writeIntToFile_3859 writes 4byte long signed and unsigned integer.
	///<Summary>
	private void writeIntToFile_3859(BufferedOutputStream bos ,int number)
	{
		String hexRep = Integer.toHexString(number);
		String fin= ("00000000"+hexRep).substring(hexRep.length());
		int len = fin.length()/2;
		
		while(len>0)
		{
			try
			{
				bos.write(Integer.parseInt(fin.substring(0, 2), 16));
			}
			catch(IOException e)
			{
				System.out.println("IO Excpetion : "+e);
			}
            
			fin = fin.substring(2);
			len--;
		}
	}
}
