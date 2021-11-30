package com.nrlm.lakhpatikisaan.network.client;

import com.nrlm.lakhpatikisaan.network.model.response.LoginResponseBean;

public interface ServiceCallback<T> {
   void success(Result<T> successResponse);
   void error(Result<T> errorResponse);

    }
