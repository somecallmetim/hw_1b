/**
 *  Please see readme.txt file for complete instructions
 */

package system2;

public class BranchTagAndValidBit{

	private boolean isValid = false;
	private int tag = 0;

	// for this program, the isValid bit always starts off as false to mimic hardware
		// so we set it to default to false above and don't mess with it in the constructor
		// also, currently the only time these are constructed is upon initial contruction of
		// the BTB when we don't have any tags to store yet, so there's nothing for the constructor
		// to do
	public BranchTagAndValidBit(){

	}

	public int getTag(){
		return this.tag;
	}

	public void setTag(int tag){
		this.tag = tag;
	}

	public boolean isValid(){
		return this.isValid;
	}

	public void setIsValid(boolean isValid){
		this.isValid = isValid;
	}
}