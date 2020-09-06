
public class PC {

	private int PC ;
	
	public PC() {
		PC =0;
	}
	
	public int getPC() {
		return PC;
	}
	
	public void setPC(int PC) {
		this.PC = PC;
	}
	
	public void increment()
	{
		PC+=1;
	}
	
	
//	public void branch (int branchadd)
//	{
//		PC+=branchadd*4;
//	}
//	public void jump (int jumpadd)
//	{
//		PC=jumpadd*4;
//	}
}
		