/* ANUPSINGH PARDESHI cs610 3859 prp */

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
///hdec3859 implements the huffman encoding to de-compress the file.
///</Class>
public class hdec3859 
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
			hdec3859 huffDecoder = new hdec3859();
			
			//Timer to calculate the time required to compress the file.
			long start = System.currentTimeMillis();
			
			//Read the header information.
			//Symbols in the file
			huffDecoder.symbols = bis.read();
			huffDecoder.decodeFile_3859(path, bis);
			bis.close();
			
			//Deleting the coded .huf file
			Files.deleteIfExists(Paths.get(path));
			
			//Total time taken for compression.
			System.out.println("Time taken to de-compress: "+(System.currentTimeMillis()-start)+"ms");
		}
		catch(IOException e)
		{
			System.out.println("IO Excpetion :"+e);
		}
	}

	///<Summary>
	///decodeFile_3859 decodes the header and body information to generate the huffman tree.
	///<Summary>	
	private void decodeFile_3859(String filePath, BufferedInputStream bis) throws IOException 
	{
		//Total file length
		fileLength = readByte_3859(bis);
		
		//Creating the frequency array based on the header information.
		for(int i=0; i<symbols;i++)
		{
			int ch = bis.read();
			frequencyArray[ch] = readByte_3859(bis);
		}
		
		//Re-create the tree with the huffman algorithm
		regenerateHuffmanTree_3859();
		
		//De-code the coded file with the codes from huffman algorithm
		createOrignalFile_3859(bis,filePath);
	}
	
	///<Summary>
	///createOrignalFile_3859 re-creates the original file using huffman tree.
	///<Summary>	
	private void createOrignalFile_3859(BufferedInputStream bis,String filePath) 
	{
		try 
		{
			String outputPath = filePath.substring(0, filePath.length()-4);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputPath));
			
			//assign the rooth node of the huffman tree to traverse and use prefix decoding.
			node_3859 rootNode = hufArray[1];
			int ch;
			
			while(true) 
			{
                ch = bis.read();
                int i=0;
                
                //Uses 8 bit decoding.
                while(i<8)
                {
                	//If the node is leaf node and prefix matches the nodes code, write the symbol into the new file and 
                	//assign the rootnode of the huffman tree again to variable root node
                	if(rootNode.leftChildNode == null && rootNode.rightChildNode == null) 
                	{
                		bos.write(rootNode.symbol);
                		rootNode = hufArray[1];
                		fileLength--;
                		
                		//if the length of the file is zero then stop the decoding
                		if(fileLength == 0) 
                			break;
                	}
                	
                	//Left shift the bit
                	int bit = (ch & 0x80);                   
                	ch <<= 1;
                	
                	//If bit is 1 then traverse it to the right else to the left
                	if(bit == 0x80) 
                		rootNode = rootNode.rightChildNode;
                	else
                		rootNode = rootNode.leftChildNode;
                	
                	i++;
                }
                if(fileLength == 0) 
                	break;
            }
			
			bos.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	///<Summary>
	///regenerateHuffmanTree_3859 re-creates the huffman tree based on the frequency of the characters.
	///<Summary>
	private void regenerateHuffmanTree_3859() 
	{
		//Creating a min heap.
		createMinHeap_3859();
		symbols = location;
		
		//Create huffman tree.
		createHuffmanTree_3859();
		//Assign codes to the nodes
		
		if(hufArray[1]!=null)
			assignCodes_3859(hufArray[1], "");
	}

	///<Summary>
	///createHuffmanTree_3859 creates the huffman tree by taking two min freq nodes and creating a single node out of them.
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
			
		}
	}

	///<Summary>
	///assignCodes_3859 uses recursion to assign the code traversing the tree. 0 to left and 1 to the right.
	///<Summary>
	public void assignCodes_3859(node_3859 node, String code)
	{
		try 
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
		catch(NullPointerException e)
		{
			System.out.println(e);
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
	///isParentAvailable_3859 return the boolean value if the parent location is available is the array or not.
	///<Summary>	
	public boolean isParentAvailable_3859(int loc)
	{
		if(location > (int) Math.round(Math.floor(loc/2)) && (int) Math.round(Math.floor(loc/2))!=0)
			return true;
		
		return false;
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
	///readByte_3859 reads the 4 byte long integer.
	///<Summar	
	private int readByte_3859(BufferedInputStream bis) 
	{
		byte[] b = new byte[4];
		int read = 0;
		try 
		{
			for(int i = 0; i < 4; ++i) 
			{
				int byteRead = bis.read();
				b[i]=(byte) byteRead;
			}
			
			read = b[0] << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8
			          | (b[3] & 0xff);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return read;
	}
}
