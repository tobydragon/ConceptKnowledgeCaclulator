package edu.ithaca.dragonlab.ckc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//TODO, this shouldn't be in this test folder with no automated tests, it also doesn't correspond to a class...
public class MultCGTest {

	ConceptGraph cgA;
	ConceptGraph cgB;
	ConceptGraph cgC;
	NamedGraph studA;
	NamedGraph studB;
	NamedGraph studC;
	List students;
	
	ObjectMapper mapper = new ObjectMapper();
	 
	String outputLocation = "MultStudentsOutput.json";
	
	public MultCGTest(){
		setCG();
		studA = new NamedGraph("StudentA", cgA);
		studB = new NamedGraph("StudentB", cgB);
		studC = new NamedGraph("StudentC", cgC);
		students = new ArrayList();
		students.add(studA);
		students.add(studB);
		students.add(studC);
		
		try {
			//writes JSON to file
			mapper.writeValue(new File(outputLocation), this.students);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String toString(){
		return students+" ";
	}
	public void setCG(){
		cgA = makeSimple();
		System.out.println(cgA);
		cgB = makeSimple();
		cgC = makeSimple();
	}

	//TODO: Integrate this with ExampleConceptGraphFactory, either use one from there or add this there with a different name
	public ConceptGraph makeSimple(){
		List<ConceptNode> cnList = new ArrayList<ConceptNode>();
		List<IDLink> linkList = new ArrayList<IDLink>(); 

		ConceptNode cn = new ConceptNode("A");
		cn.setActualComp(Math.random());
		cnList.add(cn);
		cn = new ConceptNode("B");
		cn.setActualComp(Math.random());
		cnList.add(cn);
		cn = new ConceptNode("C");
		cn.setActualComp(Math.random());
		cnList.add(cn);
		cn = new ConceptNode("D");
		cn.setActualComp(Math.random());
		cnList.add(cn);
		cn = new ConceptNode("E");
		cn.setActualComp(Math.random());
		cnList.add(cn);
		
		IDLink link = new IDLink("A","B");
		linkList.add(link);
		link = new IDLink("A","C");
		linkList.add(link);
		link = new IDLink("B","D");
		linkList.add(link);
		link = new IDLink("D","E");
		linkList.add(link);
		link = new IDLink("C","E");
		linkList.add(link);
		
		NodesAndIDLinks lists = new NodesAndIDLinks(cnList, linkList);
		return new ConceptGraph(lists);
	}
	
	public static void main(String args[]){
		MultCGTest myTest = new MultCGTest();
		System.out.println(myTest);
	}
}
