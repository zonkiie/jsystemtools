class ExecutorReturn
{
	public int returnCode;
	public String returnString;
	
	public static ExecutorReturn newInstance()
	{
		return new ExecutorReturn();
	}
	
	public ExecutorReturn setReturnCode(int returnCode)
	{
		this.returnCode = returnCode;
		return this;
	}
	
	public ExecutorReturn setReturnString(String returnString)
	{
		this.returnString = returnString;
		return this;
	}
	
	public String toString()
	{
		return returnString;
	}
}

