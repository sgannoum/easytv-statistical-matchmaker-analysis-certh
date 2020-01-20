package com.certh.iti.easytv.stmm.association.analysis.fpgrowth;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;



public class Itemset_Tests {
	
    private Itemset is1 = new Itemset();
    private Itemset is2 = new Itemset();
	
	@BeforeTest
	public void beforeTests() {

	    is1.add(7);
	    is1.add(3);
	    is1.add(15);
	    is1.add(5);
	    is1.add(12);
	    is1.add(12);

	    System.out.println("is1: " + is1);

	    is2.add(12);
	    is2.add(15);
	    is2.add(7);
	    is2.add(5);
	    is2.add(3);
	    is2.add(8);
	    
	    System.out.println("is2: " + is2);
	}
	
	@Test
	public void test_intersects() {
		
	    System.out.println("do is1 and is2 share items: "+ is1.intersects(is2));
		System.out.println("do is2 and is1 share items: "+ is2.intersects(is1));
		
		Assert.assertTrue(is1.intersects(is2));
		Assert.assertTrue(is2.intersects(is1));
	}
	
	@Test
	public void test_subtraction() {
		
	    Itemset is3 = Itemset.subtraction(is2, is1);
	    System.out.println("is3 <= subtracting is2 from is1: " + is3);

	    System.out.println("do is1 and is3 share items: "+ is1.intersects(is3));
	    System.out.println("do is3 and is1 share items: "+ is3.intersects(is1));
	    
		Assert.assertFalse(is1.intersects(is3));
		Assert.assertFalse(is3.intersects(is1));
		
		
		is3 = Itemset.subtraction(is1, is2);
	    System.out.println("is3 <= subtracting is1 from is2: " + is3);

	    System.out.println("do is1 and is3 share items: "+ is1.intersects(is3));
	    System.out.println("do is3 and is1 share items: "+ is3.intersects(is1));

	    System.out.println("do is3 and is2 share items: "+ is3.intersects(is2));
	    System.out.println("do is2 and is3 share items: "+ is2.intersects(is3));
		
		Assert.assertFalse(is1.intersects(is3));
		Assert.assertFalse(is3.intersects(is1));
	}
	
	@Test
	public void test_union() {
		
	    Itemset is3 = new Itemset(is1);
	    is3.add(17);
	    System.out.println("is3: " + is3);
	    System.out.println("adding is2 to is3: " + Itemset.union(is3, is2));

	    Assert.assertEquals(Itemset.union(is3, is2), Itemset.union(is2, is3));
	    Assert.assertFalse(is1.equals(is2));
	}
	
