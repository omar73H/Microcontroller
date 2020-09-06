
public class ControlUnit {
	String ALUOP;
	boolean registDest;
	boolean aluSrc;
	boolean regWrite;
	boolean memRead;
	boolean memWrite;
	boolean branch;
	boolean BNE;
	boolean memToReg;
	boolean shortReg;
	boolean jump;
	boolean slt;
	public ControlUnit(String instruction) {
		// Switch to set values
		String op = instruction.substring(0, 2);
		String func = instruction.substring(14, 16);

		// if r type
		if (op.equals("00")) 
		{
			// To set ALUOP
			switch (func) 
			{
			//ADD
			case "00":
				ALUOP = "000";
				break;
			//SUB
			case "01":
				ALUOP = "001";
				break;
			//MUL
			case "10":
				ALUOP = "010";
				break;
			//AND
			case "11":
				ALUOP = "100";
				break;
			}
			registDest = true;
			aluSrc = true;
			regWrite = true;
			
			return;
		}

		// if I type
		if (op.equals("01")) 
		{
			//To set ALUOP
			switch (func) {
			//AdDI
			case "00":
				ALUOP = "000";
				break;
			//ORI
			case "01":
				ALUOP = "011";
				break;
			//SLL
			case "10":
				ALUOP = "110";
				break;
			//SRL
			case "11":
				ALUOP = "101";
				break;
			}
			regWrite = true;
			shortReg = true;
			
			return;
		}

		// if w type
		if (op.equals("10")) {
			ALUOP = "000";
			shortReg = true;
			if (func.equals("00")) 
			{
				regWrite = true;
				memRead = true;
				memToReg = true;
			} 	
			else if(func.equals("11")) 
			{
				memWrite = true;
			}
			
			return;
		}

		// if c+j type
		if (op.equals("11")) 
		{
			ALUOP = "001";
			switch (func) {
			//BNE | BGT
			case "00":
				BNE = true;
				aluSrc = true;
				branch = true;
				shortReg = true;
				break;
			case "01":
				BNE = false;
				aluSrc = true;
				branch = true;
				shortReg = true;
				break;
			//SLT
			case "10":
				registDest = true;
				aluSrc = true;
				regWrite = true;
				slt  = true;
				break;
			//JUMP
			case "11":
				jump = true;
				ALUOP = "000"; // DONOT CARE
				break;
			}

		}
	}


	public static void main(String[] args) {
//		ControlUnit cu = new ControlUnit("1100000000000011");
//		System.out.println("hello world");
//		System.out.println();
	}

}
