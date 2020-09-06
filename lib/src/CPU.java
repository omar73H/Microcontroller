import java.util.Arrays;
import java.util.Scanner;


public class CPU {
	
	private FetchStage FETCH;
	private DecodeStage DECODE;
	private ExecuteStage EXECUTE;
	private MemoryAccStage MEMACC;
	private WriteBackStage WB;
	
	
	private MemoryUnit memUnit;
	private PC pc;
	private RegisterFile rf;
	
	public CPU() {
		memUnit = new MemoryUnit();
		pc = new PC();
		rf = new RegisterFile();
		FETCH = new FetchStage(memUnit, pc);
		DECODE = new DecodeStage();
		EXECUTE = new ExecuteStage();
		MEMACC = new MemoryAccStage();
		WB = new WriteBackStage(rf);
	}
	
	public static void main(String[] args) throws InterruptedException {
		CPU cpu = new CPU();
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Please enter the instruction count:");
		int IC = sc.nextInt(); // instruction count
		//Take the program in assembly
		String[] AssemlyProgram = new String[IC];
		System.out.println("Enter the Assembly Program Instructions");
		sc.nextLine();
		for(int i=0;i<IC;i++)
		{
			AssemlyProgram[i] =sc.nextLine();
		}
		sc.close();
		
		//Convert the program from assembly to Machine code
		String[] MachineCodeProgram = new String[IC];
		for(int i=0;i<IC;i++)
		{
			MachineCodeProgram[i] = Assembler.convertToMachineCode(AssemlyProgram[i]);
		}
		
		//load the program in memory
		cpu.memUnit.LoadProgram(MachineCodeProgram);
		
		PipelinedInst[] pipelineSchedule = new PipelinedInst[5]; // 5 pipeline stages
		Arrays.fill(pipelineSchedule,null);
		// initially empty
		// @idx 0 --> the instruction in fetch stage
		// @idx 1 --> the instruction in decode stage
		// @idx 2 --> the instruction in execute stage
		// @idx 3 --> the instruction in mem access stage
		// @idx 4 --> the instruction in write back stage
		
		
		int cycleCounter = 0; // To keep track of the cycles count
		
		
		//Pipeline Registers used for execute stage
		ExecutePipelineReg exCurr = new ExecutePipelineReg();
		ExecutePipelineReg  exNext = new ExecutePipelineReg();
		
		//Pipeline Registers used for Memory Access stage
		MemAccPipelineReg memCurr = new MemAccPipelineReg();
		MemAccPipelineReg memNext = new MemAccPipelineReg();
		
		//Pipeline Registers used for Write Back stage
		WriteBackPipelineReg wbCurr = new WriteBackPipelineReg();
		WriteBackPipelineReg wbNext = new WriteBackPipelineReg();
		
		do
		{
			cycleCounter++;
			int currPC = cpu.pc.getPC();
			
			System.out.println("In clock-cycle number: "+cycleCounter);
			//Instruction Fetch
			if(currPC >= 0 && currPC < AssemlyProgram.length)// if there are still instructions to be fetched
			{
				 // The address of the currently fetched instruction
				String mCodeInst = cpu.FETCH.InstFetch(); // the machine code of the currently fetched instruction
				String assemblyInst = AssemlyProgram[currPC];// the assembly of the currently fetched instruction
				ControlUnit cu = new ControlUnit(mCodeInst);
				pipelineSchedule[0] = new PipelinedInst(assemblyInst, mCodeInst,cu,currPC);
				
				System.out.println(pipelineSchedule[0].assemblyFormat +" in Fetch Stage");
				System.out.println("Next PC: "+cpu.pc.getPC());
				System.out.println("Instruction Machine Code: "+pipelineSchedule[0].machineCodeFormat);
				System.out.println();
				System.out.println("___");
				System.out.println();
			}
			else
			{
				pipelineSchedule[0] = null; // No new instructions to be fetched
			}
			
			
			
			//Instruction Decode
			if(pipelineSchedule[1] != null)
			{
				cpu.DECODE.InstDecode(pipelineSchedule[1].machineCodeFormat);
				exNext.ALUOp = pipelineSchedule[1].controlUnit.ALUOP;
				exNext.ALUSrc = pipelineSchedule[1].controlUnit.aluSrc;
				exNext.branch= pipelineSchedule[1].controlUnit.branch;
				exNext.BNE= pipelineSchedule[1].controlUnit.BNE;
				exNext.jump= pipelineSchedule[1].controlUnit.jump;
				exNext.slt = pipelineSchedule[1].controlUnit.slt;
				int readRegIdx1 = cpu.DECODE.readRegIdx1;
				if(readRegIdx1 != -1)
				{
					exNext.readData1 = cpu.rf.readReg(readRegIdx1);
				}
				int readRegIdx2 = cpu.DECODE.readRegIdx2;
				if(readRegIdx2 != -1)
				{
					exNext.readData2 = cpu.rf.readReg(readRegIdx2);
				}
				exNext.extendedImmediateVal = cpu.DECODE.extendedImmediateVal;
				exNext.writeRegIdx = cpu.DECODE.writeRegIdx;
				exNext.nextPC = pipelineSchedule[1].address+1;
				exNext.pc=cpu.pc;
				
				System.out.println(pipelineSchedule[1].assemblyFormat+" in Decode Stage:");
				System.out.println("read data 1:"+((readRegIdx1==-1)?"NO DATA TO Read":exNext.readData1));
				System.out.println("read data 2:"+((readRegIdx2==-1)?"NO DATA TO Read":exNext.readData2));
				System.out.println((pipelineSchedule[1].controlUnit.registDest)?
						"NO Immediate Value (R-Type or Set less than)"
						:
						"sign-extend immediate: "+cpu.DECODE.extendedImmediateVal
						);
				System.out.println("Next PC: "+ (pipelineSchedule[1].address+1));
				System.out.println("Source reg1: "+((readRegIdx1==-1)?"Don't Care":readRegIdx1));
				System.out.println("Source reg2: "+((readRegIdx2==-1)?"Don't Care":readRegIdx2));
				System.out.println("Destination reg: "+((exNext.writeRegIdx==-1)?"Don't Care":exNext.writeRegIdx));
				System.out.println("WB Controls: "
						+"MemToReg: "+((pipelineSchedule[1].controlUnit.memToReg)?1:0)
						+", RegWrite: "+((pipelineSchedule[1].controlUnit.regWrite)?1:0));
				System.out.println("MEM Controls: "
						+"MemRead: "+((pipelineSchedule[1].controlUnit.memRead)?1:0)
						+", MemWrite: "+((pipelineSchedule[1].controlUnit.memWrite)?1:0)
						+", Branch: "+((pipelineSchedule[1].controlUnit.branch)?1:0));
				System.out.println("EX Controls: "
						+"RegDest: "+((pipelineSchedule[1].controlUnit.registDest)?1:0)
						+", ALUOp: "+(pipelineSchedule[1].controlUnit.ALUOP)
						+", ALUSrc: "+((pipelineSchedule[1].controlUnit.aluSrc)?1:0)
						+", JumpFlag: "+((pipelineSchedule[1].controlUnit.jump)?1:0));
				System.out.println();
				System.out.println("___");
				System.out.println();
			}
			
			//Execute
			if(pipelineSchedule[2] != null)
			{
				int res = cpu.EXECUTE.execute(exCurr);
				memNext.readData2 = exCurr.readData2;
				memNext.extendedImmediateVal=exCurr.extendedImmediateVal;
				memNext.MemWrite=pipelineSchedule[2].controlUnit.memWrite;
				memNext.MemRead=pipelineSchedule[2].controlUnit.memRead;
				memNext.writeRegIdx = exCurr.writeRegIdx;
				memNext.ALUres = res;
				memNext.memUnit= cpu.memUnit;
				
				System.out.println(pipelineSchedule[2].assemblyFormat+" in Execute Stage");
				System.out.println("zero flag: "+(cpu.EXECUTE.alu.isZero()?1:0));
				System.out.println("ALU result/Branch address(-1 if false branch cond)/Memory address/Jump address: "+res);
				System.out.println("register value to write to memory: "+
								(pipelineSchedule[2].controlUnit.memWrite?
								exCurr.readData2
								:
								"Not a Store word instruction so no data to write"
								));
				
				System.out.println("WB Controls: "
						+"MemToReg: "+((pipelineSchedule[2].controlUnit.memToReg)?1:0)
						+", RegWrite: "+((pipelineSchedule[2].controlUnit.regWrite)?1:0));
				System.out.println("MEM Controls: "
						+"MemRead: "+((pipelineSchedule[2].controlUnit.memRead)?1:0)
						+", MemWrite: "+((pipelineSchedule[2].controlUnit.memWrite)?1:0)
						+", Branch: "+((pipelineSchedule[2].controlUnit.branch)?1:0)
						+", JumpFlag: "+((pipelineSchedule[2].controlUnit.jump)?1:0));
				System.out.println();
				System.out.println("___");
				System.out.println();
			}
			
			//Memory Access
			if(pipelineSchedule[3] != null)
			{
				cpu.MEMACC.MemAccess(memCurr);
				wbNext.MemToReg = pipelineSchedule[3].controlUnit.memToReg;
				wbNext.RegDst = pipelineSchedule[3].controlUnit.registDest;
				wbNext.writeRegIdx= memCurr.writeRegIdx;
				wbNext.memData = memCurr.memData;
				wbNext.ALUres = memCurr.ALUres;
				System.out.println(pipelineSchedule[3].assemblyFormat+" in Memory Access Stage");
				System.out.println("ALUres :"+memCurr.ALUres);
				int address = memCurr.ALUres%1024;
				System.out.println("memory word read address ||| Data: "+
								(pipelineSchedule[3].controlUnit.memRead?
								address + " ||| " + (cpu.memUnit.loadDataAt(address)==null?"NO DATA @add"+address:cpu.memUnit.loadDataAt(address))
								:
								"Not a load word so no data to read from memory"
								));
				System.out.println();
				System.out.println("___");
				System.out.println();
			}
			//Write Back
			if(pipelineSchedule[4] != null)
			{
				cpu.WB.writeBack(wbCurr);
				System.out.println(pipelineSchedule[4].assemblyFormat+" in WB Stage");
				System.out.println();
			}
			
			exCurr.deepCopy(exNext);
			memCurr.deepCopy(memNext);
			wbCurr.deepCopy(wbNext);

			
			// Shifts each instruction to the next state 
			updatePipelineStages(pipelineSchedule);
			System.out.println("_____________________________________________________");
			Thread.sleep(1500);
		}
		while(!isEmpty(pipelineSchedule));
	}
	
	
	private static boolean isEmpty(PipelinedInst[] pipelineSchedule) {
		return pipelineSchedule[0] == null && pipelineSchedule[1] == null &&
				pipelineSchedule[2] == null &&
				pipelineSchedule[3] == null &&
				pipelineSchedule[4] == null ;
	}

