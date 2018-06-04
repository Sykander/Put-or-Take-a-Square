import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Main {

	public static String fileLocation = "";
	
	public static int[] gameSetup = new int[10];
	//gameSetup[0] (0, Player vs Player) (1, Player vs Computer)(2, Computer vs Computer) (3, computer vs computer for decision list testing)
	//gameSetup[1] (any integer listed here will be the starting number)(0 starts the game with a random int)
	//gameSetup[2] (this number decides which decision list is loaded for the AI)(0 starts the game with a new random decision List)		
	//gameSetup[3] (How many games have been played so far since launch ) 
	//gameSetup[4] (this number decides which decision list is loaded for the second AI if the computer is playing
	//					itself)(0 starts the game with a new random decision List)
	
	public static final void start() throws Exception, Exception{
		
		//Reset gameSetup 0 is this is a new game, then ask the question
		gameSetup[0] = 0;
		gameSetup[1] = 0;
		gameSetup[0] = GUI.choose("Who is going to play?", "Player vs Player", "Player vs Computer", "Computer vs Computer" );
		
		//These questions only need to be asked if the computer is a player
		if( gameSetup[0] == 1 || gameSetup[0] == 2){
			
			//Reset gameSetup[1] then ask for an input
			gameSetup[1] = 0;
			gameSetup[1] = GUI.chooseNumber("What should the initial integer be ? (leave blank for a random integer)");
			
			//Reset and ask for input
			gameSetup[2] = 0;
			gameSetup[2] = GUI.chooseNumber("What AI should be loaded ? (leave blank for arbitrary AI decisions)");
			if( gameSetup[2] == 0) AI.loadRandomDecisionList();
			else AI.loadDecisionList( AI.getDecisionList(gameSetup[2]) );
		}
		
		//These questions only need to be asked if the computer is playing against itself
		if ( gameSetup[0] == 2 ){
			
			//reset and ask for input
			gameSetup[4] = 0;
			gameSetup[4] = GUI.chooseNumber("What AI should be loaded for the other CPU? (leave blank for arbitrary AI decisions)");
			if( gameSetup[2] == 0) AI.loadRandomDecisionList2();
		}
		
		Rules.newGame(gameSetup[1]);
	}
	
	//Gets the location of the running jar file and saves it
	private static void getFileLocation() throws UnsupportedEncodingException{
		String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String location = URLDecoder.decode(path, "UTF-8");
		System.out.println(location);
		location.replace(':', ':');
		location = location.substring(1);
		
		location = location.substring(0, location.length() - 15);
		//GUI.choose(location, "OK");
		
		System.out.println(location);
		fileLocation = location;
	}
	
	
	public static void main(String[] args) throws Exception {
		//returns the address of the running jar and so can access necessary fiels
		getFileLocation();
		//Uncomment Next Line to play
		start();
		
		//N is the number up to which is searched
		int integerLimit = 1000000;
		//M is the number of rounds ahead searched
		int roundLimit = 10;
		
		//uncomment next line to find Winning Numbers
		//data.winningNumbers(roundLimit,integerLimit);
		//uncomment next line to find Losing Numbers
		//data.losingNumbers(roundLimit);
		//uncomment next line to split WN sets into mutually exclusive sets
		//data.winningNumbers_split(roundLimit);
		
		int listNumber = 10;
		int newCPU = 7;
		
		//uncomment to generate a new, fairly logical decision list
		//AI.saveDecisionList(AI.trainNewDecisionList(listNumber, integerLimit, roundLimit),newCPU);
		//uncomment to improve the newCPU's decision making for Winning and Losing Numbers up to 10 rounds ahead
		//AI.groomDecisionList(7, 10);
		
	}	
}
