import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Oblig1{

	public static void main(String[] args) throws Exception{
		BinaryTree test = new BinaryTree("words.txt");
		test.readFile();
		test.remove("busybody");
		test.insert("busybody");
		//--------------------TESTING WITH LETTERS-------------------------

/*		char[] randomAlphabet = "cfabunzdghymio".toCharArray();
		char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

		for(int i = 0; i < randomAlphabet.length; i++){
			String tmp = randomAlphabet[i] + "";
			test.insert(tmp);
		}
*/
		//--------------------USER INTERFACE------------------------------
		Scanner in = new Scanner(System.in);
		boolean run = true;
		while(run){
			String input;

			System.out.println("To search for a word, press:\t \"s\" -> ENTER");
			System.out.println("To add insert word, press:  \t \"i\" -> ENTER");
			System.out.println("To delete a word, press:    \t \"d\" -> ENTER");

			System.out.println("To exit press:              \t \"q\" -> ENTER");

			input = in.nextLine();
			System.out.println("-------------------------\n");
			if(input.equalsIgnoreCase("s")){
				System.out.println("Type the word you want to search for: ");
				input = in.nextLine();
				String result = test.search(input);
				if(result == null){
					System.out.println("Cant find word you are searching for");
					System.out.println("Would you like do do a search for similar words? press: y/n");
					String answer = in.nextLine();
					if(answer.equalsIgnoreCase("y")){
						String[] possibleWords = test.spellcheck(input);
						System.out.println("Found " + possibleWords.length + " similar words in dictionary");
						System.out.println("Word you may look for: ");
						for(int i = 0; i < possibleWords.length;i++){
							System.out.println(possibleWords[i]);
						}
					}

				}
				else{
					System.out.println("found: " + result);
				}
				System.out.println("-------------------------\n");
			}
			if(input.equalsIgnoreCase("i")){
				System.out.println("Type the word you want to insert: ");
				input = in.nextLine();
			}
			if(input.equalsIgnoreCase("d")){
				System.out.println("Type the word you want to delete: ");
				input = in.nextLine();
			}




			if(input.equals("q")){
				System.out.println("STATS: \n");
				System.out.println("max depth:     " + test.depth() + "\n");
				System.out.println("smallest:      " + test.smallest() + "\n");
				System.out.println("largest:       " + test.largest() + "\n");
				System.out.println("total depth:   " + test.totalDepth() + "\n");
				System.out.println("average depth: " + test.averageDepth() + "\n");
				run = false;
			}
		}



		//--------------------TESTING METHODS------------------------------
		//System.out.println(test.search("a"));
/*		System.out.println("max depth:     " + test.depth() + "\n");
		System.out.println("smallest:      " + test.smallest() + "\n");
		System.out.println("largest:       " + test.largest() + "\n");
		System.out.println("total depth:   " + test.totalDepth() + "\n");
		System.out.println("average depth: " + test.averageDepth() + "\n");

		System.out.println(Arrays.toString(test.similarOne("here")));
		System.out.println(Arrays.toString(test.replace("here")));
		System.out.println(Arrays.toString(test.addLetter("here")));
		System.out.println(Arrays.toString(test.removeLetter("apple")));
		System.out.println("---------------------------------------\n");

		String[] words = test.spellcheck("apple");
		System.out.println("mulige ord: ");
		for(int i = 0; i < words.length; i ++){
			System.out.println(words[i]);
		}*/


/*		System.out.println(test.search("word"));
		if(test.search("apple") == null){
			System.out.println("WFDSDFSDFSDFSDFSDFS");
		}*/
/*		test.traverse();
		test.remove("f");
		System.out.println("----------");
		test.traverse();
		test.remove("a");
		System.out.println("----------");
		test.traverse();
		test.remove("u");
		System.out.println("----------");
		test.traverse();*/
	}
}