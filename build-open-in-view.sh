#!/usr/bin/env bash

mvn clean package -DskipTests=true
open -a "/Applications/Google Chrome.app" http://localhost:8080
java -jar target/spring-data-jpa-daitokai-0.0.1-SNAPSHOT.jar --spring.jpa.open-in-view=true