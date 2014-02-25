#!/bin/sh
curl -X POST -H "Content-Type: application/json" -d '{"username":"xyz","password":"xyz"}' "http://localhost:8089/api/?[1-1000]" &
pidlist="$pidlist $!" 
curl -X POST -H "Content-Type: application/json" -d '{"username":"xyz","password":"xyz"}' "http://localhost:8089/api/?[1-1000]" &
pidlist="$pidlist $!" 
curl -X POST -H "Content-Type: application/json" -d '{"username":"xyz","password":"xyz"}' "http://localhost:8089/api/?[1-1000]" &
pidlist="$pidlist $!" 
curl -X POST -H "Content-Type: application/json" -d '{"username":"xyz","password":"xyz"}' "http://localhost:8089/api/?[1-1000]" &
pidlist="$pidlist $!" 
curl -X POST -H "Content-Type: application/json" -d '{"username":"xyz","password":"xyz"}' "http://localhost:8089/api/?[1-1000]" &
pidlist="$pidlist $!" 
curl -X POST -H "Content-Type: application/json" -d '{"username":"xyz","password":"xyz"}' "http://localhost:8089/api/?[1-1000]" &
pidlist="$pidlist $!" 
curl -X POST -H "Content-Type: application/json" -d '{"username":"xyz","password":"xyz"}' "http://localhost:8089/api/?[1-1000]" &
pidlist="$pidlist $!"  

for job in $pidlist do 
  echo $job     
  wait $job || let "FAIL+=1" 
done  

if [ "$FAIL" == "0" ]; then 
  echo "YAY!" 
else 
  echo "FAIL! ($FAIL)" 
fi