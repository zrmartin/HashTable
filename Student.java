/*
 * Zach Martin
 * zrmartin@calply.edu 
 * 11/17/15
 * Project 4 
 */

public class Student 
{
	private long id; //The id of the student, each id is unique
	private String lastName; //The last name of the student
	
	/*
	 * Constructor for a student
	 * @param id the id for this given student
	 * @param lastName the last name for this given student
	 */
	public Student(long id, String lastName)
	{
		this.id = id;
		this.lastName = lastName;
	}
	
	/*
	 * Method to test if two students are equal to each other.
	 * Used for insertion, deletion, and finding elements in the table.
	 * @param other the other student to compare to
	 * @return boolean true if both student id's are equal; false otherwise
	 */
	public boolean equals(Object other)
	{
		Student o = (Student) other;
		return id == o.id;
	}
	
	/*
	 * Method to print the id and name of a given student
	 * @return the id and name of the student formatted
	 */
	public String toString()
	{
		return "{id: " + id + ", name: " + lastName + "}";
	}
	
	/*
	 * Method to obtain the hashCode of a given student
	 * @return the hashCode of the student as type long 
	 */
	public int hashCode()
	{
		return Long.hashCode(id);
	}

}
