import java.util.Arrays;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;





public class Strategy
{ 
	public static int Const=4000;
	static int noofPlayers = 4;
	static Player[] players = new Player[noofPlayers];
	static int[] board = {0,0,0,0,0};
	public static int send_me_factors(int a1,int a2,int a3, int a4, int a5)
	{
		return ((((a1*15+a2)*15+a3)*15+a4)*15+a5)/1000;
	
	}
	public static void dealing(int c1, int c2) 
	{
		int[] cardDeck = new int[50];
		int j =0;
		int card1 =c1;
		int card2 = c2;
		for(int i = 0; i < 52; i++)
		{
			if (i != card1 && i != card2)
			{
				cardDeck[j]=i;
				j++;
			}
		}
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(5);
		for(int xx=0;xx<randomInt;xx++)
		{
			shuffleArray(cardDeck);
			shuffleArray(cardDeck);
			shuffleArray(cardDeck);
		}
		players[0] =  new Player();
		players[0].cards[0] =  card1;
		players[0].cards[1] =  card2;
		
		
		for(int i = 1; i < noofPlayers; i++)
		{   
			players[i] =  new Player();
			players[i].cards[0] =  cardDeck[2*i-2];
			players[i].cards[1] =  cardDeck[2*i-1];
			
		
		}		
		board[0] = cardDeck[noofPlayers*2 -1];
		board[1] = cardDeck[noofPlayers*2 +0];
		board[2] = cardDeck[noofPlayers*2 +1];
		board[3] = cardDeck[noofPlayers*2 +3];
		board[4] = cardDeck[noofPlayers*2 +5];
	
	}
	
	
	static void shuffleArray(int[] ar)
   {
		Random rnd = new Random();
		for (int i = ar.length - 1; i > 0; i--)
		{
			int index = rnd.nextInt(i + 1);
			// Simple swap
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}	
	}
	
	
	public static void game() throws Exception
	{	
		
		int[][] pairs = makePairs();
		FileWriter writer = new FileWriter("List.csv");		
		for(int j=0;j<pairs[0].length;j++){
			int wins = 0;
			System.out.println("Pair:- " + pairs[0][j]+","+pairs[1][j]);
			for(int k=0;k<50;k++)
			{
				dealing(pairs[0][j]-1,pairs[1][j]-1);
				int[] points = new int[noofPlayers];
				for(int i = 0; i < noofPlayers; i++)
				{
					int[] finalCards = new int[7];
					System.arraycopy(players[i].cards, 0,finalCards, 0, 2);
					System.arraycopy(board, 0,finalCards, 2, 5);
					points[i] = handPower(finalCards);
					//System.out.println(points[i]);
				}
				boolean w=true;
				for(int i = 0;(i < noofPlayers) && w; i++ ) {
					w = (points[0] >= points[i]);
				}
				if(w){
				wins++;				
				}
				else{
				}
			}
			
			
			//System.out.println("New");
			writer.append(String.valueOf(pairs[0][j]));
			writer.append(",");
			writer.append(String.valueOf(pairs[1][j]));
			writer.append(",");
			writer.append(String.valueOf(wins));
			writer.append("\n");
			
			
			
		}
		writer.flush();
	    writer.close();
	}
	
	 
	public static int[][] makePairs() {
		int i,j,count=0,k=0;
		int[][] ans = new int[2][];
		for(i=1;i<=13;i++) {
			for(j=i+1;j<=26;j++) {
				count++;
			}
		}
		System.out.println(count);
		ans[0] = new int[count];
		ans[1] = new int[count];
		for(i=1;i<=13;i++) {
			for(j=i+1;j<=26;j++) {
				ans[0][k] = i;
				ans[1][k] = j;
			//	System.out.println(i+"-"+j);
				k++;
			}
		}
		
		return ans;
	}
   
	
   public static void un_main() throws Exception
   { 

		//int[] myIntArray = {0,13,1,14,16,6,4};
		//int[] myIntArray1 = {12,1,25,14,16,6,4};
		//int[] myIntArray2 = {8,12,25,31,16,6,4};
		//int[] myIntArray3 = {14,19,25,31,16,6,4};
		//int[] myIntArray4 = {28,47,25,31,16,6,4};
		//handPower(myIntArray);
		game();
		//System.out.println(handPower(myIntArray));
		//System.out.println(handPower(myIntArray1));
		//System.out.println(handPower(myIntArray2));
		//System.out.println(handPower(myIntArray3));
		//System.out.println(handPower(myIntArray4));
   }
   
