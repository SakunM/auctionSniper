package test.integration.auctionsniper.ui;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import test.endtoend.auctionsniper.AuctionSniperDriver;
import auctionsniper.Item;
import auctionsniper.SniperPortfolio;
import auctionsniper.UserRequestListener;
import auctionsniper.ui.MainWindow;

import com.objogate.wl.swing.probe.ValueMatcherProbe;

public class MainWindowTest {
	private final SniperPortfolio portfolio = new SniperPortfolio();
	private final MainWindow mainWindow = new MainWindow(portfolio);
	private final AuctionSniperDriver driver = new AuctionSniperDriver(100);

	@Test public void
	makeUserRequestWhenJoinButtonClicked() {
		final ValueMatcherProbe<String> buttonProbe = new ValueMatcherProbe<String>(equalTo("an item-id"), "join request");
		mainWindow.addUserRequestListener(new UserRequestListener() {

			@Override
			public void joinAuction(Item item) {
				buttonProbe.setReceivedValue(item.identifier);
			}
		});
		driver.startBiddingFor("an item-id", Integer.MAX_VALUE);
		driver.check(buttonProbe);
	}
}
