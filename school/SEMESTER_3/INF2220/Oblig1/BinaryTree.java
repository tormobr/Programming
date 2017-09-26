import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class BinaryTree{
	
	private String fileName;
	private ArrayList<String> words = new ArrayList<>();

	//constructor
	BinaryTree(String fileName){
		this.fileName = fileName;
	}


	//--------------------NODE CLASS-------------------------
	private Node root = null;
	private float numberOfNodes = 0;
	class Node{

		int depth;
		String data;
		Node left;
		Node right;

		Node(String s){
			data = s;

			left = null;
			right = null;
		}
	}


	//--------------------READING FILE-------------------------

	public void readFile() throws Exception{
		Scanner myFile = new Scanner(new File(fileName));
		while(myFile.hasNextLine()){
			String word = myFile.nextLine();
			insert(word);
			words.add(word);
		}
	}


	//--------------------INSERTING-------------------------

	//inserting a new node recursive by starting at the root/top
	private Node recursiveInsert(Node n, String data){
		//If the tree is empty
		if(n == null){
			Node tmp = new Node(data);
			n = tmp;
			numberOfNodes++;
			return n;
		}
		else if((data.compareTo(n.data)) < 0){
			n.left = recursiveInsert(n.left, data);
		}
		else if((data.compareTo(n.data)) > 0){
			n.right = recursiveInsert(n.right, data);
		}
		return n;
	}
	
	//method that starts the recursive method
	public void insert(String data){
		root = recursiveInsert(root, data);
	}


	//--------------------SEARCHING-------------------------

	//searching recursive for data/word in tree..
	private String recursiveSearch(Node n, String data){
		if(n == null){
			return null;
		}
		else if(data.compareTo(n.data) < 0){
			return recursiveSearch(n.left, data);
		}
		else if(data.compareTo(n.data) > 0){
			return recursiveSearch(n.right, data);
		}
		else{
			return n.data;
		}
	}
	
	//search method that start recursive search
	public String search(String data){
		return recursiveSearch(root, data);
	}


	//--------------------SPELLCHECK-------------------------

	//Check if two letters next to eachother have switched..
	public String[] similarOne(String word){
			char[] word_array = word.toCharArray();
			char[] tmp;
			String[] words = new String[word_array.length-1];

			for(int i = 0; i < word_array.length - 1; i++){
				tmp = word_array.clone();
				words[i] = swap(i, i+1, tmp);
			}

			return words;
		}

	public String swap(int a, int b, char[] word){
		char tmp = word[a];   //tmp = e
		word[a] = word[b];	  //[h,h,y]
		word[b] = tmp;		  //[h,e,y]
		return new String(word);
	}

	//checks if letter in word has been replaced with someting wrong..
	public String[] replace(String word){
		char[] letters = word.toCharArray();
		char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		ArrayList<String> words = new ArrayList<>();

		//replacing letter with any other letter in alphabet..
		for(int i = 0; i < letters.length; i++){
			letters = word.toCharArray();
			for(int j = 0; j < alphabet.length; j++){
				letters[i] = alphabet[j];
				words.add(new String(letters));
			}
		}


		//converting arraylist to array..
		String[] returnArray = new String[words.size()];
		for(int i = 0; i < returnArray.length; i++){
			returnArray[i] = words.get(i);
		}
		return returnArray;
	}

	//checks if one letter from word has beem removed..
	public String[] addLetter(String word){
		char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		ArrayList<String> words = new ArrayList<>();

		//adding a letter in each spot in the word..
		for(int i = 0; i < word.length() + 1; i++){
			String left = word.substring(0, i);
			left = left + " ";
			String right = word.substring(i);
			String total = left + right;
			char[] letters = total.toCharArray();

			for(int j = 0; j < alphabet.length; j++){
				letters[i] = alphabet[j];
				words.add(new String(letters));
			}
		}


		//converting arraylist to array..
		String[] returnArray = new String[words.size()];
		for(int i = 0; i < returnArray.length; i++){
			returnArray[i] = words.get(i);
		}
		return returnArray;
	}

	public String[] removeLetter(String word){
		String total;
		char[] letters = word.toCharArray();
		ArrayList<String> words = new ArrayList<>();

		//Removing letter, in case user typed extra letter...
		for(int i = 1; i < letters.length + 1; i++){
			String left = word.substring(0,i - 1);
			String right = word.substring(i);
			total = left + right;

			words.add(total);
		}


		//converting arraylist to array..
		String[] returnArray = new String[words.size()];
		for(int i = 0; i < returnArray.length; i++){
			returnArray[i] = words.get(i);
		}
		return returnArray;
	}

	public String[] spellcheck(String word){
		String[] posibleWords;
		ArrayList<String> words = new ArrayList<>();

		long startTime = System.currentTimeMillis();


		posibleWords = similarOne(word);
		for(int i = 0; i < posibleWords.length; i++){
			if(search(posibleWords[i]) != null){
				words.add(posibleWords[i]);
			}
		}

		posibleWords = replace(word);
		for(int i = 0; i < posibleWords.length; i++){
			if(search(posibleWords[i]) != null){
				words.add(posibleWords[i]);
			}
		}

		posibleWords = addLetter(word);
		for(int i = 0; i < posibleWords.length; i++){
			if(search(posibleWords[i]) != null){
				words.add(posibleWords[i]);
			}
		}

		posibleWords = removeLetter(word);
		for(int i = 0; i < posibleWords.length; i++){
			if(search(posibleWords[i]) != null){
				words.add(posibleWords[i]);
			}
		}

		//converting arraylist to array
		String[] returnArray = new String[words.size()];
		for(int i = 0; i < returnArray.length; i++){
			returnArray[i] = words.get(i);
		}

		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Search took: " + totalTime + " seconds");
		return returnArray;
	}


	//--------------------REMOVE NODE-------------------------

	//recursive method that searches for word to remove...
	private Node recursiveRemove(Node n, String data){
		//If tree is empty
		if(n == null){
			return null;
		}
		//traversing down tree, until we find Node to delete
		else if(data.compareTo(n.data) < 0){
			n.left = recursiveRemove(n.left, data);
		}
		else if(data.compareTo(n.data) > 0){
			n.right = recursiveRemove(n.right, data);
		}
		//Node found
		else{
			//If Node only have one child
			if(n.left == null){
				n = n.right;
			}
			else if(n.right == null){
				n = n.left;
			}
			//If node has two children
			//find the smallest in right tree
			//replace, and delete the node you replaced with
			else{
				n.data = findSmallest(n.right);
				n.right = recursiveRemove(n.right, n.data);
			}
		}
		return n;
	}

	//Finding the smallest Node in a tree
	//Retuning its data, needed for delete method
	private String findSmallest(Node n){
		String smallest = n.data;
		while(n.left != null){
			n = n.left;
		}
		smallest = n.data;
		return smallest;
	}

	//function that starts the recursive search and delete
	public Node remove(String data){
		return recursiveRemove(root, data);
	}

	//--------------------TRAVERSE TREE-------------------------

	private void recursiveTraverse(Node n){
		if(n != null){
			System.out.println(n.data);
			recursiveTraverse(n.left);
			recursiveTraverse(n.right);
		}
	}


	public void traverse(){
		recursiveTraverse(root);
	}

	//--------------------FINDING MAX DEPTH-------------------------

	private int recursiveDepth(Node n){
		int left;
		int right;
		if(n == null){
			return -1;
		}

		left = recursiveDepth(n.left);
		right = recursiveDepth(n.right);
		if(left > right){
			return left + 1;
		}
		else{
			return right + 1;
		}

	}

	public int depth(){
		return recursiveDepth(root);
	}

	//--------------------FIDNING SMALLEST AND LARGEST-------------------------

	public String smallest(){
		Node n = root;
		if (n == null){
			return null;
		}

		String smallest = n.data;
		while(n.left != null){
			smallest = n.left.data;
			n = n.left;
		}
		return smallest;
	}

	public String largest(){
		Node n = root;
		if (n == null){
			return null;
		}

		String largest = n.data;
		while(n.right != null){
			largest = n.right.data;
			n = n.right;
		}
		return largest;
	}

	//--------------------FINDING AVG. DEPTH-------------------------

	private int recursiveTotalDepth(Node n, int i) {
	    if (n == null) {
	      return 0;
	    }
	    int left = recursiveTotalDepth(n.left, i + 1);
	    int right = recursiveTotalDepth(n.right, i + 1);
	    return i + left + right;
	}

	public int totalDepth(){
		return recursiveTotalDepth(root, 0);
	}

/*	public int[] nodeEachLevel(Node root){
		//traverse the tree in leved-order and store the number of nodes in each
		//level in a interger array, when you get back up, return the integer array,
		//int[6] = number of nodes at level 6
		//can do this with recursion, or loops
	}*/




	//--------------------FINDING AVG. DEPTH-------------------------

	public double averageDepth(){
		float tmp = totalDepth() / numberOfNodes;
		return tmp;
	}
	//--------------------TESTING-------------------------

	public String print(int i){
		return words.get(i);
	}


}