package GrpcImpl;

import DroneThreads.sendPositionDroneThread;
import REST.beans.Drone;
import REST.beans.GlobalStats;
import REST.beans.Position;
import com.example.grpc.GlobalStatsToMaster;
import com.example.grpc.globalStatsServiceGrpc;
import com.example.grpc.electionGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class GlobalStatsMasterImpl extends globalStatsServiceGrpc.globalStatsServiceImplBase{
    private Drone drone;

    public GlobalStatsMasterImpl(Drone drone) {
        this.drone=drone;
    }

    @Override
    public void globalStatsMaster(GlobalStatsToMaster.globalStatsToMaster request, StreamObserver<GlobalStatsToMaster.responseGlobalStats> responseObserver) {
        int idDroneInMessage = request.getIdDrone();
        Drone droneInMessage = null;
        for (Drone d: drone.getDrones()) {
            if(d.getIdDrone().equals(idDroneInMessage)){
                droneInMessage=d;
                break;
            }
        }
        assert droneInMessage!=null;
        System.out.println("received global stats from one drone to master");

        droneInMessage.setBattery(request.getBattery());
        System.out.println("setted battery");

        droneInMessage.setMyPosition(new Position(request.getNewPositionX(), request.getNewPositionY()));
        System.out.println("setted new position");

        droneInMessage.setProcessingDelivery(false);
        System.out.println("setted processing delivery");

        double avgb = request.getBattery();
        double avgkm = request.getKmTravelled();
        double avgpol = request.getAvgPm10();
        double avgd = 1;

        GlobalStats gstats = new GlobalStats(avgd,avgkm,avgpol,avgb);
        drone.addToStatsList(gstats);
    }
}

/*

    }
}
*/