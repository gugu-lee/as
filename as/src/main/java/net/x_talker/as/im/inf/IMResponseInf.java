package net.x_talker.as.im.inf;

import javax.sip.message.Response;


public interface IMResponseInf {


	public void onErrorResponse(Response resp);


	public void onSuccessResponse(Response resp);
}
