/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.*;

/**
 *
 * @author Marco
 */

@Path("manager")
public class Manager {
    public ArrayList<Ordine> getListaOrdini() throws ClassNotFoundException, SQLException{
        ArrayList<Ordine> lOrdini=new ArrayList<>();
        Connection connection=null;
        String username="user1";
        String password="user1";
        String query="select * from ordini";
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        connection=DriverManager.getConnection("jdbc:derby://localhost:1527/DB_ordini",username,password);
        Statement st=connection.createStatement();
        ResultSet rs= st.executeQuery(query);
        while (rs.next()){
            Ordine o=new Ordine();
            o.setId(rs.getInt("id"));
            o.setTipo(rs.getString("tipo"));
            o.setSpecifiche(rs.getString("specifiche"));
            o.setOra(rs.getString("ora"));
            o.setTavolo(rs.getString("tavolo"));
            o.setNote(rs.getString("note"));
            lOrdini.add(o);
        }
        return lOrdini;
    }
    
    @GET
    @Path("/getordini")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJSONOrdini(){
        String risultato="{\"ordini\":{\"ordine\":[";
        try {
            for (Ordine o:getListaOrdini()){
                if(risultato!="{\"ordini\":{\"ordine\":["){
                    risultato+=",";
                }
                String st="{\"id\":\""+o.getId()+"\",\"tipo\":\""+o.getTipo()+"\", \"specifiche\":\""+o.getSpecifiche()+"\", \"ora\":\""+o.getOra()+"\", \"tavolo\":\""+o.getTavolo()+"\", \"note\":\""+o.getNote()+"\"}";
                risultato+=st;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        risultato+="]}}";
        return risultato;
    }
    
    
    @POST
    @Path("/post")
    public void addOrdine(@FormParam("specifiche") String specifiche,@FormParam("ora") String ora,@FormParam("tavolo") String tavolo,@FormParam("note") String tipo,@FormParam("tipo") String note) throws ClassNotFoundException, SQLException {
        
        Connection connection=null;
        String username="user1";
        String password="user1";
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        connection=DriverManager.getConnection("jdbc:derby://localhost:1527/DB_ordini",username,password);
        Statement st=connection.createStatement();
        String sql="insert into ordini (tipo,specifiche,ora,tavolo,note) values ('"+tipo+"','"+specifiche+"','"+ora+"','"+tavolo+"','"+note+"')";
        st.executeUpdate(sql);
        
    }
    
    public ArrayList<HashMap<String,String>> listaMapOrdini(String JSON) throws JSONException{
        ArrayList<HashMap<String,String>> lMap=new ArrayList<>();
        JSONObject obj = new JSONObject(JSON);
        JSONObject ordini = obj.getJSONObject("ordini");
        JSONArray arr = ordini.getJSONArray("ordine");
        for (int i = 0; i < arr.length(); i++){
            HashMap<String,String> map=new HashMap<>();
            String tipo = arr.getJSONObject(i).getString("tipo");
            String specifiche = arr.getJSONObject(i).getString("specifiche");
            String ora = arr.getJSONObject(i).getString("ora");
            String tavolo = arr.getJSONObject(i).getString("tavolo");
            String note = arr.getJSONObject(i).getString("note");
            map.put("tipo", tipo);
            map.put("specifiche", specifiche);
            map.put("ora", ora);
            map.put("tavolo", tavolo);
            map.put("note", note);
            lMap.add(map);
        }
        return lMap;
    }
}
