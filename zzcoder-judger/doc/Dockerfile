FROM ubuntu:22.04

# 设置工作目录
WORKDIR /app

RUN apt-get update && apt-get install -y  \
    time \
    openjdk-17-jdk

#构建的容器会默认保持运行状态，一个后台进程保持容器运行
CMD ["tail", "-f", "/dev/null"]
