package REST;

import REST.beans.AvgStatisticsModel;
import REST.beans.Drone;
import REST.beans.DroneList;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.InputMismatchException;
import java.util.Scanner;
@XmlRootElement
public class ClientAdmin {
    public static void main(String[] args) {
        int numberMenu;
        Scanner in = new Scanner(System.in);
        Gson gson = new Gson();
        Client client = Client.create();
        WebResource webResource;
        ClientResponse response;
        MultivaluedMap<String, String> params;
        DroneList output;
        String t1;
        String t2;

        while (true) {
            System.out.println("\nWelcome to the admin page");
            System.out.print(
                    "<1> list of drones in the city \n" +
                    "<2> last n global statistics \n" +
                    "<3> average number of delivery between two timestamp \n" +
                    "<4> average number of kilometers between two timestamp \n" +
                    "press any other key to exit\n");
            try {
                numberMenu = in.nextInt();
            }catch (InputMismatchException e){
                break;
            }

            switch (numberMenu) {
                case 1:

                    webResource = client.resource("http://localhost:1337/drone");

                    response = webResource.accept("application/json")
                            .get(ClientResponse.class);

                    if (response.getStatus() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
                    }

                    output = response.getEntity(DroneList.class);
                    System.out.print("Output from Server .... \n");
                    for (Drone d:output.getDrones()) {
                        System.out.println(d.toString());
                    }

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

                    String outputString = response.getEntity(String.class);
                    System.out.println("Output from Server .... \n");
                    System.out.println(outputString);

                    //DroneList list = gson.fromJson(output,DroneList.class);
                    break;
                case 3:
                    in.nextLine();
                    System.out.println("insert t1");
                    t1 = in.nextLine();
                    System.out.println("insert t2");
                    t2 = in.nextLine();
                    params = new MultivaluedMapImpl();
                    params.add("t1",t1);
                    params.add("t2",t2);
                    webResource = client.resource("http://localhost:1337/statistics/delivery/avg").queryParams(params);

                    response = webResource.accept("application/json")
                            .get(ClientResponse.class);

                    if (response.getStatus() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
                    }

                    AvgStatisticsModel avgModel = response.getEntity(AvgStatisticsModel.class);
                    System.out.println("Output from Server .... \n");
                    double avg = avgModel.getAvg();
                    if(Double.isNaN(avg)){
                        System.out.println("media non disponibile");
                    }
                    else{
                        System.out.println("The average number of delivery is"+avg);
                    }

                    break;
                case 4:
                    in.nextLine();
                    System.out.println("insert t1");
                    t1 = in.nextLine();
                    System.out.println("insert t2");
                    t2 = in.nextLine();
                    params = new MultivaluedMapImpl();
                    params.add("t1",t1);
                    params.add("t2",t2);
                    webResource = client.resource("http://localhost:1337/statistics/kilometers/avg").queryParams(params);

                    response = webResource.accept("application/json")
                            .get(ClientResponse.class);

                    if (response.getStatus() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
                    }

                    output = response.getEntity(DroneList.class);
                    System.out.println("Output from Server .... \n");
                    System.out.println(output);

                    break;
                default:
                    System.out.println("exit from menu");
                    return;

            }
        }
    }
}
