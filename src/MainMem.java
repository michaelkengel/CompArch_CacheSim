import java.util.Arrays;

public class MainMem {

	public static short[] MM = new short[2048];

	public void Init(){
		for (short i = 0; i<2048;i++){
			
			MM[i] = (short) i;	
			System.out.println(MM[i]);
		}
	}
	
	public String getAddressValueHex(int address){
		return Integer.toHexString(MM[address]);
	}
	
	public void setAddress(int address, byte valueToAdd){
		MM[address] = valueToAdd;
	}
}
