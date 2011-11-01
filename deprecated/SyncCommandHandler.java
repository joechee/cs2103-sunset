package cs2103.aug11.t11j2.fin.parser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.RequestTokenPair;
import com.dropbox.client2.session.WebAuthSession;

import cs2103.aug11.t11j2.fin.application.FinConstants;
import cs2103.aug11.t11j2.fin.errorhandler.FinProductionException;
import cs2103.aug11.t11j2.fin.ui.UIContext;

public class SyncCommandHandler extends ICommandHandler {
	@Override
	@SuppressWarnings("serial")
	public List<String> getCommandStrings() {
		return new ArrayList<String>() {
			{
				add("sync");
				add("dropbox");
				add("db");
				add("syn");				
				add("update");				
				add("commit");				
				add("svn");				
			}
		};
	}

	boolean openDropboxAuth(String url, Display display) {
		if (display == null) return false;
		if (display.getShells().length == 0) return false;
		if (!(display.getShells()[0] instanceof Shell)) return false;
		
		final Shell shell = new Shell(display.getShells()[0]);
		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);
		
		final Browser browser;
		
		try {
			browser = new Browser(shell, SWT.NONE);
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: " + e.getMessage());
			shell.dispose();
			return false;
		}
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.horizontalSpan = 3;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		browser.setLayoutData(data);
		
				
		shell.open();
		browser.setUrl(url);
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		
		return true;
	}
	
	@Override
	CommandResult executeCommands(String command, String arguments,
			UIContext context) throws FinProductionException {
		
		AppKeyPair appKeys = new AppKeyPair(FinConstants.DROPBOX_APP_KEY, FinConstants.DROPBOX_APP_SECRET);
		WebAuthSession session = new WebAuthSession(appKeys, FinConstants.DROPBOX_ACCESS_TYPE);

		DropboxAPI<WebAuthSession> mDBApi = new DropboxAPI<WebAuthSession>(session);
		
		try {			
			WebAuthSession.WebAuthInfo authInfo = mDBApi.getSession().getAuthInfo();
			RequestTokenPair requestTokenPair = authInfo.requestTokenPair;
			
			if (openDropboxAuth(authInfo.url, context.getDisplay())) {
				
				String userUID = mDBApi.getSession().retrieveWebAccessToken(requestTokenPair);
				System.out.println(mDBApi.accountInfo());
	            
				AccessTokenPair tokens = mDBApi.getSession().getAccessTokenPair();
				mDBApi.getSession().setAccessTokenPair(tokens);
				String fileContents = "Hello World!";
				ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContents.getBytes());
				mDBApi.putFile("test",inputStream,fileContents.length(),null,null);
				Entry newEntry = mDBApi.putFile("/testing.txt", inputStream,
			           fileContents.length(), null, null);
			    

			} else {
				// TODO: use cli to open browser
			}
		} catch (DropboxException e) {
			if (FinConstants.IS_DEVELOPMENT) {
				e.printStackTrace();
			}
		}
		
		return new CommandResult(this, arguments, CommandResult.RenderType.ERROR, "Invalid Task Index!");
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HelpTablePair getHelpTablePair() {
		// TODO Auto-generated method stub
		return null;
	}

}
