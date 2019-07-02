package com.certh.iti.easytv.stmm.similarity;

import com.certh.iti.easytv.stmm.similarity.dimension.AsymmetricBinary;
import com.certh.iti.easytv.stmm.similarity.dimension.Dimension;
import com.certh.iti.easytv.stmm.similarity.dimension.Nominal;
import com.certh.iti.easytv.stmm.similarity.dimension.Numeric;
import com.certh.iti.easytv.stmm.similarity.dimension.Ordinal;
import com.certh.iti.easytv.stmm.similarity.dimension.SymmetricBinary;
import com.certh.iti.easytv.user.preference.attributes.AsymmetricBinaryAttribute;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.ColorAttribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.NumericAttribute;
import com.certh.iti.easytv.user.preference.attributes.OrdinalAttribute;
import com.certh.iti.easytv.user.preference.attributes.SymmetricBinaryAttribute;

public class DimensionsGenerator {

	protected Dimension[] dimensions;
	
	public DimensionsGenerator(Attribute[] operands) {
		int length = 0, i = 0;
		for(Attribute attribute : operands)
			length += attribute.getDimensionsNumber();
		
		dimensions = new Dimension[length];
		for (Attribute operand : operands) {
			
			if(ColorAttribute.class.isInstance(operand)) {
				ColorAttribute colorAttribute = (ColorAttribute) operand;
				
				for (NumericAttribute attribte : colorAttribute.getDimensions()) 
					dimensions[i++] = new Numeric(attribte.getOperandMissingValue(), attribte.getMaxValue(), attribte.getMinValue());
				
			} else if (NumericAttribute.class.isInstance(operand)) {
				NumericAttribute numeric = (NumericAttribute) operand;
				dimensions[i++] = new Numeric(numeric.getOperandMissingValue(), numeric.getMaxValue(), numeric.getMinValue());
				
			} else if (OrdinalAttribute.class.isInstance(operand)) {
				OrdinalAttribute ordinal = (OrdinalAttribute) operand;
				dimensions[i++] = new Ordinal(ordinal.getStates().length - 1, ordinal.getMaxValue(), ordinal.getMinValue());
				
			} else if (NominalAttribute.class.isInstance(operand)) {
				dimensions[i++] = new Nominal(operand.getOperandMissingValue());
				
			} else if (SymmetricBinaryAttribute.class.isInstance(operand)) {
				dimensions[i++] = new SymmetricBinary(operand.getOperandMissingValue());

			} else if (AsymmetricBinaryAttribute.class.isInstance(operand)) {
				dimensions[i++] = new AsymmetricBinary();
			}
			
		}
	}
	
	public Dimension[] getDimensions() {
		return dimensions;
	}

}
