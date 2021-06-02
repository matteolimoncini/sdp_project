import REST.beans.Drone;
import com.example.grpc.Election;
import com.example.grpc.electionGrpc;
import com.example.grpc.electionGrpc.electionImplBase;
import io.grpc.stub.StreamObserver;

public class electionImpl extends electionImplBase{
    private Drone drone;
    public electionImpl(Drone drone) {
        this.drone = drone;
    }

    @Override
    public void election(Election.message request, StreamObserver<Election.message> responseObserver) {

        int idDroneInMessage = request.getIdDrone();
        String typeMessage = request.getType();
        int myId = drone.getIdDrone();

        System.out.print("message received: "+ typeMessage +" from: "+ idDroneInMessage);

        if (typeMessage.equals("ELECTION")) {

            if (drone.getPartecipant()) {   //if drone is participant
                if (myId > idDroneInMessage) {
                    //propagate message as is

                } else {
                    if(myId < idDroneInMessage){
                        //not propagate message
                    }
                    else{
                        //myId = idDroneInMessage

                        //i am elected as master
                        drone.setIdMaster(myId);

                        //set as not participant
                        drone.setPartecipant(false);

                        //send ELECTED message
                    }
                }
            } else {                //if drone is not participant

                //set as participant
                drone.setPartecipant(true);

                if (myId>idDroneInMessage){
                    //change idDroneInMessage with myId and send message
                }
                else{
                    if(myId<idDroneInMessage){
                        //propagate message as is
                    }
                }

            }
        }
        else{ //type message not ELECTION
            if (typeMessage.equals("ELECTED")){

                //set as non participant
                drone.setPartecipant(false);

                //save id master
                drone.setIdMaster(idDroneInMessage);

                if (myId != idDroneInMessage){
                    //propagate message as is

                }
            }
            else{ //type message not ELECTION and not ELECTED
                //type error
                System.err.println("message type error");
            }
        }
    }
}
