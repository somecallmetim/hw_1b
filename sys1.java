/**
 *  Please see readme.txt file for complete instructions
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class sys1{
	public static void main(String[] args) throws Exception{
        
        // initialize traceFile for readability and ability to use outside of if/else block
		File traceFile;

		// initialize all our counters for tracking various branches and their statistics
		int totalBranches = 0;
		int forwardBranches = 0;
		int backwardBranches = 0; 
		int forwardBranchesTaken = 0;
		int backwardBranchesTaken = 0;
		int mispredictedBranches = 0;
		double mispredictionRate = 0;

		// this runs if we get at least 1 command line argument which should be a file name for us to read
        if (args.length >= 1) {
        	// attempts to read file
        	try{
    			traceFile = new File(args[0]);
    			// FileReader reads the entire file
    			FileReader fileReader = new FileReader(traceFile);

    			// BufferedReader lets us go through that file line by line
    			BufferedReader bufferedReader = new BufferedReader(fileReader);

    			// line will store the string of the current line we're on
    			String line;

    			// this loop will actually process each line of the file until we read EOF
    			while((line = bufferedReader.readLine()) != null){
    				// gets char from Field 7, aka Branch
    				char branch = line.charAt(20);

    				// here we assume the 'Branch Field' can only be one of three things, T, N, or -
    					// where '-' indicates the instruction isn't a branch
    				if(branch != '-'){
    					// increment total branches
    					totalBranches++;

    					// get instruction address and convert it to an integer
    					String instructionAddressString = line.substring(2,8);
    					int instructionAddress = Integer.decode("0x" + instructionAddressString); 

    					// get target address and convert it to an integer
    					String targetAddressString = line.substring(66,72);
    					int targetAddress = Integer.decode("0x" + targetAddressString);

    					// check if branch is forward or backward
    					if(instructionAddress < targetAddress){
    						// increment foward branches
    						forwardBranches++;
    						// check and record if branch was or was not taken
    						if(branch == 'T'){
    							forwardBranchesTaken++;
    							// recall that we're predicting forward branches are never taken
    							mispredictedBranches++;
                                // print additional information if the "-v" option was passed in
                                    // violated DRY here in favor of more efficient code
                                if(args.length > 1 && (args[1].equals("-v") || args[1].equals("[-v]"))){
                                    System.out.printf("%s %s 1 \n", instructionAddressString, targetAddressString);
                                }           
    						}else {
                                // print additional information if the "-v" option was passed in
                                    // violated DRY here in favor of more efficient code
                                if(args.length > 1 && (args[1].equals("-v") || args[1].equals("[-v]"))){
                                    System.out.printf("%s %s 0 \n", instructionAddressString, targetAddressString);
                                } 
                            }

    					}else {
    						// increment backward branches
    						backwardBranches++;
    						// check and record if branch was or was not taken
    						if(branch == 'T'){
    							backwardBranchesTaken++;
                                // print additional information if the "-v" option was passed in
                                    // violated DRY here in favor of more efficient code
                                if(args.length > 1 && (args[1].equals("-v") || args[1].equals("[-v]"))){
                                    System.out.printf("%s %s 1 \n", instructionAddressString, targetAddressString);
                                } 
    						}else {
    							// recall that we're predicting backward branches are always taken
    							mispredictedBranches++;
                                // print additional information if the "-v" option was passed in
                                    // violated DRY here in favor of more efficient code
                                if(args.length > 1 && (args[1].equals("-v") || args[1].equals("[-v]"))){
                                    System.out.printf("%s %s 0 \n", instructionAddressString, targetAddressString);
                                } 
    						}
    					}
    				}
    			}
			// catches any IO Exceptions and prints a stack trace
			} catch(IOException e){
        		e.printStackTrace();
        	}

        	// make sure there was at least one branch to avoid dividing by zero exception
        	if(totalBranches > 0){
        		// calculate branch prediction miss percentage
	        	mispredictionRate = (double)mispredictedBranches/(double)totalBranches;

	        	// print results
	        	System.out.printf("Number of branches = %d \n", totalBranches);
	        	System.out.printf("Number of forward branches = %d \n", forwardBranches);
	        	System.out.printf("Number of forward branches taken = %d \n", forwardBranchesTaken);
	        	System.out.printf("Number of backward branches = %d \n", backwardBranches);
	        	System.out.printf("Number of backward branches taken = %d \n", backwardBranchesTaken);
	        	System.out.printf("Number of mispredictedBranches branches = %d %f \n", mispredictedBranches, mispredictionRate);
	        	
        	}else {
        		System.out.println("No branches in trace...");
        	}

        }else{
        	System.out.println("No File Given...");
        }
    }
}