package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecordOld;

public class NamedGraph {
	public String name;
	public ConceptGraphRecordOld cg;
	
	public NamedGraph(String nameIn, ConceptGraph cgIn){
		name = nameIn;
		ConceptGraph tree = cgIn.graphToTree();
		cg = tree.buildNodesAndLinks();
	}
	
	public String toString(){
		return this.name;
	}
}
