import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

public class Driver {

	static CacheSlot[] Cache = new CacheSlot[16];
	static short[] MainMem = new short[2048];
	static String[] testCases = new String[46];
	static int hitCount = 0;
	static int missCount = 0;
	static Scanner kb = new Scanner(System.in);
	static int testCaseCount= 0;

	public static void main(String[] args) throws FileNotFoundException {
		setMem(); // Initialize Memory
		zeroCache(); // Initialize Cache 
		testCases(); // Load test cases from file

		boolean run = true;
		while (run == true){
			System.out.println("(R)ead (W)rite (D)isplay (Q)uit (x) Run test case. \n"
					+ "Given instructions Run: " + testCaseCount + ". " + (46-testCaseCount) + " remaining.");
			String userChoice = kb.nextLine().toLowerCase();
			if (userChoice.matches("x")) {
				System.out.println("Loaded " + testCases[testCaseCount]);
				userChoice = testCases[testCaseCount];
				testCaseCount++;
			}

			switch (userChoice.toLowerCase()){
			case ("r"):
				readByte();
				break;
			case ("w"):;
				writeData();
				break;
			case ("d"):
				showCache();
				break;
			case ("q"):
				System.out.println("Goodbye");
			System.exit(0);
			default:
				System.out.println("Invalid input");
				run = true;
			}
		}
	}

	private static void readByte(){
		int slot = 0;
		int tag = 0;
		int offset=0;
		String HexAddressFull;

		System.out.println("Enter address");
		HexAddressFull = kb.nextLine();
		if (HexAddressFull.matches("x")) {
			System.out.println("Loaded " + testCases[testCaseCount]);
			HexAddressFull = testCases[testCaseCount];
			testCaseCount++;
		}

		if (HexAddressFull.length() == 1){
			slot = 0;
			tag = 0;
			offset = Integer.decode(HexAddressFull);
		}

		else if (HexAddressFull.length() == 2){
			tag = 0;
			String slotTemp = "0x" + HexAddressFull.substring(0,1);
			slot = Integer.decode(slotTemp);
			String OffsetTemp = "0x" + HexAddressFull.substring(1,2);
			offset = Integer.decode(OffsetTemp);
		}
		else if (HexAddressFull.length() == 3){
			String tagTemp = "0x" + HexAddressFull.substring(0,1);
			tag = Integer.decode(tagTemp);
			String slotTemp = "0x" + HexAddressFull.substring(1,2);
			slot = Integer.decode(slotTemp);
			String OffsetTemp = "0x" + HexAddressFull.substring(2,3);
			offset = Integer.decode(OffsetTemp);
		};

		if (Cache[slot].tag != tag || Cache[slot].valid == 0 ){ // GET MEMORY AND MISS ALGO
			missCount ++;
			System.out.println("Miss. Total: " + missCount);

			String beginFetchMemAddress = "0x" + Integer.toHexString(tag) + Integer.toHexString(slot) + "0";
			int fetchMemAddress = Integer.decode(beginFetchMemAddress);
			/// Fetch Main mem		

			Cache[slot].tag= (byte) tag;
			Cache[slot].valid=1;

			for (int i = 0; i< 16; i++){
				Cache[slot].data[i] = (byte) (MainMem[fetchMemAddress] & 0xff);
				fetchMemAddress ++;
			}	
		}
		else if (Cache[slot].tag == tag && Cache[slot].valid == 1){ // CACHE HIT
			hitCount++;
			System.out.println("Hit. Total: " + hitCount);
		}
		showCache();
	}

