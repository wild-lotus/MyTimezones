package com.carlosgines.mytimezones.data.datastores.requests;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class SigninReq extends Req {

    // ========================================================================
    // Member variables
    // ========================================================================

    private final String mUserName;
    private final String mPassword;

    // ========================================================================
    // Member variables
    // ========================================================================

    public SigninReq(final String userName, final String password) {
        mUserName = userName;
        mPassword = password;
    }

    // ========================================================================
    // Public methods
    // ========================================================================

    public String signin(final Context ctx) {
        try {
            return super.send(ctx).getString(Contract.RES_TOKEN);
        } catch (ExecutionException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof AuthFailureError) {
                return "";
            } else {
                super.handleExecutionException(e);
                throw null;
            }
        } catch (JSONException e) {
            throw new RuntimeException("JSON exception at " + getRoute(), e);
        }
    }

    // ========================================================================
    // Req implementation
    // ========================================================================

    @Override
    public String getRoute() {
        return Contract.ROUTE;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public JSONObject getJsonRequest() throws JSONException {
        return new JSONObject()
                .put(Contract.REQ_USERNAME, mUserName)
                .put(Contract.REQ_PASSWORD, mPassword);
    }

    // ========================================================================
    // Request contract
    // ========================================================================

    /**
     * Request contract
     */
    public static abstract class Contract {

        private static final String ROUTE = "signin";

        private static final String REQ_USERNAME = "username";
        private static final String REQ_PASSWORD = "password";

        private static final String RES_TOKEN = "token";
    }
}