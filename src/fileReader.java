import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class fileReader {

	public static List<Integer> readNumbers(String fileLocation) throws NumberFormatException, IOException{
		List<Integer> numbers = new ArrayList<>();
		System.out.println(fileLocation);
		for (String line : Files.readAllLines(Paths.get(fileLocation))) {
		    for (String part : line.split("\\s+")) {
		        Integer i = Integer.valueOf(part);
		        numbers.add(i);
		    }
		}
		
		return numbers;
	}
	
	public static void writeNumbers(List<Integer> numbers, String fileLocation){
        try {
            // Assume default encoding.
            FileWriter fileWriter =
                new FileWriter(fileLocation);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                new BufferedWriter(fileWriter);
            for(int i = 0; i < numbers.size(); ++i ){
                bufferedWriter.write(Integer.toString(numbers.get(i)));
                bufferedWriter.newLine();
            }
            // Always close files.
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + fileLocation + "'");
        }
	}
	
}
