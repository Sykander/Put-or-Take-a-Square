import java.awt.*;

import javax.swing.*;


public class GUI {

	//Jframe with game info
	static JFrame game = new JFrame("Put-or-Take-a-Square: Game");
	
	static //JFrame layout
	GridLayout gridLayout = new GridLayout(2,3);
	
	//Creates window for the player to enter a number
	public static int chooseNumber(String Question){
		
		//take input from the player
		String text = JOptionPane.showInputDialog(Question);
		
		int choice = 0;
		
		//try to convert player input to an integer
		try {
		    choice = Integer.parseInt(text);
		}
		catch(NumberFormatException ex)
		{
		    System.out.println("Exception : "+ex);
		}
		
		//return the players input integer
		//otherwise return 0
		return choice;
	}
	
	//Creates window for the player to read information and say OK
		public static int choose(String Question, String Option1){
			
			Object[] options = {Option1,};
			
			int s = (int)JOptionPane.showOptionDialog(null, Question, "Choose", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			return s;
		}
	
	//Creates window for the player to make a 2-option choice
	public static int choose(String Question, String Option1, String Option2){
		
		Object[] options = {Option1, Option2};
		
		int s = (int)JOptionPane.showOptionDialog(null, Question, "Choose", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		return s;
	}
	
	//Creates window for the player to make a 3-option choice
	public static int choose(String Question, String Option1, String Option2, String Option3){
		
		Object[] options = {Option1, Option2, Option3};
		
		int s = (int)JOptionPane.showOptionDialog(null, Question, "Choose", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		return s;
	}
	
	private static JLabel turn = new JLabel();
	private static JLabel currentInt = new JLabel();
	private static JLabel turnCount = new JLabel();
	
	//Creates game board for displaying game information (player vs player)
	public static void gameBoard(){
		game.setLayout(gridLayout);
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Specify current number, turn count and player turn
		String turnString = "It is " + Rules.currentPlayer + "'s turn.";
		String currentIntString = "Current Number: " + Rules.currentInt;
		String turnCountString = "Turn Count: " + Rules.turnCount;
		
		if( Main.gameSetup[3] == 0 ){
			turn = new JLabel(turnString);
			currentInt = new JLabel(currentIntString);
			turnCount = new JLabel(turnCountString);
			game.add(turn);
			game.add(currentInt);
			game.add(turnCount);
			game.setSize(720, 256);
			game.setLocation(256, 256);
		}
		else{
			turn.setText(turnString);
			currentInt.setText(currentIntString);
			turnCount.setText(turnCountString);
		}
		Main.gameSetup[3]++;
		
		//Make choice visible
		game.setVisible(true);
	}
	
	//Update the gameboard on each turn to display new information
	public static void gameUpdate(){
		
		
		//Specify current number, turn count and player turn
		String turnString = "It is " + Rules.currentPlayer + "'s turn.";
		String currentIntString = "Current Number: " + Rules.currentInt;
		String turnCountString = "Turn Count: " + Rules.turnCount;
		
		turn.setText(turnString);
		currentInt.setText(currentIntString);
		turnCount.setText(turnCountString);

	}

	public static void announceWinner(String winnerName) throws Exception {
		String theWinnerIs = "The winner is " + winnerName + "!";
		String OK = "play again?";
		choose(theWinnerIs, OK);
		Main.start();
	}
	

}
