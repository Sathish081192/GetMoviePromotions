package pojo;

import java.util.List;

public class Promotions {
	private String promotionId;
	private int orderId;
	private String promoType;
	private boolean showPrice;
	private boolean showText;
	private List<Images> images;
	private List<String> promoArea;
	private LocalizedTexts localizedTexts;
	private List<Properties> properties;
	
	public String getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getPromoType() {
		return promoType;
	}
	public void setPromoType(String promoType) {
		this.promoType = promoType;
	}
	public boolean isShowPrice() {
		return showPrice;
	}
	public void setShowPrice(boolean showPrice) {
		this.showPrice = showPrice;
	}
	public boolean isShowText() {
		return showText;
	}
	public void setShowText(boolean showText) {
		this.showText = showText;
	}
	public List<Images> getImages() {
		return images;
	}
	public void setImages(List<Images> images) {
		this.images = images;
	}
	public List<String> getPromoArea() {
		return promoArea;
	}
	public void setPromoArea(List<String> promoArea) {
		this.promoArea = promoArea;
	}
	public LocalizedTexts getLocalizedTexts() {
		return localizedTexts;
	}
	public void setLocalizedTexts(LocalizedTexts localizedTexts) {
		this.localizedTexts = localizedTexts;
	}
	public List<Properties> getProperties() {
		return properties;
	}
	public void setProperties(List<Properties> properties) {
		this.properties = properties;
	}
	
	
	
	
	

}
