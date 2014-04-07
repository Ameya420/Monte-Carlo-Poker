import java.util.Arrays;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;



class flop_after
{
	public static int temp_count1=0;
	public static int temp_count2=0;
	public static int Const=4000;
	static int noofPlayers = 4;
	static Player[] players = new Player[noofPlayers];
	static int[] board = {0,0,0,0,0};
	public int[] cards = {0,0};
	public static Strategy str=new Strategy();
	public static void main(String[] args) throws Exception
	{
		//int[] myIntArray = {1,2,3,4,18,14,13};
		//str.un_main();
		game_with_strategy();
		/* for(int k=1;k<1234;k++)
		{
			Random rnd = new Random();
			System.out.println(rnd.nextInt(52));
		} */
		//System.out.println(what_thinking(myIntArray));
		//System.out.println(str.handPower(myIntArray));
		
		//is_waiting_royal_flush(Card[] hand_number_suit);
	}
	
	public static void game_with_strategy() throws Exception
	{
		
		FileWriter writer = new FileWriter("List_strat.csv");
		
		
		for(int j=1;j<14;j++)
		//for(int j=1;j<1;j++)
		{
			int cnst=100;
			if(j==1)
			{
				cnst=200;
			}
			else if(j<9)
			{
				cnst=1000;
			}
			else
			{
				cnst=1500;
			}
			
			int wins = 0;
			System.out.println("Number Currently going :- " + j);
			int count = 0;
			int noOfFolds =0;
			while(count<cnst)
			{
				dealing(0,0);
				int[] points = new int[noofPlayers];
				
				int[] midCards = new int[5];
				int [] flop = Arrays.copyOfRange(board,0,3);
				int apna_banda=99;
				for(int i = 0; i < noofPlayers; i++)
				{
					midCards = new int[5];
					flop = Arrays.copyOfRange(board,0,3);
					System.arraycopy(players[i].cards, 0,midCards, 0, 2);
					System.arraycopy(flop, 0,midCards, 2, 3);
				
					int decider = what_thinking(midCards);
					if ( j*100==decider)
					{
						players[i].fold= 100;
						apna_banda=i;
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
					System.out.println( finalCards[6]/13+","+finalCards[6]%13); */
				}
				
				
				
				
				
				boolean w=true;
				//System.out.println("Apna_banda->"+apna_banda);
				if (apna_banda!=99)
				{
					for(int i = 0;(i < noofPlayers) && w ; i++ ) 
					{
						//System.out.println("Apna_banda ->"+apna_banda);
						w = (points[apna_banda] >= points[i]);
						if(!w)
						{
							//System.out.println("Winner_banda ->"+i);
							break;
						}
					}
					if(w)
					{
						wins++;
						//System.out.println("Jeet gaya re bawa"+apna_banda);
					}
					//System.out.println("Count"+count);
					count++;
				}
				else
				{
					//System.out.println("Game Not Counted");
				}
			}
			
			
			
			float prob=(float)wins/cnst;
			//System.out.println("New");
			writer.append(String.valueOf(wins));
			writer.append(",");
			writer.append(String.valueOf(cnst));
			writer.append(",");
			writer.append(String.valueOf(prob));
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
		/* for(int card_num = 0; card_num < 7; card_num++) 
		{			
			System.out.println(number_array[card_num]);
		} */
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
			return 100*2;
		}		
		
		//checking full-house
		//System.out.println("Checking full");
		if(-1!=str.is_full_house(number_array))
		{
			return 100*3;
		}				
		//checking  flush
		//System.out.println("Checking flush");
		if(-1!=str.is_flush(hand_number_suit))
		{			
			return 100*4;
		}				
		//check straight
		//System.out.println("Checking Staright");
		if(-1!=str.is_straight(number_array))
		{			
			return 100*5;
		}		
		//checking triplet
		//System.out.println("Checking triplet one in hand");
		if(-1!=str.is_triplet(number_array))
		{			
			temp_count1=0;
			temp_count2=0;
			//System.out.println("Checking triplet one in hand");
			if(inhand_cards[0].number==inhand_cards[1].number) 
			{
				//System.out.println("Chhhhecking triplet one in hand");
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
				if (flop_cards[i].number==inhand_cards[0].number)
				{
					
					temp_count1++;
					//System.out.println("Chhhhecking triplet one in hand"+temp_count1);
				}
				else if (flop_cards[i].number==inhand_cards[1].number)
				{
					temp_count2++;
				}
				if (temp_count2>2) 
				{
					//System.out.println("Chhhhecking triplet one in hand");
					return 6*100;	
				}			
			}			
		}
		
		//ace 2 pair
		if(-1!=str.is_two_ace_pair(number_array))
		{
			return 700;//str.is_two_ace_pair(number_array);
		}
		//check 2 pair
		//System.out.println("Checking double pair");
		if(-1!=str.is_two_pair(number_array))
		{
			return 700;
		}
		//one waiting for royal flush
		if(-1!=is_waiting_royal_flush(hand_number_suit))
		{
			return 800;
		}
		//one to flush
		if(-1!=is_one_card_waiting_flush(hand_number_suit))
		{
			if(inhand_cards[0].suit==inhand_cards[1].suit) 
			{
				return 900;				
			}
		}
		
		//one to seq open ended
		if(2==straight_wait(number_array))
		{
			return 1000;
		}
		//any triplet
		if(-1!=str.is_triplet(number_array))
		{	
			return 1100;
		}
		//check pair
		//System.out.println("Checking  pair");
		if(-1!=str.is_pair(number_array))
		{
			//if(10>=str.return_is_pair(number_array)) 
			//System.out.println("is pair");
			return 1200;
		}
		//one to seq close ended
		if(1==straight_wait(number_array))
		{
			return 1300;
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
		int[] cardDeck = new int[52];
		int card1 =c1;
		int card2 = c2;
		for(int i = 0; i < 52; i++)
		{
			cardDeck[i]=i;
			
		}
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(11);
		str.shuffleArray(cardDeck);
		for(int xx=0;xx<randomInt;xx++)
		{
			str.shuffleArray(cardDeck);
		}
		
		/* for(int i = 0; i < 52; i++)
		{
			System.out.print(cardDeck[i]+",");
			
			
		} */
		//System.out.println();
		for(int i = 0; i < noofPlayers; i++)
		{   
			players[i] =  new Player();
			players[i].statergy = 0;
			players[i].cards[0] =  cardDeck[2*i];
			players[i].cards[1] =  cardDeck[2*i+1];
			
		
		}
		
		board[0] = cardDeck[noofPlayers*2 +1];
		board[1] = cardDeck[noofPlayers*2 +2];
		board[2] = cardDeck[noofPlayers*2 +3];
		board[3] = cardDeck[noofPlayers*2 +5];
		board[4] = cardDeck[noofPlayers*2 +7];
	
	}
}
