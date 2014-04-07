

class Card implements Comparable<Card>
{
	public int number;
	public int suit;
	

public int compareTo(Card compareCard) 
	{
 
		int compareQuantity = ((Card) compareCard).suit; 
		return this.suit - compareQuantity;
 
	}	
	
}





