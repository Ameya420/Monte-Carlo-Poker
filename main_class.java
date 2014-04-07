import java.util.Arrays;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;



class Main_class
{
	public static int temp_count1=0;
	public static int temp_count2=0;
	public static int Const=4000;
	static int noofPlayers = 8;
	static Player[] players = new Player[noofPlayers];
	static int[] board = {0,0,0,0,0};
	public int[] cards = {0,0};
	public static Strategy str=new Strategy();
	public static void main(String[] args) throws Exception
	{
		//int[] myIntArray = {12,5,7,25,24,23,45};
		//int[] myIntArray1 = {32,5,7,20,24,23,45};
		//str.un_main();
		game_with_strategy();
		//what_thinking(myIntArray);
		
		//System.out.println(str.handPower(myIntArray));
		//System.out.println(str.handPower(myIntArray1));
		//is_waiting_royal_flush(Card[] hand_number_suit);
	}
	
	public static void game_with_strategy() throws Exception
	{
		
		int[][] pairs = str.makePairs();
		//int[][] pairs = new int[2][1];
		//pairs[0][0]=1;
		//pairs[1][0]=14;
		FileWriter writer = new FileWriter("List_strat.csv");
		
		
		for(int j=0;j<pairs[0].length;j++)
		//for(int j=0;j<1;j++)
		{
			int wins = 0;
			System.out.println("Pair:- " + pairs[0][j]+","+pairs[1][j]);
			int count = 0;
			int noOfFolds =0;
			while(count<5000)
			{
				//System.out.println( "New iter......");
				dealing(pairs[0][j]-1,pairs[1][j]-1);
				int[] points = new int[noofPlayers];
				
				int[] midCards = new int[5];
				int [] flop = Arrays.copyOfRange(board,0,3);
				System.arraycopy(players[0].cards, 0,midCards, 0, 2);
				System.arraycopy(flop, 0,midCards, 2, 3);
				
				int decider = what_thinking(midCards);
				if ( (decider > 0 && decider >= 50*players[0].statergy) || players[0].statergy == 0)
				{				
					for(int i = 0; i < noofPlayers; i++)
					{
						midCards = new int[5];
						flop = Arrays.copyOfRange(board,0,3);
						System.arraycopy(players[i].cards, 0,midCards, 0, 2);
						System.arraycopy(flop, 0,midCards, 2, 3);
				
						decider = what_thinking(midCards);
						if ( players[i].statergy > 0 && (decider < 0 || decider < 50*players[i].statergy) )
						{
							players[i].fold= 1;
							//System.out.println("Player "+i+" folded");
						}				
					}
				
				
				
					for(int i = 0; i < noofPlayers; i++)
					{
						int[] finalCards = new int[7];
						System.arraycopy(players[i].cards, 0,finalCards, 0, 2);
						System.arraycopy(board, 0,finalCards, 2, 5);
						points[i] = str.handPower(finalCards);
						/* System.out.print("Card of "+ i +"th player-> "+ finalCards[0]/13+","+finalCards[0]%13 + "--");
					System.out.print( finalCards[1]/13+","+finalCards[1]%13 + "--");
					System.out.print( finalCards[2]/13+","+finalCards[2]%13 + "--");
					System.out.print( finalCards[3]/13+","+finalCards[3]%13 + "--");
					System.out.print(  finalCards[4]/13+","+finalCards[4]%13 + "--");
					System.out.print( finalCards[5]/13+","+finalCards[5]%13 + "--");
					System.out.print( finalCards[6]/13+","+finalCards[6]%13);
					System.out.println( "..Points->"+points[i]);
					System.out.println( "..Fold->"+players[i].fold); */
					}
					boolean w=true;
					for(int i = 0;(i < noofPlayers)  ; i++ ) 
					{
						if(players[i].fold == 0)
						{
							w = (points[0] >= points[i]);
							if(!w)
							{
								//System.out.println( "pLAYER wONN=="+ i);
								break;
							}
						}
					}
					
					if(w)
					{
						wins++;
						//System.out.println( "Jeeeeeeeeeta");
					}
					count++;
				}
				else { noOfFolds++; }
			
			}
			
			
			//System.out.println("New");
			writer.append(String.valueOf(pairs[0][j]));
			writer.append(",");
			writer.append(String.valueOf(pairs[1][j]));
			writer.append(",");
			writer.append(String.valueOf(wins));
			writer.append(",");
			writer.append(String.valueOf(noOfFolds));
			writer.append("\n");
			
			
			
		}
		writer.flush();
	    writer.close();
	
	}
	
	
	
