FROM ubuntu:20.04

COPY target/server-keysets generate_password.sh /usr/local/bin/

ENTRYPOINT [ "/bin/bash" ]
CMD [ "generate_password.sh" ]