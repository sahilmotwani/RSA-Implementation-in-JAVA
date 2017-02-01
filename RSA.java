package code;
import java.util.*;
import java.math.*;
import java.io.IOException;


/**
 * @author sahil
 * 
 * 	Step 1: Select two large prime numbers p and q.
	Step 2: Calculate n = p*q
	Step 3: Calculate Euler's totient function �(n) = (p � 1)*(q � 1)
	Step 4: Find e such that it is relatively prime with �(n), i.e. gcd(e, �(n)) = 1 ; 1 < e < �(n)
	Step 5: Calculate d such that e.d = 1 (mod �(n)) i.e. multiplicative inverse of e.(mod �(n))

	Public Key is (n, e) and Private Key is (d, n).

	Encryption: Ciphertext = (Plaintext ^ e) mod n
	Decryption: Plaintext  = (Ciphertext ^ d) mod n

	We will do all these operations using the BigInteger class in Java
 *
 */

public class RSA
{
	private BigInteger p, q;
	private BigInteger n;
	private BigInteger phiN;
	private int bitLength = 32;
	private BigInteger e, d;

	public RSA(int choice)
	{
		generateKey(choice);
	}

	private void generateKey(int choice)
	{

		// Step 1 . Randomly generate two BigInteger prime numbers p , q with bitlength 512 using the BigInteger class
		// The below constructor will generate a prime number with certainty 15 
		p = new BigInteger(bitLength, 15, new Random());
		q = new BigInteger(bitLength, 15, new Random());

		/* Step 2: Calculate n = p.q */
		n = p.multiply(q);
		
		printPrimes(p,q,n);
		// Step 3. Calculate �(n) = (p-1)*(q-1)
		phiN = p.subtract(BigInteger.valueOf(1));
		phiN = phiN.multiply( q.subtract( BigInteger.valueOf(1)));

		//Step 4 . Find e such that gcd(e ,�(n)) = 1 and e < �(n)
		//this while loop will run until gcd of e and �(n) is not 1

		switch(choice){
		case 1:
		do
		{
			e = new BigInteger(2*bitLength, new Random());

		}
		while( (e.compareTo(phiN) != 1) || (e.gcd(phiN).compareTo(BigInteger.valueOf(1)) != 0));
		break;
		case 2:
			e= 	getPublicKey();
			break;
		default:
			e= getPublicKey();
			break;
		}
		/* Step 5: Calculate d such that e.d = 1 (mod �(n)) */
		d = e.modInverse(phiN);

	}


	private void printPrimes(BigInteger p, BigInteger q, BigInteger n) {
		System.out.println("value of p is: "+p.toString());
		System.out.println("value of q is: "+q.toString());
		System.out.println("value of n is: "+n.toString());
	}

	public BigInteger getPublicKey(){
		Scanner scan = new Scanner(System.in);
		do
		{
			System.out.println("Enter a large prime number");
			e =scan.nextBigInteger();
		}
		while( (e.compareTo(phiN) != 1) || (e.gcd(phiN).compareTo(BigInteger.valueOf(1)) != 0));
		
		return e;
	}
	
	
	public BigInteger encrypt(BigInteger plaintext)
	{
		return plaintext.modPow(e, n);
	}

	public BigInteger decrypt(BigInteger ciphertext)
	{
		return ciphertext.modPow(d, n);
	}

	public static void main(String[] args) throws IOException
	{

		String plainText;
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the text to encrypt : ");
		plainText = scan.nextLine();

		System.out.println("Press 1 to automatically generate public key or press 2 to enter your own public key");
		System.out.println("If entering your own key, make it significantly larger for eg. 1280367512230007307189");
		int choice =scan.nextInt();
		
		if(choice != 1 && choice != 2){
			System.out.println("Invalid argument.");
			System.exit(0);
		}
		RSA rsaTest = new RSA(choice);
		
		BigInteger bigPlainText, bigCipherText;

		//Generate byte array from string and create big integer for it
		bigPlainText = new BigInteger(plainText.getBytes("us-ascii"));

		//encrypt the bigInteger value
		bigCipherText = rsaTest.encrypt(bigPlainText);

		System.out.println("Your text is : " +  plainText);

		System.out.println("The generated Cipher text is : " +  bigCipherText.toString());

		//decrypt the cipher text and get back the original big integer value
		bigPlainText = rsaTest.decrypt(bigCipherText);

		//recover the original string from the big integer value and print
		System.out.println("After Decryption Plaintext : " +  new String(bigPlainText.toByteArray()));
	}
}