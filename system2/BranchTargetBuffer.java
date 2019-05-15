/**
 *  Please see readme.txt file for complete instructions
 */

package system2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.lang.Boolean;


public class BranchTargetBuffer{

	// set up a hashmap that will take an index related to the instruction address as a key 
		// and will track the related target tag and it's valid bit
	private HashMap<Integer, BranchTagAndValidBit> branchTargetBuffHashMap;


	public BranchTargetBuffer(int sizeOfBuffer){
		// initialize hash map
        branchTargetBuffHashMap = new HashMap<Integer, BranchTagAndValidBit>(sizeOfBuffer);

        // fill up our hash map with indices and tags (valid bit is set to false by default)
        for(int i = 0; i < sizeOfBuffer; i++){
        	branchTargetBuffHashMap.put(i, new BranchTagAndValidBit());
        }
	}


	public void setTag(int tag, int index){
		// get the current tag and it's valid bit
		BranchTagAndValidBit branchTagAndValidBit = branchTargetBuffHashMap.get(index);
		// set new values for tag and valid bit
		branchTagAndValidBit.setIsValid(true);
		branchTagAndValidBit.setTag(tag);
	}


	public boolean isIndexValid(int tag, int index){
		// get the current tag and it's valid bit
		BranchTagAndValidBit branchTagAndValidBit = branchTargetBuffHashMap.get(index);
		// checks the valid bit
		if(!(branchTagAndValidBit.isValid())){
			return false;
		// checks if the stored target tag matches the target tag of the current instruction
		}else if(tag == branchTagAndValidBit.getTag()){
			return true;
		// if the valid bit is 1 (true) but the tags don't match, return false
		}else {
			return false;
		}
	}


	// gets target tag for sys2.java to print extra data when user enters -v as a CLI argument
	public int getTag(int index){
		return branchTargetBuffHashMap.get(index).getTag();
	}
}