# FROM ubuntu:22.04
#
# # 设置工作目录
# WORKDIR /app
#
# RUN apt-get update && apt-get install -y  \
#     time \
#     openjdk-17-jdk
#
# #构建的容器会默认保持运行状态，一个后台进程保持容器运行
# # CMD ["tail", "-f", "/dev/null"]


# FROM ubuntu:22.04
#
# WORKDIR /app
#
# RUN apt-get update && \
#     apt-get install -y --no-install-recommends time openjdk-17-jdk && \
#     rm -rf /var/lib/apt/lists/*

FROM alpine:latest

WORKDIR /app

# 安装JDK8和time工具，然后清理不必要的文件
RUN apk add --no-cache openjdk8-jdk tzdata && \
    # 移除不必要的组件以减小体积
    rm -rf /usr/lib/jvm/java-1.8-openjdk/jre/doc/ && \
    rm -rf /usr/lib/jvm/java-1.8-openjdk/jre/lib/fonts/ && \
    rm -rf /usr/lib/jvm/java-1.8-openjdk/jre/lib/security/cacerts && \
    rm -rf /usr/lib/jvm/java-1.8-openjdk/jre/lib/locale/ && \
    rm -f /usr/lib/jvm/java-1.8-openjdk/src.zip && \
    # 清理apk缓存
    rm -rf /var/cache/apk/*