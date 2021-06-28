package GrpcImpl;

import DroneThreads.PositionDroneThread;
import REST.beans.Drone;
import com.example.grpc.Election.message;
import com.example.grpc.electionGrpc;
import com.example.grpc.electionGrpc.electionImplBase;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class ElectionImpl extends electionImplBase {
    private Drone drone;

    public ElectionImpl(Drone drone) {
        this.drone = drone;
    }

    @Override
    public void election(message request, StreamObserver<message> responseObserver) {

        int idDroneInMessage = request.getIdDrone();
        String typeMessage = request.getType();
        Integer myId = drone.getIdDrone();

        Drone nextDroneInRing = drone.getNextInRing();
        String targetAddress = nextDroneInRing.getIpAddress() + ":" + nextDroneInRing.getPortNumber();
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(targetAddress).usePlaintext().build();
        electionGrpc.electionStub stub = electionGrpc.newStub(channel);
        message propagatedMessage = null;

        System.out.println("message received: " + typeMessage + " whit id inside4: " + idDroneInMessage);
        if (typeMessage.equals("ELECTION")) {

            if (drone.getPartecipant()) {   //if drone is participant
                if (myId < idDroneInMessage) {
                    //propagate message as is
                    propagatedMessage= request;

                } else {
                    if (myId > idDroneInMessage) {
                        //not propagate message
                        propagatedMessage = null;
                    } else {
                        //in this case myId = idDroneInMessage

                        //i am elected as master
                        drone.setIdMaster(myId);

                        //set as not participant
                        drone.setPartecipant(false);

                        //send ELECTED message
                        propagatedMessage= message.newBuilder()
                                .setType("ELECTED")
                                .setIdDrone(myId)
                                .build();
                    }
                }
            } else {                //if drone is not participant

                //set as participant
                drone.setPartecipant(true);

                if (myId > idDroneInMessage) {
                    //change idDroneInMessage with myId and send message
                    propagatedMessage= message.newBuilder()
                            .setType("ELECTION")
                            .setIdDrone(myId)
                            .build();
                } else {
                    if (myId < idDroneInMessage) {
                        //propagate message as is
                        propagatedMessage= request;
                    }
                }

            }
        } else { //type message not ELECTION
            if (typeMessage.equals("ELECTED")) {

                //set as non participant
                drone.setPartecipant(false);

                //save id master
                drone.setIdMaster(idDroneInMessage);

                if (myId != idDroneInMessage) {
                    //propagate message as is
                    propagatedMessage= request;

                    //send position to master
                    Thread sendPosition = new PositionDroneThread(drone);
                    sendPosition.start();

                }
                //responseObserver.onCompleted();
            } else { //type message not ELECTION and not ELECTED
                //type error
                System.err.println("message type error");
            }
        }
        if(propagatedMessage!=null) {
            stub.election(propagatedMessage, new StreamObserver<message>() {
                @Override
                public void onNext(message value) {

                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onCompleted() {
                    channel.shutdown();
                }
            });
        }
        else {
            System.out.println("message not sent");
        }
    }
}
