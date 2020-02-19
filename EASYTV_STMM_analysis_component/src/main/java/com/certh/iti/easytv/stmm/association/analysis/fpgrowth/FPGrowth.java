package com.certh.iti.easytv.stmm.association.analysis.fpgrowth;

import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * <p>
 * Title: FPGrowth
 * </p>
 */
public class FPGrowth {
	
	private final static Logger logger = Logger.getLogger(FPGrowth.class.getName());
	
	private Vector<Itemset> input;
	private FPTree fpt;
	
	// useful information
	private long num_rows;
	private long min_weight;

	private int[] counts; // stores count of items starting from 1

	private int pass_num;
	

	public FPGrowth(Vector<Itemset> input, int[] counts) {
		this.input = input;
		this.counts = counts;
		this.num_rows = input.size();
	}

	public Vector<Itemset> getFrequentItemsets() {
		return fpt.getFrequentItemsets();
	}

	public FPTree getFPTree() {
		return fpt;
	}


	/**
	 * Find the frequent itemsets in a database
	 *
	 * if this is null, then nothing will be saved, this is useful for benchmarking
	 * 
	 * @param minSupport the minimum support
	 * @return the number of passes executed over the database
	 */
	public int findFrequentItemsets(double minSupport) {

		// Calculate min_weight
		min_weight = (long) Math.ceil(num_rows * minSupport);

		// second pass constructs FPTree
		this.fpt = constructFPTree();

		// finally we mine the FPTree using the FP-growth algorithm
		if (fpt != null) {
			logger.info("<FPgrowth>: Weight "+min_weight+" FPTree has " + fpt.getCountNodes() + " nodes");
			fpt.fp_growth(new Itemset());
		} else
			logger.info("<FPgrowth>: FPTree is empty");

		// there will usually be 2 passes unless there are
		// no frequent items, in which case we do only 1 pass
		return pass_num;
	}

	private FPTree constructFPTree() {
		// see how many frequent items there are in the database
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
				item_objs[j++] = new Item(i, counts[i]);

		// and sort them ascendingly according to weight
		Arrays.sort(item_objs);

		// then place the items in an array of ints in descending order
		int[] items = new int[num_frequent];
		for (int i = 0; i < num_frequent; i++)
			items[i] = item_objs[num_frequent - i - 1].item;

		// initialize FPTree
		FPTree fpt = new FPTree(items, num_rows, min_weight);

		//add the items to the fp-tree to be processed	
		for(Itemset itemset : input) {
			//Itemset row = (Itemset) new Itemset(input.get(i).getPreferencesAsItemSet());
						
			//process the given row
			processRow(itemset, fpt);
		}

		pass_num++;

		return fpt;
	}

	/**
	 * Given a row of itemset process the row properly 
	 * 
	 * @param itemset
	 * @param fpt
	 */
	private void processRow(Itemset itemset, FPTree fpt) {
		int i, j, item, num_frequent;
		int[] items;
		Item[] item_objs;

		// how many frequent items are in this row?
		for (i = 0, num_frequent = 0; i < itemset.size(); i++) {
			item = itemset.get(i);
			if (counts[item] >= min_weight)
				num_frequent++;
		}

		if (num_frequent > 0) {
			// select only frequent items into an array of Items
			item_objs = new Item[num_frequent];
			for (i = 0, j = 0; i < itemset.size(); i++) {
				item = itemset.get(i);
				if (counts[item] >= min_weight) {
					item_objs[j++] = new Item(item, counts[item]);
				}
			}

			// sort them
			Arrays.sort(item_objs);

			// get the items in reverse order into an array of ints
			items = new int[num_frequent];
			for (i = 0; i < num_frequent; i++)
				items[i] = item_objs[num_frequent - i - 1].item;

			// insert them in the FPTree
			fpt.insert(items, 1);
		}
	}
}
