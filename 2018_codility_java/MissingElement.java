/*

A zero-indexed array A consisting of N different integers is given. The array contains integers in the range [1..(N + 1)], which means that exactly one element is missing.

Your goal is to find that missing element.

Write a function:

class Solution { public int solution(int[] A); }

that, given a zero-indexed array A, returns the value of the missing element.

For example, given array A such that:

  A[0] = 2
  A[1] = 3
  A[2] = 1
  A[3] = 5
the function should return 4, as it is the missing element.

Assume that:

N is an integer within the range [0..100,000];
the elements of A are all distinct;
each element of array A is an integer within the range [1..(N + 1)].
Complexity:

expected worst-case time complexity is O(N);
expected worst-case space complexity is O(1), beyond input storage (not counting the storage required for input arguments).
Copyright 2009–2018 by Codility Limited. All Rights Reserved. Unauthorized copying, publication or disclosure prohibited.

*/

package codility;

import java.util.Arrays;

public class MissingElement {

	public int solution(int[] A) {
		
		int missingNumber = 1;
		boolean missingFound = false;
		
		if(A!= null && A.length > 0) {
			if(A.length == 1) {
				if(A[0] == 1) missingNumber = 2; 
				else missingNumber = A[0] - 1;
			} else if (A.length > 1) {
				Arrays.sort(A);
				for (int i=0; i<A.length - 1; i++){
					if(Integer.toString(A[i]) != null) {
						if(A[i] == A[i+1]){
							continue;
						}
						else if ((A[i] + 1) != A[i + 1]){
							missingNumber = A[i] + 1;
							missingFound = true;
							break;
						}						
					}
				}
				if(!missingFound) {
					if(A[0] != 1) missingNumber = 1;
					else missingNumber = A[A.length -1] + 1;
				}
			}	
		}
		
		return missingNumber;
		
	}
	
	
	public static void main (String[] args) {
	
		int[] A = {1, 2, 3, 4};
		MissingElement me = new MissingElement();
		System.out.println(me.solution(A));
		
	}
	
	
}