	private static void readByte(String HexAddressFull, byte valueToMem){
		int slot = 0;
		int tag = 0;
		int offset=0;

		if (HexAddressFull.length() == 1){
			slot = 0;
			tag = 0;
			offset = Integer.decode(HexAddressFull);
		}

		else if (HexAddressFull.length() == 2){
			tag = 0;
			String slotTemp = "0x" + HexAddressFull.substring(0,1);
			slot = Integer.decode(slotTemp);
			String OffsetTemp = "0x" + HexAddressFull.substring(1,2);
			offset = Integer.decode(OffsetTemp);
		}
		else if (HexAddressFull.length() == 3){
			String tagTemp = "0x" + HexAddressFull.substring(0,1);
			tag = Integer.decode(tagTemp);
			String slotTemp = "0x" + HexAddressFull.substring(1,2);
			slot = Integer.decode(slotTemp);
			String OffsetTemp = "0x" + HexAddressFull.substring(2,3);
			offset = Integer.decode(OffsetTemp);
		};

		String beginFetchMemAddress = "0x" + Integer.toHexString(tag) + Integer.toHexString(slot) + "0";
		int fetchMemAddress = Integer.decode(beginFetchMemAddress);

		if (Cache[slot].tag != tag || Cache[slot].valid == 0 ){ // GET MEMORY AND MISS ALGO
			missCount ++;
			System.out.println("Miss. Total: " + missCount);

			/// Fetch Main mem		

			Cache[slot].tag= (byte) tag;
			Cache[slot].valid=1;

			for (int i = 0; i< 16; i++){

				Cache[slot].data[i] = (byte) (MainMem[fetchMemAddress] & 0xff);
				fetchMemAddress ++;
			}	
		}
		else if (Cache[slot].tag == tag && Cache[slot].valid == 1){ // CACHE HIT
			hitCount++;
			System.out.println("Hit. Total: " + hitCount);
		}

		// Place into Cache and write back back to mem
		// WRITE BACK - STYLE 

		Cache[slot].data[offset] = valueToMem;
		MainMem[fetchMemAddress] = valueToMem;		
		showCache();
	}

	private static void writeData(){
		String address;
		int inVal;
		byte valtoByte;

		System.out.println("Address: ");
		address = kb.nextLine();
		if (address.matches("x")) {
			System.out.println("Loaded " + testCases[testCaseCount]);
			address = testCases[testCaseCount];
			testCaseCount++;
		}

		// HERE have address and offset

		System.out.println("Value: ");
		String valueAsString = "0x" + kb.nextLine();
		if (valueAsString.matches("0xx")) {
			System.out.println("Loaded " + testCases[testCaseCount]);
			valueAsString = "0x" + testCases[testCaseCount];
			testCaseCount++;
		}

		inVal = Integer.decode(valueAsString);
		valtoByte = (byte) (inVal & 0xff);

		readByte(address, valtoByte);
	}

	private static void setMem(){
		int counter = 0;
		while (counter < 2048){
			for (int i =0; i < 256 ; i++){
				MainMem[counter] = (short) i;
				counter++;
			}
		}
	}

	private static void testCases() throws FileNotFoundException{
		FileInputStream file = new FileInputStream("Commands");
		BufferedReader readFile = new BufferedReader(new InputStreamReader(file));
		String line;
		int index = 0;
		try {
			while ((line = readFile.readLine()) != null) {
				testCases[index] = line;
				index++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void zeroCache(){
		for (int i = 0; i < 16; i++){	
			Cache[i] = new CacheSlot();
			Cache[i].slot = (byte) i;
			for (int k = 0; k < 16; k++){
				Cache[i].data[k] = 0;
			}
		}
	}

	private static void showCache(){
		System.out.println("Slot ~ Valid ~ Tag |                  Data");
		for (int i = 0; i < 16; i++){
			System.out.print("  " + Integer.toHexString(Cache[i].slot).toUpperCase() + "\t " + Cache[i].valid+ "      " + Cache[i].tag + "  |  ");
			for (int k = 0; k < 16; k++){

				if (Cache[i].data[k] < 0){
					int tempO = Cache[i].data[k] & 0xff;
					System.out.print(Integer.toHexString(tempO).toUpperCase() + " ");
					if (Integer.toHexString(Cache[i].data[k]).length() <2 ) System.out.print(" ");
				}
				else{
					System.out.print(Integer.toHexString(Cache[i].data[k]).toUpperCase() + " ");
					if (Integer.toHexString(Cache[i].data[k]).length() < 2 ) System.out.print(" ");
				}
			}
			System.out.println();
		}
	}
}
