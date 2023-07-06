package com.wsj.server.api;

import com.dtflys.forest.annotation.Get;

//@Address(basePath = "https://clashnode.com")
public interface BaseApi {
    @Get("{0}")
    String getRequest(String url);
}