   public static int handPower(int[] hand)
   {	
		//System.out.println("Starting HandPower");
		int CASE = 0;
		Card[] hand_number_suit = new Card[7];
		int[] suit_array=new int[7];
		int[] number_array=new int[7];
		for(int card_num = 0; card_num < 7; card_num++) 
	    {
			hand_number_suit[card_num]=number_suit(hand[card_num]);
			suit_array[card_num]=hand_number_suit[card_num].suit;
			number_array[card_num]=hand_number_suit[card_num].number;
			
		}
		
		Arrays.sort(hand_number_suit);
		Arrays.sort(number_array);
		
		
		//checking straight flush
		if(-1!=is_straight_flush(hand_number_suit))
		{
			return is_straight_flush(hand_number_suit);
		}
		
		//checking 4-of-a-kind
		if(-1!=is_4_of_kind(number_array))
		{
			return is_4_of_kind(number_array);
		}		
		
		//checking full-house
		//System.out.println("Checking full");
		if(-1!=is_full_house(number_array))
		{
			int[] for_full=new int[number_array.length];
			for (int ss=0;ss<number_array.length;ss++)
			{
				if(number_array[ss]!=0)
				{
					for_full[ss]=number_array[ss];
				}
				else
				{
					for_full[ss]=13;
				}
			}
			return is_full_house(for_full);
		}		
		
		//checking  flush
		//System.out.println("Checking flush");
		if(-1!=is_flush(hand_number_suit))
		{			
			return is_flush(hand_number_suit);
		}		
		
		//check straight
		//System.out.println("Checking Staright");
		if(-1!=is_straight(number_array))
		{
			
			return is_straight(number_array);
		}
		
		//checking triplet
		//System.out.println("Checking triplet");
		if(-1!=is_triplet(number_array))
		{
			return is_triplet(number_array);
		}
		
		//ace 2 pair
		if(-1!=is_two_ace_pair(number_array))
		{
			return is_two_ace_pair(number_array);
		}
		//check 2 pair
		//System.out.println("Checking double pair");
		if(-1!=is_two_pair(number_array))
		{
			return is_two_pair(number_array);
		}
		
		//check pair
		//System.out.println("Checking  pair");
		if(-1!=is_pair(number_array))
		{
			return is_pair(number_array);
		}
		//check high card
		//System.out.println("highest card is"+return_high(a_desc) );
		
		
		
		return return_high(number_array);
   }
   
   
   
   
   
   
   
   
   
   
   
   
   
