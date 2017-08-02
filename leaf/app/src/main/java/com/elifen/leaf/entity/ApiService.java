package com.elifen.leaf.entity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Elifen on 2017/5/2.
 */

public interface ApiService {
    @GET("wxnew/")
    Observable<NewsGson> getNewsData(@Query("key")String key, @Query("num") String num, @Query("page") int page);
    @GET("keji/")
    Observable<NewsGson> getTechNewsData(@Query("key")String key, @Query("num") String num, @Query("page") int page);
}
