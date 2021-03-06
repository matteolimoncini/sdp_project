package GrpcImpl;

import DroneThreads.PositionDroneThread;
import REST.beans.Drone;
import com.example.grpc.Election;
import com.example.grpc.Election.message;
import com.example.grpc.electionGrpc;
import com.example.grpc.electionGrpc.electionImplBase;
import io.grpc.Context;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class ElectionImpl extends electionImplBase {
    private Drone drone;

    public ElectionImpl(Drone drone) {
        this.drone = drone;
    }

    @Override
    public void election(message request, StreamObserver<message> responseObserver) {

        int idDroneInMessage = request.getIdDrone();
        int myId = drone.getIdDrone();

        int batteryDroneInMessage = request.getBatteryDrone();
        String typeMessage = request.getType();
        int myBattery = drone.getBattery();


        Drone nextDroneInRing = drone.getNextInRing();
        Drone nextNextDroneInRing = drone.getNextNextInRing();
        String targetAddressNext = nextDroneInRing.getIpAddress() + ":" + nextDroneInRing.getPortNumber();
        Context.current().fork().run(() -> {

            message propagatedMessage = null;
            //System.out.println("message received: " + typeMessage + " whit id inside: " + idDroneInMessage);
            if (typeMessage.equals("ELECTION")) {

                drone.setElectionInProgress(true);

                if (drone.getParticipant()) {   //if drone is participant
                    if (myBattery < batteryDroneInMessage) {
                        //propagate message as is
                        propagatedMessage = request;

                    } else {
                        if (myBattery > batteryDroneInMessage) {
                            //not propagate message
                            propagatedMessage = null;
                        } else {
                            //same battery
                            if (myId == idDroneInMessage) {
                                //in this case myBattery = batteryIdDroneInMessage

                                //i am elected as master
                                drone.setIdMaster(myId);

                                //set as not participant
                                drone.setParticipant(false);

                                //send ELECTED message
                                propagatedMessage = message.newBuilder()
                                        .setType("ELECTED")
                                        .setIdDrone(myId)
                                        .build();
                            }
                            if (myId < idDroneInMessage) {
                                //propagate message as is
                                propagatedMessage = request;
                            }
                            if (myId > idDroneInMessage) {
                                //not propagate message
                                propagatedMessage = null;
                            }

                        }
                    }
                } else {      //if drone is not participant

                    //set as participant
                    drone.setParticipant(true);

                    if (myBattery > batteryDroneInMessage) {
                        //change idDroneInMessage with myId and send message
                        propagatedMessage = message.newBuilder()
                                .setType("ELECTION")
                                .setIdDrone(myId)
                                .setBatteryDrone(myBattery)
                                .build();
                    } else {
                        if (myBattery < batteryDroneInMessage) {
                            //propagate message as is
                            propagatedMessage = request;
                        }

                        if (myBattery == batteryDroneInMessage) {
                            if (myId < idDroneInMessage) {
                                //propagate message as is
                                propagatedMessage = request;
                            }
                            if (myId > idDroneInMessage) {
                                //change idDroneInMessage with myId and send message
                                propagatedMessage = message.newBuilder()
                                        .setType("ELECTION")
                                        .setIdDrone(myId)
                                        .setBatteryDrone(myBattery)
                                        .build();
                            }
                        }
                    }

                }
            } else { //type message not ELECTION
                if (typeMessage.equals("ELECTED")) {

                    //set as non participant
                    drone.setParticipant(false);

                    //save id master
                    drone.setIdMaster(idDroneInMessage);

                    if (myId != idDroneInMessage) {
                        //propagate message as is
                        propagatedMessage = request;

                        //send position to master
                        Thread sendPosition = new PositionDroneThread(drone);
                        sendPosition.start();

                    } else {
                        System.out.println("Election finished. I am master");
                    }

                    drone.setElectionInProgress(false);
                } else { //type message not ELECTION and not ELECTED
                    //type error
                    System.err.println("message type error");
                }
            }
            if (propagatedMessage != null) {
                final ManagedChannel channel = ManagedChannelBuilder.forTarget(targetAddressNext).usePlaintext().build();
                electionGrpc.electionStub stub = electionGrpc.newStub(channel);
                message finalPropagatedMessage = propagatedMessage;
                stub.election(propagatedMessage, new StreamObserver<message>() {
                    @Override
                    public void onNext(message value) {
                    }

                    @Override
                    public void onError(Throwable t) {
                        if(nextNextDroneInRing!=null){
                            String targetAddressNextNext = nextNextDroneInRing.getIpAddress() + ":" + nextNextDroneInRing.getPortNumber();
                            final ManagedChannel channel = ManagedChannelBuilder.forTarget(targetAddressNextNext).usePlaintext().build();
                            electionGrpc.electionStub stubNextNext = electionGrpc.newStub(channel);
                            stubNextNext.election(finalPropagatedMessage, new StreamObserver<message>() {
                                @Override
                                public void onNext(message message) {

                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    channel.shutdown();
                                }

                                @Override
                                public void onCompleted() {
                                    channel.shutdown();
                                }
                            });

                        }


                        channel.shutdown();
                    }

                    @Override
                    public void onCompleted() {
                        channel.shutdown();
                    }
                });
                try {
                    channel.awaitTermination(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                responseObserver.onNext(Election.message.newBuilder().build());
                responseObserver.onCompleted();
            } else {
                //System.out.println("message not sent");
                responseObserver.onNext(Election.message.newBuilder().build());
                responseObserver.onCompleted();
            }

        });
    }
}
