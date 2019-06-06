package codility;

public class SMS {
	
	/* main idea: 
	Split the string into words; iterate through all words and create SMS messages based on the given conditions
	Time complexity O(n) when n is the length of the string
	*/
	
	/*
	 Assumption: Only upper and lower case Engilsh letters and spaces are provided in the text and no special characters. Hence regular UTF-8 / ASCII charset should be sufficient 
	 */
	
	public int solution(String S, int K) {
		
		//initialize number of sms messages and and length of each message in question
		int numOfSMS = 0;
		int smsLength = 0;
		
		//validate inputs
		if (S== null || S.length() == 0) {
			return numOfSMS;
		}
		
		if(S!= null && S.length() <= K){
			return 1;
		}
		
		//Create a string array of words
		String[] words = S.split(" ");
		
		//Use String builder instead of String to save on space complexity
		StringBuilder smsText = null;
		
		//initialize the length of the current word for the loop
		int currentWordLength = 0;
		
		//iterate through the words and check on conditions to arrive at number of sms messages
		//uncomment the print lines to see teh actual sms message
		for (int i=0; i<words.length; i++) {
			currentWordLength = words[i].length();
			if(currentWordLength > 12){
				return -1;
			}
			if(smsText == null) {
				if(currentWordLength <= K ){					
					smsText = 	new StringBuilder(words[i]);				
					smsLength = currentWordLength;
				} 
				if(i == words.length - 1) {
					//System.out.println("SMS: " + smsText);
					numOfSMS ++;
				}
			} else {
				if(smsLength + 1 + currentWordLength <= K ) {
					smsText = smsText.append(" ").append(words[i]);
					smsLength = smsLength + 1 + currentWordLength;
				} else {
					//System.out.println("SMS: " + smsText);
					smsText = null;
					i = i - 1;
					numOfSMS ++;					
				}
			}
		}
		
		return numOfSMS;
		
	}
	
	
	public static void main (String[] args) {
		
		SMS sms = new SMS();
		int K = 12;
		
		/* test case given in the example */
		String S = "SMS messages are really short";
		
		/* null verfication */
		//String S = "";
		
		/* small size text that can fit into a single sms */
		//String S = "12345678901";
		
		/* exact size text */
		//String S = "123456789012";
		
		/* text a little over sms size */
		//String S = "12345678901 2";
		
		/* another regular test case */
		//String S = "1 2 3 4 5 6 7 8 9 10 11 12";
		
		/*long message */
		//String S = "This is a really long message. Let's see if you can get it. ZZZZZZZZZZZ ZZZZZZZZZZZ ZZZZZZZZZZZ ZZZZZZZZZZZ ZZZZZZZZZZZ ZZZZZZZZZZZ ZZZZZZZZZZZ ZZZZZZZZZZZ";
		
		/* message with a long word that can't be sent */
		//String S = "ThisMessageCanNotBeSentWithCurrentSMSTechnology";
		
		System.out.println(sms.solution(S, K));
		
	}

}
                                              