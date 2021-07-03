package REST;

import REST.beans.*;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@XmlRootElement
public class ClientAdmin {
    public static void main(String[] args) {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        clientConfig.getClasses().add(JacksonJsonProvider.class);
        Client client = Client.create(clientConfig);
        Scanner in = new Scanner(System.in);
        WebResource webResource;
        ClientResponse response;
        MultivaluedMap<String, String> params;
        DroneList output;
        String t1;
        String t2;
        int numberMenu;

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
            } catch (InputMismatchException e) {
                break;
            }

            switch (numberMenu) {
                case 1:

                    webResource = client.resource("http://localhost:1337/drone");

                    response = webResource.accept("application/json")
                            .get(ClientResponse.class);

                    if (response.getStatus() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : " + response.getEntity(ExceptionModel.class).getMessage());
                    }

                    output = response.getEntity(DroneList.class);
                    List<Drone> drones = output.getDrones();
                    if (drones.size() > 0) {
                        System.out.println("Drones in the system:");
                        for (int i = 0; i < drones.size(); i++) {
                            Drone d = drones.get(i);
                            System.out.println("Drone " + d.getIdDrone());
                        }
                    } else {
                        System.out.println("There aren't drones in the system");
                    }

                    break;
                case 2:
                    System.out.println("Insert number of statistics that you want to see");
                    int n;
                    try {
                        n = in.nextInt();
                    } catch (InputMismatchException e) {
                        in.next();
                        System.err.println("Input error. Not an integer");
                        break;
                    }
                    webResource = client.resource("http://localhost:1337/statistics/globals/" + n);

                    response = webResource.accept("application/json")
                            .get(ClientResponse.class);

                    if (response.getStatus() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : " + response.getEntity(ExceptionModel.class).getMessage());
                    }

                    List<GlobalStats> gstatsList = response.getEntity(new GenericType<List<GlobalStats>>() {
                    });
                    if (gstatsList.isEmpty()) {
                        System.out.println("There aren't statistics available");
                    } else {
                        for (int i = 0; i < gstatsList.size(); i++) {
                            System.out.println((i + 1) + ")");
                            GlobalStats globalStats = gstatsList.get(i);
                            long timestamp = globalStats.getTimestamp();
                            double avgDelivery = globalStats.getAvgDelivery();
                            double avgBattery = globalStats.getAvgBattery();
                            List<Double> avgPollutions = globalStats.getAvgPollution();
                            assert avgPollutions.size() == 1;

                            System.out.println("time: " + new Timestamp(timestamp));
                            System.out.printf("average delivery: %.2f \n", avgDelivery);
                            System.out.printf("average battery: %.2f \n", avgBattery);
                            System.out.printf("average pollution: %.2f \n", avgPollutions.get(0));

                        }
                    }

                    break;
                case 3:
                    in.nextLine();
                    System.out.println("insert first timestamp [YYYY-MM-DD hh:mm:ss]");
                    t1 = in.nextLine();
                    System.out.println("insert second timestamp [YYYY-MM-DD hh:mm:ss]");
                    t2 = in.nextLine();
                    params = new MultivaluedMapImpl();
                    params.add("t1", t1);
                    params.add("t2", t2);
                    webResource = client.resource("http://localhost:1337/statistics/delivery/avg").queryParams(params);

                    response = webResource.accept("application/json")
                            .get(ClientResponse.class);

                    if (response.getStatus() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : " + response.getEntity(ExceptionModel.class).getMessage());
                    }

                    AvgStatisticsModel avgModel = response.getEntity(AvgStatisticsModel.class);
                    double avg = avgModel.getAvg();
                    if (Double.isNaN(avg)) {
                        System.out.println("Average not available");
                    } else {
                        System.out.printf("The average number of delivery is %.2f \n", avg);
                    }

                    break;
                case 4:
                    in.nextLine();
                    System.out.println("insert first timestamp [YYYY-MM-DD hh:mm:ss]");
                    t1 = in.nextLine();
                    System.out.println("insert second timestamp [YYYY-MM-DD hh:mm:ss]");
                    t2 = in.nextLine();
                    params = new MultivaluedMapImpl();
                    params.add("t1", t1);
                    params.add("t2", t2);
                    webResource = client.resource("http://localhost:1337/statistics/kilometers/avg").queryParams(params);

                    response = webResource.accept("application/json")
                            .get(ClientResponse.class);

                    if (response.getStatus() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : " + response.getEntity(ExceptionModel.class).getMessage());
                    }

                    AvgStatisticsModel avgModelKm = response.getEntity(AvgStatisticsModel.class);
                    double avgKm = avgModelKm.getAvg();
                    if (Double.isNaN(avgKm)) {
                        System.out.println("Average not available");
                    } else {
                        System.out.printf("The average number of kilometers is %.2f \n", avgKm);
                    }

                    break;

                default:
                    System.out.println("exit from menu");
                    return;

            }
        }
    }
}
