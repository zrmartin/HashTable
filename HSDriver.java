/*
 * Zach Martin
 * zrmartin@calply.edu 
 * 11/17/15
 * Project 4 
 */

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;

/*
 * Driver class to test my HashTable class.
 * Takes in initial N number of students from a given file and 
 * inserts them into the hash table. After, a menu is printed 
 * allowing testing of every method of the HashTable.
 */
public class HSDriver 
{	
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in); //For user keyboard input
		System.out.println("Enter input filename");
		Scanner file = null; //Scanner for the file that contains the initial students
		try
		{
			File input = new File(in.next()); //The file that contains the initial students
			file = new Scanner(input);
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		int N = Integer.parseInt(file.nextLine()); //The number of students in the file
		HashTable table = new HashTable(N); //Creating a new hash table
		//Testing and inserting students from input file.
		for(int i = 0; i < N ; i++)
		{
			StringTokenizer t = new StringTokenizer(file.nextLine()); //To test number of elements per line and to divide info
			if(t.countTokens() == 2)
			{
				String stringID = t.nextToken(); //The id of the student as a string
				String last = t.nextToken(); //The last name of the student
				if(isNumeric(stringID))
				{
					Long longID = Long.parseLong(stringID); //Making the string id into type long
					if(longID > 0)
					{
						table.insert(new Student(longID, last));
					}
				}
			}
		}
		//Printing menu for user to perform any of the given operations on the Hash Table
		String greeting = "Choose one of the following operations by entering provided letter:\n" 
						+ "a - add the element\n"
						+ "d - delete the element\n"
						+ "f - find and retrieve the element\n"
						+ "n - get the number of elements in the collection\n"
						+ "e - check if the collection is empty\n"
						+ "k - make the hash table empty\n"
						+ "p - print the content of the hash table\n"
						+ "o - output the elements of the collection\n"
						+ "q - Quit the program";
		System.out.println(greeting);
		
		/*
		 * The case that is executed is determined by the user's input.
		 * the program will continue to run for testing until user enters 'q'
		 */
		mainloop:
		while(in.hasNext())
		{
			String choice = in.next(); //The letter choice of the user
			switch(choice)
			{
				case "a":
				{
					in.nextLine();
					System.out.println("Enter the student's ID and lastName (seperated by a space)");
					StringTokenizer s = new StringTokenizer(in.nextLine());
					if(s.countTokens() == 2)
					{
						try
						{
							String id = s.nextToken();
							String lastName = s.nextToken();
							long idLong = Long.parseLong(id);
							if(idLong > 0)
							{
								table.insert(new Student(idLong, lastName));
								System.out.println(lastName + " has been added to the table");
							}
							else
							{
								System.out.println("Skipping, negative id");
							}
						}
						catch(NumberFormatException e)
						{
							System.out.println("Skipping, id not of type long");
						}
					}
					else
					{
						System.out.println("Skipping, lineELements != 2");
					}
					break;
				}
				case "d":
				{
					in.nextLine();
					System.out.println("Enter id of student to delete");
					String id = in.next();
					try
					{
						long idLong = Long.parseLong(id);
						if(idLong < 0)
						{
							System.out.println("Skipping, negative id");
							break;
						}
						Student dummy = new Student(idLong, "dummy");
						table.delete(dummy);
						System.out.println("The student with id " + idLong + " has been deleted");
						break;
					}
					catch(NumberFormatException e)
					{
						System.out.println("Skipping, id not of type long");
						break;
					}
				}
				case "f":
				{
					in.nextLine();
					System.out.println("Enter id of student to find");
					String id = in.next();
					try
					{
						long idLong = Long.parseLong(id);
						if(idLong < 0)
						{
							System.out.println("Skipping, negative id");
							break;
						}
						Student dummy = new Student(idLong, "dummy");
						Object found = table.find(dummy);
						if(found == null)
						{
							System.out.println(found + " was not found in the table");
						}
						else
						{
							System.out.println(found + " was found in the table");
						}
						break;
					}
					catch(NumberFormatException e)
					{
						System.out.println("Skipping, id not of type long");
						break;
					}
				}
				case "n":
				{
					in.nextLine();
					System.out.println("There are " + table.elementCount() + " active elements in the table");
					break;
				}
				case "e":
				{
					in.nextLine();
					if(table.isEmpty())
					{
						System.out.println("The table is empty");
					}
					else
					{
						System.out.println("The table is not empty");
					}
					break;
				}
				case "k":
				{
					in.nextLine();
					table.makeEmpty();
					System.out.println("The table is now empty");
					break;
				}
				case "p":
				{
					in.nextLine();
					table.printTable();
					break;
				}
				case "o":
				{
					Iterator iter = table.iterator(); //Used to print each active element in the hash table
					while(iter.hasNext())
					{
						Object item = iter.next();
						Student s = (Student) item;
						System.out.println(s.toString());
					}
					break;
				}
				case "q":
				{
					in.nextLine();
					System.out.println("Quitting, have a nice day");
					break mainloop;
				}
		}
		file.close();
	}
}
	
	/*
	 * Private helper class used in order to test if
	 * the given id for a student can be parsed into
	 * type long.
	 * @param s the id of type string to test
	 * @return true if can parse and is valid; false otherwise
	 */
	private static boolean isNumeric(String s)
	{
		try
		{
			Long l = Long.parseLong(s);
		}
		catch(NumberFormatException e)
		{
			return false;
		}
		return true;
	}

	
}
