/**
 * 
 * created on 2007-5-27
 */
package net.ninecube.venus.query.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ninecube.formula.Formula;
import net.ninecube.venus.domain.Metric;
import net.ninecube.venus.query.Dimension;
import net.ninecube.venus.query.EvaluationValues.MetricInfo;

/**
 * 
 * @author jxb
 */
public class MetricInfoImpl implements MetricInfo {
	public final static BigDecimal ZERO = new BigDecimal(0);
	private Metric metric;
	private List<DimensionType> types = new ArrayList<DimensionType>();
	private BigDecimal[] totalValuesByDate;

	public MetricInfoImpl(Metric metric, int size) {
		this.metric = metric;
		this.totalValuesByDate = new BigDecimal[size];
		for (int i = 0; i < size; i++) this.totalValuesByDate[i] = ZERO;
	}

	public void addValue(int dateIndex, Dimension dimension, BigDecimal value) {
		if (value == null) value = ZERO;
		if (Dimension.isDefault(dimension)) {
			this.totalValuesByDate[dateIndex] = value;
		}
		else {
			getDimension(dimension).values[dateIndex] = value;
		}
	}
	
	private DimensionValue getDimension(Dimension dim) {
		DimensionValue ret = null;
		for (DimensionType t : types) {
			if (t.type.equals(dim.getType())) {
				for (DimensionValue d : t.dimensions) {
					if (d.dimension.equals(dim.getValue())) {
						return d;
					}
				}
				return createDimensionValue(t, dim.getValue());
			}
		}
		return createDimensionValue(dim);
	}
	
	private DimensionValue createDimensionValue(Dimension dim) {
		DimensionType t = new DimensionType();
		t.type = dim.getType();
		types.add(t);
		return createDimensionValue(t, dim.getValue());
	}
	
	private DimensionValue createDimensionValue(DimensionType t, String dim) {
		DimensionValue ret = new DimensionValue();
		ret.dimension = dim;
		ret.values = new BigDecimal[this.totalValuesByDate.length];
		for (int i = 0; i < ret.values.length; i++) ret.values[i] = ZERO;
		t.dimensions.add(ret);
		return ret;
	}

	public String[] getDimensionTypes() {
		String[] ret = new String[types.size()];
		int i = 0; for (DimensionType t : types) ret[i++] = t.type;
		return ret;
	}

	public String[] getDimensions(String type) {
		for (DimensionType t : types) if (t.type.equals(type)) {
			List<String> ret = new ArrayList<String>();
			for (DimensionValue dim : t.dimensions) ret.add(dim.dimension);
			return ret.toArray(new String[0]);
		}
		throw new IllegalArgumentException("invalid type: '" + type + "' in " + types);
	}

	public String[] getDimensions() {
		List<String> ret = new ArrayList<String>();
		for (DimensionType type : types)
			for (DimensionValue dim : type.dimensions)
				ret.add(dim.dimension);
		return ret.toArray(new String[ret.size()]);
	}

	public Metric getMetric() {
		return metric;
	}
	
	public BigDecimal[] getTotalValues() {
		return this.totalValuesByDate;
	}
	
	public BigDecimal[] getDimensionValues(String type, String dimension) {
		if (Dimension.isDefault(type, dimension)) {
			return this.totalValuesByDate;
		}
		for (DimensionType t : this.types)
			if (t.type.equals(type))
				for (DimensionValue dim : t.dimensions)
					if (dim.dimension.equals(dimension))
						return dim.values;
		throw new IllegalArgumentException("invalid dimension: '" + type + "' , '" + dimension + "'");
	}
	
	public BigDecimal[] getValuesByDate(int dateIndex) {
		List<BigDecimal> ret = new ArrayList<BigDecimal>();
		ret.add(this.totalValuesByDate[dateIndex]);
		for (DimensionType t : this.types)
			for (DimensionValue dim : t.dimensions)
				ret.add(dim.values[dateIndex]);
		return ret.toArray(new BigDecimal[0]);
	}

	private static class DimensionType {
		private String type;
		private List<DimensionValue> dimensions = new ArrayList<DimensionValue>();
		public String toString() { return type; }
	}
	
	private static class DimensionValue {
		private String dimension;
		private BigDecimal[] values;
		public String toString() { return dimension; }
	}
}
