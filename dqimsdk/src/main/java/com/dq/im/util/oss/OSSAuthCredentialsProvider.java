package com.dq.im.util.oss;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.common.OSSConstants;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.common.utils.IOUtils;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OSSAuthCredentialsProvider extends OSSFederationCredentialProvider {

    private String mAuthServerUrl;
    private com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider.AuthDecoder mDecoder;

    public OSSAuthCredentialsProvider(String authServerUrl) {
        this.mAuthServerUrl = authServerUrl;
    }

    /**
     * set auth server url
     *
     * @param authServerUrl
     */
    public void setAuthServerUrl(String authServerUrl) {
        this.mAuthServerUrl = authServerUrl;
    }

    /**
     * set response data decoder
     *
     * @param decoder
     */
    public void setDecoder(com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider.AuthDecoder decoder) {
        this.mDecoder = decoder;
    }

    @Override
    public OSSFederationToken getFederationToken() throws ClientException {
        OSSFederationToken authToken;
        String authData;
        try {
            URL stsUrl = new URL(mAuthServerUrl);
            HttpURLConnection conn = (HttpURLConnection) stsUrl.openConnection();
            conn.setConnectTimeout(10000);
            InputStream input = conn.getInputStream();
            authData = IOUtils.readStreamAsString(input, OSSConstants.DEFAULT_CHARSET_NAME);
            if (mDecoder != null) {
                authData = mDecoder.decode(authData);
            }
            JSONObject jsonObj = new JSONObject(authData);
            Log.e("YM","获取的临时TOKEN:"+jsonObj.toString());
            int statusCode = jsonObj.getInt("status");
            if (statusCode == 1) {
                JSONObject data = jsonObj.getJSONObject("data");
                String ak = data.getString("accessKeyId");
                String sk = data.getString("accessKeySecret");
//                String ak = "LTAI4FfBfzJtxaoW1QgLdaXF";
//                String sk = "ljijX0VWObA1iySyJF5s3aOXO24wms";
                String token = data.getString("securityToken");
                String expiration = data.getString("expiration");
                authToken = new OSSFederationToken(ak, sk, token, expiration);
            } else {
                String errorCode = jsonObj.getString("ErrorCode");
                String errorMessage = jsonObj.getString("ErrorMessage");
                throw new ClientException("ErrorCode: " + errorCode + "| ErrorMessage: " + errorMessage);
            }
            return authToken;
        } catch (Exception e) {
            throw new ClientException(e);
        }
    }

    public interface AuthDecoder {
        String decode(String data);
    }
}
