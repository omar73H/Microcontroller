
public class WriteBackStage {

	RegisterFile rf;
	
	public WriteBackStage(RegisterFile rf) {
		this.rf = rf;
	}
	
	public void writeBack(CPU.WriteBackPipelineReg wbInfo) {
		boolean MemToReg = wbInfo.MemToReg;
		
		
		int destination = wbInfo.writeRegIdx;
		
		int memoryData = -1;
		if(wbInfo.memData != null)
			memoryData = DecodeStage.evalBits(wbInfo.memData, 0,15);
		int ALUresult = wbInfo.ALUres;
		if(destination != -1)
			rf.writeReg(destination, MemToReg? memoryData : ALUresult);
	}
}
