package com.certh.iti.easytv.stmm.similarity;

import java.util.Vector;
import java.util.logging.Logger;

import com.certh.iti.easytv.stmm.similarity.dimension.AsymmetricBinary;
import com.certh.iti.easytv.stmm.similarity.dimension.Dimension;
import com.certh.iti.easytv.stmm.similarity.dimension.MultiNominal;
import com.certh.iti.easytv.stmm.similarity.dimension.MultiNumeric;
import com.certh.iti.easytv.stmm.similarity.dimension.Nominal;
import com.certh.iti.easytv.stmm.similarity.dimension.Numeric;
import com.certh.iti.easytv.stmm.similarity.dimension.Ordinal;
import com.certh.iti.easytv.stmm.similarity.dimension.SymmetricBinary;
import com.certh.iti.easytv.stmm.similarity.dimension.Time;
import com.certh.iti.easytv.user.preference.attributes.AsymmetricBinaryAttribute;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.ColorAttribute;
import com.certh.iti.easytv.user.preference.attributes.MultiNominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.NumericAttribute;
import com.certh.iti.easytv.user.preference.attributes.OrdinalAttribute;
import com.certh.iti.easytv.user.preference.attributes.SymmetricBinaryAttribute;
import com.certh.iti.easytv.user.preference.attributes.TimeAttribute;

public class DimensionsGenerator {

	private final static Logger logger = Logger.getLogger(DimensionsGenerator.class.getName());

	protected Dimension[] dimensions;
	protected String[] lables;
	
	public DimensionsGenerator(Vector<String> lable, Vector<Attribute> operands) {
		this.lables = new String[operands.size()];
		this.dimensions = new Dimension[operands.size()];
		String msg = "\n";

		for (int i = 0; i < operands.size(); i++) {
			Attribute operand = operands.get(i);
			this.lables[i] = lable.get(i);
			
			if(ColorAttribute.class.isInstance(operand)) {
				ColorAttribute colorAttribute = (ColorAttribute) operand;
				Numeric[] subDimensions = new Numeric[colorAttribute.getDimensions().length];

				int index = 0;
				for (NumericAttribute attribte : colorAttribute.getDimensions()) 
					subDimensions[index++] = new Numeric(attribte.getOperandMissingValue(), attribte.getMaxValue(), attribte.getMinValue());
				
				dimensions[i] = new MultiNumeric(subDimensions, 8);
				
			} else if (NumericAttribute.class.isInstance(operand)) {
				NumericAttribute numeric = (NumericAttribute) operand;
				dimensions[i] = new Numeric(numeric.getOperandMissingValue(), numeric.getMaxValue(), numeric.getMinValue());
				
			} else if (OrdinalAttribute.class.isInstance(operand)) {
				OrdinalAttribute ordinal = (OrdinalAttribute) operand;
				dimensions[i] = new Ordinal(ordinal.getStates().length - 1, ordinal.getMaxValue(), ordinal.getMinValue());
				
			} else if (MultiNominalAttribute.class.isInstance(operand)) {
				MultiNominalAttribute multi = (MultiNominalAttribute) operand;
				dimensions[i] = new MultiNominal(multi.getStates().length, multi.getOperandMissingValue());
				
			} else if (NominalAttribute.class.isInstance(operand)) {
				dimensions[i] = new Nominal(operand.getOperandMissingValue());
				
			} else if (SymmetricBinaryAttribute.class.isInstance(operand)) {
				dimensions[i] = new SymmetricBinary(operand.getOperandMissingValue());

			} else if (AsymmetricBinaryAttribute.class.isInstance(operand)) {
				dimensions[i] = new AsymmetricBinary();
				
			} else if(TimeAttribute.class.isInstance(operand)) {
				dimensions[i] = new Time();
				
			} else {
				throw new IllegalStateException("Unknown operand type: "+operand.toString()+" associated with dimension: "+ lable.get(i));
			}
			
			
			msg +=  String.format("%s\t%s => %s\n", lable.get(i), operand.getClass().getSimpleName(), dimensions[i].getClass().getSimpleName());
			
		}
		
		
		logger.info("Dimensions lables are..." + msg);

	}
	
	public Dimension[] getDimensions() {
		return dimensions;
	}
	
	public String[] getLables() {
		return lables;
	}

}
