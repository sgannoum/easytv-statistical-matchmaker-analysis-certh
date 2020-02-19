package com.certh.iti.easytv.stmm.association.analysis.rules;

import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;

public class AssociationRule implements Comparable<AssociationRule>{
	
	private Itemset head;
	private Itemset body;
	private Itemset union;
	private double confidence = 0.0;
	
	public AssociationRule(Itemset body, Itemset head) {
		this.setHead(head);
		this.setBody(body);
		union = Itemset.union(head, body);
	}
	
	public AssociationRule(Itemset body, Itemset head, Itemset union, double confidence) {
		this.setHead(head);
		this.setBody(body);
		this.setUnion(union);
		this.confidence = confidence;
	}

	public Itemset getHead() {
		return head;
	}

	public void setHead(Itemset head) {
		this.head = head;
	}

	public Itemset getBody() {
		return body;
	}

	public void setBody(Itemset body) {
		this.body = body;
	}
	
	public double getConfidence() {
		return this.confidence;
	}
	
	public double getHeadSupport() {
		return head.getWeight();
	}

	public double getUnionSupport() {
		return union.getWeight();
	}
	
	public Itemset getUnion() {
		return union;
	}

	public void setUnion(Itemset union) {
		this.union = union;
	}
	
	@Override
	public String toString() {
/*		return String.format("{%s}:%d => {%s}:%d	{%s}:%d conf: %.2f", head.toString(), head.getWeight(), 
																		 body.toString(), body.getWeight(), 
																		 union.toString(), union.getWeight(), 
																		 confidence);*/
		return String.format("{%-5s} => {%-5s} conf: %.2f", body.toString(), head.toString(), confidence);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!this.getClass().isInstance(o)) return false;
		AssociationRule rule = (AssociationRule) o;
		
		return (head.equals(rule.head)) && (body.equals(rule.body));
	}

	@Override
	public int compareTo(AssociationRule o) {
		return union.size() - o.union.size();
	}
	
	/**
	 * 	
		header		body		results
		-------------------------------
		eq			eq			true
		eq			less		false
		eq			more		true
		eq			no			false
		less		eq			false
		less		less		false
		less		more		false
		less		no			false
		more		eq			true
		more		less		false
		more		more		true
		more		no			false
		no			eq			false
		no			less		false
		no			more		false
		no			no			false
	 */
	public boolean canSubstituted(AssociationRule o) {
		if(!o.head.isIncludedIn(head))
			return false;
		
		if(!o.body.isIncludedIn(body))
			return false;
		
		return true;
	}


}
