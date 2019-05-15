**Welcome to my Hw 1b Project!!!**

**Inside the timbauer folder, you should find the following files & directories**

- sys1.java
- hw1b_result_table.xlsx 
- hw1b_result_table.txt
- readme.txt
- system2/
	- sys2.java
	- BranchPredictorBuffer.java
	- BranchTargetBuffer.java 
	- BranchTagAndValidBit.java

**From within the timbauer directory, please do the following**
     *NOTE: When in doubt, copy paste :)

- compile sys1
	javac sys1.java

- run sys1 with any of the below options
	java sys1 ~whsu/csc656/Traces/S18/P1/gcc.xac [-v]
	java sys1 ~whsu/csc656/Traces/S18/P1/gcc.xac -v
	java sys1 ~whsu/csc656/Traces/S18/P1/gcc.xac

- compile sys2
     *NOTE Do NOT switch directories! 
	javac system2/sys2.java

- run sys2 with any of the below options 
     *NOTE Java uses a '.' instead of a '/' ie 'java system2.sys2' instead of 'java system2/sys2'
	java system2.sys2 ~whsu/csc656/Traces/S18/P1/gcc.xac 512 128 [-v]
	java system2.sys2 ~whsu/csc656/Traces/S18/P1/gcc.xac 512 128 -v
	java system2.sys2 ~whsu/csc656/Traces/S18/P1/gcc.xac 512 128
