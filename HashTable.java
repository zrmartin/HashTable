/*
 * Zach Martin
 * zrmartin@calply.edu 
 * 11/17/15
 * Project 4 
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

/*
 * HashTable is an ADT of a hash table with quadratic probing. 
 * The hash table automatically re-hashes the table to twice it's
 * current size when the table reaches half capacity.
 * The hash function for this table is the objects hash-code modded
 * with the current table size
 */

public class HashTable
{
	/*
	 * HashEntry is a private inner class to represent 
	 * each element in the hash table. 
	 */
	private class HashEntry
	{
		public Object element; //element of the collection, generic type Object 
		public boolean active; //Whether this element is in the collection or deleted
		
		/*
		 * Constructor for a hash entry, sets element to parameter 
		 * and initializes 'active' to true
		 * @param element the element to add to the hash table  
		 */
		public HashEntry(Object element)
		{
			this.element = element;
			active = true;
		}
	}
	
	private HashEntry[] table; //Array of HashEntry's to represent the hash-table
	private int occupied; //Current number of elements active an inactive (used for re-hashing)
	
	/*
	 * Constructor for the HashTable ADT, sets the 'table' array
	 * size to the first prime number after twice the number of 
	 * elements to put into the table initially.
	 * @param the number of elements initially added to the table
	 */
	public HashTable(int numOfElements)
	{
		table = new HashEntry[findNextPrime(numOfElements)];
	}
	
	/*
	 * Private helper method to find the next prime number after a given integer
	 * @param numOfElements the number of elements initially added to the table
	 * @return the next prime number after the given parameter 
	 */
	private int findNextPrime(int numOfElements)
	{
		boolean found = false; //false until prime number is found
		int start = numOfElements * 2; //Start the search at twice the number of elements
		int check = 0; //Check 'start' with every integer < 'start' to see if prime
		int result = 0; //The prime number result
		while(!found)
		{
			while(check <= numOfElements)
			{
				if(start % check == 0)
				{
					break;
				}
				else if(check == numOfElements)
				{
					result = start;
					found = true;
					break;
				}
				check++;
			}
			check = 2;
			start++;
		}
		return result;
	}
	
	/*
	 * Private inner class Iterator to navigate through the hash table.
	 * The iterator only stops at indexes with active elements. 
	 * Used for outputting the elements of the collection.
	 */
	private class Iter implements Iterator
	{
		public int cursor; //What index the iterator is currently at
		
		/*
		 * Constructor for the hash-table iterator.
		 * Sets cursor initially to 0, then to the 
		 * next index with an active element.
		 */
		public Iter()
		{
			cursor = 0;
			while(( hasNext() && (table[cursor] == null || !table[cursor].active)))
			{
				cursor++;
			}
		}
		
		/*
		 * Tells whether or not the hash-table has any unvisited HashEntries 
		 * @return true if there is an unvisited HashEntry; false otherwise
		 */
		public boolean hasNext()
		{
			return cursor < table.length;
		}
		
		/*
		 * If the hash-table doesn't have any more unvisited HashEntries throw
		 * a NoSuchElementException. Else save the item at the cursor's current index
		 * and advance the iterator to the next index with an active element.
		 * @return The element at the index before the iterator moves.
		 */
		public Object next() throws NoSuchElementException
		{
			if(!hasNext())
			{
				throw new NoSuchElementException("Table has no more objects");
			}
			Object item = table[cursor].element;
			cursor++;
			while(hasNext() && (table[cursor] == null || !table[cursor].active))
			{
				cursor++;
			}
			return item;
		}
	}
	
	/*
	 * To insert a given object into the hash-table, utilizes 
	 * quadratic probing to deal with collisions. 
	 */
	public void insert(Object item)
	{
		int index = hashCode(item); //Starting index from the item's hash code
		int quadProbe = 1; //Used for quadratic probing in case of collision

		while(table[index] != null)
		{
			if(table[index].element.equals(item))
			{
				if(table[index].active)
				{
					return;
				}
				table[index].active = true;
				return;
			}
			index += Math.pow(quadProbe, 2);
			int temp = quadProbe - 1; //Need to subtract the index we added last loop
			index -= Math.pow(temp, 2);
			quadProbe++;
			//To make probing wrap-around.
			if(index >= table.length)
			{
				index -= table.length;
			}
		}
		table[index] = new HashEntry(item);
		occupied ++;
		if(occupied >= table.length/2)
		{
			rehash();
		}
	}
	
