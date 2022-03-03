package io.agora.board.fast.internal;

import com.herewhite.sdk.domain.Promise;
import com.herewhite.sdk.domain.SDKError;

import io.agora.board.fast.extension.FastResult;

public class PromiseResultAdapter<T> implements Promise<T> {
    private final FastResult<T> result;

    public PromiseResultAdapter(FastResult<T> result) {
        this.result = result;
    }

    @Override
    public void then(T t) {
        if (result != null) {
            result.onSuccess(t);
        }
    }

    @Override
    public void catchEx(SDKError t) {
        if (result != null) {
            result.onError(t);
        }
    }
}
