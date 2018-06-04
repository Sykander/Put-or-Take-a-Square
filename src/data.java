import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class data {
	
	//Removes lower hierarchy Winning Numbers from larger Winning Number sets
	public static void winningNumbers_split(int M) throws NumberFormatException, IOException{
		
		//Declare list of WinningNumberN for testing
		List<List<Integer>> WinningNumbers = new ArrayList<>();
		
		//Declare String for file locations
		String fileLocation;
		
		//import current winning number lists from saved directory
		for(int i=1;i<M+1;++i) WinningNumbers.add(i-1, fileReader.readNumbers(Main.fileLocation + "\\numbers" + i + ".txt"));
		
		//
		for(int i=M;i>1;--i) WinningNumbers.get(i-1).removeAll(WinningNumbers.get(i-2));
		
		//for however many rounds
		for(int i=0; i<M; ++i){
			fileLocation = Main.fileLocation + "\\numbers" + (i+1) + "_ split.txt";
			fileReader.writeNumbers(WinningNumbers.get(i), fileLocation);
			System.out.println("Sucessfully split WN" + (i+1) + " and saved.");
		}
	}
	
	//Returns winning numbers for M rounds, up to an integer N
	public static void winningNumbers(int M, int N){
		
		//Declare List to return
		List<Integer> numbers = new ArrayList<>();
		
		//Declare variables to record how long it takes to find each winning number set
		long time;
		
		//consider one round
		time=System.currentTimeMillis();
		numbers = winningNumbers_oneRound(N);
		time = System.currentTimeMillis() - time;
		
		System.out.println("It took " + time + " milliseconds to find WN1 .");
		
		String fileLocation = Main. fileLocation + "\\numbers" + 1 + ".txt";
		fileReader.writeNumbers(numbers, fileLocation);
		System.out.println("Sucessfully generated WN" + 1 + " and saved.");
		
		//for however many rounds
		for(int i=2; i<M+1; ++i){
			
			time=System.currentTimeMillis();
			numbers = winningNumbers_manyRounds(N,numbers);
			time = System.currentTimeMillis() - time;
			
			System.out.println("It took " + time + " milliseconds to find WN" + i + " .");
			
			
			fileLocation = Main.fileLocation + "\\numbers" + i + ".txt";
			fileReader.writeNumbers(numbers, fileLocation);
			System.out.println("Sucessfully generated WN" + i + " and saved.");
		}
	}
	
	
	//Creates a list of winning numbers up to a Integer N (for winning after 2 turns)
	private static List<Integer> winningNumbers_oneRound(int N){
		
		//Declare List to return
		List<Integer> numbers = new ArrayList<>();
		numbers.add(0);
		
		//For ever integer up to N
		for(int i=1;i<N+1;++i){
			
			//Evaluate i under the rules of the game
			Rules.newInt(i);
			//Record values for 2 turns ahead
			int lowerIntHigher = Rules.lowerIntHigher;
			int lowerIntLower = Rules.lowerIntLower;
			
			//Test for winning number conditions
			if(lowerIntHigher ==0 && lowerIntLower==0) numbers.add(i);
		}
		
		// return List
		return numbers;
	}
	
	
	//Creates a list of winning numbers up to a Integer N (for winning after m+2 turns for the number of turns of the entered List)
	private static List<Integer> winningNumbers_manyRounds(int N,List<Integer> winningNumbers){
		
		//Declare List to return
		List<Integer> numbers = new ArrayList<>();
		
		numbers.add(0, 0);;
		//For ever integer up to N
			for(int i=1;i<N+1;++i){
					
				//Evaluate i under the rules of the game
				Rules.newInt(i);
				//Record values for 2 turns ahead
				int lowerIntHigher = Rules.lowerIntHigher;
				int higherIntHigher = Rules.higherIntHigher;
				int lowerIntLower = Rules.lowerIntLower;
				int higherIntLower = Rules.higherIntLower;
				
				//test if at least one option on each branch is a winning number
				if( (winningNumbers.contains(lowerIntHigher) || winningNumbers.contains(higherIntHigher) ) && ( winningNumbers.contains(lowerIntLower) || winningNumbers.contains(higherIntLower) )){
					numbers.add(i);
				}
			}
		
		//return List
		return numbers;
	}
	
	//find all possible losing numbers 
	//round limit is the number of rounds of winning numbers looked through to find losing numbers
	public static void losingNumbers(int roundLimit) throws NumberFormatException, IOException{
		//Declare List to return
		List<Integer> numbers = new ArrayList<>();
		
		List<Integer> WinningNumbers = fileReader.readNumbers(Main.fileLocation + "\\numbers" + roundLimit + ".txt");
		
		//Add every integer which is linked to from a Winning Number
		for(int i=0;i<WinningNumbers.size(); ++i){
			
			//Evaluate the Winning Number under the rules of the game
			Rules.newInt(WinningNumbers.get(i));
			//Record values for 1 turn ahead as losing numbers
			numbers.add(Rules.lowerInt);
			numbers.add(Rules.higherInt);
		}
		
		
		for(int i=1; i < WinningNumbers.get( WinningNumbers.size()-1 ) ; ++i ){
			
			//evaluate i under the rules of the game
			Rules.newInt(i);
			//Record values for 1 turn ahead
			int lowerInt = Rules.lowerInt;
			int higherInt = Rules.higherInt;
			
			//test if either is a Winning Number
			if(WinningNumbers.contains(lowerInt)==true || WinningNumbers.contains(higherInt) == true){
				//add the number to the losingNumberList
				numbers.add(i);
			}
			
			
			
		}
		
		//remove duplicates and order the list by converting to a set and back
		Set<Integer> Temp = new HashSet<>();
		Temp.addAll(numbers);
		numbers.clear();
		numbers.addAll(Temp);
		
		Collections.sort(numbers);
		
		//save numbers and declare they've been saved
		String saveLocation = Main.fileLocation + "\\losingNumbers" + roundLimit + ".txt";
		fileReader.writeNumbers(numbers, saveLocation);
		System.out.println("Sucessfully generated LN" + roundLimit + " and saved.");
	}
}
