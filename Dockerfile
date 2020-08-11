FROM ubuntu:20.04

COPY target/server-keysets /usr/local/bin

ENTRYPOINT [ "server-keysets" ]