	public static int what_thinking(int[] hand )
	{
		Card[] hand_number_suit = new Card[7];
		Card[] inhand_cards = new Card[2];
		Card[] flop_cards = new Card[2];
		int[] suit_array=new int[7];
		int[] number_array=new int[7];
		
		for(int card_num = 0; card_num < 7; card_num++) 
	    {
			hand_number_suit[card_num]=new Card();
			if(card_num<hand.length)
			{
				hand_number_suit[card_num]=str.number_suit(hand[card_num]);
				suit_array[card_num]=hand_number_suit[card_num].suit;
				number_array[card_num]=hand_number_suit[card_num].number;
			}
			else
			{
				hand_number_suit[card_num].suit=card_num*99;
				hand_number_suit[card_num].number=card_num*99*-1;
				suit_array[card_num]=hand_number_suit[card_num].suit;
				number_array[card_num]=hand_number_suit[card_num].number;			
			}
			
		}
		inhand_cards[0]=new Card();
		inhand_cards[1]=new Card();
		inhand_cards[0]=hand_number_suit[0];
		inhand_cards[1]=hand_number_suit[1];
		for (int x=0;x<flop_cards.length;x++)
		{
			flop_cards[x]=new Card();
			flop_cards[x]=hand_number_suit[x+2];
		}
		Arrays.sort(hand_number_suit);
		Arrays.sort(number_array);
		//is_waiting_royal_flush(hand_number_suit);
		//checking straight flush
		if(-1!=str.is_straight_flush(hand_number_suit))
		{
			return 100;
		}
		
		//checking 4-of-a-kind
		if(-1!=str.is_4_of_kind(number_array))
		{
			return 100;
		}		
		
		//checking full-house
		//System.out.println("Checking full");
		if(-1!=str.is_full_house(number_array))
		{
			return 100;
		}				
		//checking  flush
		//System.out.println("Checking flush");
		if(-1!=str.is_flush(hand_number_suit))
		{			
			return 100;
		}				
		//check straight
		//System.out.println("Checking Staright");
		if(-1!=str.is_straight(number_array))
		{			
			return 100;
		}		
		//checking triplet
		//System.out.println("Checking triplet one in hand");
		if(-1!=str.is_triplet(number_array))
		{			
			temp_count1=0;
			temp_count2=0;
			if(inhand_cards[0].number==inhand_cards[1].number) 
			{
				temp_count1=2;
				temp_count2=2;
			}
			else
			{
				temp_count1=1;
				temp_count2=1;
			}
			for(int i=0;i<flop_cards.length;i++)
			{
				if (flop_cards[i]==inhand_cards[0])
				{
					temp_count1++;
				}
				else if (flop_cards[i]==inhand_cards[1])
				{
					temp_count2++;
				}
				if (temp_count2>2) return 100;				
			}			
		}
		
		//ace 2 pair
		if(-1!=str.is_two_ace_pair(number_array))
		{
			return 100;
		}
		//check 2 pair
		//System.out.println("Checking double pair");
		if(-1!=str.is_two_pair(number_array))
		{
			return 100;
		}
		//one waiting for royal flush
		if(-1!=is_waiting_royal_flush(hand_number_suit))
		{
			return 100;
		}
		//one to flush
		if(-1!=is_one_card_waiting_flush(hand_number_suit))
		{
			if(inhand_cards[0].suit==inhand_cards[1].suit) 
			{
				return 100;				
			}
		}
		
		//one to seq open ended
		if(2==straight_wait(number_array))
		{
			return 100;
		}
		//any triplet
		if(-1!=str.is_triplet(number_array))
		{	
			return 50;
		}
		//check pair
		//System.out.println("Checking  pair");
		if(-1!=str.is_pair(number_array))
		{
			//if(10>=str.return_is_pair(number_array)) 
			//System.out.println("is pair");
			return 50;
		}
		//one to seq close ended
		if(1==straight_wait(number_array))
		{
			return 50;
		}
		//2 to flush
		
		
		//check high card
		//System.out.println("highest card is"+return_high(a_desc) );
		
		
		
		return -1;
		
		
		
		
		
	
	}
	public static int straight_wait(int[] aa)
	{
		int count=0;
		//public static int is_straight(int[] a);
		 
		for(int i=0;i<12;i++)
		{
			int[] temp=new int[aa.length];
			temp[0]=aa[0];
			temp[1]=aa[1];
			temp[3]=aa[3];
			temp[4]=aa[4];
			temp[5]=aa[5];
			temp[6]=aa[6];
			temp[2]=i;
			/* System.out.print(temp[0]+",");
			System.out.print(temp[1]+",");
			System.out.print(temp[2]+",");
			System.out.print(temp[3]+",");
			System.out.print(temp[4]+",");
			System.out.print(temp[5]+",");
			System.out.println(temp[6]);  */
			int xxx=str.is_straight(temp);
			if(xxx!=-1)
			{
				//System.out.println(i);
				count++;			
			}
			
			
		}
		if( count==1)
			{
				//System.out.println("Single Card straight Waight");
				return 1;
			}
			else if( count>=2)
			{
				//System.out.println("Double Card straight Waight");
				return 2;
			}
			else 
			{
				//System.out.println("Baba Ka thullu");
				return -1;
			}
	}
	public static int is_one_card_waiting_flush(Card[] hand_number_suit)
	{
		int count=0;
		Card[] temp=hand_number_suit;
		for(int i=0;i<52;i++)
		{
			temp[5]=str.number_suit(i);
			//System.out.println(temp[6].suit);
			//System.out.println(temp[6].number);
			int xxx=str.is_flush(temp);
			//System.out.println(xxx);
			if(xxx!=-1)
			{
				count++;			
			}
		}
		//System.out.println(count);
		if (count>=1)
		{
			//System.out.println(" Flush Waiting");
			return 1;
		}
		else
		{
			return -1;
		}
	
	}
 	public static int is_waiting_royal_flush(Card[] hand_number_suit)
	{
		int count=0;
		Card[] temp=hand_number_suit;
		for(int i=0;i<52;i++)
		{
			temp[5]=str.number_suit(i);
			//System.out.println(temp[6].suit);
			//System.out.println(temp[6].number);
			int xxx=str.is_straight_flush(temp);
			//System.out.println(xxx);
			if(xxx!=-1)
			{
				count++;			
			}
		}
		//System.out.println(count);
		if (count>=1)
		{
			//System.out.println("Royal Flush Waiting");
			return 1;
		}
		else
		{
			return -1;
		}
	
	} 
	
