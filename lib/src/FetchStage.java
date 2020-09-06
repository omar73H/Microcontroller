
public class FetchStage {

	
	private MemoryUnit memUnit;
	private PC pc;
	
	public FetchStage(MemoryUnit memUnit,PC pc) {
		this.memUnit = memUnit;
		this.pc = pc;
	}


	public String InstFetch() {
		String inst = memUnit.loadInstAt(pc.getPC());
		pc.increment();
		return inst;
	}
}