   public static int return_high(int[] num_array)
   {
		int[] a_desc=new int[num_array.length]; 
		for(int i=0;i<num_array.length;i++)
		{
			a_desc[i]=num_array[(num_array.length-i-1)];
		}
		//System.out.println(num_array[num_array.length-1]);
		if (a_desc[a_desc.length-1]==0)
		{
			return send_me_factors(13,a_desc[1],a_desc[2], a_desc[3], a_desc[4]);
		}
		else
		{
			return send_me_factors(a_desc[0],a_desc[1],a_desc[2], a_desc[3], a_desc[4]);
		}
   
   }
   public static int is_pair(int[] number_array)
   {
		int same_card_number=1;
		int temp=9999;
		Arrays.sort(number_array);
		int[] a_desc=new int[number_array.length]; 
		for(int i=0;i<number_array.length;i++)
		{
			a_desc[i]=number_array[(number_array.length-i-1)];
		}
		for(int card_num = 0; card_num < 7; card_num++)
		{
			if(temp==a_desc[card_num])
			{
				same_card_number++;
				if(same_card_number>=2)
				{
					//System.out.println("pair of "+a_desc[card_num]);
					if(a_desc[card_num]==0)
					{
						return Const*2+send_me_factors(13,0,0, 0, 0);/*frhdffet*/
					}
					else
					{
						return Const*2+send_me_factors(a_desc[card_num],0,0, 0, 0);
					}
					
					
				}				
			}
			else
			{
				same_card_number=1;
			}
			temp=a_desc[card_num];
		}
		return -1;
   }
   public static int return_is_pair(int[] number_array)
   {
		int same_card_number=1;
		int temp=9999;
		Arrays.sort(number_array);
		int[] a_desc=new int[number_array.length]; 
		for(int i=0;i<number_array.length;i++)
		{
			a_desc[i]=number_array[(number_array.length-i-1)];
		}
		for(int card_num = 0; card_num < 7; card_num++)
		{
			if(temp==a_desc[card_num])
			{
				same_card_number++;
				if(same_card_number>=2)
				{
					//System.out.println("pair of "+a_desc[card_num]);
					if(a_desc[card_num]==0)
					{
						return 13;
					}
					else
					{
						return a_desc[card_num];
					}					
				}				
			}
			else
			{
				same_card_number=1;
			}
			temp=a_desc[card_num];
		}
		return -1;
   }
   public static int is_two_pair(int[] number_array)
   {
		
		int same_card_number=1;
		int temp=9999;
		
		int[] a_desc=new int[number_array.length]; 
		for(int i=0;i<number_array.length;i++)
		{
			a_desc[i]=number_array[(number_array.length-i-1)];
		}
		for(int card_num = 0; card_num < 7; card_num++)
		{
			if(temp==a_desc[card_num])
			{
				same_card_number++;
				if(same_card_number>=2)
				{					
					//check pair-after-pair
					same_card_number=1;
					temp=9999;
					for(int j=0;j<number_array.length;j++)
					{						
						if(j!=card_num && j!=card_num-1 )
						{							
							if(temp==a_desc[j])
							{
								//System.out.println("pair of "+a_desc[card_num]+"found and double of"+a_desc[j] );
								if(a_desc[card_num]==0)
								{									
									if(a_desc[j]==0)
									{
										return 3*Const+send_me_factors(13,13,0, 0, 0);
									}
									else
									{
										return 3*Const+send_me_factors(13,a_desc[j],0, 0, 0);/*edhtdhtdtg*/
									}
								}
								else
								{
									if(a_desc[j]==0)
									{
										return 3*Const+send_me_factors(a_desc[card_num],13,0, 0, 0);
									}
									else
									{
										return 3*Const+send_me_factors(a_desc[card_num],a_desc[j],0, 0, 0);
									}								
								}								
							}
							temp=a_desc[j];
						}					
					}					
				}				
			}
			else
			{
				same_card_number=1;
			}
			temp=a_desc[card_num];
		}
   
		return -1;
   }
   public static int is_triplet(int[] number_array)
   {
		int same_card_number=1;
		int temp=9999;
		Arrays.sort(number_array);
		int[] a_desc=new int[number_array.length]; 
		for(int i=0;i<number_array.length;i++)
		{
			a_desc[i]=number_array[(number_array.length-i-1)];
		}
		for(int card_num = 0; card_num < 7; card_num++)
		{
			if(temp==a_desc[card_num])
			{
				same_card_number++;
				if(same_card_number>=3)
				{
					//System.out.println("triplet of "+a_desc[card_num]);
					if(a_desc[card_num]==0)
					{
						//return 4*300+13;
						return 4*Const+send_me_factors(13,0,0, 0, 0);
					}
					else
					{
						//return 4*300+a_desc[card_num];
						return 4*Const+send_me_factors(a_desc[card_num],0,0, 0, 0);/*tyggh*/
					}					
				}				
			}
			else
			{
				same_card_number=1;
			}
			temp=a_desc[card_num];
		}
		return -1;
   }
   
