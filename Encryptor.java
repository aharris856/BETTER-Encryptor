
import java.util.Random;

//encryptor class
public class Encryptor
{
	//get encrypted version of string
	public String getEncrypted(String str)
	{
		return encrypt(str);
	}

	private String encrypt(String str)  
	{

		int size = str.length();
		int factor = greatestFactor(size);//greatest prime factor of the size of the input 
		int key = 2*size - factor; //key to use as seeds
		int newSize = size*factor;
		
		//append random dead letters before encrypting and running cypher
		String unScrambledString = str+generateStr(newSize-size, key);
		
		//generate a random array to give the order of the cypher (used in scramble method)
		int[] order = generateCypher(newSize,key);
		
		//change values to random values based off the key
		String enStr = changeValues(unScrambledString,key);
	
		//scramble the encrypted string that has appended random letters with the order array
		String encryptedStr = scramble(enStr.toCharArray(), order);

		return encryptedStr;
	}
///////////////////////////////////////////functions

	//scramble the arr given key
	private String scramble(char[] arr, int[] order)
	{
		String str = "";
		int l = arr.length;
		for(int i = 0; i < order.length; i++)
		{
			if(order[i]==1){
				if(arr[i%l]!=0){
					str = str+arr[i%l];
					arr[i%l]=0;
				}else{
					int tr = i;
					while(true)
					{
						tr++;
						if(arr[tr%l]!=0){
							str = str+arr[tr%l];
							arr[tr%l]=0;
							break;
						}
					}
				}
			}
		}
		return str;
	}
	
	//change values with random shift given a key (EACH SHIFT ON EACH CHAR IS DIFFERENT)
	//random shifts are generated by the key as a seed
	private String changeValues(String str, int key)
	{
		Random r = new Random(key);
		int val = -1;
		int adder = -1;
		String newStr = "";
		for(int i = 0; i < str.length(); i++)
		{
			val = (int)str.charAt(i);
			adder = r.nextInt(6969);// < lol 69
			val = (val + adder)%96;
			val = val + 32;
			char append = (char) val;
			newStr = newStr + append;
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
	//generate random string with size factor and seed key
	private String generateStr(int factor, int key)
	{
		Random r = new Random(key);
	
		String str = "";
		int generate = -1;
		for(int i = 0; i < factor; i++)
		{
			generate = 32+r.nextInt(96);//generate random num between 32 and 128 to use ascii table
			char append = (char) generate;
			str=str+append;
		}
      
		return str;
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
