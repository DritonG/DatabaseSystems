/***************************************************************
 * Tupelgenerator                                              *
 * (c) Manuel Mayr                                             *
 ***************************************************************/

import java.util.Random;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.BufferedOutputStream;

public class TupleGen
{
	private static final int paramsCount = 3;
	
	private static final int paramTupleCount = 0;
	private static final int paramTupleSize = 1;
	private static final int paramFilename = 2;
	
	private static final String helpText =
	               "\nCreate an unsorted input file (Assignment08)\n"
	             + "Lecture \"Database Systems 2\"\n"
	             + "\nUsage:\n"
	             + "java TupleGen <number_tuples> <tuple_width> <output_file>\n";
	
	public static void main(String[] args)
	{
		// Check parameters
		if (args.length != paramsCount) {
			System.err.println(helpText);
			System.exit(1);
		}
		
		int tupleCount = 0;
		int tupleSize = 0;
		String filename = args[paramFilename];
		
		PrintStream out = null;
		/* instantiate PRNG */
		Random generator = null;
		 
		try {
			tupleCount  = Integer.parseInt(args[paramTupleCount]);
			tupleSize   = Integer.parseInt(args[paramTupleSize]);
		} catch(NumberFormatException nfs) {
			System.err.println("Could not parse parameter:\n" + nfs.toString());
			System.exit(1);
		}
		
		try {
			out = new PrintStream(
			        new BufferedOutputStream(
			          new FileOutputStream(filename)));
		} catch(FileNotFoundException fe) {
			System.err.println(fe.toString());
			System.exit(1);
		}
		
		// Create PRNG
		generator = new Random(System.currentTimeMillis());
		
		StringBuffer randNum = new StringBuffer();
		
		// Create random numbers
		for (int i = 0; i < tupleCount; i++) {
			randNum.delete(0,randNum.length());
			for (int j = 0; j < tupleSize; j++) {
				randNum.append(generator.nextInt(10));
			}
			out.println(randNum);
		}
		
		out.close();
		
		System.exit(0);
	}
}
