FROM ubuntu:latest
RUN apt-get update && apt-get install -y time openjdk-11-jdk
WORKDIR /app
#构建的容器会默认保持运行状态，一个后台进程保持容器运行
CMD ["tail", "-f", "/dev/null"] 
