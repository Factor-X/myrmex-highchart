package controllers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ChartDTO;
import dto.DTO;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Application extends Controller {


    final static public String URL_HIGHCHART = "http://localhost:3000/";


    public Result index() {
        return ok(index.render());
    }


    protected static <T extends DTO> T extractDTOFromRequest(Class<T> DTOclass) {
        JsonNode json = request().body().asJson();
        T dto = DTO.getDTO(json, DTOclass);
        if (dto == null) {
            throw new RuntimeException("error extract DTO");
        }
        return dto;
    }

    public Result generateChart() {

        ChartDTO chartDTO  = extractDTOFromRequest(ChartDTO.class);

        //build content
        String content = "{" +
                //the type can be png or svg (or other, not tested)
                "\"type\":\""+chartDTO.getType()+"\"," +
                //infile must be a string build like a JSON => parse the " (here convert to ')
                "\"infile\":\""+chartDTO .getOptions().replaceAll("\"","'")+"\"" +
                "}";
        // => content must be a json => can use a JSON, not tested here

        HttpClient httpClient = new DefaultHttpClient();

        try {
            //build request
            HttpPost request = new HttpPost(URL_HIGHCHART);
            request.addHeader("content-type", "application/json");
            StringEntity stringEntity = new StringEntity(content);
            request.setEntity(stringEntity);
            HttpResponse response = httpClient.execute(request);

            String img = EntityUtils.toString(response.getEntity(), "UTF-8");

            Logger.info(img);
            return ok(img);

            // handle response here...
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return ok(index.render());
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }



}
