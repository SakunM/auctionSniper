package auctionsniper.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.util.Announcer;

public final class XMPPAuction implements Auction {
	private final Announcer<AuctionEventListener> auctionEventListeners =
			Announcer.to(AuctionEventListener.class);
	private final Chat chat;
	public static final String AUCTION_RESOURCE = "Auction";
	private static final String ITEM_ID_AS_LOGIN = "auction-%s";
	private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: Bid; Price: %d;";

	public XMPPAuction(XMPPConnection connection, String itemId) {
		chat = connection.getChatManager().
				createChat(auctionId(itemId, connection),
						new AuctionMessageTranslator(
								connection.getUser(),
								auctionEventListeners.announce()));
	}

	@Override
	public void bid(int amount) {
		sendMessage(String.format(BID_COMMAND_FORMAT, amount));
	}

	@Override
	public void join() {
		sendMessage(JOIN_COMMAND_FORMAT);
	}

	@Override
	public void addAuctionEventListener(AuctionEventListener listener) {
		auctionEventListeners.addListener(listener);
	}

	private void sendMessage(final String message) {
		try {
			chat.sendMessage(message);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String auctionId(String itemId, XMPPConnection connection) {
		return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
	}
};