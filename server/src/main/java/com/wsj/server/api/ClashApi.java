package com.wsj.server.api;

import com.dtflys.forest.annotation.Address;
import com.dtflys.forest.annotation.Get;

//@Address(basePath = "https://clashnode.com")
public interface ClashApi {
    @Get("{0}")
    String getRequest(String url);
}
