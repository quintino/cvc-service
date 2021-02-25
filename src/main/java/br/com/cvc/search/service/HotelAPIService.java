package br.com.cvc.search.service;

import br.com.cvc.search.model.Hotel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HotelAPIService {
    private Gson gson = new Gson();

    @Value("${api.hotel.url}")
    private String url;

    private HttpEntity requestGet(final String complement) {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final HttpGet httpGet = new HttpGet(this.url + complement);
        try {
            final HttpResponse httpResponse = httpClient.execute(httpGet);
            return httpResponse.getEntity();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
        return null;
    }

    public List<Hotel> getHotelsByCityId(final String cityId) {
        final HttpEntity responseEntity = this.requestGet("avail/" + cityId);
        if (responseEntity != null) {
            try {
                return this.gson.fromJson(EntityUtils.toString(responseEntity), new TypeToken<List<Hotel>>(){}.getType());
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    public Hotel getByHotelId(final String hotelId) {
        final HttpEntity responseEntity = this.requestGet(hotelId);
        if (responseEntity != null) {
            try {
                final List<Hotel> result = this.gson.fromJson(EntityUtils.toString(responseEntity), new TypeToken<List<Hotel>>(){}.getType());
                if (result != null && result.size() > 0) {
                    return result.get(0);
                }
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        }
        return null;
    }

}