	private static void updatePipelineStages(PipelinedInst[] pipelineSchedule) {
		for(int i=4;i>=1;i--)
			pipelineSchedule[i] = pipelineSchedule[i-1];
	}
	
	
	static class PipelinedInst{
		String assemblyFormat;
		String machineCodeFormat;
		ControlUnit controlUnit;
		int address;
		public PipelinedInst(String assembly,String mCode,ControlUnit cu,int addr) {
			assemblyFormat = assembly;
			machineCodeFormat = mCode;
			controlUnit = cu;
			address = addr;
		}
	}
	
	
	static class ExecutePipelineReg{
		String ALUOp;
		boolean ALUSrc,branch,BNE,jump,slt;
		int readData1,readData2;
		String extendedImmediateVal;
		int writeRegIdx;
		
		int nextPC;
		PC pc;
		public ExecutePipelineReg() {
			
		}
		private void deepCopy(ExecutePipelineReg exNext) {
			this.ALUOp = exNext.ALUOp;this.ALUSrc = exNext.ALUSrc;this.branch = exNext.branch;
			this.BNE = exNext.BNE;this.jump = exNext.jump;this.slt = exNext.slt;this.readData1 = exNext.readData1;
			this.readData2 = exNext.readData2;this.extendedImmediateVal=exNext.extendedImmediateVal;
			this.writeRegIdx=exNext.writeRegIdx;this.nextPC=exNext.nextPC;this.pc=exNext.pc;
		}
	}
	
