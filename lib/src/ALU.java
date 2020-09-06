
public class ALU {
	
	private int output = 0;
	private boolean zero;
	private boolean postive;
	
	public int ALUEvaluator(String ALUop, int Operand1, int Operand2) {
		switch (ALUop) {
		case "000":
			output = addOp(Operand1, Operand2);
			break;
		case "001":
			output = subOp(Operand1, Operand2);
			break;
		case "010":
			output = mulOp(Operand1, Operand2);
			break;
		case "011":
			output = orOp(Operand1, Operand2);
			break;
		case "100":
			output = andOp(Operand1, Operand2);
			break;
		case "101":
			output = shrOp(Operand1, Operand2);
			break;
		case "110":
			output = shlOp(Operand1, Operand2);
			break;
		}
		zero = output == 0;
		postive = output > 0;
		return output;
	}

	public boolean isZero() {
		return zero;
	}
	
	public boolean isPostive() {
		return postive;
	}
	
	private int addOp(int Operand1, int Operand2) {
		return (Operand1 + Operand2);
	}

	private int subOp(int Operand1, int Operand2) {
		return (Operand1 - Operand2);
	}
	
	private int mulOp(int Operand1, int Operand2) {
		return (Operand1 * Operand2);
	}

	private int orOp(int Operand1, int Operand2) {
		return (Operand1 | Operand2); //Bitwise or operation
//		String s1 = Integer.toBinaryString(Operand1);
//		String s2 = Integer.toBinaryString(Operand2);
//		int diff = Math.abs(s1.length() - s2.length());
//		if (s1.length() > s2.length())
//			for (int i = 0; i < diff; i++)
//				s2 = "0" + s2;
//		else
//			for (int i = 0; i < diff; i++)
//				s1 = "0" + s1;
//		String output = "";
//		for (int i = 0; i < s1.length(); i++) {
//			if (s1.charAt(i) == '1' || s2.charAt(i) == '1')
//				output += '1';
//			else
//				output += '0';
//		}
//		return Integer.parseInt(output, 2);
	}
	
	private int andOp(int Operand1, int Operand2) {
		return (Operand1 & Operand2); //Bitwise and operation
//		String s1 = Integer.toBinaryString(Operand1);
//		String s2 = Integer.toBinaryString(Operand2);
//		int diff = Math.abs(s1.length() - s2.length());
//		if (s1.length() > s2.length())
//			for (int i = 0; i < diff; i++)
//				s2 = "0" + s2;
//		else
//			for (int i = 0; i < diff; i++)
//				s1 = "0" + s1;
//		String output = "";
//		for (int i = 0; i < s1.length(); i++) {
//			if (s1.charAt(i) == '1' && s2.charAt(i) == '1')
//				output += '1';
//			else
//				output += '0';
//		}
//		return Integer.parseInt(output, 2);
	}

	private int shrOp(int Operand1, int Operand2) {
		return (Operand1 >> Operand2); // Bitwise shift right
	//	return (int)(operand1 * Math.pow(2, operand2));
	}
	
	private int shlOp(int Operand1, int Operand2) {
		return (Operand1 << Operand2);
	//	return (int)(operand1 / Math.pow(2, operand2));
	}

}
