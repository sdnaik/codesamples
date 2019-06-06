/**
 * 
 */
package mns.service;

/**
 * @author deva
 *
 */
public class UserServiceTest {

	/***************************************************************************************************/
	
	//Tests for creating the user 
	
		//success cases (should return 200; should return a valid user id; should return values as provided in the request)
	
		//failure cases 

			//Parameter validation (should return 400)
				//missing parameters
				//incorrect data types (string instead of number etc)
				//incorrect data length 
	
			//User already exists (should return existing user values)
	

	/***************************************************************************************************/	
	
	
	//Tests for reading the user
	
		//success cases (should return 200; should return user data)
	
		//Parameter validation (should return 400)
			//missing user id
			//incorrect data type (string instead of number)

		//User not found (should return 404)

	
	/***************************************************************************************************/
	
	
	//Tests for updating the user
	
		//success cases (should return 200; should return updated user data)
			//update for each data element
			//update for a single data element
			//update for multiple data elements
	
		//Parameter validation (should return 400)
			//missing user id
			//incorrect data types (string instead of number etc)
			//incorrect data length 

	
		//User not found (should return 404)
	

	/***************************************************************************************************/
	
	//Tests for deleting the user
	
		//success cases (should return 200; should return deleted=true flag and the user id)

		//Parameter validation (should return 400)
			//missing user id
			//incorrect data type (string instead of number)

		//User not found (should return 404)

	
	/***************************************************************************************************/
	
	
	//Tests for searching the makers
	
		//success cases (should return 200; should return a list of users or no users )
	
		//Parameter validation (should return 400)
			//incorrect data types (string instead of number etc)
			//incorrect data length 
	
	
}