   public static Card number_suit(int num)
   {
		Card card=new Card();
		int number= num%13;
		int suit=num/13;
		card.number=number;
		card.suit=suit;
		return card;  
   }  
   public static int is_full_house(int[] number_array)
   {
		int same_card_number=1;
		int temp=9999;
		
		int[] a_desc=new int[number_array.length]; 
		for(int i=0;i<number_array.length;i++)
		{
			a_desc[i]=number_array[(number_array.length-i-1)];
		}
		for(int card_num = 0; card_num < 7; card_num++)
		{
			if(temp==a_desc[card_num])
			{
				same_card_number++;
				if(same_card_number>=3)
				{
					
					//check pair-after-triplet
					same_card_number=1;
					temp=9999;
					for(int j=0;j<number_array.length;j++)
					{
						
						if(j!=card_num && j!=card_num-1 && j!=card_num-2)
						{
							
							if(temp==a_desc[j])
							{
								//System.out.println("triplet of "+a_desc[card_num]+"found and double of"+a_desc[j] );
								if(a_desc[card_num]==0)
								{
									if(a_desc[j]==0)
									{
										return 7*Const+13*14+13;
									}
									else
									{
										return 7*Const+13*14+a_desc[j];
									}
								}
								else
								{
									if(a_desc[j]==0)
									{
										return 7*Const+14*a_desc[card_num]+13;
									}
									else
									{
										return 7*Const+14*a_desc[card_num]+a_desc[j];
									}
								
								}
							}
							temp=a_desc[j];
						}
					
					}
					
				}
				
			}
			else
			{
				same_card_number=1;
			}
			temp=a_desc[card_num];
		}
   
		return -1;
   }
   public static int is_flush(Card[] hand_number_suit)
   {
		int temp=9999;
		int same_card_number=1;
		for(int card_num = 0; card_num < 7; card_num++) //checking flush
		{
			if(temp==hand_number_suit[card_num].suit)
			{
				same_card_number++;
				if(same_card_number>=5)
				{
					//checking for highest in flush
					int suit=temp;
					int[] temp_array= {-99,-99,-99,-99,-99,-99,-99};
					for(int card_num1 = 0; card_num1 < 7; card_num1++)
					{
						if(hand_number_suit[card_num1].suit==temp)
						{
							temp_array[card_num1]=hand_number_suit[card_num1].number;
						}
					}
					Arrays.sort(temp_array);
					
					//System.out.println("Flush of" + return_high(a_desc));
					//System.out.println("Flush of" + return_high(a_desc));
					//System.out.println(6*Const+return_high(a_desc));
					return 6*Const+return_high(temp_array);
				}
				
			}
			else
			{
				same_card_number=1;
			}
			
			temp=hand_number_suit[card_num].suit;
		}
   
		return -1;
   }
   public static int is_4_of_kind(int[] number_array)
   {
		int same_card_number=1;
		int temp=9999;
		Arrays.sort(number_array);
		for(int card_num = 0; card_num < 7; card_num++)
		{
			if(temp==number_array[card_num])
			{
				same_card_number++;
				if(same_card_number>=4)
				{		
					//System.out.println("4 of a kind for " +temp);
					if (temp==0)
					{
						return 8*Const+13;
					}
					else
					{
						return 8*Const+temp;
					}
				}
				
			}
			else
			{
				same_card_number=1;
			}
			temp=number_array[card_num];
		}
		return -1;
   
   }
   public static int is_straight_flush(Card[] hand_number_suit)
   {
		//checking royal flush
		int temp=9999;
		int same_card_number=1;
		for(int card_num = 0; card_num < 7; card_num++) //checking flush
		{
			
			if(temp==hand_number_suit[card_num].suit)
			{
				same_card_number++;
				if(same_card_number>=5)
				{
					//checking for royal staraight flush
					int suit=temp;
					int[] temp_array= {99,99,99,99,99,99,99};
					for(int card_num1 = 0; card_num1 < 7; card_num1++)
					{
						if(hand_number_suit[card_num1].suit==temp)
						{
							temp_array[card_num1]=hand_number_suit[card_num1].number;
						}
					}
					//check straight
					if(-1!=is_straight(temp_array))
					{
						//System.out.println("Straight flush from :- "+ is_straight(temp_array));
						return 9*Const+is_straight(temp_array);
					}
					
				}
				
			}
			else
			{
				same_card_number=1;
			}
			
			temp=hand_number_suit[card_num].suit;
		}
   
		return -1;
   }
   public static int is_straight(int[] a)
   {
		Arrays.sort(a);
		int[] a_desc=new int[a.length+1]; 
		a_desc[0]=999;
		int length=1;
		int temp=999;
		for(int i=0;i<a.length;i++)
		{
			a_desc[i+1]=a[(a.length-i-1)];
			if(a_desc[i+1]==0)
			{
				a_desc[0]=13;			
			}
		}
		
		for(int i=0;i<a.length+1;i++)
		{
			//System.out.println(a_desc[i]);
			if(temp-1==a_desc[i])
			{
				
				length++;
				if(length>=5)
				{
					//System.out.println("Straight from :- "+a_desc[i]);
					return Const*5+a_desc[i];
				}
			}
			else if(temp!=a_desc[i])
			{
				//System.out.println("Come");
				length=1;
			}
			temp=a_desc[i];
			
		}
		
		return -1;		
   }
	public static int is_two_ace_pair(int[] number_array)
   {
		
		int same_card_number=1;
		int temp=9999;
		
		int[] a_desc=new int[number_array.length]; 
		for(int i=0;i<number_array.length;i++)
		{
			a_desc[i]=number_array[(number_array.length-i-1)];
		}
		for(int card_num = 0; card_num < 7; card_num++)
		{
			if(a_desc[a_desc.length-1]==0)
			{
				if(a_desc[a_desc.length-2]==0)
				{
					same_card_number=1;
					temp=9999;
					for(int j=0;j<number_array.length;j++)
					{						
						if(j!=card_num && j!=card_num-1 )
						{							
							if(temp==a_desc[j] && temp!=0)
							{
								return 3*Const+send_me_factors(13,temp,0, 0, 0);				
							}
							temp=a_desc[j];
						}					
					}						
				}						
			}		
		}
   
		return -1;
   }
   
   
   
}
