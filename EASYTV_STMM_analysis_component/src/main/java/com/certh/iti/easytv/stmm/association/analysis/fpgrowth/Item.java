package com.certh.iti.easytv.stmm.association.analysis.fpgrowth;


public class Item implements Comparable<Item> {
	int item;
	int count;

	Item(int i, int c) {
		item = i;
		count = c;
	}

	public int compareTo(Item o) {
		Item other = (Item) o;

		return (count - other.count);
	}

	public String toString() {
		return "<" + item + ", " + count + ">";
	}
}