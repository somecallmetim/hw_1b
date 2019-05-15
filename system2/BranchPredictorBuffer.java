/**
 *  Please see readme.txt file for complete instructions
 */

package system2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class BranchPredictorBuffer{
	
	// set up a hashmap that will take a predictor index as a key 
		// and hold it's current state as a value
	private HashMap<Integer, Integer> branchPredictionHashMap;
 
 	// constructor
    public BranchPredictorBuffer(int sizeOfBuffer){
        branchPredictionHashMap = new HashMap<Integer, Integer>(sizeOfBuffer);
            for(int i = 0; i < sizeOfBuffer; i++){
                branchPredictionHashMap.put(i, 1);
            }
    }

    public char getPrediction(int predictorIndex){
    	// currentState tells us what our current branch prediction is
    		// 0 and 1 represent 'N' or 'Not Taken'
    		// 2 and 3 represent 'T' or 'Taken'
    	int currentState = branchPredictionHashMap.get(predictorIndex);

    	// if currentState is greater than one, our prediction is that the branch is taken
    	if(currentState > 1){
    		return 'T';
    	}else return 'N';
    }

    public void incrementState(int predictorIndex){
    	// currentState tells us what our current branch prediction is
    		// 0 and 1 represent 'N' or 'Not Taken'
    		// 2 and 3 represent 'T' or 'Taken'
    	int currentState = branchPredictionHashMap.get(predictorIndex);

    	// makes sure we don't go over our state ceiling of 3
    	if(currentState != 3){
    		currentState++;
    		branchPredictionHashMap.put(predictorIndex, currentState);
    	}
    }

    public void decrementState(int predictorIndex){
    	// currentState tells us what our current branch prediction is
    		// 0 and 1 represent 'N' or 'Not Taken'
    		// 2 and 3 represent 'T' or 'Taken'
    	int currentState = branchPredictionHashMap.get(predictorIndex);

    	// makes sure we don't go over our state floor of 0
    	if(currentState != 0){
    		currentState--;
    		branchPredictionHashMap.put(predictorIndex, currentState);
    	}
    }

    // added this method to get current prediction state for printing extra data
    	// when user adds the -v CLI argument
    public int getCurrentPredictionState(int predictorIndex){
    	return this.branchPredictionHashMap.get(predictorIndex);
    }
}