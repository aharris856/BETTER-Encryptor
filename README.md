# BETTER-Encryptor
So I made this encryption algorithm a lot better,
more complex in my opinion and faster as well.
before I go into detail on the algorithm lemme just go over basic function calls 
on the seperated class files 'Encryptor.java' and 'Decryptor.java'
you can use the GUI i provided and not have to create a run file but to do so here are a few examples of valid calls:

      public class EncryptionTester{
        public static void main(String[]args)
        {
          //if you saw my previous less well made encryptor this is a big improvement
          //only need one of each object rather than having to make a new encryptor for each call to the encryption
          Encryptor e = new Encryptor();
          Decryptor d = new Decryptor(); 

          String str = "String to encrypt";
          String encryptedStr = e.getEncrypted(str);
          System.out.println(str+" Encrypted = \n"); 
          System.out.println( encryptedStr ); //should generate long line of random text

          String decryptedStr = d.getDecrypted( (encryptedStr) );
          System.out.println("Decrypted of encrypted String = \n");
          System.out.println( decryptedStr ); //should output "String to encrypt"

          //other valid ways to call on methods
          System.out.println( (new Encryptor()).getEncrypted("encrypt me") );
          System.out.println( (new Decryptor()).getDecrypted(encryptedStr) );

          System.out.println( (new Decryptor()).getDecrypted( (new Encryptor()).getEncrypted("Lol why do this though?") ) );

        }
      }

SO now that thats out of the way, how the algorithm works:

Encrypt:
My goal was to create an encryption algorithm that, yes not as secure as having a key not displayed in 
a file where you choose a key and told the receiver said key, to challenge myself I wanted to see
if I could come up with a well made algorithm that encrypts and uses something from the file itself
as the key without ever printing it out like my previous encryption repository did.
You'll first start with an input string to encrypt str.
you get the size of the string to encrypt. 
i then calculate the greatest prime factor of the size and call that factor. 
I then use these 2 values as the key and set key to 'key = 2*size - factor'
and to make it somewhat more secure I resize the output to be 'size*factor'
to do this I have to generate a set of random chars ( [2*factor - size] generated).
these chars are randomly generated from a seeded random with seed = key.
after doing so I append these chars to the end of the original string. After doing so I change the values 
of this new long string that has the original string + a bunch of stuff randomly added to the end. by using
another key seeded random generator to add onto the ascii value (each char has a different randomly generated 
number not just one number the entire file). Once we have the almost encrypted file we generate an array of type int[].
however the size of this array is initially unknown. inside this method we generate with seeded Random r = new Random(key);
and then run a loop to generate a string that each time the loop runs through r.nextInt(2) is called and is appended onto
the initially empty string. if the generated int is = 1 we add one to our counter to track how many 1s there are.
once count = length of the new string (size*factor) we then put the string of 0s and 1s (looks kinda like 011010111)
into an array of int[] and return this.
how they cypher works:

as we run through the array that I called order[] which contains the 0s and 1s that was returned from the cypher, 
each 0 corresponds to just doing nothing but going up the string. if it is a 1 we append it to the newer string.

        Visual Example:
          say,
          order = { 0 , 1 , 0 , 1 , 1 , 1 , 0 , 0 , 1 , 0 , 0 , 0 , 0 , 1 , 0 , 1};
          newString = "abcdefg";
if we attach to each of the order values in order the char corresponding to that modulos the size of the new string
without repeating (i.e. once a letter is attached to a 1 it can no longer be used) the order they are attached is the 
outcome of the cypher that we called after encrypting the letters as well. 

          example from the visual:
          abcdefg
          0 1 0 1 1 1 0 0 1 0 0 0 0 1 0 1

          0a 1b 0c 1d 1e 1f 0g 0a 1c 0g 0a 0g 0a 1g 0a 1a
             v     v  v  v        v              v     v
             b     d  e  f        c              g     a
          thus, bdefcga.

Once we have the randomly per letter shifted plus cyphered text we return that as the encrypted text.

Decryption is the same thing but in reverse including the order if you undo the character change
before reversing the cypher it will generate gibberish because the character change is random per letter
but since it is seeded you can get the same generator once the seed (key) is calculated at the beginning.

ONE THING TO NOTE if you use my runEncryption file that has the GUI built in it. if you try to encrypt text and not a file
then decrypt it back. I have not given a case value for ascii value = 10 (line break) so that will show up as something random.
maybe a j or ' or something because of that. However files should be encrypted fine as i use a buffered reader and a print writer to
read and write into files and encrypt line by line not all at once.

