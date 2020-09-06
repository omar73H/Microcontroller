public class DecodeStage {

	
	// The index of the registers that their data will be read
	public int readRegIdx1=-1 , readRegIdx2=-1;
	
	//the other values extracted from the instruction
	public int writeRegIdx=-1;
	public String extendedImmediateVal;
	
	public DecodeStage() {
		
	}
	
	public void InstDecode(String inst) {
		
		
		String OPCode =inst.substring(0, 2);
		String funct =inst.substring(14, 16);
		
		String immediateVal="";
		
		switch(OPCode)
		{
		case "00"://R-type
			readRegIdx1 = evalBits(inst,2,5); // rs
			readRegIdx2 = evalBits(inst,6,9); // rt
			writeRegIdx = evalBits(inst,10,13); // rd
			
			break;
		case "01"://I-type
			readRegIdx1 = evalBits(inst,2,3); // rs
			readRegIdx2 = -1; // no register2 to read data from as it comes as an immediate value
			writeRegIdx = evalBits(inst,4,5); // rt
			immediateVal = inst.substring(6, 14);// the immediate value
						
			extendedImmediateVal = SignExtend(immediateVal); // extend it to 16-bit
			break;
		
		case "10"://W-type
			
			switch (funct) 
			{
			case "00"://LW
				readRegIdx1 = evalBits(inst,2,3); // rs
				readRegIdx2 = -1; //  no second register to read data from as it is a load word inst
				writeRegIdx = evalBits(inst,4,5); // rt
				immediateVal = inst.substring(6,14);// the offset from the base address
				
				extendedImmediateVal = SignExtend(immediateVal);
				break;
				
			case "11"://SW
				readRegIdx1 = evalBits(inst,2,3); // rs
				readRegIdx2 = evalBits(inst,4,5); // rt
				writeRegIdx = -1 ; // no register to write data in as it is a store word inst
				immediateVal = inst.substring(6,14);// the offset from the base address
				
				extendedImmediateVal = SignExtend(immediateVal);
				
				break;
			}
			break;
		case "11"://C+J type
			switch(funct)
			{
			case"00"://BNE
			case"01"://BGT
				readRegIdx1 = evalBits(inst,2,3); // rs
				readRegIdx2 = evalBits(inst,4,5); // rt
				writeRegIdx = -1 ; // no register to write data in as it is a branch inst
				immediateVal = inst.substring(6,14);// the branch address
				
				extendedImmediateVal = SignExtend(immediateVal);
				
				break;
			case"10"://SLT
				readRegIdx1 = evalBits(inst,2,5); // rs
				readRegIdx2 = evalBits(inst,6,9); // rt
				writeRegIdx = evalBits(inst,10,13); // rd
				
				break;
			case"11"://Jump
				immediateVal = inst.substring(2,14);// the jump address
				
				extendedImmediateVal = SignExtend(immediateVal);
				
				break;
			}
			break;
		}
		
		
	}
	
	
	public static int evalBits(String currInst ,int start , int end) {
		
		boolean negative = false;
		if(start ==0 && end == 15 && currInst.charAt(0)=='1') // negative value of 16-bit
		{
			negative = true;
			String twosComp = "";
			boolean convert = false;
			for(int i=15;i>=0;i--)
			{
				char currChar = currInst.charAt(i);
				if(!convert)
				{
					twosComp = currChar+twosComp;
					if(currChar == '1')
						convert = true;
				}
				else
				{
					if(currChar == '1')
						twosComp = '0'+twosComp;
					else
						twosComp = '1'+twosComp;
				}
			}
			currInst = twosComp;
		}
		
		int power = 0;
		int val =0;
		for(int i=end;i>=start;i--)
		{
			if(currInst.charAt(i) == '1')
			{
				val += (1 << power);
			}
			power++;
		}
		return (negative? val * -1 : val);
	}
	
	public String SignExtend(String immediateVal) {
		StringBuilder sb = new StringBuilder();
		
		if(immediateVal.charAt(0) == '1')
		{
			for(int i=0;i<(16-immediateVal.length());i++)
				sb.append("1");
			for(int i=0;i<immediateVal.length();i++)
				sb.append(immediateVal.charAt(i)+"");
			
			return sb.toString();
		}
		else
		{
			for(int i=0;i<(16-immediateVal.length());i++)
				sb.append("0");
			for(int i=0;i<immediateVal.length();i++)
				sb.append(immediateVal.charAt(i)+"");
			
			return sb.toString();
		}
	}
	
	public static void main(String[] args) {
	}
}
