import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * TextBuddy CE1
 * @author Duan Xu Zhou A0108453J
 *
 */
public class TextBuddy {
	private static final String MESSAGE_MISSING_ARGUMENTS = "ERROR: %s.";
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use";
	private static final String MESSAGE_ADD = "added to %s: \"%s\"";
	private static final String MESSAGE_DELETE = "deleted from %s: \"%s\"";
	private static final String MESSAGE_CLEAR = "all content deleted from %s";
	private static final String MESSAGE_EMPTY_FILE = "%s is empty";
	private static final String MESSAGE_DISPLAY = "%d. %s";
	private static final String MESSAGE_COMMAND = "command: ";
	
	//data structure to store text file
	private static ArrayList<String> data;

	private static File file;
	private static Scanner sc;
	private static final int INDEX_OUT_OF_BOUND = -1;

	private static boolean isNotExit = true;

	public static void main(String[] args) {
		verifyArgument(args);
		createNewFile(args[0]);
		showWelcomeMessage();
		scanCommandUntilExit();
	}
	
	private static void verifyArgument(String[] args) {
		if (args.length == 0) {
			printMessage(MESSAGE_MISSING_ARGUMENTS, "MISSING ARGUMENTS");
			System.exit(0);
		}
	}

	private static void createNewFile(String filename) {
		createConstruct(filename);
		readFromFile(file);
	}
	
	private static void showWelcomeMessage() {
		printMessage(MESSAGE_WELCOME, file.getName());
	}
	
	private static void scanCommandUntilExit() {
		while (isNotExit) {
			acceptUserInput();
		}
	}
	
	private static void createConstruct(String filename) {
		data = new ArrayList<String>();
		file = new File(filename);
		sc = new Scanner(System.in);
	}
	
	private static void readFromFile(File file) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String temp;
			while ((temp = br.readLine()) != null) {
				data.add(temp);
			}
			br.close();
		}catch (IOException e) {
		}
	}

	private static void acceptUserInput(){
		String input;
		String userInput = null;
		displayPrompt();
		input = sc.nextLine().trim();
		String[] tokenizer = input.split(" ",2);
		String userCommand=tokenizer[0];
		if(tokenizer.length>1){
			userInput=tokenizer[1];
		}
		executeCommand(userCommand,userInput);
	}

	private static void executeCommand(String command, String input) {
		switch (command) {
		case "add" :
			command_ADD(input);
			break;
		case "display" :
			command_Display();
			return; 
		case "delete" :
			command_Delete(input);
			break;
		case "clear" :
			command_Clear();
			break;
		case "exit" :
			exit();
			break;
		default :
			throw new Error(); 
		}

		save();
	}

	private static void command_ADD(String input) {
		if (input == null) {
			return;
		}
		data.add(input);
		printMessage(MESSAGE_ADD, file.getName(), input);
	}

	private static void command_Display() {
		if (data.isEmpty()) {
			printMessage(MESSAGE_EMPTY_FILE, file.getName());
		}
		for (int i = 0; i < data.size(); i++) {
			printMessage(MESSAGE_DISPLAY, i + 1, data.get(i));
		}
	}

	private static void command_Delete(String input) {
		if (data.isEmpty()) {
			printMessage(MESSAGE_EMPTY_FILE, file.getName());
			return;
		}
		int index = getIndex(input);
		deleteContentAtIndex(index);
	}

	private static void command_Clear() {
		data.clear();
		printMessage(MESSAGE_CLEAR, file.getName());
	}

	private static void exit() {
		sc.close();
		isNotExit = false;
	}

	private static int getIndex(String input) {
		try {
			return Integer.parseInt(input) - 1;
		} catch (NumberFormatException e) {
		}
		return INDEX_OUT_OF_BOUND;
	}

	private static void deleteContentAtIndex(int index) {
		if (index == INDEX_OUT_OF_BOUND) {
			return;
		}
		try {
			String content = data.get(index);
			data.remove(index);
			printMessage(MESSAGE_DELETE, file.getName(), content);
		} catch (IndexOutOfBoundsException e) {
		}

	}

	private static void save() {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));
			for (String element : data) {
				bw.write(element);
				bw.newLine();
			}
			bw.close();
		}catch (IOException e) {
		}
	}

	private static void displayPrompt() {
		printMessage(MESSAGE_COMMAND);
	}

	private static void printMessage(String message, Object... args) {
		System.out.println(String.format(message, args));
	}
}