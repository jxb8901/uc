/**
 * 
 * created on 2007-3-28
 */
package net.ninecube.venus.domain;

import java.math.BigDecimal;

import net.ninecube.core.domain.Entity;

/**
 * 
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class  table="TGift" 
 * @author jxb
 * 
 */
public class Gift implements Entity {

	private Long idno;
	/** 序号 */
	private String seqno;
	private String name;
	/** 图片，使用字节数组保存 */
	private byte[] image;
	private String imageFileName;
	/** 市场价格 */
	private BigDecimal marketPrice;
	/** 成本价格 */
	private BigDecimal costPrice;
	/** 可用数量 */
	private int availableQuantity;
	/** 兑换规则 */
	private String exchangeRule;
	private String description;
	
	//~ getter and setter

	/**
	 * @hibernate.property
	 */
	public int getAvailableQuantity() {
		return availableQuantity;
	}
	public void setAvailableQuantity(int availableQuantity) {
		this.availableQuantity = availableQuantity;
	}
	/**
	 * @hibernate.property
	 */
	public BigDecimal getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}
	/**
	 * @hibernate.property
	 */
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @hibernate.property
	 */
	public String getExchangeRule() {
		return exchangeRule;
	}
	public void setExchangeRule(String exchangeRule) {
		this.exchangeRule = exchangeRule;
	}
	/**
	 * @hibernate.id  generator-class="native"
	 */
	public Long getIdno() {
		return idno;
	}
	public void setIdno(Long idno) {
		this.idno = idno;
	}
	/**
	 * @hibernate.property
	 */
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}
	/**
	 * @hibernate.property
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @hibernate.property unique="true"
	 */
	public String getSeqno() {
		return seqno;
	}
	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}
	/**
	 * @hibernate.property length="1116777216" type="org.springframework.orm.hibernate3.support.BlobByteArrayType"
	 */
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	/**
	 * @hibernate.property
	 */
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
}