	/*
	 * Private helper class to determine the hash code for a 
	 * given object. The hash code is the java produced hash 
	 * code modded with the table size.
	 */
	private int hashCode(Object o)
	{
		return(o.hashCode() % table.length);
	}
	
	/*
	 * Used to increase the size of the table when capacity reaches at least 50%
	 * New table size is the first prime number of double the current table size
	 */
	private void rehash()
	{
		HashTable temp = new HashTable(table.length); //Doubles length in finding next prime method
		for(int i = 0; i < table.length; i++)
		{
			if(table[i] != null && table[i].active)
			{
				temp.insert(table[i].element);
			}
		}
		table = temp.table;
	}
	
	/*
	 * Method to delete a given object from the hash table
	 * The method of deletion is lazy-deletion. If the object 
	 * is in the table, it's 'active' boolean is changed to false.
	 * @param item the object to search to delete from the table
	 */
	public void delete(Object item)
	{
		boolean loop = false; //if the search for the given item loops through the table twice, the search stops
		int index = hashCode(item); //index of where to start given by hashCode private helper
		int quadProbe = 1; //Used for quadratic probing in case of collision
		while(table[index] == null || !table[index].element.equals(item))
		{
			index += Math.pow(quadProbe, 2);
			int temp = quadProbe - 1; //Need to subtract the index we added last loop
			index -= Math.pow(temp, 2);
			quadProbe ++;
			if(index >= table.length)
			{
				if(loop)
				{
					return;
				}
				index -= table.length;
				loop = true;
			}
		}
		table[index].active = false;
	}
	
	/*
	 * Method to search for a given object in the hash table.
	 * @param item the given item to search for
	 * @return the given item printed toString if found, null if not
	 */
	public Object find(Object item)
	{
		boolean loop = false; //if the search for the given item loops through the table twice, the search stops
		int index = hashCode(item); //index of where to start given by hashCode private helper
		int quadProbe = 1; //Used for quadratic probing in case of collision
		while(table[index] == null || !table[index].element.equals(item))
		{
			index += Math.pow(quadProbe, 2);
			int temp = quadProbe - 1; //Need to subtract the index we added last loop
			index -= Math.pow(temp, 2);
			quadProbe ++;
			if(index >= table.length)
			{
				if(loop)
				{
					return null;
				}
				index -= table.length;
				loop = true;
			}
		}
		return table[index].element;
	}
	
	/*
	 * Method to count the total number of ACTIVE elements in the table
	 * @return the number of active elements in the table
	 */
	public int elementCount()
	{
		int result = 0; //The total number of active elements in the table
		for(int i = 0; i < table.length; i++)
		{
			if(table[i] != null)
			{
				if(table[i].active)
				{
					result++;
				}
			}
		}
		return result;
	}
	
	/*
	 * Method to tell if the table is empty or not. Table is 
	 * considered not empty if there is at least one active element
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty()
	{
		for(int i = 0; i < table.length; i++)
		{
			if(table[i] != null)
			{
				if(table[i].active)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/*
	 * Method to make the hash table empty.
	 * Makes every index of the array of HashEntries null
	 */
	public void makeEmpty()
	{
		for(int i = 0; i < table.length; i++)
		{
			table[i] = null;
		}
	}
	
	/*
	 * Method to print the contents of the hash table.
	 * Each index of the hash table is printed followed
	 * by either "empty" or the element and whether 
	 * it is active or not
	 * 
	 */
	public void printTable()
	{
		for(int i = 0; i < table.length; i++)
		{
			System.out.print("[" + i + "]: ");
			if(table[i] == null)
			{
				System.out.print("empty\n");
			}
			else
			{
				System.out.print(table[i].element + ", ");
				if(table[i].active)
				{
					System.out.print("active\n");
				}
				else
				{
					System.out.print("inactive\n");
				}
			}
		}
	}
	
	/*
	 * Method to create an iterator for the hash table
	 * @return an iterator that traverses each active element in the hash table
	 */
	public Iterator iterator()
	{
		return new Iter();
	}
}
