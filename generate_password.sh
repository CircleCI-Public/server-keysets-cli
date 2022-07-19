#!/bin/bash
set -e

function gen_password(){
    env LC_ALL=C tr -dc 'A-Za-z0-9_' < /dev/urandom | head -c "$1"
}

function sign_enc_keys(){
    sign=$(server-keysets generate signing)
    enc=$(server-keysets generate encryption)
    echo """
keyset:
  encryption: '$enc'
  signing: '$sign'
"""
}

function error_exit(){
  msg="$*"
  if [ -n "$msg" ] || [ "$msg" != "" ]; then
    echo "------->> Error: $msg"
  fi
  kill $$
}

############ MAIN ############

# Generating Passwords
sign_enc_keys
echo "apiToken: \"$(gen_password 48)\""
echo "sessionCookieKey: \"$(gen_password 16)\""
echo """
postgresql:
  auth:
    postgresPassword: \"$(gen_password 32)\"
"""
echo """
mongodb:
  auth:
    rootPassword: \"$(gen_password 32)\"
    password: \"$(gen_password 20)\"
"""
echo """
pusher:
  secret: \"$(gen_password 48)\"
"""
echo """
rabbitmq:
  auth:
    erlangCookie: \"$(gen_password 32)\"
    password: \"$(gen_password 32)\"
"""