	@Test
	public void test_isIncludedIn() {
		
		Itemset is1 = new Itemset(this.is1);
		Itemset is2 = new Itemset(this.is2);
	    Itemset is3 = new Itemset(is1);
	    is3.add(17);

	    System.out.println("is3: " + is3);
	    System.out.println("is3 included in is2: " + is1.isIncludedIn(is2));
	    System.out.println("is2 included in is3: " + is2.isIncludedIn(is3));
	    
	    Assert.assertNotEquals(is2, is3);
	    Assert.assertTrue(is1.isIncludedIn(is2));
	    Assert.assertFalse(is2.isIncludedIn(is3));
	    
	    is1.add(8);

	    System.out.println();
	    System.out.println("is1: " + is1);

	    System.out.println("is1 equal to is2: " + is1.equals(is2));
	    System.out.println("is1 included in is2: " + is1.isIncludedIn(is2));
	    System.out.println("is2 included in is1: " + is2.isIncludedIn(is1));
	    
	    Assert.assertEquals(is1, is2);
	    Assert.assertTrue(is1.isIncludedIn(is2));
	    Assert.assertTrue(is2.isIncludedIn(is1));


	    is1.add(1);

	    System.out.println();
	    System.out.println("is1: " + is1);

	    System.out.println("is1 equal to is2: " + is1.equals(is2));
	    System.out.println("is1 included in is2: " + is1.isIncludedIn(is2));
	    System.out.println("is2 included in is1: " + is2.isIncludedIn(is1));
	    
	    Assert.assertNotEquals(is1, is2);
	    Assert.assertFalse(is1.isIncludedIn(is2));
	    Assert.assertTrue(is2.isIncludedIn(is1));

	    is1.add(50);

	    System.out.println();
	    System.out.println("is1: " + is1);

	    System.out.println("is1 equal to is2: " + is1.equals(is2));
	    System.out.println("is1 included in is2: " + is1.isIncludedIn(is2));
	    System.out.println("is2 included in is1: " + is2.isIncludedIn(is1));

	    is1.add(100);

	    System.out.println();
	    System.out.println("is1: " + is1);

	    System.out.println("is1 equal to is2: " + is1.equals(is2));
	    System.out.println("is1 included in is2: " + is1.isIncludedIn(is2));
	    System.out.println("is2 included in is1: " + is2.isIncludedIn(is1));

	    System.out.println("adding 70 to is2: " + is2.add(70));
	    System.out.println("adding 70 to is2: " + is2.add(70));

	    System.out.println("is2: " + is2);

	    System.out.println("is1 equal to is2: " + is1.equals(is2));
	    System.out.println("is1 included in is2: " + is1.isIncludedIn(is2));
	    System.out.println("is2 included in is1: " + is2.isIncludedIn(is1));

	    System.out.println();
	    System.out.println("removing 1 from is1: " + is1.remove(1));
	    System.out.println("removing 1 from is1: " + is1.remove(1));
	    System.out.println("is1: " + is1);
	    System.out.println("removing 50 from is1: " + is1.remove(50));
	    System.out.println("is1: " + is1);
	    System.out.println("removing 70 from is2: " + is2.remove(70));
	    System.out.println("is2: " + is2);

	}
	
	
	  /**
	   * sample usage and testing
	   */
	  /*public static void main(String[] args)
	  {
	    Itemset is1 = new Itemset();
	    Itemset is2 = new Itemset();

	    is1.add(7);
	    is1.add(3);
	    is1.add(15);
	    is1.add(5);
	    is1.add(12);
	    is1.add(12);

	    System.out.println("is1: " + is1);

	    is2.add(12);
	    is2.add(15);
	    is2.add(7);
	    is2.add(5);
	    is2.add(3);
	    is2.add(8);

	    System.out.println("is2: " + is2);

	    System.out.println("do is1 and is2 share items: "
	                       + is1.intersects(is2));
	    System.out.println("do is2 and is1 share items: "
	                       + is2.intersects(is1));

	    Itemset is3 = Itemset.subtraction(is1, is2);
	    System.out.println("is3 <= subtracting is2 from is1:" + is3);

	    System.out.println("do is1 and is3 share items: "
	                       + is1.intersects(is3));
	    System.out.println("do is3 and is1 share items: "
	                       + is3.intersects(is1));

	    is3 = Itemset.subtraction(is2, is1);
	    System.out.println("is3 <= subtracting is1 from is2:" + is3);

	    System.out.println("do is1 and is3 share items: "
	                       + is1.intersects(is3));
	    System.out.println("do is3 and is1 share items: "
	                       + is3.intersects(is1));

	    System.out.println("do is3 and is2 share items: "
	                       + is3.intersects(is2));
	    System.out.println("do is2 and is3 share items: "
	                       + is2.intersects(is3));

	    is1.add(17);
	    System.out.println("is1: " + is1);
	    System.out.println("is2: " + is2);
	    System.out.println("adding is2 to is1:" + Itemset.union(is1, is2));
	    System.out.println("adding is1 to is2:" + Itemset.union(is2, is1));

	    System.out.println("is1: " + is1);
	    System.out.println("is2: " + is2);
	    System.out.println("is1 equal to is2: " + is1.equals(is2));
	    System.out.println("is1 included in is2: " + is1.isIncludedIn(is2));
	    System.out.println("is2 included in is1: " + is2.isIncludedIn(is1));

	    is1.add(8);

	    System.out.println("is1: " + is1);

	    System.out.println("is1 equal to is2: " + is1.equals(is2));
	    System.out.println("is1 included in is2: " + is1.isIncludedIn(is2));
	    System.out.println("is2 included in is1: " + is2.isIncludedIn(is1));

	    is1.add(1);

	    System.out.println("is1: " + is1);

	    System.out.println("is1 equal to is2: " + is1.equals(is2));
	    System.out.println("is1 included in is2: " + is1.isIncludedIn(is2));
	    System.out.println("is2 included in is1: " + is2.isIncludedIn(is1));

	    is1.add(50);

	    System.out.println("is1: " + is1);

	    System.out.println("is1 equal to is2: " + is1.equals(is2));
	    System.out.println("is1 included in is2: " + is1.isIncludedIn(is2));
	    System.out.println("is2 included in is1: " + is2.isIncludedIn(is1));

	    is1.add(100);

	    System.out.println("is1: " + is1);

	    System.out.println("is1 equal to is2: " + is1.equals(is2));
	    System.out.println("is1 included in is2: " + is1.isIncludedIn(is2));
	    System.out.println("is2 included in is1: " + is2.isIncludedIn(is1));

	    System.out.println("adding 70 to is2: " + is2.add(70));
	    System.out.println("adding 70 to is2: " + is2.add(70));

	    System.out.println("is2: " + is2);

	    System.out.println("is1 equal to is2: " + is1.equals(is2));
	    System.out.println("is1 included in is2: " + is1.isIncludedIn(is2));
	    System.out.println("is2 included in is1: " + is2.isIncludedIn(is1));

	    System.out.println("removing 1 from is1: " + is1.remove(1));
	    System.out.println("removing 1 from is1: " + is1.remove(1));
	    System.out.println("is1: " + is1);
	    System.out.println("removing 50 from is1: " + is1.remove(50));
	    System.out.println("is1: " + is1);
	    System.out.println("removing 70 from is2: " + is2.remove(70));
	    System.out.println("is2: " + is2);

	    System.out.print("going through items of is1:");
	    for (int i = 0; i < is1.size(); i++)
	      System.out.print(" " + is1.get(i));
	    System.out.println("");

	    System.out.print("going through items of is2:");
	    for (int i = 0; i < is2.size(); i++)
	      System.out.print(" " + is2.get(i));
	    System.out.println("");

	    while (is2.removeLast())
	      ;
	    System.out.println("is2: " + is2);

	    System.out.println("mark is1, previous state: " + is1.mark());
	    System.out.println("mark is1, previous state: " + is1.mark());
	    System.out.println("is1 mark state: " + is1.isMarked());
	    System.out.println("unmark is1, previous state: " + is1.unmark());
	    System.out.println("unmark is1, previous state: " + is1.unmark());
	  }*/
	

}
