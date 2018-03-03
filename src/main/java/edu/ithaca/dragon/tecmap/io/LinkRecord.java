package edu.ithaca.dragon.tecmap.io;

public class LinkRecord {
	
	private String parent;
	private String child;
	
	public LinkRecord(){}
	
	public LinkRecord(String parentIn, String childIn){
		this.parent = parentIn;
		this.child = childIn;
	}
	
	public String getParent(){
		return this.parent;
	}
	
	public String getChild(){
		return this.child;
	}
	
	public void setParent(String parentIn){
		this.parent = parentIn;
	}
	
	public void setChild(String childIn){
		this.child = childIn;
	}
	
	public String toString(){
		return "(Parent: " + this.parent + " Child: " + this.child + ")\n";
	}

	public boolean equals(Object other){
		if(other == null){
			return false;
		}
		if(!this.getClass().isAssignableFrom(other.getClass())){
			return false;
		}
		LinkRecord otherNode = (LinkRecord) other;
		if(this.parent.equals(otherNode.parent) && this.child.equals(otherNode.child)) {
			return true;
		} else {
			return false;
		}
	}

}
