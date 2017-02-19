package edu.ithaca.dragonlab.ckc;

public class NamedGraph {
	public String name;
	public NodesAndIDLinks cg;
	
	public NamedGraph(String nameIn, ConceptGraph cgIn){
		name = nameIn;
		ConceptGraph tree = cgIn.graphToTree();
		cg = tree.buildNodesAndLinks();
	}
	
	public String toString(){
		return this.name;
	}
}
