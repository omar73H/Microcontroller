

public class ExecuteStage {
	ALU alu;
	
	public ExecuteStage() {
		alu = new ALU();
	}
	
	public int execute(CPU.ExecutePipelineReg exInfo) {
		String ALUOp = exInfo.ALUOp;
		boolean ALUSrc = exInfo.ALUSrc;
		int ReadData1 = exInfo.readData1;
		int ReadData2 = exInfo.readData2;
		String extendedImmediateVal =exInfo.extendedImmediateVal;
		
		
		
		int signExtVal = -1;
		if(extendedImmediateVal != null)
			signExtVal = DecodeStage.evalBits(extendedImmediateVal, 0, 15);
		
		boolean slt = exInfo.slt;
		boolean branch = exInfo.branch;
		boolean BNE = exInfo.BNE;
		boolean jump = exInfo.jump;
		
		
		
		if(branch) // branch or jump
		{
			int res = alu.ALUEvaluator(ALUOp, ReadData1, ReadData2);
			if(BNE)//Branch not equal
			{
				if(!alu.isZero()) // Branch not equal condition is true
				{
					int newPC = ( exInfo.nextPC + (signExtVal) ) % (1024);
					if(exInfo.nextPC + (signExtVal) < 0)
						newPC = 0;
					exInfo.pc.setPC(newPC);
					return newPC;
				}
			}
			else//Branch Grater than
			{
				if(alu.isPostive()) // Branch greater than condition is true
				{
					int newPC = ( exInfo.nextPC + (signExtVal) ) % (1024);
					if(exInfo.nextPC + (signExtVal) < 0)
						newPC = 0;
					exInfo.pc.setPC(newPC);
					return newPC;
				}
			}
			return -1;
		}
		
		if(jump)
		{
			int newPC = (signExtVal) % (1024);
			if(newPC < 0)
				newPC = 0;
			exInfo.pc.setPC(newPC);
			return newPC;
		}
		
		if(slt)
		{
			int res = alu.ALUEvaluator(ALUOp, ReadData1, ReadData2);
			return (res<0)? 1:0;
		}
		return ALUSrc? alu.ALUEvaluator(ALUOp, ReadData1, ReadData2):alu.ALUEvaluator(ALUOp, ReadData1, signExtVal);
	}
}
