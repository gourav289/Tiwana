package com.gk.myapp.interfaces;

import com.gk.myapp.model.ProductListResponse;

/**
 * Created by Gk on 04-02-2018.
 */
public interface ExpEdListener {
    void onOkClick(ProductListResponse.ProductAttributes obj,String type,int text);
}
