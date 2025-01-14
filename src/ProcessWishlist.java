import java.io.File;
//import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import partII.ShowList.ShowNode;


public class ProcessWishlist {

	public static void main(String[] args) {
		
		//Create two empty lists from the ShowList class
		ShowList linkedList1 = new ShowList();
		ShowList linkedList2 = new ShowList();
		
		//initialize the scanner object
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter the TV Guide file path");              
		String tVGuideFilePath = sc.nextLine();                          
		
		System.out.println("Enter the Interest file path");              
		String interestFilePath = sc.nextLine();
		
		//Open the TVGuide.txt file and read its contents line by line
		try{
			Scanner scTVGuide = new Scanner(new File(tVGuideFilePath));
			while(scTVGuide.hasNextLine())
			{
				String currentLine = scTVGuide.nextLine();
				if(currentLine.equals("")){
					continue;                                              //to pass the empty lines between the shows
				}else{ //save the information                              
					String idAndName = currentLine;                        //save ID and name of the show
					String startTime = scTVGuide.nextLine();               //save startTime of the show
					String endTime = scTVGuide.nextLine();                 //save endTime of the show
					                                                       //Note for myself: split() returns an array of String
					TVShow newTVShow = new TVShow(idAndName.split(" ")[0], // Show's ID: .split(" ")[0]: the 1st item of the array, separated by space 
							idAndName.split(" ")[1],					   // Name of the show: .split(" ")[1]: the 2nd item of the array, separated by space: 
							Double.parseDouble(startTime.split(" ")[1]),   //pass the "S" and reads the start time as a double
							Double.parseDouble(endTime.split(" ")[1]));    //pass the "E" and reads the end time as a double
					
					if(!linkedList1.contains(newTVShow.getShowID())){  //the list should not have any duplicate records
						linkedList1.addToStart(newTVShow);             //using the addToStart() method, add the new ShowID to the list
					}
				}
			}
			
			//Open Interest.txt
			Scanner scInterest = new Scanner(new File(interestFilePath));
			
			//initialize ArrayLists to create Lists from the contents
			ArrayList<String> watchingList = new ArrayList<String>();
			ArrayList<String> wishList = new ArrayList<String>();
			
			scInterest.nextLine();
			while(scInterest.hasNextLine()){                    //for watching
				String currentLine = scInterest.nextLine();
				if(!currentLine.equalsIgnoreCase("wishlist")){  
					watchingList.add(currentLine.trim());       //add it to watchingList
				}else
					break;
			}
			
			while(scInterest.hasNextLine()){                    //for wishlist
				String currentLine = scInterest.nextLine(); 
				if(currentLine.equals(""))                     // if line is empty, pass
					continue;
				else                                           //NOTE to myself:  trim() removes whitespace from both ends of a string
					wishList.add(currentLine.trim());          //add the content to wishlist
			}
			
			//processing wish-list
			System.out.println("\nLet's process the wish list in the Ineterest.txt:");
			for(int i=0; i<wishList.size(); i++){
				linkedList1.find(wishList.get(i),false);
				
				TVShow wishListShow = linkedList1.find(wishList.get(i),false).getDataTVShow();
				
				//comparison of above wish-list-show with watching
				TVShow watchingShow = null;
				boolean canWatch = false;
				for(int j=0; j<watchingList.size(); j++){
					watchingShow=linkedList1.find(watchingList.get(j),false).getDataTVShow();
					
					if(wishListShow.isOnSameTime(watchingShow).equalsIgnoreCase("Some Overlap")){
						canWatch = false;
						break;
					}
					if(wishListShow.isOnSameTime(watchingShow).equalsIgnoreCase("Same time")){
						canWatch = false;
						break;
					}
					if(wishListShow.isOnSameTime(watchingShow).equalsIgnoreCase("Different time")){
						canWatch = true;
					}
				}
				
				if(canWatch == true){
					System.out.println("User can watch show "+wishListShow.getShowID()+" as he/she is not watching anything else during that time");
				}else{
					if(wishListShow.isOnSameTime(watchingShow).equalsIgnoreCase("Some Overlap")){
						System.out.println("User can’t watch show "+wishListShow.getShowID()+" as he/she is not finished with a show he/she is watching");
					}else if(wishListShow.isOnSameTime(watchingShow).equalsIgnoreCase("Same time")){
						System.out.println("User can’t watch show "+wishListShow.getShowID()+" as he/she will begin another show at the same time");
					}
				}
			} //end of wish list processing
			
			//Prompt the user to enter a few showIDs and search the created list
			System.out.print("\nEnter no. of showIDs to check: ");
			int limit = Integer.parseInt(sc.nextLine());
			
			for(int i=1; i<=limit; i++){
				System.out.println("Enter a showID:");
				String s_ID = sc.nextLine();
				linkedList1.find(s_ID, true); //the find method also displays the number of iterations performed
			}
			System.out.println();
			
			//Testing other methods
			System.out.println("Now, testing other methods/constructos of ShowList class:\n");
			
			linkedList2 = new ShowList(linkedList1); 
			
			//Testing equals method
			System.out.println("Checking equals method:");
			if(linkedList1.equals(linkedList2)){
				System.out.println("Original and copied Linked-Lists are equal");
			}
			else
			{
				System.out.println("Original and copied Linked-Lists are NOT equal");
			}
			
			//printing the original linkedList
			System.out.println("\nThe original list before adding/remobing new elements:");
			displayLinkedList(linkedList1);
			
			//Testing insertAtIndex method
			TVShow newTVShow = new TVShow("TESTSHOW", "FRIENDS", 22.00, 23.00);
			linkedList1.insertAtIndex(newTVShow, 2);
			System.out.println("\nAfter adding new element at index 2:");
			displayLinkedList(linkedList1);
			
			//Testing deleteFromStart method
			System.out.println("\nAfter deleting element from start:");
			linkedList1.deleteFromStart();
			displayLinkedList(linkedList1);
			
			//Testing deleteFromIndex method
			System.out.println("\nAfter deleting element from index 5:");
			linkedList1.deleteFromIndex(5);
			displayLinkedList(linkedList1);
			
			//Testing replaceAtIndex method
			System.out.println("\nAfter replacing element at index 3:");
			linkedList1.replaceAtIndex(newTVShow, 3);
			displayLinkedList(linkedList1);
			
			//Testing AddtoStart method
			System.out.println("\nAfter adding element at start:");
			linkedList1.addToStart(newTVShow);
			displayLinkedList(linkedList1);
			
			scTVGuide.close(); 
			scInterest.close();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			sc.close();
		}
	}
	
		public static void displayLinkedList(ShowList list) {
		ShowNode node = list.getHead();
		System.out.print(node.getDataTVShow().getShowID());
		node = node.getNextShowNode();
		
		while(node != null) {
			System.out.print("->"); 
			System.out.print(node.getDataTVShow().getShowID());
			node = node.getNextShowNode(); //move to the next node
		}
		System.out.println();
	}

}
