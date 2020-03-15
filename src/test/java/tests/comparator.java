package tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * created by yash.zanwar on 2020-03-15
 */
public class comparator {
    static File file1 = new File("src/test/resources/file1");
    static File file2 = new File("src/test/resources/file2");

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<Integer, ArrayList<Object>> hashMap = getResponses(file1);
        HashMap<Integer, ArrayList<Object>> hashMap1 = getResponses(file2);
        for (Map.Entry<Integer, ArrayList<Object>> entry : hashMap.entrySet()) {
            try {
                if (mapper.readTree(String.valueOf(entry.getValue().get(1))).equals(mapper.readTree(String.valueOf(hashMap1.get(entry.getKey()).get(1)))))
                    System.out.println(entry.getValue().get(0) + " equals " + hashMap1.get(entry.getKey()).get(0));
                else System.out.println(entry.getValue().get(0) + " not equals " + hashMap1.get(entry.getKey()).get(0));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException ignored) {

            }
        }
    }

    private static HashMap<Integer, ArrayList<Object>> getResponses(File file) {
        HashMap<Integer, ArrayList<Object>> hashMap = new HashMap<Integer, ArrayList<Object>>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String st = null;
        int i = 0;
        while (true) {
            try {
                assert br != null;
                if ((st = br.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            RestAssured.baseURI = st;
            RequestSpecification httpRequest = RestAssured.given();
            Response response = httpRequest.request(Method.GET);
            JsonNode jsonNode = response.as(JsonNode.class);
            ArrayList<Object> arrayList = new ArrayList<Object>();
            arrayList.add(st);
            arrayList.add(jsonNode);
            hashMap.put(i, arrayList);
            i++;
        }
        return hashMap;
    }
}
