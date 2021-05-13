package REST;

import REST.beans.DroneList;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Scanner;
@XmlRootElement
public class ClientAdmin {
    public static void main(String[] args) {
        int numberMenu;
        while (true) {
            System.out.println("Welcome to the admin page");
            System.out.print("<1> list of drones in the city \n" +
                    "<2> last n global statistics \n" +
                    "<3> average number of delivery between two timestamp \n" +
                    "<4> average number of kilometers between two timestamp \n" +
                    "press any other key to exit\n");
            Scanner in = new Scanner(System.in);
            numberMenu = in.nextInt();
            Gson gson = new Gson();
            Client client = Client.create();
            WebResource webResource;
            ClientResponse response;
            String output;
            String t1;
            String t2;

            switch (numberMenu) {
                case 1:

                    webResource = client.resource("http://localhost:1337/drone");

                    response = webResource.accept("application/json")
                            .get(ClientResponse.class);

                    if (response.getStatus() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
                    }

                    output = response.getEntity(String.class);
                    System.out.println("Output from Server .... \n");
                    System.out.println(output);

                    //DroneList list = gson.fromJson(output,DroneList.class);

                    break;
                case 2:
                    System.out.println("insert n");
                    int n = in.nextInt();
                    webResource = client.resource("http://localhost:1337/statistics/globals/"+n);

                    response = webResource.accept("application/json")
                            .get(ClientResponse.class);

                    if (response.getStatus() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
                    }

                    output = response.getEntity(String.class);
                    System.out.println("Output from Server .... \n");
                    System.out.println(output);

                    //DroneList list = gson.fromJson(output,DroneList.class);
                    break;
                case 3:
                    in.nextLine();
                    System.out.println("insert t1");
                    t1 = in.nextLine();
                    System.out.println("insert t2");
                    t2 = in.nextLine();
                    MultivaluedMap<String, String> params = new MultivaluedMapImpl();
                    params.add("t1",t1);
                    params.add("t2",t2);
                    webResource = client.resource("http://localhost:1337/statistics/delivery/avg").queryParams(params);

                    response = webResource.accept("application/json")
                            .get(ClientResponse.class);

                    if (response.getStatus() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
                    }

                    output = response.getEntity(String.class);
                    System.out.println("Output from Server .... \n");
                    System.out.println(output);

                    //DroneList list = gson.fromJson(output,DroneList.class);


                    break;
                case 4:
                    break;
                default:
                    System.out.println("exit from menu");
                    return;

            }
        }
    }
}
