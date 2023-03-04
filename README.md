# sdp_project
Project for "Distributed and pervasive systems" course - University of Milan - Year 2020/21

# Dronazon

## Introduction
The goal of the project is to simulate a network of drones delivering orders, published by an external service. They cooperate to deliver and they organize themselves to elect a Master Drone.

This was an university project for the **distributed and pervasive systems** course.

## Components
### Dronazon
**MQTT server** publishing orders on a given topic. The orders have starting and end coordinates. 

### REST API
The api makes possible to drones to enter the system and offers a resource to put statistics computed by the master drone.

### Drones
The drones are the critical point of the project. They communicate with each other via **gRPC** and they send statistics to the REST API.

### Pollution sensors
Every drone has a pollution sensor.
The code simulates a sensor that produces a stream of data, the measurements are processed by every drone with an **overlapping sliding window**.

## Functionalities
### Drone election
The drone network doesn't rely on the REST API to choose a Drone, they use in fact a **ring election** to choose the drone with the highest battery and ID. 

Every drone periodically ping the master and when he's down, they start the election process choosing a new one. 

### Orders assignment

The Master Drone receives orders by subscribing to the order topic with an **MQTT client**.

After that, he decides who will deliver an order based on battery level and proximity.
The chosen drone will deliver and send back statistics to the master, such as kms, residual battery, average pollution etc.

### Network statistics 

Every ten seconds the Master Drone sends statistics to the REST API, such as the average number of deliveries per drone, the average kms, pollution etc. 
They are computed aggregating statistics sent by drones after each delivery. 

### RPC communications
Drones communicates with each other via gRPC. Each one of them has a server that implements services and each service is a particular type of communication. For instance, when a drone enters the network it sends other its details with an RPC request on a given service, attaching the a payload.

The messages are defined with **protobuf**. 



Author: [Matteo Limoncini](https://github.com/matteolimoncini)