	static class MemAccPipelineReg{
		
		int readData2;
		String extendedImmediateVal;
		boolean MemWrite;
		boolean MemRead;
		boolean branch;
		int writeRegIdx;
		int ALUres;
		String memData;
		MemoryUnit memUnit;
		public MemAccPipelineReg() {
			
		}
		private void deepCopy(MemAccPipelineReg memNext) {
			this.readData2=memNext.readData2;this.extendedImmediateVal = memNext.extendedImmediateVal;this.MemWrite = memNext.MemWrite;this.MemRead = memNext.MemRead;
			this.branch = memNext.branch;this.writeRegIdx = memNext.writeRegIdx;this.ALUres = memNext.ALUres;this.memData = memNext.memData;
			this.memUnit = memNext.memUnit;
		}
	}
	
	static class WriteBackPipelineReg{
		boolean MemToReg;
		boolean RegDst;
		int writeRegIdx;
		int ALUres;
		String memData;
		public WriteBackPipelineReg() {
			
		}
		private void deepCopy(WriteBackPipelineReg wbNext) {
			this.MemToReg=wbNext.MemToReg;this.RegDst = wbNext.RegDst;this.writeRegIdx = wbNext.writeRegIdx;this.ALUres = wbNext.ALUres;
			this.memData = wbNext.memData;
		}
	}
}
