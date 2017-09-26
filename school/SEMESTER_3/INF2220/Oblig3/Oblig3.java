import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.util.Arrays;
import java.io.FileNotFoundException;

public class Oblig3{
	//Main-method
	public static void endProgram() throws Exception{
		System.out.println("Ending program...");
		Thread.sleep(2000);
		System.exit(0);	
	}
	public static void main(String[] args) throws Exception{
		//Reding the files, and saving them as strings, and converting to char arrays
		File needleFile = null;
		File haystackFile = null;
		if(args.length == 2){
			needleFile = new File(args[0]);
			haystackFile = new File(args[1]);			
		}
		else{
			System.out.println("cant import files, correct use: <program>.java <needleFile> <haystackFile>");	
			endProgram();	
		}
		Scanner needleScanner = null;
		Scanner haystackScanner = null;
		try{
			needleScanner = new Scanner(needleFile);
			haystackScanner = new Scanner(haystackFile);
		}
		catch(FileNotFoundException e){
			System.out.println("Something wrong with filename");
			endProgram();
		}


		if(haystackScanner.hasNextLine() == false || needleScanner.hasNextLine() == false){
			System.out.println("Needle or haystack has no length");
			endProgram();
		}
		String haystackString = haystackScanner.nextLine();
		String needleString = needleScanner.nextLine();

		char[] haystack = haystackString.toCharArray();
		char[] needle = needleString.toCharArray();		

		//Running the algorithem
		ArrayList<Integer> matches = new ArrayList<>();
		matches = findMatch(haystack, needle);
		visualizeResults(haystack, needle, matches);

	}

	//Method that locates the positions of the needle in the haystack, and saves them in an arraylist
	static ArrayList<Integer> findMatch(char[] haystack, char[] needle){
		ArrayList<Integer> matches = new ArrayList<>();

		//Creating the badShift array, containing integers, 
		//and changing the values for the letters that exist in the needle, depending on position
		int[] badCharShift = new int[256];
		int last = needle.length - 1;	
		//sets all to the length of needle
		for(int i = 0; i < badCharShift.length; i++){
			badCharShift[i] = needle.length;
		}
		//changes the ones that exist in needle
		for(int i = 0; i < last; i++){
			badCharShift[needle[i]] = last - i;
		}

		int offset = 0;
		int search = 0;
		int maxOffset = haystack.length - needle.length;
		boolean wild = false;
		//looping through the haystack and searching for the needle
		while(offset <= maxOffset){
			
			search = last;
			boolean run = true;
			//"moving" the needle, and check if all the letters match
			while(run){

				//checks if the letter in haystack match with letter in needle
				if(needle[search] != haystack[search + offset]){
					//if wildcard is on that position, we keep on searching, even though the letters dont match
					if(needle[search] == '_'){
						wild = true;
						//System.out.println("needle:   " + needle[search] + "\thaystack:   " + haystack[search + offset]);

					}
					//exits the search, and updates the offset, if letters dont match, or its a wildcard
					else{
						run = false;
					}

					/*
					In a case where we move the offset to a position that represent the wildcard, we will not find a match
					and we will check if the letter on the left or right side is a match. this will mean that we might be on a position
					in the midle of a word that is a wildcard. ie.: needle = d_e haystack = lllldlel. if we dont check the positions on
					left and right side we can en up "jumping" over the spot in the haystack containing the needle.
					*/
					if(search + offset > 0 && badCharShift[haystack[search - 1 + offset]] != needle.length){
						//System.out.println(haystack[search - 1 + offset]);
						wild = true;
						//System.out.println("-1 er true hax");
					}
					if(search + offset < haystack.length -1 && badCharShift[haystack[search + 1 + offset]] != needle.length){
						//System.out.println(haystack[search + 1 + offset]);
						wild = true;
						//System.out.println("+1 er true");
					}

				}


				//if the loop has gone all the way throug the needle and mathcing all the letters to a substring in the haystack, we have an answer 
				if(search == 0 && run == true){
					matches.add(search + offset);
					wild = false;
					run = false;	
				}
				search --;
				System.out.println("\n-----------------------------\n");
			}

			//if we hit a wildcard, we want to change the offset differently
			if(wild){
				offset += 1;
				//System.out.println("offset changing by wildcard");
				wild = false;
				
			}
			else{
				offset += badCharShift[haystack[offset + last]];
			}
		}

		System.out.println("-----DONE-----\n\n");
		return matches;

	}

	//method that prints out the result
	static void visualizeResults(char[] h, char[] n, ArrayList<Integer> matches){
		String haystack = String.valueOf(h);
		char[] haxorNeedle = new char[h.length];

		for(int i = 0; i < h.length; i++){
			haxorNeedle[i] = '-';
		}

		for(int i = 0; i < matches.size(); i++){
			for(int j = matches.get(i); j < matches.get(i) + n.length; j++){
				haxorNeedle[j] = n[j - matches.get(i)];
			}
		}


		String needle = String.valueOf(haxorNeedle);
		System.out.println(haystack);
		System.out.println(haxorNeedle);
		System.out.println("\n");


	}


}

