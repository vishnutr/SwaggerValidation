import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

public class valApp {

	public static void main(String[] args) throws FileNotFoundException, IOException, org.json.simple.parser.ParseException, JSONException, GeneralSecurityException {
		// TODO Auto-generated method stub
		String jsonInput = readFile("applications.json");

		//Hardcoded path
		String pathToCheck = "/apps/{app_name_or_uuid}/token";
		String meThodtype = "GET";
		HashMap parametersMap = new HashMap<String,String>();
		parametersMap.put("grant_type", "");
		parametersMap.put("app_name_or_uuid", "");

		JSONObject outerObject = new JSONObject(jsonInput);
		JSONObject innerObject = outerObject.getJSONObject("JObjects");
		JSONArray getArray = innerObject.getJSONArray("apis");



		for (int i = 0; i < getArray.length(); i++) {
			JSONObject objects = getArray.getJSONObject(i);

			if(!isThisPath(pathToCheck,objects.getString("path"))){
				continue;
			}

			System.out.println("Path "+objects.getString("path"));

			JSONArray operations = objects.getJSONArray("operations");

			for(int j=0;j<operations.length();j++){
				JSONObject opObjects = operations.getJSONObject(j);

				if(!isThisMethodType(meThodtype,opObjects.get("httpMethod").toString())){
					continue;
				}
				System.out.println("Method"+opObjects.get("httpMethod"));


				JSONArray parameters = opObjects.getJSONArray("parameters");

				for(int k=0;k<parameters.length();k++){
					JSONObject parObj = parameters.getJSONObject(k);
					if(parObj.getBoolean("required")){
						if(!parametersMap.containsKey(parObj.get("name"))){
							throw new GeneralSecurityException("This Parameter needs to be Checked"+parObj.get("name"));
						}

					}


				}
				System.out.println("The api used is "+opObjects.getString("nickname"));
			}


		}





	}

	private static boolean isThisMethodType(String actMethod, String apiMethod) {
		// TODO Auto-generated method stub
		return actMethod.equals(apiMethod);
	}

	private static boolean isThisPath(String pathToCheck, String apiPath) {
		// TODO Auto-generated method stub
		return pathToCheck.equals(apiPath);
	}

	public static String readFile(String filename) {
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			result = sb.toString();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
