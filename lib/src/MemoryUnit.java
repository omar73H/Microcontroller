
public class MemoryUnit {
	private String[] memory;
	
	
	public MemoryUnit() {
		this.memory = new String[1<<11]; // size is (2048) --> 2^11
		// The first 1024 is for instruction and the last 1024 is for data
	}
	
	public void LoadProgram(String[] program) {
	     int i = 0;
	     for(i=0;i<Math.min(1024,program.length);i++)
	            memory[i]=program[i];
	     for(int j = i;j<memory.length;j++)
	            memory[j] = "0000000000000000";
	    }

	
	
	public String loadInstAt(int addr) {
		return memory[addr%1024];//first half
	}
	public String loadDataAt(int addr) {
		return memory[1024+(addr%1024)];//second half
	}

	public void storeDataAt(int addr, String data) {
		if(data.length()!=16)
			System.out.println("INVALID DATA -->"+data+"\nData length should be 16-bit");
		else
			memory[1024+(addr%1024)] = data;
	}
	
//	
//	public void MemAccess(StageInfo S,String ALUresult,String Readdata2, boolean branchflag,PCCounter PC)
//	{
//		if(S.controlUnit.memRead==true)
//		{
//			Readdata= memory[Integer.parseInt(ALUresult,2)/4];
//			System.out.println("WORD LOADED: "+Readdata +" FROM LOCATION "+Integer.parseInt(ALUresult,2)/4+" IN MEMORY" );
//
//		}
//		if(S.controlUnit.memWrite==true)
//		{
//			memory[Integer.parseInt(ALUresult,2)/4]=Readdata2;
//			Readdata= "00000000000000000000000000000000";
//			System.out.println("WORD STORED: "+Readdata2 +" AT LOCATION "+Integer.parseInt(ALUresult,2)/4+" IN MEMORY" );
//		}
//		if(branchflag==true)
//		{
//			if(S.controlUnit.branch== true)
//			{
//				PC.branch(S.immediate);
//			}
//		}
//		
//		
//		if(S.controlUnit.jump==true)
//		{
//			
//			
//				PC.jump(S.immediate);
//			
//		}
//	}
}
