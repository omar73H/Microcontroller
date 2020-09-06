import java.util.StringTokenizer;
public class Assembler {

	
	public static String convertToMachineCode(String assemblyInst) {
		StringTokenizer st = new StringTokenizer(assemblyInst, ", ()$");
		
		String operation = st.nextToken().toLowerCase();

		switch(operation)
		{
		case"add":
				String opCode = "00";String funct="00";
				String rd = convertToBinary(st.nextToken(),4);
				String rs = convertToBinary(st.nextToken(),4);
				String rt = convertToBinary(st.nextToken(),4);
				
				return opCode+rs+rt+rd+funct;
		case"sub":
				opCode = "00";funct="01";
				rd = convertToBinary(st.nextToken(),4);
				rs = convertToBinary(st.nextToken(),4);
				rt = convertToBinary(st.nextToken(),4);
			
				return opCode+rs+rt+rd+funct;
		case"mult":
				opCode = "00";funct="10";
				rd = convertToBinary(st.nextToken(),4);
				rs = convertToBinary(st.nextToken(),4);
				rt = convertToBinary(st.nextToken(),4);
		
				return opCode+rs+rt+rd+funct;
		case"and":
				opCode = "00";funct="11";
				rd = convertToBinary(st.nextToken(),4);
				rs = convertToBinary(st.nextToken(),4);
				rt = convertToBinary(st.nextToken(),4);
		
				return opCode+rs+rt+rd+funct;
		case"addi":
				opCode = "01";funct="00";
				rt = convertToBinary(st.nextToken(),2);
				rs = convertToBinary(st.nextToken(),2);
				String immediate = convertToBinary(st.nextToken(),8);
	
				return opCode+rs+rt+immediate+funct;
		case"ori":
				opCode = "01";funct="01";
				rt = convertToBinary(st.nextToken(),2);
				rs = convertToBinary(st.nextToken(),2);
				immediate = convertToBinary(st.nextToken(),8);

				return opCode+rs+rt+immediate+funct;
		case"sll":
				opCode = "01";funct="10";
				rt = convertToBinary(st.nextToken(),2);
				rs = convertToBinary(st.nextToken(),2);
				immediate = convertToBinary(st.nextToken(),8);

				return opCode+rs+rt+immediate+funct;
		case"srl":
				opCode = "01";funct="11";
				rt = convertToBinary(st.nextToken(),2);
				rs = convertToBinary(st.nextToken(),2);
				immediate = convertToBinary(st.nextToken(),8);

				return opCode+rs+rt+immediate+funct;
		case"lw":
				opCode = "10";funct="00";
				rt = convertToBinary(st.nextToken(),2);
				immediate = convertToBinary(st.nextToken(),8);
				rs = convertToBinary(st.nextToken(),2);

				return opCode+rs+rt+immediate+funct;
		case"sw":
				opCode = "10";funct="11";
				rt = convertToBinary(st.nextToken(),2);
				immediate = convertToBinary(st.nextToken(),8);
				rs = convertToBinary(st.nextToken(),2);

				return opCode+rs+rt+immediate+funct;
		case"bne":
				opCode = "11";funct="00";
				rt = convertToBinary(st.nextToken(),2);
				rs = convertToBinary(st.nextToken(),2);
				immediate = convertToBinary(st.nextToken(),8);

				return opCode+rs+rt+immediate+funct;
		case"bgt":
				opCode = "11";funct="01";
				rt = convertToBinary(st.nextToken(),2);
				rs = convertToBinary(st.nextToken(),2);
				immediate = convertToBinary(st.nextToken(),8);

				return opCode+rs+rt+immediate+funct;
		case"slt":
				opCode = "11";funct="10";
				rd = convertToBinary(st.nextToken(),4);
				rs = convertToBinary(st.nextToken(),4);
				rt = convertToBinary(st.nextToken(),4);

				return opCode+rs+rt+rd+funct;
		case"jump":
				opCode = "11";funct="11";
				immediate = convertToBinary(st.nextToken(),12);

				return opCode+immediate+funct;
		default:
			System.out.println("Invalid Operation in Assembly -->"+operation);
			return null;
		}
	}
	
	public static String convertToBinary(String strData,int length) {
		int data = Integer.parseInt(strData);
		
		String ans ="";
		for(int i=0;i<length;i++)
		{
			if( (data & (1<<i)) == 0)
			{
				ans = "0"+ ans;
			}
			else
			{
				ans = "1"+ans;
			}
		}
		return ans;
	}
	
	
	public static void main(String[] args) {
		System.out.println(convertToMachineCode("Add $4, $2, $3"));
	}
}
