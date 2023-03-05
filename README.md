# sdp_project
Project for "Distributed and pervasive systems" course - University of Milan - Year 2020/21

## Dronazon

Overview

The purpose of this project is to simulate a drone delivery network that receives and executes orders from an external service. The drones work together to complete the deliveries, with one drone being elected as the Master Drone to coordinate the operation.

Components
Dronazon

This is the MQTT server responsible for publishing orders on a specific topic. The orders contain starting and ending coordinates.
REST API

The REST API allows drones to enter the system and provides a resource for the storage of statistics computed by the Master Drone.
Drones

The drones are a critical component of the system. They communicate with each other using gRPC and send statistics to the REST API.
Pollution Sensors

Each drone is equipped with a pollution sensor. The sensor produces a stream of data, which is processed by each drone using an overlapping sliding window.
Features
Drone Election

The drone network does not rely on the REST API to elect a drone. Instead, a ring election process is used to choose the drone with the highest battery level and ID. Every drone pings the Master Drone periodically, and when it's unresponsive, the drones initiate the election process to select a new Master Drone.
Order Assignment

The Master Drone receives orders by subscribing to the order topic with an MQTT client. It then decides which drone will fulfill the order based on battery level and proximity. The chosen drone will deliver the order and send back statistics to the Master Drone, such as kilometers traveled, residual battery level, and average pollution.
Network Statistics

Every ten seconds, the Master Drone sends statistics to the REST API, including the average number of deliveries per drone, the average kilometers traveled, and the average pollution level. These statistics are computed by aggregating data sent by drones after each delivery.
RPC Communications

Drones communicate with each other via gRPC. Each drone has a server that implements services, and each service is a specific type of communication. For example, when a drone enters the network, it sends its details to other drones using an RPC request on a designated service, including a payload. The messages are defined using protobuf.



Author: [Matteo Limoncini](https://github.com/matteolimoncini)
