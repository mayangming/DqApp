
import android.util.Log
import com.dq.sdk.ad.http.bean.HttpBaseBean
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import http.callback.Callback
import okhttp3.Response

abstract class HttpBaseBeanCallBack<T> : Callback<HttpBaseBean<String>>(){
    val gson = Gson()
    override fun parseNetworkResponse(response: Response?, id: Int): HttpBaseBean<String> {
        val result = response?.body?.string() ?: ""
        val type1 = object : TypeToken<HttpBaseBean<String>>() {}.type
        val httpBean = gson.fromJson<HttpBaseBean<String>>(result,type1)
        return httpBean
    }


}