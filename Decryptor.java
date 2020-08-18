package encryptor;

//decryptor class
import java.util.Random;

public class Decryptor
{
	//get decrypted string with given string
	public String getDecrypted(String str)
	{
		return decrypt(str);
	}
	
	private String decrypt(String str)
	{
		int size = str.length();
		if(size == 0)return str;
		//encrypted greatest factor should = this one because
		//resized encrypted string size = oldsize*factor
		int factor = greatestFactor(size);
		//prev is always size / factor
		int prev = size/factor;          
		int key = 2*prev - factor; //generate the same key as what was encrypted for the same seed

		//order for cypher
		int[] order = generateCypher(size,key);
		char[] arr = str.toCharArray();
		
		//back track the scramble aglorithm with given arr and order
		String unscrambledStr = unscramble(arr,order);
		
		//undo character changes
		String appended = undoValueChange(unscrambledStr,key);

		String decryptedStr = "";
		//remove appended dead values (should always be at the end)
		for(int i = 0; i < prev; i++)
			decryptedStr = decryptedStr+appended.charAt(i);
		return decryptedStr;

	}
	////////////////////////////////////////////////////////////functions
	
	//reverse scramble function with given order and post-scrambled array
	private String unscramble(char[] arr, int[] order)
	{
		String newStr = "";
		int l = arr.length;
		char[] newArr = new char[l];
		int[] pos = new int[l];//generate array of positions
		int tracker = 0;
		
		//fill position array
		for(int i = 0; i < order.length; i++) 
			if(order[i]==1){				   
				pos[tracker] = i;
				tracker++;
			}

		tracker = 0;
		for(int i = 0; i < l; i++){
			if(newArr[pos[i]%l]==0)newArr[pos[i]%l] = arr[tracker];
			else{
				boolean hold = true;
				int inc = (pos[i]+1)%l;
				while(hold)
				{
					if(newArr[inc]==0){
						newArr[inc] = arr[tracker];
						hold = false;
					}
					inc = (inc+1)%l;
				}
			}
			tracker++;
		}

		for(int i = 0; i < newArr.length; i++)
			newStr = newStr+newArr[i];
		return newStr;
	}
	
	//undo encrypt
	private String undoValueChange(String str, int key)
	{
		Random r = new Random(key);
		int val = 0;
		int sub = 0;
		String newStr = "";
		for(int i  = 0; i < str.length(); i++)
		{
			val = (int)str.charAt(i);
			sub = r.nextInt(6969);
			val = 128 -(sub - 32 - val)%96;
			if(val >=128) val = 32+(val)%128;
			char append = (char)val;
			newStr = newStr+append;
		}
		return newStr;
	}
	

	//generate cypher array with random seed = key.
	//[0 1 0 0 1 1 1 0 1 0 1 0 0 1 1 0 1 0 0 .... 1] (should always end with 1)
	//explanation of how this is used in ReadMe file
	private int[] generateCypher(int length, int key)
	{
		Random r = new Random(key);
		int value = 0;
		int count = 0;
		String cypherStr = "";
		while(count < length)
		{
			value = r.nextInt(2);
			cypherStr = cypherStr+value;
			if(value == 1)count++;
		}
		char[] arr = cypherStr.toCharArray();
		int[] finalCypher = new int[arr.length];
		for(int i = 0; i < arr.length; i++)
			finalCypher[i] = Integer.parseInt(arr[i]+"");
		return finalCypher;

	}

	//greatest factor of n
	private int greatestFactor(int n)
	{
		if(n == 0 || n == 1)return n;
		int factor = n;
		int gf = 2;//greatest factor initially 2

		//remove all factors of 2
		while(factor%2==0)
			factor = factor/2;
		//if factor == 1 greatest factor = 2
		if(factor == 1)return gf;
		//pull out all factors
		for(int i = 3; i <= Math.sqrt(n); i+=2)
		{
			while(factor%i==0){
				factor = factor/i;
				gf = i;
			}
		}
		if(factor > 2)return factor;
		return gf;
	}
}