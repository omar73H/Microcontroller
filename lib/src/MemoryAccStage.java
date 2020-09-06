
public class MemoryAccStage {

	public String writeBackData;
	private static CacheItem[] Cache=new CacheItem[128];
	private static int front=0; 
	private static boolean inCache(int index) {
		
		for(int i=0;i<Cache.length&&Cache[i]!=null;i++) {
			if(Cache[i].idx==index) return true ;
		}
		return false ;
	}
	private static void putCache(int index,String instruction) {
		CacheItem ci =new CacheItem(index,instruction);
		if(front==128) {
			front=1;
			Cache[0]=ci;
			
		}
		else {
			Cache[front++]=ci;
		}
	}
	private static void updateCache(int index,String data) {
		CacheItem ci =new CacheItem( index , data );
		for	(int i=0 ; i < Cache.length &&Cache[i]!=null; i++) {
			
			if	(Cache[i].idx==index)
				Cache[i]=ci;
				
		}
	}
	private static String getFromCache(int index) {
		for(int i=0;i<Cache.length&&Cache[i]!=null;i++) {
			if(Cache[i].idx==index) return Cache[i].instruction;
		}
		
		return null;
		
	}
	public MemoryAccStage() {
	}
	
	public void MemAccess(CPU.MemAccPipelineReg memAccInfo) {
		
		int ReadData2 = memAccInfo.readData2;
		boolean MemWrite = memAccInfo.MemWrite;
		boolean MemRead = memAccInfo.MemRead;
		
		int ALUresult = memAccInfo.ALUres;
		
		if(MemWrite)
		{
			String data = Assembler.convertToBinary(ReadData2+"", 16);
			int address = ALUresult%1024;
			memAccInfo.memUnit.storeDataAt(address, data);
			if(inCache(address))
				updateCache(address, data);
		}
		else if(MemRead)
		{
			int address = ALUresult%1024;
			if(inCache(address)) {
				writeBackData = getFromCache(address);
				memAccInfo.memData = writeBackData;
			}
			
			
			else {
				writeBackData = memAccInfo.memUnit.loadDataAt(address);
				memAccInfo.memData = writeBackData;
				putCache(address, writeBackData);
			}
		}
		
	}
}
