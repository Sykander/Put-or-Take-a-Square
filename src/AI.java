import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AI {
	
	//Each possible choice is given a integer value for the AI
	public static List<Integer> DecisionList = new ArrayList<Integer>();
	
	//This decision list stores integer values for the second AI
	public static List<Integer> DecisionList2 = new ArrayList<Integer>();
	
	//Each possible WN is collected in this list for 10 rounds
	public static List<Integer> winningNumbers10 = new ArrayList<Integer>();	
	
	//Create winningNumbers
	public static void generateWinningNumbers() throws NumberFormatException, IOException{
		winningNumbers10 = fileReader.readNumbers(Main.fileLocation + "\\numbers10.txt");
	}
	
	public static void loadRandomDecisionList() throws NumberFormatException, IOException{
		int N = 1000000;
		loadDecisionList( generateDecisionList(N) );
	}
	
	public static void loadRandomDecisionList2() throws NumberFormatException, IOException{
		int N = 1000000;
		loadDecisionList2( generateDecisionList(N) );
	}
	
	//Generates a random decision up to a certain integer N
	public static List<Integer> generateDecisionList(int N){
		List<Integer> DecisionListTemp = new ArrayList<Integer>();
		for(int i=1;i<N;++i) DecisionListTemp.add( (int) Math.round(Math.random()*10 ));
		return DecisionListTemp;
	}
	
	//Save the decision list to a specific slot n
	public static void saveDecisionList(List<Integer> decisionList, int n){
		fileReader.writeNumbers(decisionList, Main.fileLocation +  "\\DecisionList_" + n + ".txt");
		System.out.println("Decision List saved to " + Main.fileLocation + "\\DecisionList_" + n + ".txt");
	}
	
	//Get decision list from the specific slot n
	public static List<Integer> getDecisionList(int n) throws NumberFormatException, IOException{
		GUI.choose(Main.fileLocation + "\\DecisionList_" + n + ".txt", "OK");		
		return fileReader.readNumbers(Main.fileLocation + "\\DecisionList_" + n + ".txt");
	}
	
	//Loads the decision list 'n' into the AI
	public static void loadDecisionList(List<Integer> decisionList) throws NumberFormatException, IOException{
		DecisionList = decisionList;
	}
	
	//Loads the decision list 'n' into the second AI
	public static void loadDecisionList2(List<Integer> decisionList) throws NumberFormatException, IOException{
		DecisionList2 = decisionList;
	}
	
	//Search for logical moves
	
	//Chooses the next move for the AI
	public static int choose(int currentInt){
		/*int change = (int) Math.pow( Math.floor( Math.sqrt(currentInt) ) , 2 ) ;
		
		//Checks to see if there is a possible winning move
		if( winningNumbers10.contains(currentInt)==true ){
			 if ( winningNumbers9.contains(currentInt - change)==true) return currentInt-change;
			 else return currentInt+change;
		}
		else if (winningNumbers10.contains(currentInt-change)==true && winningNumbers10.contains(currentInt+change)==false){
			return currentInt+change;
		}
		else if (winningNumbers10.contains(currentInt+change)==true && winningNumbers10.contains(currentInt-change)==true){
			return currentInt-change;
		}
		else 
		
		*/
		return chooseByDecisionList(currentInt);
	}
	
	//Use the Decision List to make a choice when no obvious winning moves are present
	public static int chooseByDecisionList(int currentInt){
		
		int change = (int) Math.pow( Math.floor( Math.sqrt(currentInt) ) , 2 ) ;
		
		//Create a temporary array list to store the decision list
		List<Integer> DL = new ArrayList<Integer>();
		
		//if it is computer vs computer
		if (Main.gameSetup[0] == 2){
		//decide which decision list to use based on which cpu is playing
		if( Rules.playerOneTurn == true) DL = DecisionList;
		else if (Rules.playerTwoTurn == true) DL = DecisionList2;
		}
		//otherwise load default Decision List
		else DL = DecisionList;
		
		int lower = currentInt - change;
		int higher = currentInt + change;
		
		double chance = .5;
		//Create threshold chance for making decision
		
		if(DL.size()>=higher){
			//if there isn't an entry in the decision list for a specific integer then it reverts to choosing at random
			if (DL.get(lower) == 10 ) return lower;
			else if (DL.get(higher) == 10 ) return higher;
			else if( DL.get(higher) != 0 &&  DL.get(lower) != 0 ) chance = DL.get(lower) / ( DL.get(higher) + DL.get(lower) );
			else if (DL.get(higher) == 0 && DL.get(lower) != 0) return lower;
			else if (DL.get(higher) != 0 && DL.get(lower) == 0) return higher;
		}
		//Generate random chance between 0 and 1
		double rand = Math.random();
		
		//make decision based on probability
		if(rand > chance) return lower;
		else return higher;
	}
	
	//trains new decision lists
	//the integer limit is number of integers to be used in the decision list
	//the integer listNumber is the number of lists to be trained at once
	//the integer roundLimit decides how many rounds to test over
	public static List<Integer> trainNewDecisionList(int listNumber,int limit, int roundLimit) throws Exception{
		//Declare integer for counting rounds so far
		int rounds = 0;
		
		//Declare list of DecisionLists for testing
		List<List<Integer>> decisionLists = new ArrayList<>();
		
		//Declare array for storing victory count
		int victoryCount[] = new int[listNumber];
		
		//Initialise the list of Decision Lists with random lists
		for(int i=0;i< listNumber ;++i) decisionLists.add(i, generateDecisionList(limit));

		//Initialise the victory count array
		for(int i=0;i< listNumber ;++i) victoryCount[i]=0;
		
		//while a certain number of rounds remain
		while(rounds < roundLimit*listNumber){
			for(int i=0;i< listNumber ; ++i){
				for(int j=0;j < listNumber ; ++j){
					
					loadDecisionList( decisionLists.get(i) );
					loadDecisionList2( decisionLists.get(j) );
					
					Main.gameSetup[0] = 3;
					Rules.newGame();
					//games should end within 100 turns or else they're declared a tie
					for(int k=0;k<100;++k){
						
						//update the game and check for win
						if(Rules.gameUpdateTraining(chooseByDecisionList(Rules.currentInt)) == true){
							//update the victory list
							if(Rules.playerOneWin==true) victoryCount[i]++;
							else if(Rules.playerTwoWin==true) victoryCount[j]++;
							
							//kill the for loop
							k=100;
						}
					}	
				}
			}
			
			//increment number of rounds
			rounds++;
		}
		
		//create placeholder for returned decision list
		List<Integer> finalDecisionList = null;
		int highestCount = 0;
		//find decision list with the most victories
		for(int i =0; i< listNumber; ++i){
			
			//if the next entry in the array has more victories than any previous 
			if (victoryCount[i] > highestCount){
				
				//update the highest victory count
				highestCount=victoryCount[i];
				
				//update placeholder returned list
				finalDecisionList = decisionLists.get(i);
			}
		}
		
		return finalDecisionList;
	}
	
	//Improve the decision making abilities of the decision list n, for roundLimit rounds of Winning/Losing Numbers
	public static void groomDecisionList(int n, int roundLimit) throws NumberFormatException, IOException{
		List<Integer> DecisionList = getDecisionList(n);
		List<Integer> WinningNumbers = fileReader.readNumbers(Main.fileLocation + "\\numbers" + roundLimit + ".txt");
		List<Integer> LosingNumbers = fileReader.readNumbers(Main.fileLocation + "\\losingNumbers" + roundLimit + ".txt");
		
		//change decision values for winning numbers and losing numbers
		for(int i=0;i<WinningNumbers.size()-1;++i){
			//stop the process if the decision list doesn't have room for more winning numbers or losing numbers
			if(WinningNumbers.get(i) > DecisionList.size() ) break;
			DecisionList.set(WinningNumbers.get(i), 10);
		}
		for(int i=0;i<LosingNumbers.size()-1;++i){
			if(LosingNumbers.get(i) > DecisionList.size() ) break;
			DecisionList.set(LosingNumbers.get(i), 0);
		}
		
		//minor edits to the list to make it probably avoid cycles (2,3,6 are obvious examples of cycles)
		DecisionList.set(2, 1);
		DecisionList.set(3, 1);
		DecisionList.set(6, 1);
		
		saveDecisionList(DecisionList, n);
	}

}
