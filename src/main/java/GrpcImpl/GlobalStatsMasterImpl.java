package GrpcImpl;

import REST.beans.Drone;
import REST.beans.GlobalStats;
import REST.beans.Position;
import com.example.grpc.GlobalStatsToMaster;
import com.example.grpc.Ping;
import com.example.grpc.globalStatsServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class GlobalStatsMasterImpl extends globalStatsServiceGrpc.globalStatsServiceImplBase{
    private Drone drone;

    public GlobalStatsMasterImpl(Drone drone) {
        this.drone=drone;
    }

    @Override
    public void globalStatsMaster(GlobalStatsToMaster.globalStatsToMaster request, StreamObserver<GlobalStatsToMaster.responseGlobalStats> responseObserver) {
        int idDroneInMessage = request.getIdDrone();
        Drone droneInMessage = null;
        List<Drone> drones = drone.getDrones();
        if (drones!=null) {
            for (Drone d : drones) {
                if (d.getIdDrone().equals(idDroneInMessage)) {
                    droneInMessage = d;
                    break;
                }
            }
        }
        if(idDroneInMessage == (drone.getIdDrone())){
            droneInMessage=drone;
        }
        assertNotNull(droneInMessage);
        System.out.println("received global stats from one drone to master");

        droneInMessage.setBattery(request.getBattery());
        //System.out.println("setted battery");

        droneInMessage.setMyPosition(new Position(request.getNewPositionX(), request.getNewPositionY()));
        //System.out.println("setted new position");

        droneInMessage.setProcessingDelivery(false);
        //System.out.println("setted processing delivery");

        double avgb = request.getBattery();
        double avgkm = request.getKmTravelled();
        List<Double> avgpol = request.getAvgPm10List();
        double avgd = 1;

        GlobalStats gstats = new GlobalStats(avgd,avgkm,avgpol,avgb);
        drone.addToStatsList(gstats);
        System.out.println("STATS ADDED TO LIST IN MASTER");
        GlobalStatsToMaster.responseGlobalStats response = GlobalStatsToMaster.responseGlobalStats.newBuilder().setMessageResponse("").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

/*

    }
}
*/