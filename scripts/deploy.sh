#!/bin/bash

REPOSITORY=/home/ubuntu/app

echo "> 현재 실행 중인 Docker 컨테이너 pid 확인"
CURRENT_PID=$(sudo docker container ls -q)

if [ -z ${CURRENT_PID} ]
then
  echo "> 현재 구동중인 Docker 컨테이너가 없으므로 종료하지 않습니다." 
else
  echo "> sudo docker stop ${CURRENT_PID}"   # 현재 구동중인 Docker 컨테이너가 있다면 모두 중지
  sudo docker stop ${CURRENT_PID}
  sudo docker rm ${CURRENT_PID}
  sleep 5
fi

cd $REPOSITORY
docker build -t makar0726/zeje .
docker run -d -p 8080:8080 makar0726/zeje:latest
