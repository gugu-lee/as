
package net.x_talker.as.im.load;

import net.x_talker.as.im.container.entity.XTalkerSipMsg;


public interface MessageLoad {


	public boolean loadRegularlyMessage();


	public boolean loadCacheMessage();


	public boolean loadSendMessage();


	public boolean loadSendedMessage();
	
	public XTalkerSipMsg loadSingleXTalkerSipMsgFromFile(String callID);

}
