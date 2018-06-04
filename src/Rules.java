import java.util.Random;

public class Rules {
	
	//Specify Turn count
	public static int turnCount;
	
	//Specify current integer as well as both possible choices for next integer
	public static int currentInt;
	public static int change;
	//one turn forward
	public static int lowerInt;
	public static int higherInt;
	//two turns forward
	public static int changeLower;
	public static int lowerIntLower;
	public static int higherIntLower;
	public static int changeHigher;
	public static int lowerIntHigher;
	public static int higherIntHigher;
	
	
	//Specify players
	public static final String playerOne = "PlayerOne";
	public static boolean playerOneTurn;
	public static boolean playerOneWin;
	
	public static final String playerTwo = "PlayerTwo";
	public static boolean playerTwoTurn;
	public static boolean playerTwoWin;
	
	public static String currentPlayer;
	
	//Win message
	public static String winMessage;
	
	//Check for Winner and generate win message
	public static boolean checkForWin(){
		if(lowerInt ==0){
			
			if( playerOneTurn ==true)	playerOneWin = true;
			else if ( playerTwoTurn ==true)	playerTwoWin = true;
			
			winMessage = currentPlayer + " has won as " + currentInt + " is a square number.";
			
			return true;
		}
		//Uncomment next code section to check for win in earlier turns
		/*
		else if(lowerIntHigher ==0 && lowerIntLower ==0){
			if( playerOneTurn ==true){
				playerTwoWin = true;
				winMessage = playerTwo + " has won as " + currentInt + " can only go to " + lowerInt + "  or " + higherInt + " which are squares.";
			}
			else if ( playerTwoTurn ==true){
				playerOneWin = true;
				winMessage = playerOne + " has won as " + currentInt + " can only go to " + lowerInt + "  or " + higherInt + " which are squares.";
			}
			
			return true;
		}
		*/
		else return false;
	}
	
	//update the game after a turn, accept new integer as input
	public static void gameUpdate(int a) throws Exception{
		//update player turns
		if( playerOneTurn == true){
			playerOneTurn = false;
			playerTwoTurn = true;
			currentPlayer = playerTwo;
		}
		else if( playerTwoTurn == true){
			playerTwoTurn = false;
			playerOneTurn = true;
			currentPlayer = playerOne;
		}
		//increment turn count
		turnCount++;
		
		//update integer calculations via game rules
		newInt(a);
		
		//check for winner
		if (checkForWin() == true && playerOneWin == true)	GUI.announceWinner(playerOne);
		else if (checkForWin() == true && playerTwoWin == true)	GUI.announceWinner(playerTwo);
		else{
			
			//initialise game information window
			GUI.gameUpdate();
			
			//if two are playing make a player choice each time
			if(Main.gameSetup[0] == 0){	
				//check for player's turn and then make decison accordingly
				if(playerOneTurn == true) gameUpdate(playerDecision(playerOne));
				else gameUpdate(playerDecision(playerTwo));
			}
			//else if it is one player vs the computer
			else if(Main.gameSetup[0] == 1){
				//check for player's turn and then make decison accordingly
				if(playerOneTurn == true) gameUpdate(playerDecision(playerOne));
				else{
					int integerCPU = AI.choose(currentInt);
					String questionCPU = "The CPU chooses " + integerCPU + ".";
					GUI.choose(questionCPU, "OK");
					gameUpdate(integerCPU);
				}
			}
			//else if it is the computer vs the computer
			else if( Main.gameSetup[0] == 2){
				//determine the CPU's choice as integerCPU
				int integerCPU = AI.choose(currentInt);
				String questionCPU = "The CPU chooses " + integerCPU + ".";
				
				//display a message to state this is the choice made
				GUI.choose(questionCPU, "OK");
				
				//update the game with the decision
				gameUpdate(integerCPU);
			}
		}
	}
	
