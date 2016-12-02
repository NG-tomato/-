import java.util.Arrays; 

public class hairetu{
	static int[] a4;
	public static void main(String args[]){
    	int[] a1 = {33, 66, 99};
    	int[] a2 = new int[a1.length];
		int[] a3 = a1;
		a4 = new int[a1.length];
		
		a2 = Arrays.copyOf(a1, a1.length);
		System.arraycopy(a1, 0, a4, 0, a1.length);
		
		a1[1] = 44;  // a2‚Ì—v‘f1‚Ì’l‚ğ‘‚«Š·‚¦
		
		System.out.println("a1: "+a1[0]+" "+a1[1]+" "+a1[2]);
		System.out.println("a2: "+a2[0]+" "+a2[1]+" "+a2[2]);
		System.out.println("a3: "+a3[0]+" "+a3[1]+" "+a3[2]);
		System.out.println("a4: "+a4[0]+" "+a4[1]+" "+a4[2]);



    }
}