	public static void dealing(int c1, int c2) 
	{
		Strategy str=new Strategy();
		int[] cardDeck = new int[50];
		int j =0;
		int card1 =c1;
		int card2 = c2;
		for(int i = 0; i < 52; i++)
		{
			//System.out.println(i);
			if (i != card1 && i != card2)
			{
				cardDeck[j]=i;
				j++;
			}
		}
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(5);
		str.shuffleArray(cardDeck);
		for(int xx=0;xx<randomInt;xx++)
		{
			str.shuffleArray(cardDeck);
			str.shuffleArray(cardDeck);
			str.shuffleArray(cardDeck);
		}
		players[0] =  new Player();
		players[0].statergy = 1;
		players[0].cards[0] =  card1;
		players[0].cards[1] =  card2;
		//System.out.println(players[0].statergy);
		
		for(int i = 1; i < noofPlayers; i++)
		{   
			players[i] =  new Player();
			players[i].statergy = 1;
			players[i].cards[0] =  cardDeck[2*i-2];
			players[i].cards[1] =  cardDeck[2*i-1];
			
		
		}
		
		board[0] = cardDeck[noofPlayers*2 -1];
		board[1] = cardDeck[noofPlayers*2 +0];
		board[2] = cardDeck[noofPlayers*2 +1];
		board[3] = cardDeck[noofPlayers*2 +3];
		board[4] = cardDeck[noofPlayers*2 +5];
	
	}
}
