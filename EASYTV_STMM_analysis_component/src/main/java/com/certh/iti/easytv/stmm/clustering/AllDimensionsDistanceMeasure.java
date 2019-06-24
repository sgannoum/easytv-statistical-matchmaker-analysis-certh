package com.certh.iti.easytv.stmm.clustering;

import org.apache.commons.math3.ml.distance.DistanceMeasure;

import com.certh.iti.easytv.stmm.similarity.SimilarityMeasure;
import com.certh.iti.easytv.stmm.similarity.dimension.AsymmetricBinary;
import com.certh.iti.easytv.stmm.similarity.dimension.Dimension;
import com.certh.iti.easytv.stmm.similarity.dimension.Nominal;
import com.certh.iti.easytv.stmm.similarity.dimension.Numeric;
import com.certh.iti.easytv.stmm.similarity.dimension.Ordinal;
import com.certh.iti.easytv.stmm.similarity.dimension.SymmetricBinary;
import com.certh.iti.easytv.user.preference.operand.NumericLiteral;
import com.certh.iti.easytv.user.preference.operand.OperandLiteral;
import com.certh.iti.easytv.user.preference.operand.OperandLiteral.Type;
import com.certh.iti.easytv.user.preference.operand.OrdinalLiteral;

public class AllDimensionsDistanceMeasure {

	protected DistanceMeasure dist;

	protected AllDimensionsDistanceMeasure() {}
	
	public AllDimensionsDistanceMeasure(OperandLiteral[] operands) {
		Dimension[] dimensions = new Dimension[operands.length];

		for (int i = 0; i < operands.length; i++) {
			OperandLiteral operand = operands[i];

			if (operand.getType() == Type.Nominal) {
				dimensions[i] = new Nominal(operand.getOperandMissingValue());
			} else if (operand.getType() == Type.Asymetric_Binary) {
				dimensions[i] = new AsymmetricBinary();
			} else if (operand.getType() == Type.Symmetric_Binary) {
				dimensions[i] = new SymmetricBinary(operand.getOperandMissingValue());
			} else if (operand.getType() == Type.Ordinal) {
				OrdinalLiteral ordinal = (OrdinalLiteral) operand;
				dimensions[i] = new Ordinal(ordinal.getOperandMissingValue(), ordinal.getMaxValue(), ordinal.getMinValue());
			} else if (operand.getType() == Type.Numeric) {
				NumericLiteral numeric = (NumericLiteral) operand;
				dimensions[i] = new Numeric(numeric.getOperandMissingValue(), numeric.getMaxValue(), numeric.getMinValue());
			}
		}

		dist = new SimilarityMeasure(dimensions);
	}
	
	public DistanceMeasure getDistanceMeasure() {
		return dist;
	}

}
