package org.clockworx.hyperconomy.display;

import org.clockworx.hyperconomy.minecraft.HLocation;
import org.clockworx.hyperconomy.shop.Shop;
import org.clockworx.hyperconomy.tradeobject.TradeObject;

public interface FrameShopHandler {
	public boolean frameShopExists(HLocation l);
	public void removeFrameShop(HLocation l);
	public void createFrameShop(HLocation l, TradeObject to, Shop s);
	public void removeFrameShops(TradeObject to);
}
