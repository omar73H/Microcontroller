public class RegisterFile {
	private int[] file;
	
	public RegisterFile() {
		file = new int[16];
	}
	
	public int readReg(int addr) {
		return file[addr%16];
	}
	
	public void writeReg(int addr, int data) {
		file[addr%16] = data;
	}
}
