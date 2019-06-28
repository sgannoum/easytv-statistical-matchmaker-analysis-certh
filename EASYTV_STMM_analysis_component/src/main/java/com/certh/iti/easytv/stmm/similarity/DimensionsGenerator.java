package com.certh.iti.easytv.stmm.similarity;

import com.certh.iti.easytv.stmm.similarity.dimension.AsymmetricBinary;
import com.certh.iti.easytv.stmm.similarity.dimension.Dimension;
import com.certh.iti.easytv.stmm.similarity.dimension.Nominal;
import com.certh.iti.easytv.stmm.similarity.dimension.Numeric;
import com.certh.iti.easytv.stmm.similarity.dimension.Ordinal;
import com.certh.iti.easytv.stmm.similarity.dimension.SymmetricBinary;
import com.certh.iti.easytv.user.preference.attributes.AsymmetricBinaryAttribute;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.NumericAttribute;
import com.certh.iti.easytv.user.preference.attributes.OrdinalAttribute;
import com.certh.iti.easytv.user.preference.attributes.SymmetricBinaryAttribute;

public class DimensionsGenerator {

	protected Dimension[] dimensions;
	
	public DimensionsGenerator(Attribute[] operands) {
		dimensions = new Dimension[operands.length];
		int i = 0;
		for (Attribute operand : operands) {
			Dimension d = null; 
			
			if (NumericAttribute.class.isInstance(operand)) {
				NumericAttribute numeric = (NumericAttribute) operand;
				d = new Numeric(numeric.getOperandMissingValue(), numeric.getMaxValue(), numeric.getMinValue());
				
			} else if (OrdinalAttribute.class.isInstance(operand)) {
				OrdinalAttribute ordinal = (OrdinalAttribute) operand;
				d = new Ordinal(ordinal.getStates().length - 1, ordinal.getMaxValue(), ordinal.getMinValue());
				
			} else if (NominalAttribute.class.isInstance(operand)) {
				d = new Nominal(operand.getOperandMissingValue());
				
			} else if (SymmetricBinaryAttribute.class.isInstance(operand)) {
				d = new SymmetricBinary(operand.getOperandMissingValue());

			} else if (AsymmetricBinaryAttribute.class.isInstance(operand)) {
				d = new AsymmetricBinary();
			}
			
			dimensions[i++] = d;
		}
	}
	
	public Dimension[] getDimensions() {
		return dimensions;
	}

}
