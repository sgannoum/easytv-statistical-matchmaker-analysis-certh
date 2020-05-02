package com.certh.iti.easytv.stmm.association.analysis.fpgrowth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class FPTree {
	
	private Vector<Itemset> frequentItemsets;
	private FPTreeHeaderEntry[] header;
	private FPTreeNode root;

	// we need to be able to figure out
	// the index of an FPTreeHeaderEntry
	// for a given item
	private Map<Integer, Integer> item2index;

	// to quickly tell whether we have a single path or not
	private boolean hasMultiplePaths;

	// for statistics
	private int count_nodes;

	// information we need about the database
	private long num_rows;
	private long min_weight;

	
	private static class FPTreeNode {
		// data
		int item; // item id
		int count; // item count

		// the following two are kept to speed up calculations
		int seq_num; // sequence number of node, = (depth + 1)
		int header_index; // index in header of the entry for item

		// links
		FPTreeNode parent; // parent node
		FPTreeNode child; // first child node
		FPTreeNode sibling; // next sibling node
		FPTreeNode next; // next node in FPTree containing same item

		FPTreeNode() {
		}

		FPTreeNode(int i, int c, int sn, int hi, FPTreeNode p, FPTreeNode s, FPTreeNode n) {
			item = i;
			count = c;
			seq_num = sn;
			header_index = hi;
			parent = p;
			sibling = s;
			next = n;
		}
	}

	private static class FPTreeHeaderEntry {
		int item;
		int count; // total item count in tree
		FPTreeNode head;

		FPTreeHeaderEntry() {
		}

		FPTreeHeaderEntry(int i) {
			item = i;
		}
	}
	
	public Vector<Itemset> getFrequentItemsets() {
		return frequentItemsets;
	}
	
	public int getCountNodes() {
		return count_nodes;
	}


	public FPTree(int[] items, long num_rows, long min_weight) {
		header = new FPTreeHeaderEntry[items.length];
		root = new FPTreeNode();
		item2index = new HashMap<Integer, Integer>(items.length);

		this.num_rows = num_rows;
		this.min_weight = min_weight;

		for (int i = 0; i < items.length; i++) {
			header[i] = new FPTreeHeaderEntry(items[i]);
			item2index.put(new Integer(items[i]), new Integer(i));
		}
		
		frequentItemsets = new Vector<Itemset>();

	}

	public void insert(int[] items, int count) {
		// current_node will be the node below which we look to insert
		FPTreeNode current_node = root;

		for (int index = 0; index < items.length; index++) {
			// find header entry for items[index]
			int entry_index = ((Integer) item2index.get(new Integer(items[index]))).intValue();

			// update item count in header entry
			header[entry_index].count += count;

			// we look among the children of current_node
			// for the one containing items[index]
			FPTreeNode walker = current_node.child;
			for (; walker != null; walker = walker.sibling)
				if (walker.item == items[index])
					break;

			// case no child contained the item
			// -> we need to insert a new node
			if (walker == null) {
				// if we're creating a new branch
				if (current_node.child != null)
					hasMultiplePaths = true;

				count_nodes++;

				// parent is current_node, sibling is current_node.child,
				// next is head from header entry
				// (we insert at the beginning of the 'next'
				// and 'sibling' based linked lists)
				FPTreeNode new_node = new FPTreeNode(items[index], count, index + 1, entry_index, current_node,
						current_node.child, header[entry_index].head);
				header[entry_index].head = new_node;
				current_node.child = new_node;
				// and continue inserting from this new node
				current_node = new_node;
			}
			// if we get here then walker points
			// to a node containing items[index]
			else {
				// update count of node
				walker.count += count;
				// and continue inserting from this node
				current_node = walker;
			}
		}
	}

	public  Vector<Itemset> fp_growth(Itemset is_suffix) {
		if (!hasMultiplePaths) {
			// collect items from the tree, least frequent item first!!!
			int[] items = new int[header.length];
			for (int i = 0; i < header.length; i++)
				items[header.length - i - 1] = header[i].item;
			// generate all item combinations of the tree's single branch
			combine(items, is_suffix);
		
		} else
			for (int i = header.length - 1; i >= 0; i--) {
				Itemset is_new = new Itemset(is_suffix);
				is_new.add(header[i].item);
				is_new.setWeight(header[i].count);
				is_new.setSupport((double) header[i].count / (double) num_rows);

				// write itemset to the cache
				frequentItemsets.addElement(is_new);

				FPTree fpt = buildConditionalFPTree(header[i].item);
				if (fpt != null) 
					frequentItemsets.addAll(fpt.fp_growth(is_new));
			}
		
		return frequentItemsets;
	}

	void combine(int[] items, Itemset is_combination) {
		int count;
		for (int i = 0; i < items.length; i++) {
			Itemset is_new_combination = new Itemset(is_combination);
			is_new_combination.add(items[i]);
			// store in itemset the weight and support of all itemsets
			// combined with items[i];
			// note that we go through items in increasing order of their
			// support so that we can set it up correctly
			count = header[header.length - i - 1].count;
			is_new_combination.setWeight(count);
			is_new_combination.setSupport((double) count / (double) num_rows);

			// write itemset to the cache
			frequentItemsets.addElement(is_new_combination);

			combine(items, is_new_combination, i + 1);
		}
	}

	// create all combinations of elements in items[]
	// starting at index from and append them to is_combination
	void combine(int[] items, Itemset is_combination, int from) {
		for (int i = from; i < items.length; i++) {
			Itemset is_new_combination = new Itemset(is_combination);
			is_new_combination.add(items[i]);

			// write itemset to the cache
			frequentItemsets.addElement(is_new_combination);

			combine(items, is_new_combination, i + 1);
		}
	}

	public FPTree buildConditionalFPTree(int item) {

		// find header entry for item
		int entry_index = ((Integer) item2index.get(new Integer(item))).intValue();

		// we will see which of the remaining items are frequent
		// with respect to the conditional pattern base of item

		// we have a counter for each entry in the header that
		// comes before item's own entry
		int[] counts = new int[entry_index];
		for (FPTreeNode side_walker = header[entry_index].head; side_walker != null; side_walker = side_walker.next)

			for (FPTreeNode up_walker = side_walker.parent; up_walker != root; up_walker = up_walker.parent)
				counts[up_walker.header_index] += side_walker.count;

		int num_frequent = 0;
		for (int i = 0; i < counts.length; i++)
			if (counts[i] >= min_weight)
				num_frequent++;

		if (num_frequent == 0)
			return null;

		// put all frequent items in an array of Items
		Item[] item_objs = new Item[num_frequent];
		for (int i = 0, j = 0; i < counts.length; i++)
			if (counts[i] >= min_weight)
				item_objs[j++] = new Item(header[i].item, counts[i]);

		// and sort them ascendingly according to weight
		Arrays.sort(item_objs);

		// then place the items in an array of ints in descending order
		int[] items = new int[num_frequent];
		for (int i = 0; i < num_frequent; i++)
			items[i] = item_objs[num_frequent - i - 1].item;

		// initialize FPTree
		FPTree fpt = new FPTree(items, num_rows, min_weight);

		for (FPTreeNode side_walker = header[entry_index].head; side_walker != null; side_walker = side_walker.next)
			if (side_walker.parent != root) {
				int i = side_walker.parent.seq_num;
				Item[] pattern = new Item[i];
				for (FPTreeNode up_walker = side_walker.parent; up_walker != root; up_walker = up_walker.parent)
					// we store the header index in the count field
					// so that we can use it later
					// to access the count from counts[]
					pattern[--i] = new Item(up_walker.item, up_walker.header_index);

				processPattern(pattern, side_walker.count, fpt, counts);
			}

		return fpt;
	}

	// NOTE: pattern[] elements contain in their count field the
	// header entry index of the corresponding item which also
	// can be used to index the counts[] array
	public void processPattern(Item[] pattern, int count, FPTree fpt, int[] counts) {
		int i, j, num_frequent;
		Item item_obj;
		int[] items;
		Item[] item_objs;

		// how many frequent items are in this pattern?
		for (i = 0, num_frequent = 0; i < pattern.length; i++) {
			item_obj = pattern[i];
			if (counts[item_obj.count] >= min_weight)
				num_frequent++;
		}

		if (num_frequent > 0) {
			// select only frequent items into an array of Items
			// these form a conditional pattern
			item_objs = new Item[num_frequent];
			for (i = 0, j = 0; i < pattern.length; i++) {
				item_obj = pattern[i];
				if (counts[item_obj.count] >= min_weight)
					item_objs[j++] = new Item(item_obj.item, counts[item_obj.count]);
			}

			// sort them
			Arrays.sort(item_objs);

			// get the items in reverse order in an array of ints
			items = new int[num_frequent];
			for (i = 0; i < num_frequent; i++)
				items[i] = item_objs[num_frequent - i - 1].item;

			// insert them in the FPTree
			fpt.insert(items, count);
		}
	}
	
	/**
	 * Mine FPTree for the given items
	 * @param Item[]
	 * @return the number of occurrences of the items in the tree
	 */
	public int findWeight(Item[] item_objs) {
		int counts = 0;
		
		//find the header entry of the less frequent item
		int entry_index = ((Integer) item2index.get(new Integer(item_objs[0].item))).intValue();
		
		//inspect all item sets with this suffix
		for (FPTreeNode siblingWalker = header[entry_index].head; siblingWalker != null; siblingWalker = siblingWalker.next) {
			int i = 1, matched = 1;
			for(FPTreeNode parentgWalker = siblingWalker.parent; parentgWalker != null; parentgWalker = parentgWalker.parent) {
				if(parentgWalker.item == item_objs[i].item) {
					matched++;
					
					if(++i == item_objs.length)
						break;
				}
			}
			
			if(matched == item_objs.length) 
				counts += siblingWalker.count;
			
		}
		
		return counts;
	}

}