	//game rules
	public static void newInt(int a){
		change = (int) Math.pow( Math.floor( Math.sqrt(a) ) , 2 ) ;
		
		//update integer calculations
		currentInt = a;
		lowerInt = a - change;
		higherInt = a + change;
		
		changeLower = (int) Math.pow( Math.floor( Math.sqrt(lowerInt) ) , 2 );
		lowerIntLower = lowerInt - changeLower;
		higherIntLower = lowerInt + changeLower;
		
		changeHigher = (int) Math.pow( Math.floor( Math.sqrt(higherInt) ) , 2 );
		lowerIntHigher = higherInt - changeHigher;
		higherIntHigher = higherInt + changeHigher;
	}
	
	//Start a new game with a given number
	public static void newGame(int start) throws Exception{
		
		//if input starting number is 0 use alternative newGame function to
		//start with a random integer
		if (start == 0) newGame();
		else{
		turnCount=1;
		playerOneTurn = true;
		currentPlayer = playerOne;
		playerTwoTurn = false;
		currentInt = start;
		newInt(currentInt);
		
		//if we aren't training a new decision list
		if(Main.gameSetup[0] != 3){
		//initialise game window
		GUI.gameBoard();
		}
		
		//increment game count
		Main.gameSetup[3]++;
		
		//if it's PvP or PvCPU then allow the player to make the first choice and then update the game
		if(Main.gameSetup[0] == 0 || Main.gameSetup[0] == 1) gameUpdate(playerDecision(playerOne));
		
		//otherwise allow the cpu to make a decision
		else{
			//determine the CPU's choice as integerCPU
			int integerCPU = AI.choose(currentInt);
			
			//if we aren't using the program for training
			if(Main.gameSetup[0] != 3){
				//display a message to state this is the choice made
				String questionCPU = "The CPU chooses " + integerCPU + ".";
				GUI.choose(questionCPU, "OK");
				//update the game with the decision
				gameUpdate(integerCPU);
			}
			else gameUpdateTraining(integerCPU);	
		}
		}
	}
	
	//simple game update method for training decision lists
	//returns a boolean for if someone has won
	public static boolean gameUpdateTraining(int integerCPU) {
		//update player turns
		if( playerOneTurn == true){
			playerOneTurn = false;
			playerTwoTurn = true;
			currentPlayer = playerTwo;
		}
		else if( playerTwoTurn == true){
			playerTwoTurn = false;
			playerOneTurn = true;
			currentPlayer = playerOne;
		}
		//increment turn count
		turnCount++;
		
		//update integer calculations via game rules
		newInt(integerCPU);
		
		return checkForWin();
	}

	//Start new game with a random number
	public static void newGame() throws Exception{
		
		//initialise game variables
		turnCount=1;
		playerOneTurn = true;
		currentPlayer = playerOne;
		playerTwoTurn = false;
		currentInt = randomNumberAB(10,10000);
		newInt(currentInt);
		
		
		//if we aren't training a new decision list
		if(Main.gameSetup[0] != 3){
		//initialise game window
		GUI.gameBoard();
		}
		
		
		//increment game count
		Main.gameSetup[3]++;
		
		//if it's PvP or PvCPU then allow the player to make the first choice and then update the game
		if(Main.gameSetup[0] == 0 || Main.gameSetup[0] == 1) gameUpdate(playerDecision(playerOne));
				
		//otherwise allow the cpu to make a decision
		else if( Main.gameSetup[0] == 2 ) gameUpdate(AI.choose(currentInt));
		
		
	}
	
	//Player makes a decision
	public static int playerDecision(String player){
		
		//specifiy choices and get a decision
		String Question = player + ", which number do you choose?";
		String Option1 = "Go down to " + lowerInt;
		String Option2 = "Go up to " + higherInt;
		
		//decision is return as an integer
		int decision = GUI.choose(Question, Option1, Option2);
		
		//number returned is dependent on the player's choice
		if(decision == 0) return lowerInt;
		else return higherInt;
	}
	
	//Generate random integer between a and b
	static int randomNumberAB(int a, int b)
	{
		Random rand = new Random();
		int x =Math.abs(b-a);
		int r;
		if(a<b) r =  (int) (Math.round((rand.nextDouble())*x)+a);
		else r =  (int) (Math.round((rand.nextDouble())*x)+b);
		return r;
	}
	
}
