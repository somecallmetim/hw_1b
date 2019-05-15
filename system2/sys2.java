/**
 *  Please see readme.txt file for complete instructions
 */

package system2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;

public class sys2{


	public static void main(String[] args) throws Exception{

		// initialize all our primary counters for tracking various branches and their statistics
            // NOTE: These are the variables we'll either be printing or will be directly used
            //       in getting the necessary values to print
		int totalBranches = 0;
		int forwardBranches = 0;
		int backwardBranches = 0; 
		int forwardBranchesTaken = 0;
		int backwardBranchesTaken = 0;
		int mispredictedBranches = 0;
		int btbMisses = 0;
        int btbAccesses = 0;
		double mispredictionRate = 0;
        double targetBranchMissRate = 0;





		// this runs if we get at least 3 command line arguments 
            // which should be a file name for us to read, BPB size, & BTB size
        if (args.length >= 3) {



            /* ***initialize variables*** */

			// int that keeps track of how big our Branch Predictor Buffer should be
			int bpbSize = Integer.parseInt(args[1]);
            // int that keeps track of how big our Branch Target Buffer should be
            int btbSize = Integer.parseInt(args[2]);
            // used in creating indices and tags for Branch Predictor Buffer and Branch Target Buffer
            int tempShiftedBitsHolder = 0;

            // this number is used to help calculate predictor indices for BPB
			double bpbLogTwo = 0;
            // this number is used to help calculate tags for BPB
            double btbLogTwo = 0;

            // create new BranchPredictorBuffer
            BranchPredictorBuffer branchPredictorBuffer = new BranchPredictorBuffer(bpbSize);

            // create new Branch Target Buffer
            BranchTargetBuffer branchTargetBuffer = new BranchTargetBuffer(btbSize);

        	/* ***read and parse file*** */
        	try{
                // get file from first command line arg
    			File traceFile = new File(args[0]);

    			// FileReader reads the entire file
    			FileReader fileReader = new FileReader(traceFile);

    			// BufferedReader lets us go through that file line by line
    			BufferedReader bufferedReader = new BufferedReader(fileReader);

    			// line will store the string of the current line we're on
    			String line;

    			// this loop will actually process each line of the file until we read EOF
    			while((line = bufferedReader.readLine()) != null){
    				// gets char from Field 7, which tells us if we have a branch instruction
    					// and whether or not it's taken
    				char branchIndicator = line.charAt(20);

    				// here we assume the 'Branch Field' can only be one of three things, T, N, or -
    					// where '-' indicates the instruction isn't a branch
    				if(branchIndicator != '-'){
    					// increment total branches
    					totalBranches++;




                        /* ***initialize variables to be used in calculations & internal tracking*** */

                        // predictor index for Branch Prediction Buffer 
                        int predictorIndex = 0;
                        // calculated Bi tag for Branch Target Buffer
                        int biBtbTag = 0;
                        // index for Branch Target Buffer
                        int btbIndex = 0;

                        // records predictor state before any increment or decrement
                            // this is just for printing purposes when user adds in the -v argument
                        int predictorStateBeforeUpdate = 0;
                        // this is just for printing purposes when user adds in the -v argument 
                        String btbTagAccessed = null;
                        // this is just for printing purposes when user adds in the -v argument
                        String btbIndexString = null;





                        /* ***do all needed internal calculations*** */

                        // get instruction address and convert it to an integer
                        String instructionAddressString = "0x" + line.substring(2,8);
                        int instructionAddress = Integer.decode(instructionAddressString); 

                        // get target address and convert it to an integer
                        String targetAddressString = "0x" + line.substring(66,72);
                        int targetAddress = Integer.decode(targetAddressString);


                        // calculate number of bits needed for Branch Predictor Indices
                        bpbLogTwo = Math.log(bpbSize)/Math.log(2);
                        // check to make sure bpbLogTwo is a whole number (ie bpbSize is a power of 2)
                        if(bpbLogTwo != (int)bpbLogTwo){
                            // exit program if we recieved a bad value for bpbSize
                            System.out.println("Please choose a BPB value that is a power of 2");
                            System.exit(1);
                        } // end if statement

                        // calculate predictor index for Branch Predictor Buffer
                            // NOTE: return an error if user chose a value too big to handle
                        if(bpbLogTwo < 32){
                            // going to shift the bits to the point where the most sig bit is the last bit we're throwing away
                            int shiftAmount = 31 - (int)bpbLogTwo;

                            // shift the bits to drop all the bits we want don't - 1 so we can deal
                                // with accidently ending up with negative value
                            tempShiftedBitsHolder = instructionAddress << shiftAmount;
                            // 'and' the result to throw out the most sig bit, ie set it to zero, and keep the rest 
                            tempShiftedBitsHolder = tempShiftedBitsHolder & 0x7fffffff;
                            // shift the bits back to get our predictor index
                            predictorIndex = tempShiftedBitsHolder >> shiftAmount;

                            // get current state for the predictor index (for -v printing only)
                            predictorStateBeforeUpdate = branchPredictorBuffer.getCurrentPredictionState(predictorIndex);
                        }else {
                            System.out.println("buffer is too large, please try again with smaller buffer");
                            System.exit(0);
                        } // end if-else statement, if(bpbLogTwo < 32)



                        // calculate number of bits needed for Branch Target Buffer tags
                        btbLogTwo = Math.log(btbSize)/Math.log(2);
                        // check to make sure btbLogTwo is a whole number (ie btbSize is a power of 2)
                        if(btbLogTwo != (int)btbLogTwo){
                            // exit program if we recieved a bad value for btbSize
                            System.out.println("Please choose a BPB value that is a power of 2");
                            System.exit(1);
                        } // end if statement

                        // calculate index for Branch Target Buffer
                            // NOTE: return an error if user chose a value too big to handle
                        if(btbLogTwo < 32){
                            // going to shift the bits to the point where the most sig bit is the last bit we're throwing away
                            int shiftAmount = 31 - (int)btbLogTwo;

                            // shift the bits to get rid of all the bits we don't want - 1 so we can deal 
                                // with accidently ending up with negative value
                            tempShiftedBitsHolder = instructionAddress << shiftAmount;
                            // 'and' the result to throw out the most sig bit, ie set it to zero, and keep the rest 
                            tempShiftedBitsHolder = tempShiftedBitsHolder & 0x7fffffff;
                            // shift the bits back to get our index
                            btbIndex = tempShiftedBitsHolder >> shiftAmount;  

                            // recording and converting to string for printing when the -v CLI argument is used
                            btbIndexString = Integer.toHexString(btbIndex);
                            // shift the bits of the instruction by btbLogTwo to get biBtbTag
                            biBtbTag = instructionAddress >> (int)btbLogTwo;

                            // recording and converting to string for printing when the -v CLI argument is used
                            // btbTagAccessed = Integer.toHexString(branchTargetBuffer.getTag(btbIndex));
                            btbTagAccessed = Integer.toHexString(biBtbTag);
                        }else {
                            System.out.println("buffer is too large, please try again with smaller buffer");
                            System.exit(0);
                        } // end if-else statement, if(btbLogTwo < 32) 

 


                        /* ***algorithm as given by hw instructions*** */

                        /**************************************************************/
                        /* Get prediction for Bi (predict taken or predict not taken) */
                        /**************************************************************/

    					char branchPrediction = branchPredictorBuffer.getPrediction(predictorIndex);

                        /***************************************************/
                        /*if Bi is predicted taken, check BTB entry for Bi */
                        /***************************************************/

                        // check actual branchPrediction value
                        if(branchPrediction == 'T'){
                            // increment btbAccesses
                            btbAccesses++;

                            /*******************************************************************/
                            /* If valid bit of BTB entry == 1 && tag of BTB entry == tag of Bi */
                            /* We have a BTB hit                                               */
                            /* Else We have a BTB miss                                         */
                            /*******************************************************************/

                            // check to see if index contains valid tag (ie valid bit is 1 and tags match)
                            if(!(branchTargetBuffer.isIndexValid(biBtbTag, btbIndex))){
                                btbMisses++;
                            }
                        } // end if-else statement, if(branchPrediction == 'T')


                        /************************************************/
                        /* Else if Bi is predicted not taken Do nothing */
                        /************************************************/           



                        /***********************************************************/
                        /* Check whether Bi is actually taken/not taken            */
                        /* If prediction == actual behavior, prediction is correct */
                        /* Else prediction is wrong                                */
                        /***********************************************************/

                        // check our branch predictor buffer prediction against actual result
                        if(branchPrediction != branchIndicator){
                            mispredictedBranches++;
                        } // end if statement


                        /****************************/
                        /* update prediction for Bi */
                        /****************************/

						// increment or decrement state for predictor index depending on whether or not the branch was taken
    					if(branchIndicator == 'T'){

                            // increment appropriate index for BPB whenever branch is taken
    						branchPredictorBuffer.incrementState(predictorIndex);

                            /********************************/
                            /* If Bi actually taken         */
                            /* Tag of BTB entry = tag of Bi */
                            /* Valid bit of BTB entry = 1   */
                            /********************************/

                            // set tag for BTB whenever branch is taken
                            branchTargetBuffer.setTag(biBtbTag, btbIndex);

                            // check if branch is forward or backward
                            if(instructionAddress < targetAddress){
                                // increment foward branches
                                forwardBranches++;
                                // increment foward branches taken
                                forwardBranchesTaken++;
                            }else {
                                // increment backward branches
                                backwardBranches++;
                                // increment backward branches taken
                                backwardBranchesTaken++;
                            } // end if-else statement, if(instructionAddress < targetAddress)
    					}else {
                            // increment appropriate index for BPB whenever branch is taken
    						branchPredictorBuffer.decrementState(predictorIndex);
                            // check if branch is forward or backward
                            if(instructionAddress < targetAddress){
                                // increment foward branches
                                forwardBranches++;
                            }else {
                                // increment backward branches
                                backwardBranches++;
                            }
    					} // end if-else statement, if(branchIndicator == 'T')



                        /* ***print out extra details for verbose mode****/

                        if(args.length == 4 && (args[3].equals("-v") || args[3].equals("[-v]"))) { 
                            System.out.printf("%d ", totalBranches-1);
                            System.out.printf(Integer.toHexString(predictorIndex) + " ");
                            System.out.printf(predictorStateBeforeUpdate + " ");
                            System.out.printf(branchPredictorBuffer.getCurrentPredictionState(predictorIndex) + " ");
                            System.out.printf(btbIndexString + " ");
                            System.out.printf(btbTagAccessed + " ");
                            System.out.printf(btbAccesses + " ");
                            System.out.println(btbMisses);
                        } // end if statement	
    				} // end if statement if(branchIndicator != '-'){}
    			} // end while loop, while((line = bufferedReader.readLine()) != null)
			// catches any IO Exceptions and prints a stack trace
			} catch(IOException e){
        		e.printStackTrace();
        	} // end try-catch block 



            /* ***print out results*** */

        	// make sure there was at least one branch to avoid dividing by zero exception
        	if(totalBranches > 0){
        		// calculate branch prediction miss percentage
	        	mispredictionRate = (double)mispredictedBranches/(double)totalBranches;
                // calculate target branch miss rate
                targetBranchMissRate = (double)btbMisses/(double)btbAccesses;

	        	// print results
	        	System.out.printf("Number of branches = %d \n", totalBranches);
	        	System.out.printf("Number of forward branches = %d \n", forwardBranches);
	        	System.out.printf("Number of forward branches taken = %d \n", forwardBranchesTaken);
	        	System.out.printf("Number of backward branches = %d \n", backwardBranches);
	        	System.out.printf("Number of backward branches taken = %d \n", backwardBranchesTaken);
	        	System.out.printf("Number of mispredicted branches = %d %f \n", mispredictedBranches, mispredictionRate);
                System.out.printf("Number of BTB misses = %d %f \n", btbMisses, targetBranchMissRate);

        	}else {
        		System.out.println("No branches in trace...");
        	}

        }else{
        	System.out.println("Not enough CLI arguments");
        }
    }
}