package com.nrlm.lakhpatikisaan.repository;

import com.nrlm.lakhpatikisaan.network.client.Result;

public interface RepositoryCallback<T> {
    void onComplete(Result<T> result);
}
