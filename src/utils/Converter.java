package utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.*;

public class Converter {
	public static JSONArray toJSON(ResultSet resultSet) throws JSONException, SQLException {
		
		JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            
            JSONObject obj = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
                        .toLowerCase(), resultSet.getObject(i + 1));
            }
            
            jsonArray.put(obj);
        }
        
        return jsonArray;
	}
}
