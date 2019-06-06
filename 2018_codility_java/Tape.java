/*

A non-empty zero-indexed array A consisting of N integers is given. Array A represents numbers on a tape.

Any integer P, such that 0 < P < N, splits this tape into two non-empty parts: A[0], A[1], ..., A[P − 1] and A[P], A[P + 1], ..., A[N − 1].

The difference between the two parts is the value of: |(A[0] + A[1] + ... + A[P − 1]) − (A[P] + A[P + 1] + ... + A[N − 1])|

In other words, it is the absolute difference between the sum of the first part and the sum of the second part.

For example, consider array A such that:

  A[0] = 3
  A[1] = 1
  A[2] = 2
  A[3] = 4
  A[4] = 3
We can split this tape in four places:

P = 1, difference = |3 − 10| = 7 
P = 2, difference = |4 − 9| = 5 
P = 3, difference = |6 − 7| = 1 
P = 4, difference = |10 − 3| = 7 
Write a function:

class Solution { public int solution(int[] A); }

that, given a non-empty zero-indexed array A of N integers, returns the minimal difference that can be achieved.

For example, given:

  A[0] = 3
  A[1] = 1
  A[2] = 2
  A[3] = 4
  A[4] = 3
the function should return 1, as explained above.

Assume that:

N is an integer within the range [2..100,000];
each element of array A is an integer within the range [−1,000..1,000].
Complexity:

expected worst-case time complexity is O(N);
expected worst-case space complexity is O(N), beyond input storage (not counting the storage required for input arguments).
Copyright 2009–2018 by Codility Limited. All Rights Reserved. Unauthorized copying, publication or disclosure prohibited.


*/

package codility;

public class Tape {
	
	public int solution(int[] A) {
				
		if(A!=null && A.length == 2){
			return Math.abs(A[0] - A[1]);
		} 

		int total = 0;
		for (int i=0; i< A.length; i++) {
			total = total + A[i];
		}
		
		//System.out.println("total = " + total);
		
		int leftSum = A[0];
		int rightSum = total - A[0];
		int prevDiff = Math.abs(rightSum - leftSum);
		int newDiff = prevDiff;
		int leastDiff = newDiff;	
			
			/*
			System.out.println("leftSum = " + leftSum);
			System.out.println("rightSum = " + rightSum);
			System.out.println("prevDiff = " + prevDiff);
			System.out.println("newDiff = " + newDiff);
			System.out.println("leastDiff = " + leastDiff);
			System.out.println("_______________________________");
			*/
			
		if(A!= null && A.length > 1) {
			
			for (int i=1; i < A.length - 1; i++) {
				leftSum = leftSum + A[i];
				rightSum = total - leftSum;
				newDiff = Math.abs(rightSum - leftSum);
				
				/*
				System.out.println("leftSum = " + leftSum);
				System.out.println("rightSum = " + rightSum);
				System.out.println("prevDiff = " + prevDiff);
				System.out.println("newDiff = " + newDiff);
				*/
				
				if(newDiff < leastDiff) {
					leastDiff = newDiff;
					//System.out.println("leastDiff = " + leastDiff);
					//System.out.println("i = " + i);
				}
				newDiff = prevDiff;
							
			}
			
		}
		
		System.out.println("final leastDiff = " + leastDiff);
		return leastDiff;
	}
	
	
	public static void main (String[] args) {
		
		//int[] A = {3, 1, 2, 4, 3};
		int[] A = {-10, -5, -3, -4, -5};
		Tape tape = new Tape();
		tape.solution(A);
		
	}
	

}

